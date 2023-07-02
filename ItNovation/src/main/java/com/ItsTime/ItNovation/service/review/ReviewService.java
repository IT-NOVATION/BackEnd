package com.ItsTime.ItNovation.service.review;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.ReviewPostRequestDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }
}
