package com.ItsTime.ItNovation.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;
    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver")HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("error entry point");
        resolver.resolveException(request,response,null, (Exception) request.getAttribute("Exception"));
    }
}
