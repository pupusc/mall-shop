package com.ofpay.rex.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 点击劫持
 * @author of546
 */
public class ClickjackFilter extends OncePerRequestFilter {

    private String mode = "DENY";

    public void setMode(String configMode) {
        if ( configMode != null && ( configMode.equals( "DENY" ) || configMode.equals( "SAMEORIGIN" ) ) ) {
            this.mode = configMode;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
        response.addHeader("X-FRAME-OPTIONS", mode);
    }

}
