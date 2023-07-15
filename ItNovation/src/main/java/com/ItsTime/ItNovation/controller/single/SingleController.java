package com.ItsTime.ItNovation.controller.single;

import com.ItsTime.ItNovation.common.GeneralErrorCode;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import com.ItsTime.ItNovation.domain.star.dto.SingleStarEvaluateDto;
import com.ItsTime.ItNovation.domain.star.dto.SingleStarEvaluateRequestDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.review.ReviewService;
import com.ItsTime.ItNovation.service.star.StarService;
import jakarta.security.auth.message.config.AuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor

@RequestMapping("single")
public class SingleController {

    private final StarService starService;
    private final ReviewService reviewService;
    private final UserRepository userRepository;



    @PostMapping("/starEvaluate")
    public ResponseEntity singleStarEvaluate(@RequestBody SingleStarEvaluateRequestDto singleStarEvaluateRequestDto, Authentication authentication){
        String email = authentication.getName();
        try {
            User findUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(GeneralErrorCode.UNKNOWN_USER.getMessage()));
            return starService.singleStarEvaluate(singleStarEvaluateRequestDto, findUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @GetMapping("/movie/reviewCount/{movieId}")
    public ResponseEntity reviewCount(@PathVariable Long movieId){
        return reviewService.reviewCount(movieId);
    }

}
