package com.ItsTime.ItNovation.service.review;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.ReviewMovieInfoDto;
import com.ItsTime.ItNovation.domain.movie.dto.ReviewPostMovieInfoResponseDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.ReviewInfoDto;
import com.ItsTime.ItNovation.domain.review.dto.ReviewPostRequestDto;
import com.ItsTime.ItNovation.domain.review.dto.ReviewReadResponseDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.domain.user.dto.ReviewUserInfoDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    public ResponseEntity reviewWrite(ReviewPostRequestDto reviewPostRequestDto, String nowUserEmail) {
        try{
            User nowUser = userRepository.findByEmail(nowUserEmail).orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
            Movie nowMovie = movieRepository.findById(reviewPostRequestDto.getMovieId()).orElseThrow(() -> new IllegalArgumentException("일치하는 영화가 없습니다."));


            log.info(reviewPostRequestDto.getReviewTitle());
            log.info(reviewPostRequestDto.getReviewMainText());
            Review review=Review.builder().star(reviewPostRequestDto.getStar())
                    .movie(nowMovie)
                    .user(nowUser)
                    .reviewTitle(reviewPostRequestDto.getReviewTitle())
                    .reviewMainText(reviewPostRequestDto.getReviewMainText())
                    .hasGoodStory(reviewPostRequestDto.getHasGoodStory())
                    .hasGoodProduction(reviewPostRequestDto.getHasGoodProduction())
                    .hasGoodScenario(reviewPostRequestDto.getHasGoodScenario())
                    .hasGoodDirecting(reviewPostRequestDto.getHasGoodDirecting())
                    .hasGoodOst(reviewPostRequestDto.getHasGoodOst())
                    .hasGoodVisual(reviewPostRequestDto.getHasGoodVisual())
                    .hasGoodActing(reviewPostRequestDto.getHasGoodActing())
                    .hasGoodCharterCharming(reviewPostRequestDto.getHasGoodCharterCharming())
                    .hasGoodDiction(reviewPostRequestDto.getHasGoodDiction())
                    .hasCheckDate(reviewPostRequestDto.getHasCheckDate())
                    .hasSpoiler(reviewPostRequestDto.getHasSpoiler())
                    .watchDate(reviewPostRequestDto.getWatchDate())
                    .reviewLikes(null)
                    .build();

            reviewRepository.save(review);
            return ResponseEntity.status(201).body("성공적으로 생성되었습니다");
        }catch (IllegalArgumentException e) {
            //TODO: 에러 메시지  -> 관심영화 API에서 이 경우 에러 처리하면됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    public ResponseEntity reviewRead(Long reviewId) {

        try {
            Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다."));
            Movie movie = review.getMovie();
            User user = review.getUser();

            ReviewReadResponseDto reviewReadResponseDto = madeResponseDto(review, movie, user);
            return ResponseEntity.status(200).body(reviewReadResponseDto);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private ReviewReadResponseDto madeResponseDto(Review review, Movie movie, User user) {
        ReviewInfoDto reviewInfoDto = madeReviewInfoDto(review);
        ReviewMovieInfoDto reviewMovieInfoDto = madeMovieInfoDto(movie);
        ReviewUserInfoDto reviewUserInfoDto = madeUserInfoDto(user);

        return mergeInfoDto(reviewInfoDto, reviewMovieInfoDto, reviewUserInfoDto);
    }

    private ReviewReadResponseDto mergeInfoDto(ReviewInfoDto reviewInfoDto, ReviewMovieInfoDto reviewMovieInfoDto,
        ReviewUserInfoDto reviewUserInfoDto) {

        ReviewReadResponseDto reviewReadResponseDto = ReviewReadResponseDto.builder()
            .review(reviewInfoDto)
            .movie(reviewMovieInfoDto)
            .user(reviewUserInfoDto)
            .build();
        
        return reviewReadResponseDto;

    }

    private ReviewUserInfoDto madeUserInfoDto(User user) {
        ReviewUserInfoDto reviewUserInfoDto = ReviewUserInfoDto.builder()
            .userId(user.getId())
            .bgImg(user.getBgImg())
            .nickname(user.getNickname())
            .grade(user.getGrade())
            .introduction(user.getIntroduction())
            .profileImg(user.getProfileImg())
            .build();
        return reviewUserInfoDto;
    }

    private ReviewMovieInfoDto madeMovieInfoDto(Movie movie) {
        ReviewMovieInfoDto reviewMovieInfoDto = ReviewMovieInfoDto.builder()
            .movieId(movie.getId())
            .movieCountry(movie.getMovieCountry())
            .movieImg(movie.getMovieImg())
            .movieGenre(movie.getMovieGenre())
            .movieReleaseDate(movie.getMovieDate())
            .title(movie.getTitle())
            .build();

        return reviewMovieInfoDto;
    }

    private ReviewInfoDto madeReviewInfoDto(Review review) {

        ReviewInfoDto reviewInfoDto = ReviewInfoDto.builder()
            .reviewId(review.getReviewId())
            .hasCheckDate(review.getHasCheckDate())
            .hasGoodActing(review.getHasGoodActing())
            .hasGoodDiction(review.getHasGoodDiction())
            .hasSpoiler(review.getHasSpoiler())
            .hasGoodDirecting(review.getHasGoodDirecting())
            .hasGoodOst(review.getHasGoodOst())
            .hasGoodScenario(review.getHasGoodScenario())
            .hasGoodProduction(review.getHasGoodProduction())
            .hasGoodVisual(review.getHasGoodVisual())
            .hasGoodCharterCharming(review.getHasGoodCharterCharming())
            .reviewTitle(review.getReviewTitle())
            .reviewMainText(review.getReviewMainText())
            .watchDate(review.getWatchDate())
            .build();

        return reviewInfoDto;
    }

    public ResponseEntity getMovieInfo(Long movieId) {
        try {
            Movie findMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다."));
            ReviewPostMovieInfoResponseDto responseDto = buildResponse(
                movieId, findMovie);
            return ResponseEntity.status(200).body(responseDto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private ReviewPostMovieInfoResponseDto buildResponse(Long movieId,
        Movie findMovie) {
        ReviewPostMovieInfoResponseDto responseDto = ReviewPostMovieInfoResponseDto.builder()
            .movieId(movieId)
            .movieImg(findMovie.getMovieImg())
            .title(findMovie.getTitle())
            .build();
        return responseDto;
    }
}
