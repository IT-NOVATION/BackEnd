package com.ItsTime.ItNovation.controller.movieLog;


import com.ItsTime.ItNovation.common.GeneralErrorCode;
import com.ItsTime.ItNovation.domain.movielog.dto.MovieLogResponseDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.movielog.MovieLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/movielog")
public class MovieLogController {
    private final MovieLogService movieLogService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/{userId}")
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
