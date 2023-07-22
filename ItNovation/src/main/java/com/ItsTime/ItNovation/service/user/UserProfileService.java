package com.ItsTime.ItNovation.service.user;

import com.ItsTime.ItNovation.common.GeneralErrorCode;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
                    .orElseThrow(() -> new IllegalArgumentException(GeneralErrorCode.UNKNOWN_USER.getMessage()));
            Optional<User> findUserByNickName = userRepository.findUserBySameNickName(userProfileDtoMe.getNickname());

            // 닉네임 중복검사
            // 기존 설정된 자신의 닉네임인 경우 에러 안냄
            return checkDuplicateAndSendResponse(userProfileDtoMe, user, findUserByNickName);

        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    private ResponseEntity<?> checkDuplicateAndSendResponse(UserProfileDtoMe userProfileDtoMe, User user, Optional<User> findUserByNickName) {
        if (findUserByNickName.isEmpty()) {
            log.info("닉넴 존재안함");
            user.update(userProfileDtoMe.getNickname(), userProfileDtoMe.getIntroduction(), userProfileDtoMe.getProfileImg(), userProfileDtoMe.getBgImg());
            userRepository.saveAndFlush(user);
            return ResponseEntity.ok(null);
        }else{
            return handleNickNameError(user, findUserByNickName.get(),userProfileDtoMe);
        }
    }

    private ResponseEntity<?> handleNickNameError(User user, User findUserByNickName,UserProfileDtoMe userProfileDtoMe) {

        if (Objects.equals(user.getId(), findUserByNickName.getId())) {
            user.update(userProfileDtoMe.getIntroduction(), userProfileDtoMe.getProfileImg(), userProfileDtoMe.getBgImg());
            userRepository.saveAndFlush(user);
            return ResponseEntity.status(HttpStatus.OK).body(GeneralErrorCode.CONFLICT_NICKNAME.getMessage());
        }
        log.info("중복되는 닉네임");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(GeneralErrorCode.DUPLICATED_NICKNAME.getMessage());
    }

    @Transactional
    public ResponseEntity userProfile(UserProfileDto userProfileDto) {
        log.info("update");
        try {
            String email = userProfileDto.getEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(GeneralErrorCode.UNKNOWN_USER.getMessage()));

            //TODO: 기존에 DB에 변경하려는 닉네임이 있는 경우 중복 알려주기
            Optional<User> findUserByNickName = userRepository.findUserBySameNickName(userProfileDto.getNickname());

            return checkDuplicateAndSendResponse(userProfileDto, user, findUserByNickName);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }


    }
    private ResponseEntity<?> checkDuplicateAndSendResponse(UserProfileDto userProfileDto, User user, Optional<User> findUserByNickName) {
        if (findUserByNickName.isEmpty()) {
            log.info("닉넴 존재안함");
            user.update(userProfileDto.getNickname(), userProfileDto.getIntroduction());
            userRepository.saveAndFlush(user);
            return ResponseEntity.ok(null);
        }else{
            return handleNickNameError(user, findUserByNickName.get());
        }
    }
    private ResponseEntity<?> handleNickNameError(User user, User findUserByNickName) {
        if (Objects.equals(user.getId(), findUserByNickName.getId())) {
            return ResponseEntity.status(HttpStatus.OK).body(GeneralErrorCode.CONFLICT_NICKNAME.getMessage());
        }
        log.info("중복되는 닉네임");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(GeneralErrorCode.DUPLICATED_NICKNAME.getMessage());
    }

}
