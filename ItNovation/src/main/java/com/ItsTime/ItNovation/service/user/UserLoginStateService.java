package com.ItsTime.ItNovation.service.user;


import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.domain.user.dto.LoginStateDto;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserLoginStateService {

    private final UserRepository userRepository;
    private final JwtService jwtService;


    @Transactional
    public ResponseEntity<LoginStateDto> loginState(String token) {
        try {
            String email = jwtService.extractEmail(token).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
            LoginStateDto loginStateDto = LoginStateDto.builder().loginState(true).userId(user.getId()).nickname(user.getNickname()).profileImg(user.getProfileImg()).build();
            return ResponseEntity.status(HttpStatus.OK).body(loginStateDto);
        } catch (IllegalArgumentException e) {
            //FIXME:토큰이 잘못됐으면 jwtAuthenticationProcessingFilter에서 무조건 처리 따라서 해당 코드 필요없음
            LoginStateDto loginStateDto = LoginStateDto.builder().loginState(false).userId(null).nickname(null).profileImg(null).build();
            return ResponseEntity.status(HttpStatus.OK).body(loginStateDto);

        }
    }
}
