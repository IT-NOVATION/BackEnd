package com.ItsTime.ItNovation.controller.topuser;


import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.bestUser.TodayBestUserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TopUserController {

    private final TodayBestUserService todayBestUserService;
    private final JwtService jwtService;


    @GetMapping("/today/topUser")
    public ResponseEntity todayTopUser(HttpServletRequest request){

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
