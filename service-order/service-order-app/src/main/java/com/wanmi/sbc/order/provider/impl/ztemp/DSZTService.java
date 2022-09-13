package com.wanmi.sbc.order.provider.impl.ztemp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.ShaUtil;
import com.wanmi.sbc.order.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class DSZTService {

    @Value("${fandeng.host}")
    private String host;
    @Value("${fandeng.appid}")
    private String appid;
    @Value("${fandeng.appsecret}")
    private String appsecret;

    @Autowired
    private RedisService redisService;

    /**
     * 获取Access Token url
     */
    public static final String OAUTH_TOKEN_URL = "/oauth/token?grant_type=client_credentials&client_id=%s&client_secret=%s";

    /**
     * 调用樊登接口公共参数
     */
    public static final String PARAMETER = "?appid=%s&sign=%s&access_token=%s";

    /**
     * 樊登token 失效时间
     */
    public static final Long FANDENG_TIME = 7000L;

    public String doRequest(String url, String body) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(body)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return doPost(jointUrl(url + PARAMETER, body), body);
    }

    public <T> T doRequest(String url, String body, Class<T> clazz) {
        String result = doRequest(url, body);
        return JSON.parseObject(result, clazz);
    }

    public <T> T doRequestOnlyData(String url, String body, Class<T> clazz) {
        String result = doRequest(url, body);
        return exchange(result, clazz);
    }

    /**
     * 除免登接口其他调用接口统一入口
     */
    private String doPost(String url, String body) {
        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, url, getHeaders(), null, body);
            if (HttpStatus.SC_UNAUTHORIZED == httpResponse.getStatusLine().getStatusCode()) {
                redisService.delete(appid);
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
     * 获取樊登token
     */
    private String getAccessToken() {
        String accessToken = redisService.getString(appid);

        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }

        String url = String.format(OAUTH_TOKEN_URL, appid, appsecret);
        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, url, getHeaders(), null, null);

            if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
                log.warn("樊登读书接口响应异常，请求接口:{}, 请求参数:{}, 返回状态:{}", url, "", httpResponse.getStatusLine().getStatusCode());
                throw new SbcRuntimeException("K-120802", "樊登读书接口响应异常");
            }
            String entity = EntityUtils.toString(httpResponse.getEntity());
            JSONObject object = JSONObject.parseObject(entity);

            if (StringUtils.isNotBlank(object.getString("error"))) {
                log.warn("获取樊登AccessToken失败异常:{}", entity);
                throw new SbcRuntimeException("K-120802", "获取樊登读书AccessToken失败");
            }

            //获取到token
            accessToken = object.getString("value");
            redisService.setString(appid, accessToken, FANDENG_TIME);
            return accessToken;
        } catch (Exception e) {
            log.error("获取樊登读书token发生异常");
            throw new SbcRuntimeException(e);
        }
    }

    /**
     * 拼接url参数  并生成签名
     */
    private String jointUrl(String url, String body) {
        try {
            String encryptSHA1 = ShaUtil.encryptSHA1(body + appid, appsecret);
            return String.format(url, appid, encryptSHA1, getAccessToken());
        } catch (Exception e) {
            log.error("拼接加密参数:{}", e);
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

    /**
     * 转换成返回对象
     */
    private <T> T exchange(String body, Class<T> clazz) {
        JSONObject object = JSONObject.parseObject(body);
        String code = (String) object.get("status");

        if (Objects.nonNull(code) && !"0000".equals(code)) {
            throw new SbcRuntimeException("K-120801", (String) object.get("msg"));
        }

        JSONObject data = object.getJSONObject("data");
        return JSON.parseObject(data.toString(), clazz);
    }
}
