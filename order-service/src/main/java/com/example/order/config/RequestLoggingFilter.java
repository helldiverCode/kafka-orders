package com.example.order.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Generate unique request ID
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            // Log request details
            logger.info("Incoming Request: [{}] {} {} (Client IP: {})",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr()
            );

            // Continue with the filter chain
            filterChain.doFilter(requestWrapper, responseWrapper);

            // Log response details
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Request completed: [{}] {} {} - Status: {} (Duration: {}ms)",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                responseWrapper.getStatus(),
                duration
            );

        } finally {
            MDC.remove("requestId");
            responseWrapper.copyBodyToResponse();
        }
    }
} 