package com.soybean.mall.common.spring.filter;

import com.soybean.mall.common.spring.manager.TraceIdManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Liang Jun
 * @date 2022-04-20 23:25:00
 */
@Order(Integer.MIN_VALUE)
@Component
public class TraceIdFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        TraceIdManager.entrySpan(request, response);
        filterChain.doFilter(request, response);
        TraceIdManager.finishSpan(request, response);
    }
}
