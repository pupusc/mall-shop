package com.wanmi.sbc.marketing.cache;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.marketing.cache.handler.ICacheKeyHandler;
import com.wanmi.sbc.marketing.cache.handler.impl.GetDistributorByCustomerId;
import com.wanmi.sbc.marketing.cache.handler.impl.QueryDistributionStoreSetting;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EhCacheKeyGenerator implements KeyGenerator {

    public  final static Map<String,Class<? extends ICacheKeyHandler<String,Object>>> HANDLER_CONTAINER=new HashMap<>();

    static {
        HANDLER_CONTAINER.put("getDistributorByCustomerId", GetDistributorByCustomerId.class);
        HANDLER_CONTAINER.put("queryDistributionStoreSetting", QueryDistributionStoreSetting.class);

    }
    @Override
    public Object generate(Object targetClass, Method method, Object... params) {
        Map<String,Object> container = new HashMap<>();
        Class<?> targetClassClass = targetClass.getClass();
        Package pack = targetClassClass.getPackage();
        String clazz = targetClassClass.toGenericString();
        String methodName = method.getName();
        // 包名称
        container.put("package",pack);
        // 类地址
        container.put("class",clazz);
        // 方法名称
        container.put("methodName",methodName);
        Class<? extends ICacheKeyHandler<String,Object>> aClass = EhCacheKeyGenerator.HANDLER_CONTAINER.get(methodName);
        try {
            if (Objects.nonNull(aClass)){
                ICacheKeyHandler<String,Object> iCacheKeyHandler = aClass.newInstance();
                container = iCacheKeyHandler.handle(container, params);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 转为JSON字符串
        String jsonString = JSON.toJSONString(container);
        // 做SHA256 Hash计算，得到一个SHA256摘要作为Key
        return DigestUtils.sha256Hex(jsonString);
    }
}
