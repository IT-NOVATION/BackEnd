package com.ItsTime.ItNovation.service.user;

import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.domain.user.dto.UserProfileDto;
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

public class UserProfileService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public ResponseEntity userProfile(UserProfileDto userProfileDto, String token) {
        try{
            log.info(token);
            String email=jwtService.extractEmail(token).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

            // 프로필 정보 업데이트
            user.update(userProfileDto.getNickname(), userProfileDto.getIntroduction());
            // 사용자 저장
            userRepository.saveAndFlush(user);
            return ResponseEntity.ok(null);
        }catch (IllegalArgumentException e) {
            //TODO: 세부적으로 HttpStatus 설정해보기
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}
