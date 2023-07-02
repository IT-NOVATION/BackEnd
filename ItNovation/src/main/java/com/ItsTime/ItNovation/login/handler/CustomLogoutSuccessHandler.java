package com.ItsTime.ItNovation.login.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그아웃 성공 메시지를 생성
        String message = "로그아웃이 성공적으로 처리되었습니다.";
        response.setContentType("text/plain");

        // 응답 본문에 메시지를 포함하여 설정
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(message);
    }
}
