package com.app.ratelimiter.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RateLimitFilter implements Filter {
    private final RateLimitService rateLimitingService;

    @Autowired
    public RateLimitFilter(RateLimitService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String clientIP = getRemoteIpAddress((HttpServletRequest) servletRequest);

        if (!rateLimitingService.isRequestAllowed(clientIP)) {
            ((HttpServletResponse) servletResponse).sendError(429);
            rateLimitingService.updateRequest(clientIP);
            return;
        }
        rateLimitingService.updateRequest(clientIP);
        filterChain.doFilter(servletRequest, servletResponse);

    }

    private String getRemoteIpAddress(HttpServletRequest request) {
        return  request.getRemoteAddr();
    }

    @Override
    public void destroy() {

    }
}