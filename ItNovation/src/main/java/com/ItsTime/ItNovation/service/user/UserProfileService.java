package com.ItsTime.ItNovation.service.user;

import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.domain.user.dto.UserProfileDto;
import com.ItsTime.ItNovation.domain.user.dto.UserProfileDtoMe;
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
    public ResponseEntity userProfileMe(UserProfileDtoMe userProfileDtoMe, String email) {
        try{
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

            // 프로필 정보 업데이트
            user.update(userProfileDtoMe.getNickname(), userProfileDtoMe.getIntroduction(),userProfileDtoMe.getProfileImg(),userProfileDtoMe.getBgImg());
            // 사용자 저장
            userRepository.saveAndFlush(user);
            return ResponseEntity.ok(null);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity userProfile(UserProfileDto userProfileDto) {
        log.info("update");
        try {
            String email = userProfileDto.getEmail();
            User findUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
            log.info(findUser.getEmail());
            findUser.update(userProfileDto.getNickname(), userProfileDto.getIntroduction());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }


    }
}
