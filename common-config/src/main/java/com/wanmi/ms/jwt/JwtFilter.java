package com.wanmi.ms.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JWT认证过滤器
 * Created by aqlu on 15/11/20.
 */
public class JwtFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private static final String JWT_SECRET_KEY = "JWT_SECRET_KEY";

    private static final String EXCLUDED_URLS_KEY = "EXCLUDED_URLS_KEY";

    private static final String SEPARATOR = ",";

    private static final String JWT_HEADER_KEY = "Authorization";

    private String jwtSecretKey;

    // 排除精确路径列表
    private List<String> excludedFullPath = new ArrayList<>();

    // 排除文件夹路径列表
    private List<String> excludedDirPath = new ArrayList<>();

    // 排除后缀路径列表
    private List<String> excludedSuffixPath = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("*.das".replace("*.", ""));
    }

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        String jwtSecretKey = this.getFilterConfig().getInitParameter(JWT_SECRET_KEY);
        Assert.hasText(jwtSecretKey, "JwtFilter has not config init parameter [JWT_SECRET_KEY]");

        String excludedUrlStr = this.getFilterConfig().getInitParameter(EXCLUDED_URLS_KEY);
        if(org.springframework.util.StringUtils.hasText(excludedUrlStr)) {
            String[] tempExcludedURLs = excludedUrlStr.split(SEPARATOR);

            for (String tempExcludedURL : tempExcludedURLs) {
                // 不允许出现"/"开头，后面还跟有”*.xxx“的地址配置
                if(tempExcludedURL.contains("*.") && tempExcludedURL.contains("/")) {
                    throw new IllegalArgumentException("Init param [EXCLUDED_URLS_KEY] was wrong. param: " + tempExcludedURL);
                }

                if(tempExcludedURL.startsWith("/") && tempExcludedURL.endsWith("*")){ // 目录匹配
                    excludedDirPath.add(tempExcludedURL.replace("*", "").trim());
                }else if (tempExcludedURL.contains("*.")) { //后缀匹配
                    excludedSuffixPath.add(tempExcludedURL.replace("*.", "").trim());
                }else {
                    excludedFullPath.add(tempExcludedURL.trim()); // 全路径匹配
                }
            }
        }


        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        String requestURI = request.getRequestURI();

        logger.debug("JwtFilter in ..., requestURI:{}", requestURI);

        // 排除精确路径列表 包含此uri, 直接忽略
        if (excludedFullPath.contains(requestURI) || isMatchSuffix(requestURI) || isMatchDir(requestURI)) {
            chain.doFilter(req, res);
        }else {

            final String authHeader = request.getHeader(JWT_HEADER_KEY);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.info("JwtFilter out ['Missing or invalid Authorization header']");
                throw new SignatureException("Missing or invalid Authorization header.");
            }

            final String token = authHeader.substring(7); // The part after "Bearer "

            try {
                final Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
                logger.debug("JwtFilter out ['Authorization success']");
            } catch (final SignatureException | MalformedJwtException | ExpiredJwtException e) {
                logger.info("JwtFilter exception, exMsg:{}", e.getMessage());
                throw new SignatureException("Invalid token.");
            }

            chain.doFilter(req, res);
        }
    }

    private boolean isMatchSuffix(String requestURI) {
        // 获取请求URI的后缀
        String[] reqArr = requestURI.split("\\.");
        if(reqArr.length != 2){
            return false;
        }
        String requestURISuffix = reqArr[1];

        return excludedSuffixPath.contains(requestURISuffix);
    }

    private boolean isMatchDir(String requestURI) {
        for(String excludePath : excludedDirPath) {
            // 判断请求路径是否以排除的路径开头
            if (requestURI.startsWith(excludePath)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    private void writeResponse(HttpServletResponse response, Map<String, Object> responseData) {
        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), responseData);
        } catch (IOException e) {
            logger.info("write response failed. response:{}", responseData);
        }
    }
}
