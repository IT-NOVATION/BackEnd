package com.ItsTime.ItNovation.controller.movieLog;


import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.movielog.MovieLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name="무비로그 API")

@RequestMapping("/api/v1/movielog")
public class MovieLogController {
    private final MovieLogService movieLogService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/{userId}")
    @Operation(summary = "특정 유저의 무비로그")
    public ResponseEntity getMovieLogResponse(@PathVariable Long userId, HttpServletRequest request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("no user"));
            Optional<String> accessToken= jwtService.extractAccessToken(request);

            return movieLogService.getMovieLogResponse(user.getEmail(),accessToken);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }



}
