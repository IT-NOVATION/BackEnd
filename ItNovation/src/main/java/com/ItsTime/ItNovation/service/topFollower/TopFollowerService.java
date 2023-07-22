package com.ItsTime.ItNovation.service.topFollower;

import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.follow.FollowState;
import com.ItsTime.ItNovation.domain.movie.dto.FollowMovieResponseDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.TopFollowerReviewResponseDto;
import com.ItsTime.ItNovation.domain.topFollower.TopFollowerResponseDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@Slf4j
@RequiredArgsConstructor
public class TopFollowerService {
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<TopFollowerResponseDto> getTopFollowers(String email) {
        List<User> topPushUsers = followRepository.findTop3TargetUsersByPushUserCount();
        List<TopFollowerResponseDto> topFollowers = new ArrayList<>();

        for (User topPushUser : topPushUsers) {
            List<TopFollowerReviewResponseDto> reviews = new ArrayList<>();
            List<Review> newestReviews = reviewRepository.findNewestReviewByUserId(topPushUser.getId(), PageRequest.of(0, 2));

            for (Review review : newestReviews) {
                FollowMovieResponseDto movieDto = getFollowMovieResponseDto(review);
                TopFollowerReviewResponseDto reviewDto = getTopFollowerReviewResponseDto(review, movieDto);
                reviews.add(reviewDto);
            }
            TopFollowerResponseDto followerDto = getTopFollowerResponseDto(email, topPushUser, reviews);
            topFollowers.add(followerDto);
        }

        return topFollowers;
    }

    private TopFollowerResponseDto getTopFollowerResponseDto(String email, User topPushUser, List<TopFollowerReviewResponseDto> reviews) {
        TopFollowerResponseDto followerDto = TopFollowerResponseDto.builder()
                .userId(topPushUser.getId())
                .isMyProfile(profileState(email, topPushUser.getId()))
                .loginUserPushedFollow(followState(email, topPushUser.getId()))
                .profileImg(topPushUser.getProfileImg())
                .nickName(topPushUser.getNickname())
                .introduction(topPushUser.getIntroduction())
                .reviews(reviews)
                .build();
        return followerDto;
    }

    private static TopFollowerReviewResponseDto getTopFollowerReviewResponseDto(Review review, FollowMovieResponseDto movieDto) {
        TopFollowerReviewResponseDto reviewDto = TopFollowerReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .reviewTitle(review.getReviewTitle())
                .movie(movieDto)
                .build();
        return reviewDto;
    }

    private static FollowMovieResponseDto getFollowMovieResponseDto(Review review) {
        FollowMovieResponseDto movieDto = FollowMovieResponseDto.builder()
                .movieId(review.getMovie().getId())
                .movieImg(review.getMovie().getMovieImg())
                .build();
        return movieDto;
    }

    public Boolean profileState(String email, Long userId) {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<User> checkUser = userRepository.findById(userId);
        if(user.isPresent() && user == checkUser){
            return false;//로그인 했으며 자신의 프로필이므로 false 반환
        }
        return true;
    }

    public Boolean followState(String email, Long userId) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            Optional<FollowState> followState = followRepository.findByPushUserAndFollowUser(user.get().getId(), userId);
            return followState.isEmpty(); // 팔로우 상태가 비어있으면 true
        } else {
            return true;//팔로우 안한 상태면 false
        }
    }


}

