package com.ItsTime.ItNovation.controller.topfollower;

import com.ItsTime.ItNovation.domain.topFollower.TopFollowerResponseDto;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.topFollower.TopFollowerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TopFollowerController {
    private final TopFollowerService topFollowerService;
    private final JwtService jwtService;

    @GetMapping("/today/popularUser")
    public ResponseEntity getTopFollowers(HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return topFollowerService.getTopFollowers(accessToken);
    }
}
