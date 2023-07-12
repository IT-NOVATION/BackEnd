package com.ItsTime.ItNovation.service.follow;

import com.ItsTime.ItNovation.common.GeneralErrorCode;

import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.movielog.dto.MovieLogfollowersInfoDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;


    @Transactional
    public List<User> getFollowersByUserId(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        try {
            if (findUser.isEmpty()) {
                throw new IllegalArgumentException(GeneralErrorCode.UNKNOWN_USER.getMessage());
            } else {
                List<User> FollowerList = followRepository.findFollowersByUserId(findUser.get().getId());
                return FollowerList;
            }
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        return null;
    }

}
