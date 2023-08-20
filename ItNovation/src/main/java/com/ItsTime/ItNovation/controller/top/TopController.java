package com.ItsTime.ItNovation.controller.top;


import com.ItsTime.ItNovation.common.exception.ErrorCode;
import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.bestUser.TodayBestUserService;
import com.ItsTime.ItNovation.service.top.TopKeywordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/top")
public class TopController {
    private final TopKeywordService topKeywordService;
    private final TodayBestUserService todayBestUserService;
    private final JwtService jwtService;

    @GetMapping("/movie-keyword/{movieId}")
    @Operation(summary ="해당 영화의 TOP3 키워드 확인")
    public ResponseEntity topKeyword(@PathVariable Long movieId){
        return topKeywordService.findTop3Feature(movieId);
    }


    @GetMapping("/yesterday/user")
    @Operation(summary ="전날 기준 베스트 유저")
    public ResponseEntity yesterdayTopUser(HttpServletRequest request){
            try {
                Optional<String> s = jwtService.extractAccessToken(request);
                if(s.isPresent()) {
                    Optional<String> email = jwtService.extractEmail(s.get());
                    if (email.isPresent()) {
                        return todayBestUserService.getBestUserInfo(email.get());
                    }
                }
            }catch(Exception e){
                return ResponseEntity.status(ErrorCode.EXPIRED_ACCESS_TOKEN.getHttpStatus()).body(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage());
            }
        return todayBestUserService.getBestUserInfo(null);
    }

}
