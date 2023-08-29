package com.ItsTime.ItNovation.config.jwt.service;

import com.ItsTime.ItNovation.common.exception.ErrorCode;

import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Getter
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    public static final Set<String> logoutTokens = new HashSet<>(); // 로그아웃된 토큰 목록
    public void logout(String token) {
        logoutTokens.add(token); // 로그아웃된 토큰을 블랙리스트나 로그아웃 목록에 추가
    }

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email) {
        log.info("accessToken 생성");
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }


    public void sendAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("accessToken", accessToken);
        jsonResponse.put("refreshToken", refreshToken);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(jsonResponse.toString());
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        log.info("accessToken 추출");
        try {
            Optional<String> token= Optional.ofNullable(request.getHeader(accessHeader))
                    .filter(accessToken -> accessToken.startsWith(BEARER))
                    .map(accessToken -> accessToken.replace(BEARER, ""));
            if (logoutTokens.contains(token)) {
                throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
            } else return token;
        } catch (JWTDecodeException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }catch(TokenExpiredException e){
            log.error(e.getMessage());
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }

    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (JWTDecodeException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }catch(TokenExpiredException e){
            log.error(e.getMessage());
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }
    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () ->{
                            log.info("일치하는 회원이 없습니다");
                        }
                );
    }

    public boolean isTokenValid(String token) {
        if (logoutTokens.contains(token)) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (JWTDecodeException | IllegalArgumentException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

    }



}
