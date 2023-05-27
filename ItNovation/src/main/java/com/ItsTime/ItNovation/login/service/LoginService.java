package com.ItsTime.ItNovation.login.service;

import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 인증대상객체를 UsernamePasswordAuthenticationToken으로 설정하고
 * 이 객체를 먼저 AuthenticationManager(ProviderManager)에게 전달한다
 * providerManager가 전달받은 UsernamePasswordAuthenticationToken을 providerManager의 구현체인
 * DaoAuthenticationProvider로 전달한다
 * DaoAuthenticationProvider는 UserDetailsService의 loadUserByUsername(String username)을 호출해
 * UserDetails객체를 반환받는다. 이 때 파라미터인 username은 UsernamePasswordAuthenticationToken에서 username을 꺼내어 설정한다
 * (기존 유저 일치 작업 수행, 있으면 userEntity반환하고 그 Entity를 내부적으로 UserDetails객체로 반환
 * 반환받은 UserDetails의 password꺼내어, 내부의 passwordEncoder에서 password가 일치하는지 검증 수행
 * 비밀번호가 일치한다면, 인증 대상객체인 UsernamePasswordAuthenticationToken에 UserDetails객체와 Authorities를 담아서 반환
 * 최종적으로 ProviderManager에서 반환된 UserDetails객체와 Authorities가 담긴 UsernamePasswordAuthenticationToken으로
 * 인증객체 생성해 인증 성공 처리
 */
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
