package com.ItsTime.ItNovation.jwt.filter;

import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.jwt.util.PasswordUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * 실제 필터링 로직 수행, request header에서 Authorization 값 꺼내 토큰 검사하고
 * 여러가지 검사 후 유저 정보 꺼내서 SecurityContext에 저장한다.
 * 가입.로그인.재발급을 제외한 모든 Request요청은 이 필터를 거쳐 토큰 정보가 없거나 유효하지 않으면 정상적으로 수행되지 않음
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String NO_CHECK_URL = "/login";
    private final JwtService jwtService;
    private final UserRepository userRepository;
    /**
     * Spring Security에서 인증된 사용자의 권한 정보(GrantedAuthority)를 매핑할 때 사용하는 인터페이스
     * 이 인터페이스는 UserDetailsService를 통해 가져온 사용자 정보를 기반으로 UserDetails 객체를 생성할 때 사용되며, 이 과정에서 사용자의 권한 정보를 매핑하는 역할을 수행
     * 사용자 인증만 수행하고 권한 체크를 하지 않는 경우에 NullAuthoritiesMapper를 사용
     * NullAuthoritiesMapper는 GrantedAuthoritiesMapper의 구현체
     */
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }
        // 사용자 요청 헤더에서 RefreshToken 추출
        // -> RefreshToken이 없거나 유효하지 않다면(DB에 저장된 RefreshToken과 다르다면) null을 반환
        // 사용자의 요청 헤더에 RefreshToken이 있는 경우는, AccessToken이 만료되어 요청한 경우밖에 없다.
        // 따라서, 위의 경우를 제외하면 추출한 refreshToken은 모두 null
        log.info("토큰 생성");
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // 리프레시 토큰이 요청 헤더에 존재했다면, 사용자가 AccessToken이 만료되어서
        // RefreshToken까지 보낸 것이므로 리프레시 토큰이 DB의 리프레시 토큰과 일치하는지 판단 후,
        // 일치한다면 AccessToken을 재발급해준다.
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return으로 필터 진행 막기
        }
        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
        
    }



    // accessToken이 만료되어, RefreshToken을 보낸것
    // 따라서 이 로직은 리프레시 토큰이 DB의 리프레시 토큰과 일치하는지 판단 후,
    // 일치 시 AccessToken을 재발급해준다
    // 추가적으로 RTR방식으로 인해 reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
    // 그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(user);
                    jwtService.sendAccessTokenAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()), reIssuedRefreshToken);
                });
    }



    //리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드
    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken= jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;

    }



    /**
     * 요청헤더에 refreshToken이 없는 경우로,
     * accessToken 체크 및 인증 처리 메서드
     * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
     * 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
     * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     * 그 후 다음 인증 필터로 진행
     */
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");

        Optional<String> accessTokenOptional = jwtService.extractAccessToken(request);
        if (accessTokenOptional.isPresent()) {
            String accesstoken = accessTokenOptional.get();
            if (jwtService.isTokenValid(accesstoken)) {
                Optional<String> emailOptional = jwtService.extractEmail(accesstoken);
                if (emailOptional.isPresent()) {
                    String email = emailOptional.get();
                    userRepository.findByEmail(email).ifPresent(this::saveAuthentication);
                }else{
                    //이메일 추출 실패
                    log.info("이메일 추출 실패");
                }
            }else{
                //유효하지 않은 토큰
                log.info("유효하지 않은 토큰");
            }
        }else{
            //토큰 존재하지 않는 경우
            log.info("토큰 없음");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 인증허가 메서드
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     *
     * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     * UsernamePasswordAuthenticationToken의 파라미터
     * 1. UserDetailsUser 객체 (유저 정보)
     * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
     * 3. Collection < ? extends GrantedAuthority>로,
     * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
     * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
     *
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
     */

    private void saveAuthentication(User myUser) {
        String password = myUser.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser=org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getRole().name())
                .build();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
