package com.ItsTime.ItNovation.config.jwt.controller;

import com.ItsTime.ItNovation.common.exception.ErrorCode;
import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reissue")
@AllArgsConstructor
public class ReIssueTokenController {
    @GetMapping()
    public ResponseEntity reissueToken() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
