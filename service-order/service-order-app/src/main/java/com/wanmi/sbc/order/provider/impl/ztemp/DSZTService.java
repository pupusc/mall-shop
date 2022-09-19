package com.wanmi.sbc.order.provider.impl.ztemp;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DSZTService {
    private String host = "https://gateway-api.dushu365.com";

    public String doRequest(String url, String body) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(body)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return doPost(url, body);
    }

    public <T> T doRequest(String url, String body, Class<T> clazz) {
        String result = doRequest(url, body);
        return JSON.parseObject(result, clazz);
    }

    /**
     * 除免登接口其他调用接口统一入口
     */
    private String doPost(String url, String body) {
        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, url, getHeaders(), null, body);
            if (HttpStatus.SC_UNAUTHORIZED == httpResponse.getStatusLine().getStatusCode()) {
                httpResponse = HttpUtil.doPost(host, url, getHeaders(), null, body);
            }

            if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
                log.warn("樊登读书接口响应异常，请求接口:{}, 请求参数:{}, 返回状态:{}", url, body, httpResponse.getStatusLine().getStatusCode());
                throw new SbcRuntimeException("K-120801", "樊登读书接口响应异常");
            }

            String entity = EntityUtils.toString(httpResponse.getEntity());
            log.info("樊登读书接口请求成功，请求接口:{}, 请求参数:{}, 返回结果:{}", url, body, entity);
            return entity;
        } catch (Exception e) {
            log.error("樊登读书接口调用异常");
            throw new SbcRuntimeException(e);
        }
    }

    /**
     * 获取请求头参数
     */
    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        return headers;
    }
}
