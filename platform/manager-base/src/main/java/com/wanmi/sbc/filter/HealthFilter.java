package com.wanmi.sbc.filter;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Liang Jun
 * @date 2022-03-08 02:25:00
 */
//@Order(-1000)
//@Component
public class HealthFilter extends OncePerRequestFilter {
    private static String HEALTH_URI = "/";
    private static String HEALTH_METHOD = "HEAD";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (HEALTH_URI.equals(request.getRequestURI()) && HEALTH_METHOD.equals(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("ok");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        filterChain.doFilter(request, response);
    }
}
