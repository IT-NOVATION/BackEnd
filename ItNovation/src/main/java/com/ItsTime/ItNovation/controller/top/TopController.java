package com.ItsTime.ItNovation.controller.top;


import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.top.YesterdayBestUserService;
import com.ItsTime.ItNovation.service.top.TopKeywordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/top")
@Slf4j
public class TopController {
    private final TopKeywordService topKeywordService;
    private final YesterdayBestUserService yesterdayBestUserService;
    private final JwtService jwtService;

    @GetMapping("/movie-keyword/{movieId}")
    @Operation(summary ="해당 영화의 TOP3 키워드 확인")
    public ResponseEntity topKeyword(@PathVariable Long movieId){
        return topKeywordService.findTop3Feature(movieId);
    }


    @GetMapping("/yesterday/user")
    @Operation(summary ="전날 기준 베스트 유저")
    public ResponseEntity yesterdayTopUser(HttpServletRequest request){

        Optional<String> s = jwtService.extractAccessToken(request);
        if (s.isPresent()) {
            Optional<String> email = jwtService.extractEmail(s.get());
            if (email.isPresent()) {
                return yesterdayBestUserService.getBestUserInfo(email.get());
            }

        }

        return yesterdayBestUserService.getBestUserInfo(null);
    }

}
