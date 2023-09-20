package com.ItsTime.ItNovation.controller.user;

import com.ItsTime.ItNovation.common.exception.ErrorCode;
import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.domain.user.dto.SignUpRequestDto;
import com.ItsTime.ItNovation.service.user.UserLoginStateService;
import com.ItsTime.ItNovation.service.user.UserProfileService;
import com.ItsTime.ItNovation.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name="계정 관련 API")
@RequestMapping("/api/v1/account")
public class AccountController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("회원가입");
        return userService.join(signUpRequestDto);
    }
    @GetMapping("/custom-logout")
    @Operation(summary = "로그 아웃")
    public ResponseEntity logout(HttpServletRequest request) {
        log.info("로그아웃");
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        if(accessToken.isEmpty()){
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }else{
            log.info("엑세스 토큰: {}" ,accessToken.get());
            return userService.logout(accessToken.get());
        }

    }



}
