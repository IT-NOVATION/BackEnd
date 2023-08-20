package com.ItsTime.ItNovation.controller.movie;


import com.ItsTime.ItNovation.common.exception.GeneralErrorCode;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.domain.star.dto.SingleStarEvaluateRequestDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.review.ReviewService;
import com.ItsTime.ItNovation.service.singleMoviePage.SingleMoviePageService;
import com.ItsTime.ItNovation.service.star.StarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name="개별영화페이지 API")
@RequestMapping("/api/v1/single")
public class SingleMovieController {

    private final StarService starService;
    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final SingleMoviePageService singleMoviePageService;
    private final JwtService jwtService;


    @PostMapping("/star-evaluate")
    @Operation(summary = "평점 매기기")
    public ResponseEntity singleStarEvaluate(@RequestBody SingleStarEvaluateRequestDto singleStarEvaluateRequestDto, Authentication authentication){
        String email = authentication.getName();
        try {
            User findUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(
                GeneralErrorCode.UNKNOWN_USER.getMessage()));
            return starService.singleStarEvaluate(singleStarEvaluateRequestDto, findUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @GetMapping("/movie/review-count/{movieId}")
    @Operation(summary = "해당 영화의 리뷰 수 가져오기")
    public ResponseEntity reviewCount(@PathVariable Long movieId){
        return reviewService.reviewCount(movieId);
    }

    @GetMapping("/movie-page/{movieId}")
    @Operation(summary = "해당 영화 데이터 가져오기")
    public ResponseEntity moviePage(@PathVariable Long movieId, HttpServletRequest request){
        Optional<String> s = jwtService.extractAccessToken(request);
        if(s.isPresent()){
            Optional<String> email = jwtService.extractEmail(s.get());
            if(email.isPresent()) {
                return singleMoviePageService.getReviewInformationAboutMovie(movieId, email.get());
            }
        }
        log.info("로그인 상태가 아닙니다.");
        return singleMoviePageService.getReviewInformationAboutMovie(movieId, null);
    }

}
