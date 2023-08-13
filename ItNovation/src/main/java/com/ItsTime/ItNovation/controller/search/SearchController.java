package com.ItsTime.ItNovation.controller.search;

import com.ItsTime.ItNovation.domain.movie.dto.MovieSearchResponseDto;
import com.ItsTime.ItNovation.domain.user.dto.UserSearchTotalResponseDto;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.movie.MovieSearchService;
import com.ItsTime.ItNovation.service.user.UserSearchService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/search")
public class SearchController {
    private final MovieSearchService movieSearchService;
    private final UserSearchService userSearchService;
    private final JwtService jwtService;

    @GetMapping("/movie")
    public ResponseEntity <MovieSearchResponseDto> searchMoviesByTitle(@RequestParam(name="movieNm") String movieNM) {
        return movieSearchService.searchMoviesByTitle(movieNM);

    }

    @GetMapping("/user")
    public ResponseEntity <UserSearchTotalResponseDto> searchTargetUser(@RequestParam(name="userNm") String userName, HttpServletRequest request){
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return userSearchService.getTotalResponse(userName,accessToken);
    }

}


