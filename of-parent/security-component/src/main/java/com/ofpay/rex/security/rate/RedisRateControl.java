package com.ofpay.rex.security.rate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.ofpay.rex.control.Base64;
import redis.clients.jedis.Jedis;

/**
 * User: Administrator
 * Date: 15-3-5
 * Time: 上午9:39
 */
public class RedisRateControl {


    /**
     * key前缀
     */
    private static final String TIMES = "times";

    private static final String script = "local newest = ARGV[1];" +
            "local period = ARGV[2];" +
            "local hit = ARGV[3];" +
            "local len = redis.call('LPUSH',KEYS[1],newest);" +
            "redis.call('EXPIRE',KEYS[1],period);" +
            "if len >= tonumber(hit) then " +
            " local oldest = redis.call('LINDEX',KEYS[1],-1);" +
            " redis.call('LTRIM',KEYS[1],0,hit-2);" +
            " local elapsed = newest - oldest;" +
            " if elapsed < period*1000 then " +
            "  return 1;" +
            " end;" +
            "end;" +
            "return 0;";

    private static Logger logger = LoggerFactory.getLogger(RedisRateControl.class);

    private static String sha;

    /**
     * @param redisTemplate
     * @param userKey       用户的唯一标识,比如uid,ip,或sessionid
     * @param urlKey        url,或类+方法名
     * @param hit           次数上限(由于最近hit次会存在redis中,所以请尽量配置小值,并且必须大于1)
     * @param period        周期时间,秒
     * @return
     */
    public static boolean checkRate(RedisTemplate redisTemplate, String userKey, String urlKey, int hit, int period) {
        final String[] args = new String[]{TIMES + userKey + "_" + Base64.encode(urlKey),
                String.valueOf(System.currentTimeMillis()),
                String.valueOf(period),
                String.valueOf(hit)};

        Long ret = null;
        try {
            if (sha == null) {
                sha = scriptLoad(redisTemplate, script);
            }
            try {
                ret = evalsha(redisTemplate, sha, 1, args);
            } catch (Exception e) {
                sha = scriptLoad(redisTemplate, script);
                ret = evalsha(redisTemplate, sha, 1, args);
            }
        } catch (Exception e) {
            logger.error("Redis rate control error:", e);
        }

        if (ret != null && ret == 1) {
            return true;
        }
        return false;
    }

    private static <T> T scriptLoad(RedisTemplate redisTemplate, final String script) {
        return (T) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return ((Jedis) connection.getNativeConnection()).scriptLoad(script);
            }
        });
    }

    private static <T> T evalsha(RedisTemplate redisTemplate, final String sha, final int keycount, final String... args) {
        return (T) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return ((Jedis) connection.getNativeConnection()).evalsha(sha, keycount, args);
            }
        });
    }
}
