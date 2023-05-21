package com.ItsTime.ItNovation.controller.user;

import com.ItsTime.ItNovation.domain.user.dto.SignUpRequestDto;
import com.ItsTime.ItNovation.domain.user.dto.SignUpResponseDto;
import com.ItsTime.ItNovation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public SignUpResponseDto signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.join(signUpRequestDto);
    }

    @GetMapping("/profile")
    public String secondSignup() {
        return "리다이렉트";
    }
}
