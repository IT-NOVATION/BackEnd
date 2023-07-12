package com.ItsTime.ItNovation.controller;


import com.ItsTime.ItNovation.common.GeneralErrorCode;
import com.ItsTime.ItNovation.domain.movielog.dto.MovieLogResponseDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.movielog.MovieLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/movielog")
public class MovieLogController {
    private final MovieLogService movieLogService;


    @GetMapping
    public ResponseEntity getMovieLogResponse(@RequestParam(name = "userId") Long userId) {
        return movieLogService.getMovieLogResponse(userId);
    }


}
