package com.ofpay.rex.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Stack;

/**
 * 限制请求频率，每秒请求一定的阔值
 *
 * @author of546
 */
public class RequestRateThrottleFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestRateThrottleFilter.class);

    private int hits = 5;    // 一定时间内次数

    private int period = 10; // 一定时间(秒)

    private static final String HITS = "hits";

    private static final String PERIOD = "period";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        synchronized (session.getId().intern()) {
            Stack<Date> times = (Stack<Date>) session.getAttribute("times");
            if (times == null) {
                times = new Stack<Date>();
                times.push(new Date(0));
                session.setAttribute("times", times);
            }
            times.push(new Date());
            if (times.size() >= hits) {
                times.removeElementAt(0);
            }
            Date newest = times.get(times.size() - 1);
            Date oldest = times.get(0);
            long elapsed = newest.getTime() - oldest.getTime();
            if (elapsed < period * 1000) // seconds
            {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("请求频率过高");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

}