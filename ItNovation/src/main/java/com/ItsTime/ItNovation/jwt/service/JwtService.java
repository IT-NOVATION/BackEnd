package com.ItsTime.ItNovation.jwt.service;

import com.ItsTime.ItNovation.common.JwtErrorCode;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Getter
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    private final Set<String> logoutTokens = new HashSet<>(); // 로그아웃된 토큰 목록
    public void logout(String token) {
        logoutTokens.add(token); // 로그아웃된 토큰을 블랙리스트나 로그아웃 목록에 추가
    }
    /**
     * HttpServletResponse 객체를 이용해 에러 응답을 전송하는 메서드
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) {
        try {
            
            response.setStatus(statusCode);
            response.getWriter().write(message);
        } catch (IOException e) {
            // IOException 처리
            log.error("IOException occurred while sending error response: {}", e.getMessage());
        }
    }
    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email) {
        log.info("accessToken 생성");
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정

                //클레임으로는 email 하나만 사용합니다.
                //추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
                //추가하실 경우 .withClaim(클래임 이름, 클래임 값) 으로 설정해주시면 됩니다
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
    }
    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * HttpServletResponse 객체를 이용해 HTTP 응답 헤더를 설정하는 코드
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }
    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("accessToken", accessToken);
        jsonResponse.put("refreshToken", refreshToken);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(jsonResponse.toString());
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }
    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }


    /**
     * 헤더에서 AccessToken 추출
     * JWT 토큰의 헤더인 Authorization의 Value 형식: Authorization : <type> <credentials>
     * JWT 혹은 OAuth에 대한 토큰은 Bearer Type을 사용
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        log.info("accessToken 추출");

        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));

    }

    /**
     * AccessToken에서 Email 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(String accessToken,HttpServletRequest request) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(EMAIL_CLAIM) // claim(Emial) 가져오기
                    .asString());
        } catch (IllegalArgumentException e) {
            log.error("액세스 토큰이 유효하지 않습니다.");

            return Optional.empty();
        }
    }
    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () ->{
                            log.info("일치하는 회원이 없습니다");
                        }
                );
    }

    /**
     * 토큰의 유효성을 검사
     */
    public boolean isTokenValid(String token,HttpServletResponse response,HttpServletRequest request) {
        if (logoutTokens.contains(token)) {
            throw new JwtException(JwtErrorCode.EXPIRED_TOKEN.getMessage());

        }
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch(SecurityException e){
            log.error("유효하지 않은 JWT 서명입니다. {}", e.getMessage());
            throw new JwtException(JwtErrorCode.WRONG_TYPE_TOKEN.getMessage());

        }catch(TokenExpiredException e){
            log.error("로그아웃된 토큰입니다. {}",e.getMessage());
            throw new JwtException(JwtErrorCode.EXPIRED_TOKEN.getMessage());
        }catch(IllegalArgumentException e){
            log.error("잘못된 JWT 토큰입니다. {}", e.getMessage());
            throw new JwtException(JwtErrorCode.INVALID_TOKEN.getMessage());

        }catch(JWTDecodeException e){
            log.error("잘못된 JWT 토큰입니다. {}", e.getMessage());
            throw new JwtException(JwtErrorCode.INVALID_TOKEN.getMessage());
        }

    }



}
