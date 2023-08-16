package com.ItsTime.ItNovation.config.jwt.filter;

import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // JwtAuthenticationFilter로 이동
        } catch (UnauthorizedException ex) {
            logger.info(ex);
            request.setAttribute("Exception", ex);
            filterChain.doFilter(request, response);
        }


    }

}
