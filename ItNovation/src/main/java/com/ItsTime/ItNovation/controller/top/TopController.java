package com.ItsTime.ItNovation.controller.top;


import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.bestUser.TodayBestUserService;
import com.ItsTime.ItNovation.service.top.TopKeywordService;
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
    public ResponseEntity topKeyword(@PathVariable Long movieId){
        return topKeywordService.findTop3Feature(movieId);
    }


    @GetMapping("/yesterday/user")
    public ResponseEntity yesterdayTopUser(HttpServletRequest request){
        Optional<String> s = jwtService.extractAccessToken(request);
        if(s.isPresent()){
            Optional<String> email = jwtService.extractEmail(s.get());
            if(email.isPresent()) {
                return todayBestUserService.getBestUserInfo(email.get());
            }
        }
        return todayBestUserService.getBestUserInfo(null);
    }

}
