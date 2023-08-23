package com.ItsTime.ItNovation.service.movielog;

import com.ItsTime.ItNovation.common.exception.ErrorCode;

import com.ItsTime.ItNovation.common.exception.NotFoundException;
import com.ItsTime.ItNovation.domain.follow.FollowRepository;
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
                throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
            } else {
                List<User> FollowerList = followRepository.findFollowersByUserId(findUser.get().getId());
                return FollowerList;
            }
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Transactional
    public List<User> getFollowingsByUserId(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        try {
            if (findUser.isEmpty()) {
                throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
            } else {
                List<User> FollowingList = followRepository.findFollowingsByUserId(findUser.get().getId());
                return FollowingList;
            }
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
