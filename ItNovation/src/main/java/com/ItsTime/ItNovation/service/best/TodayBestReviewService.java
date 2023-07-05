package com.ItsTime.ItNovation.service.best;

import com.ItsTime.ItNovation.domain.bestReview.dto.TodayBestReviewDto;
import com.ItsTime.ItNovation.domain.bestReview.dto.TodayBestReviewResponseDto;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.TodayBestReviewMovieDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodayBestReviewService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;


    public ResponseEntity getBestReviewAndUser() {
        Pageable pageable = PageRequest.of(0,3);
        List<User> top3UsersWithTodayDate = reviewLikeRepository.findTop3UsersWithTodayDate(
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
            TodayBestReviewResponseDto bestReviewResponseDto = TodayBestReviewResponseDto.builder()
                .userId(user.getId())
                .profileImg(user.getProfileImg())
                .nickName(user.getNickname())
                .introduction(user.getIntroduction())
                .reviews(findTodayTop2Review(user))
                .build();

            todayBestReviewResponseDtos.add(bestReviewResponseDto);
        }

        return todayBestReviewResponseDtos;
    }

    private List<TodayBestReviewDto> findTodayTop2Review(User user) {
        Pageable pageable = PageRequest.of(0,2);
        List<TodayBestReviewDto> reviewDtos = new ArrayList<>();
        List<Review> top2ReviewsByUserId = reviewLikeRepository.findTop2ReviewsByUserId(
            user.getId(), pageable);

        for (int i=0; i<2; i++) {
            Review review = top2ReviewsByUserId.get(i);
            TodayBestReviewDto buildReviewDto = TodayBestReviewDto.builder()
                .reviewId(review.getReviewId())
                .reviewTitle(review.getReviewTitle())
                .movie(madeDtoMovie(review.getMovie()))
                .build();

            reviewDtos.add(buildReviewDto);
        }


        return reviewDtos;
    }

    private TodayBestReviewMovieDto madeDtoMovie(Movie movie) {
            return TodayBestReviewMovieDto.builder()
                .movieId(movie.getId())
                .movieImg(movie.getMovieImg())
                .build();
    }

}
