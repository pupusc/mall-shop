package com.wanmi.sbc.order.open;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.ShaUtil;
import com.wanmi.sbc.order.open.model.OrderDeliverInfoResDTO;
import com.wanmi.sbc.order.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class ExternalService {

    @Value("${fandeng.host}")
    private String host;
    @Value("${fandeng.appid}")
    private String appid;
    @Value("${fandeng.appsecret}")
    private String appsecret;
    @Value("${fandeng.callback.deliver}")
    private static String callbackDeliver;

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

    public void callbackForDeliver(@Valid OrderDeliverInfoResDTO request) {
        String body = JSON.toJSONString(request);
        String result = doRequest(buildUrl(callbackDeliver + PARAMETER, body), body);

        JSONObject object = JSONObject.parseObject(result);
        if (!"0000".equals(object.getString("status"))) {
            throw new SbcRuntimeException("K-120801", (String) object.get("msg"));
        }
    }

    /**
     * 除免登接口其他调用接口统一入口
     */
    private String doRequest(String url, String body) {
        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, url, getHeads(), null, body);
            if (HttpStatus.SC_UNAUTHORIZED ==  httpResponse.getStatusLine().getStatusCode()) {
                redisService.delete(appid);
                httpResponse = HttpUtil.doPost(host, url, getHeads(), null, body);
            }

            String entity = EntityUtils.toString(httpResponse.getEntity());
            log.info("樊登请求接口：{},请求参数{},返回状态：{}", url, body, entity);

            if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "发货状态回调通知发生错误");
            }
            return entity;
        } catch (Exception e) {
            log.error("发货状态回调通知发生错误");
            throw new SbcRuntimeException(e);
        }
    }

    /**
     * 获取樊登token
     *
     * @return
     */
    private String getAccessToken() {
        String accessToken = redisService.getString(appid);

        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }

        String url = String.format(OAUTH_TOKEN_URL, appid, appsecret);
        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, url, getHeads(), null, null);
            if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "获取樊登AccessToken失败异常");
            }

            String responseStr = EntityUtils.toString(httpResponse.getEntity());
            JSONObject object = JSONObject.parseObject(responseStr);

            log.info("获取樊登读书AccessToken返回 = {}", responseStr);

            if (StringUtils.isNotBlank(object.getString("error"))) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "获取樊登AccessToken失败");
            }

            //获取到token
            accessToken = object.getString("value");
            redisService.setString(appid, accessToken, FANDENG_TIME);
        } catch (Exception e) {
            log.error("获取樊登AccessToken失败异常");
            throw new SbcRuntimeException(e);
        }
        return accessToken;
    }

    /**
     * 拼接url参数  并生成签名
     *
     * @param url
     * @return
     */
    private String buildUrl(String url, String body) {
        try {
            String encryptSHA1 = ShaUtil.encryptSHA1(body + appid, appsecret);
            String realUrl = String.format(url, appid, encryptSHA1, getAccessToken());
            return realUrl;
        } catch (Exception e) {
            log.error("拼接加密参数:{}", e);
        }
        return null;
    }

    /**
     * 获取请求头参数
     *
     * @return
     */
    public Map<String, String> getHeads() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        return headers;
    }
}
