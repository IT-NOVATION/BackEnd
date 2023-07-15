package com.ItsTime.ItNovation.service.best;

import com.ItsTime.ItNovation.domain.bestUser.TopUserResponseDto;
import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.dto.TopUserMovieDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.TopUserReviewDto;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import com.ItsTime.ItNovation.domain.user.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodayBestUserService {


    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;

    private final LocalDate yesterday = LocalDate.now().minusDays(1);

    /**
     * Todo -> 전날 기준 집계해서 top User 뽑는 방식으로 고려
     * @return
     */
    @Transactional
    public ResponseEntity getBestUserInfo() {
        Pageable pageable = PageRequest.of(0, 5);
        List<User> top5UsersWithTodayDate = reviewLikeRepository.findTopUsersWithYesterdayDate(yesterday,
            pageable); // -> 이거 전날 기준으로 고쳐야 함!
        System.out.println("top5UsersWithTodayDate = " + top5UsersWithTodayDate);

        List<TopUserResponseDto> top5UserResponseDtos = new ArrayList<>();

        for (int index = 0; index < top5UsersWithTodayDate.size(); index++) {
            List<TopUserReviewDto> topUserReviewDtos = new ArrayList<>();
            User user = top5UsersWithTodayDate.get(index);
            madeInternalResponseDto(topUserReviewDtos, top5UserResponseDtos, user);
        }
        return ResponseEntity.status(200).body(top5UserResponseDtos);
    }

    private void madeInternalResponseDto(List<TopUserReviewDto> topUserReviewDtos,
        List<TopUserResponseDto> top5UserResponseDtos, User user) {
        int followers = user.getFollowStates().size();
        log.info(yesterday.toString());
        Long following = followRepository.countByFollowedUserId(user.getId());
        List<Review> reviews = reviewLikeRepository.bestReviewsByUserId(yesterday, user.getId());
        addBestReview(topUserReviewDtos, reviews);
        Pageable remainPageable = PageRequest.of(0, 2);
        addNewestReview(topUserReviewDtos, user, remainPageable);
        TopUserResponseDto topUserResponseDto = buildTopUserResponseDto(
            topUserReviewDtos, user, followers, following);
        top5UserResponseDtos.add(topUserResponseDto);
    }

    private void addNewestReview(List<TopUserReviewDto> topUserReviewDtos, User user,
        Pageable remainPageable) {
        List<Review> newestReviewByUserId = reviewRepository.findNewestReviewByUserId(
            user.getId(), remainPageable);
        addTopUserReviewDto(topUserReviewDtos, newestReviewByUserId);
    }

    private void addBestReview(List<TopUserReviewDto> topUserReviewDtos, List<Review> reviews) {
        log.info(reviews.toString());
        TopUserReviewDto topReviewDto = getTopUserReviewDto(reviews.get(0));
        topUserReviewDtos.add(topReviewDto);
    }

    private void addTopUserReviewDto(List<TopUserReviewDto> topUserReviewDtos,
        List<Review> newestReviewByUserId) {
        for (Review review : newestReviewByUserId) {
            TopUserReviewDto remainDto = getTopUserReviewDto(review);
            topUserReviewDtos.add(remainDto);
        }
    }

    private static TopUserResponseDto buildTopUserResponseDto(
        List<TopUserReviewDto> topUserReviewDtos, User user, int followers, Long following) {
        TopUserResponseDto topUserResponseDto = TopUserResponseDto.builder()
            .userId(user.getId())
            .profileImg(user.getProfileImg())
            .nickName(user.getNickname())
            .introduction(user.getIntroduction())
            .grade(user.getGrade())
            .followers(followers)
            .followings(following)
            .reviews(topUserReviewDtos)
            .build();
        return topUserResponseDto;
    }

    private TopUserReviewDto getTopUserReviewDto(Review topReview) {
        int reviewLikeCount = reviewLikeRepository.countReviewLikeByReviewId(
            topReview.getReviewId());
        TopUserReviewDto topReviewDto = TopUserReviewDto.builder()
            .star(topReview.getStar())
            .reviewTitle(topReview.getReviewTitle())
            .createdDate(topReview.getCreatedDate().toString())
            .reviewId(topReview.getReviewId())
            .reviewLikeCount(reviewLikeCount)
            .reviewMainText(topReview.getReviewMainText())
            .movie(getMovieDto(topReview.getMovie()))
            .hasSpoiler(topReview.getHasSpoiler())
            .build();
        return topReviewDto;
    }

    private TopUserMovieDto getMovieDto(Movie movie) {
        return TopUserMovieDto.builder()
            .movieId(movie.getId())
            .movieImg(movie.getMovieImg())
            .build();
    }
}