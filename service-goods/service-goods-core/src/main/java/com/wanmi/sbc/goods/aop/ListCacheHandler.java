package com.wanmi.sbc.goods.aop;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.annotation.ListCache;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 列表缓存处理器
 */
@Aspect
@Component
@Slf4j
public class ListCacheHandler {

    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(com.wanmi.sbc.common.annotation.ListCache)")
    public void pointcut() {
    }

    /**
     *  列表缓存处理方法
     *  要求目标方法入参为字符串列表、出参为结果列表，且出入参顺序一致，查不到用null占位
     */
    @Around("pointcut() && @annotation(listCache)")
    public Object around(ProceedingJoinPoint joinPoint, ListCache listCache) throws Throwable {
        Object[] args = joinPoint.getArgs();

        if (args.length == 1 && args[0] instanceof List) {
            // 从缓存中查询存在的列表
            List<String> ids = (List<String>)args[0];
            List<String> cacheStrings = redisService.getMString(
                    ids.stream().map(i -> listCache.prefix() + ':' + i).collect(Collectors.toList()));

            // 转化已缓存对象、过滤出不在缓存中的ids
            List caches = new ArrayList();
            List<String> noCacheIds = new ArrayList<>();
            for (int i = 0; i < cacheStrings.size(); i++) {
                if (StringUtils.isNotEmpty(cacheStrings.get(i))) {
                    caches.add(JSON.parseObject(cacheStrings.get(i), listCache.clazz()));
                } else {
                    noCacheIds.add(ids.get(i));
                }
            }
            if (CollectionUtils.isEmpty(noCacheIds)) {
                return caches;
            }

            // 查询不在缓存中的数据，并放入缓存
            args[0] = noCacheIds;
            List results = (List)joinPoint.proceed(args);
            Map<String, String> keyValues = new HashMap<>();
            for (int idx = 0; idx < noCacheIds.size(); idx++) {
                Object item = results.get(idx);
                if (Objects.nonNull(item)) {
                    keyValues.put(listCache.prefix() + ':' + noCacheIds.get(idx), JSON.toJSONString(item));
                }
            }
            log.info("multi cache keys: " + JSON.toJSONString(keyValues.keySet()));
            redisService.setMString(keyValues, listCache.seconds());

            results.forEach(i -> {
                if (Objects.nonNull(i)) caches.add(i);
            });
            return caches;
        }
        return joinPoint.proceed(args);
    }

}
