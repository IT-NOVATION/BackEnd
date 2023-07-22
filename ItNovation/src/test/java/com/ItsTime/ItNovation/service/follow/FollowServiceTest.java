package com.ItsTime.ItNovation.service.follow;

import com.ItsTime.ItNovation.common.GeneralErrorCode;
import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Slf4j
public class FollowServiceTest {
//    private final FollowRepository followRepository;
//    private final UserRepository userRepository;
//
//    @Test
//    @DisplayName("팔로워 리스트 가져오기 테스트")
//    public void getFollowers() {
//        Long userId=1L;
//        try {
//            User findRealUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(GeneralErrorCode.UNKNOWN_USER.getMessage()));
//            List<User> userList=followRepository.findFollowersByUserId(findRealUser.getId());
//            assertThat(userList.size()).isGreaterThan(0);
//
//        } catch (IllegalArgumentException e) {
//
//        }
//
//
//
//
//    }
}