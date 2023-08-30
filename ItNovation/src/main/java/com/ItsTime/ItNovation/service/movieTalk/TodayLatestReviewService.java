package com.ItsTime.ItNovation.service.movieTalk;

import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.follow.FollowState;
import com.ItsTime.ItNovation.domain.movie.dto.LatestReviewMovieResponseDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.LatestReviewDto;
import com.ItsTime.ItNovation.domain.review.dto.LatestReviewResponseDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Slf4j
@Service
public class TodayLatestReviewService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;

    private final JwtService jwtService;
    private String nowUserEmail = null;
    /**
     *
     * @param accessToken
     * @return 무비토크 - 최신리뷰
     */

    @Transactional
    public ResponseEntity getLatestReviews(Optional<String> accessToken) {
        nowUserEmail = null;
        if (accessToken.isPresent()) {
            Optional<String> extractedEmail = jwtService.extractEmail(accessToken.get());
            extractedEmail.ifPresent(s -> nowUserEmail = s);

        }
        List<User> recentReviewers = reviewRepository.findUsersWithNewestReview(PageRequest.of(0, 3));
        List<LatestReviewResponseDto> LatestReviewResponseList = new ArrayList<>();


        for (User recentReviewer : recentReviewers) {
            List<Review> userLatestReviews = reviewRepository.findNewestReviewByUserId(recentReviewer.getId(), PageRequest.of(0, 2));
            List<LatestReviewDto> reviews = new ArrayList<>();
            for (Review review : userLatestReviews) {
                LatestReviewDto latestReviewDto = getLatestReviewDto(review);
                reviews.add(latestReviewDto);
            }
            LatestReviewResponseDto latestReviewResponseDto = getLatestReviewResponseDto(nowUserEmail, recentReviewer, reviews);
            LatestReviewResponseList.add(latestReviewResponseDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(LatestReviewResponseList);
    }

    private  LatestReviewDto getLatestReviewDto(Review review) {
        LatestReviewDto latestReviewDto = LatestReviewDto.builder()
                .reviewId(review.getReviewId())
                .reviewTitle(review.getReviewTitle())
                .movie(LatestReviewMovieResponseDto.builder()
                        .movieId(review.getMovie().getId())
                        .movieImg(review.getMovie().getMovieImg())
                        .build())
                .build();
        return latestReviewDto;
    }

    private LatestReviewResponseDto getLatestReviewResponseDto(String nowUserEmail, User recentReviewer, List<LatestReviewDto> reviews) {
        LatestReviewResponseDto latestReviewResponseDto = LatestReviewResponseDto.builder()
                .userId(recentReviewer.getId())
                .profileImg(recentReviewer.getProfileImg())
                .isMyProfile(profileState(nowUserEmail, recentReviewer.getId()))
                .loginUserPushedFollow(followState(nowUserEmail, recentReviewer.getId()))
                .nickname(recentReviewer.getNickname())
                .introduction(recentReviewer.getIntroduction())
                .reviews(reviews)
                .build();
        return latestReviewResponseDto;
    }


    private Boolean profileState(String nowUserEmail, Long userId) {
        Optional<User> nowUser = userRepository.findByEmail(nowUserEmail);
        Optional<User> checkUser = userRepository.findById(userId);
        if (nowUser.isPresent()) {
            return nowUser.equals(checkUser);
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
