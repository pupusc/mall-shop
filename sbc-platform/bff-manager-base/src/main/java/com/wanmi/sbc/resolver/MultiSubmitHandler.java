package com.wanmi.sbc.resolver;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重复提交靠按钮解决不是特别靠谱，后台加mysql锁也是不优（今后分布式了），
 * 使用redis分布式锁(setnx)解决
 * hash值 对象实例相同，默认的hash值相等(配合lombok的@Data对hashcode的重写)，通过hash值作为锁值校验入参(不要随便重写request hash()方法)
 * hash值超时时间（默认 5 s）
 */
@Aspect
@Component
@Slf4j
public class MultiSubmitHandler {
    /**
     * 锁的默认时间为5秒
     */
    private static final long REPEAT_LOCK_TIME = 5L;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommonUtil commonUtil;

    @Pointcut("@annotation(com.wanmi.sbc.common.annotation.MultiSubmit)")
    public void pointcut() {
    }

    /**
     * 防止重复提交的请求,请求之前的逻辑
     * 对 用户id + 请求参数的hashcode 加锁
     *
     * @param joinPoint 获取请求Request
     */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        Object[] objects = joinPoint.getArgs();
        if (objects.length == 1) {
            String key = getRedisKey(objects);
            if (redisService.setNx(key, "1", REPEAT_LOCK_TIME)) {
                log.info("submitting repeat check time : " + (System.currentTimeMillis() - start) + "ms, key:" + key);
            } else {
                log.error("submitting repeat: " + joinPoint.toLongString());
                throw new SbcRuntimeException(CommonErrorCode.REPEAT_REQUEST);
            }
        }
    }

    /**
     * 防止重复提交的请求,请求之后的逻辑
     * 释放锁
     *
     * @param joinPoint 获取请求Request
     * @param res       暂无用
     */
    @AfterReturning(pointcut = "pointcut()", returning = "res")
    public void afterReturning(JoinPoint joinPoint, Object res) {
        Object[] objects = joinPoint.getArgs();
        if (objects.length == 1) {
            String key = getRedisKey(objects);
            redisService.delete(key);
            log.info("submitting repeat lock released, key:" + key);
        }
    }

    /**
     * 获取防止相同请求重复提交的redis锁的key
     *
     * @param objects 请求参数
     * @return key redis锁的key
     */
    private String getRedisKey(Object[] objects) {
        String key = String.valueOf(objects[0].hashCode());
        String uid;
        try {
            uid = commonUtil.getOperatorId();
        } catch (Exception e) {
            uid = null;
        }
        if (StringUtils.isNotBlank(uid)) {
            key = "R:" + uid + ":" + key;
        } else {
            key = "R:" + key;
        }
        return key;
    }

}
