package com.wanmi.sbc.util;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * jwt工具类
 * Created by wj on 2017/5/25.
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret-key}")
    private String key;

    public Claims validate(String token) {

        try {
            final Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            logger.debug("JwtFilter out ['Authorization success']");
            return claims;
        } catch (final SignatureException | MalformedJwtException | ExpiredJwtException e) {
            logger.info("JwtFilter exception, exMsg:{}", e.getMessage());
            throw new SignatureException("Invalid token.");
        }
    }


}
