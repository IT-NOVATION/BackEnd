package com.ItsTime.ItNovation.service.topFollower;
import com.ItsTime.ItNovation.common.GeneralErrorCode;
import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.follow.FollowState;
import com.ItsTime.ItNovation.domain.movie.dto.FollowMovieResponseDto;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.TopFollowerReviewResponseDto;
import com.ItsTime.ItNovation.domain.topFollower.TopFollowerResponseDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.HTTP;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@Slf4j
@RequiredArgsConstructor
public class TopFollowerService {
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private String nowUserEmail = null;

    @Transactional
    public ResponseEntity getTopFollowers(Optional<String> accessToken) {
        if (accessToken.isPresent()) {
            nowUserEmail = jwtService.extractEmail(accessToken.get()).get();
        }
        log.info(nowUserEmail);
        Pageable pageable = PageRequest.of(0, 2);
        List<User> topPushUsers = followRepository.findTop3PushUsersByTargetUserCount(pageable);

        if (topPushUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GeneralErrorCode.NO_REVIEW.getMessage());
        } else {
            List<TopFollowerResponseDto> topFollowerResponseDtoList=topPushUsers.stream().map(topPushUser -> {
                log.info(String.valueOf(topPushUser.getId()));
                List<TopFollowerReviewResponseDto> reviews = reviewRepository.findNewestReviewByUserId(topPushUser.getId(), PageRequest.of(0, 3))
                        .stream()
                        .map(review -> TopFollowerReviewResponseDto.builder()
                                .reviewId(review.getReviewId())
                                .reviewTitle(review.getReviewTitle())
                                .movie(FollowMovieResponseDto.builder()
                                        .movieId(review.getMovie().getId())
                                        .movieImg(review.getMovie().getMovieImg())
                                        .build())
                                .build())
                        .collect(Collectors.toList());
                return TopFollowerResponseDto.builder()
                        .userId(topPushUser.getId())
                        .profileImg(topPushUser.getProfileImg())
                        .isMyProfile(profileState(nowUserEmail, topPushUser.getId()))
                        .isNowUserFollowThisUser(followState(nowUserEmail, topPushUser.getId()))
                        .nickName(topPushUser.getNickname())
                        .introduction(topPushUser.getIntroduction())
                        .reviews(reviews)
                        .build();
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(topFollowerResponseDtoList);
        }

    }

    private Boolean profileState(String nowUserEmail, Long userId) {
        Optional<User> nowUser = userRepository.findByEmail(nowUserEmail);
        log.info(String.valueOf(nowUser.get().getId()));

        Optional<User> checkUser = userRepository.findById(userId);
        log.info(String.valueOf(checkUser.get().getId()));
        if (nowUser.isPresent()) {
            if (nowUser.equals(checkUser)) {
                return true;
            } else return false;
        } else return false;
    }

    private Boolean followState(String nowUserEmail, Long userId) {
        Optional<User> user = userRepository.findByEmail(nowUserEmail);

        /**
         * 팔로우 하면 true
         * 아니면 false ( 같은 유저여도 false)
         */
        if (user.isPresent()) {
            Optional<FollowState> followState = followRepository.findByPushUserAndFollowUser(user.get().getId(), userId);
            return followState.isPresent();
        } else {
            return false;
        }
    }
}