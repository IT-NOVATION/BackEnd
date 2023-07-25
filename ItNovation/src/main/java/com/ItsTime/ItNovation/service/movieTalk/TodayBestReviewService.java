package com.ItsTime.ItNovation.service.movieTalk;

import com.ItsTime.ItNovation.common.JwtErrorCode;
import com.ItsTime.ItNovation.domain.bestReview.dto.TodayBestReviewDto;
import com.ItsTime.ItNovation.domain.bestReview.dto.TodayBestReviewResponseDto;
import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.follow.FollowState;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.dto.TodayBestReviewMovieDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import com.ItsTime.ItNovation.domain.user.User;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodayBestReviewService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LocalDate yesterday = LocalDate.now().minusDays(0);
    private String nowUserEmail = null;
    private final JwtService jwtService;

    @Transactional
    public ResponseEntity getBestReviewAndUser(Optional<String> accessToken) {
        if (accessToken.isPresent()) {
            Optional<String> extractedEmail = jwtService.extractEmail(accessToken.get());

            if (extractedEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JwtErrorCode.INVALID_TOKEN.getMessage());
            } else {
                nowUserEmail = extractedEmail.get();
            }
        }
        Pageable pageable = PageRequest.of(0, 3);

        List<User> top3UsersWithTodayDate = reviewLikeRepository.findTopUsersWithYesterdayDate(yesterday,
            pageable);
        try {
            List<TodayBestReviewResponseDto> todayBestReviewResponseDtos = madeResponse(
                top3UsersWithTodayDate);
            return ResponseEntity.status(200).body(todayBestReviewResponseDtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private List<TodayBestReviewResponseDto> madeResponse(List<User> top3UsersWithTodayDate) {

        List<TodayBestReviewResponseDto> todayBestReviewResponseDtos = new ArrayList<>();

        if (top3UsersWithTodayDate.isEmpty()) {
            throw new IllegalArgumentException("오늘 눌러진 좋아요가 없습니다.");
        }
        for (User user : top3UsersWithTodayDate) {
            TodayBestReviewResponseDto bestReviewResponseDto = convertToDto(user);
            todayBestReviewResponseDtos.add(bestReviewResponseDto);
        }
        return todayBestReviewResponseDtos;
    }

    private TodayBestReviewResponseDto convertToDto(User user) {
        TodayBestReviewResponseDto bestReviewResponseDto = TodayBestReviewResponseDto.builder()
            .userId(user.getId())
            .profileImg(user.getProfileImg()).isMyProfile(profileState(nowUserEmail, user.getId()))
                .isNowUserFollowThisUser(followState(nowUserEmail, user.getId()))
            .nickName(user.getNickname())
            .introduction(user.getIntroduction())
            .reviews(findTodayTop2Review(user))
            .build();
        return bestReviewResponseDto;
    }

    //TODO: 없는 경우 핸들링 필요
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
    private List<TodayBestReviewDto> findTodayTop2Review(User user) {
        Pageable pageable = PageRequest.of(0, 2);
        List<TodayBestReviewDto> reviewDtos = new ArrayList<>();
        List<Review> top2ReviewsByUserId = reviewLikeRepository.findTopReviewsByUserId(yesterday,
            user.getId(), pageable);

        convertToTopBestReviewDtos(reviewDtos, top2ReviewsByUserId);

        return reviewDtos;
    }

    private void convertToTopBestReviewDtos(List<TodayBestReviewDto> reviewDtos,
        List<Review> top2ReviewsByUserId) {
        for (int i = 0; i < 2; i++) {
            Review review = top2ReviewsByUserId.get(i);
            TodayBestReviewDto buildReviewDto = TodayBestReviewDto.builder()
                .reviewId(review.getReviewId())
                .reviewTitle(review.getReviewTitle())
                .movie(convertToBestReviewMovieDto(review.getMovie()))
                .build();
            reviewDtos.add(buildReviewDto);
        }
    }

    private TodayBestReviewMovieDto convertToBestReviewMovieDto(Movie movie) {
        return TodayBestReviewMovieDto.builder()
            .movieId(movie.getId())
            .movieImg(movie.getMovieImg())
            .build();
    }

}
