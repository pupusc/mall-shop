package com.wanmi.sbc.goods.fandeng;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.ShaUtil;
import com.wanmi.sbc.goods.api.request.common.ImageAuditRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsImageAuditResponse;
import com.wanmi.sbc.goods.redis.RedisService;
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

@Slf4j
@Service
public class FddsOpenPlatformService {

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
     * 图片
     */
    public static final String AUDIT_IMAGE_URL = "/content/verifyImg";



    /**
     * 调用樊登接口公共参数
     */
    public static final String PARAMETER = "?appid=%s&sign=%s&access_token=%s";

    /**
     * 樊登token 失效时间
     */
    public static final Long FANDENG_TIME = 7000L;


    public BaseResponse<GoodsImageAuditResponse> aduitImage(@Valid ImageAuditRequest request) throws Exception {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(AUDIT_IMAGE_URL + PARAMETER, body), JSON.toJSONString(request));
        GoodsImageAuditResponse response =
                (GoodsImageAuditResponse) exchange(result, GoodsImageAuditResponse.class);
        return BaseResponse.success(response);
    }

    /**
     * 除免登接口其他调用接口统一入口
     */
    private String getUrl(String url, String body) {
        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, url, getHeaders(), null, body);
            if (HttpStatus.SC_UNAUTHORIZED == httpResponse.getStatusLine().getStatusCode()) {
                redisService.delete(appid);
                httpResponse = HttpUtil.doPost(host, url, getHeaders(), null, body);
            }
            log.info("樊登请求接口直接返回：{}", httpResponse);
            log.info("樊登请求接口：{},请求参数{}", url, body);

            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
//                log.info("请求接口：{},请求参数：{}", host + url, body);
                String entity = EntityUtils.toString(httpResponse.getEntity());
                log.info("樊登请求接口：{},请求参数{},返回状态：{}", url, body, entity);
                return entity;
            }
        } catch (Exception e) {
            log.error("樊登接口调用异常：{}", e);
        }
        throw new SbcRuntimeException("K-120801", "樊登接口异常");
    }

    /**
     * 获取樊登token
     *
     * @return
     */
    private String getAccessToken() {
        String accessToken = redisService.getString(appid);
//        String accessToken = "";
        if (StringUtils.isBlank(accessToken)) {
            String url = String.format(OAUTH_TOKEN_URL, appid, appsecret);
            try {
                HttpResponse httpResponse = HttpUtil.doPost(host,
                        url, getHeaders(), null, null);
                if (200 == httpResponse.getStatusLine().getStatusCode()) {
                    String entity = EntityUtils.toString(httpResponse.getEntity());
                    JSONObject object = JSONObject.parseObject(entity);
                    String error = object.getString("error");
                    if (StringUtils.isBlank(error)) {
                        //获取到token
                        accessToken = object.getString("value");
                        redisService.setString(appid, accessToken, FANDENG_TIME);
                        return accessToken;
                    }
                    log.error("获取樊登AccessToken失败异常:{}", entity);
                }
            } catch (Exception e) {
                log.error("获取樊登token:{}", e);
            }
            throw new SbcRuntimeException("K-120802");
        }
        return accessToken;
    }

    /**
     * 拼接url参数  并生成签名
     *
     * @param url
     * @return
     */
    private String jointUrl(String url, String body) {
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
     */
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        return headers;
    }

    /**
     * 转换成返回对象
     *
     * @param body
     * @param t
     * @return
     */
    private Object exchange(String body, Class t) {
        JSONObject object = JSONObject.parseObject(body);
        String code = (String) object.get("status");
        if (code == null || (code != null && code.equals("0000"))) {
            JSONObject data = (JSONObject) object.get("data");
            Object parseObject = JSON.parseObject(data.toString(), t);
            return parseObject;
        }
        throw new SbcRuntimeException("K-120801", (String) object.get("msg"));
    }

    /**
     * 转换成返回对象
     *
     * @param body
     * @param t
     * @return
     */
    private Boolean exchangeBoolean(String body) {
        JSONObject object = JSONObject.parseObject(body);
        String code = (String) object.get("status");
        if (code == null || (code != null && code.equals("0000"))) {
            Boolean data = (Boolean) object.get("data");
            return data;
        }
        throw new SbcRuntimeException("K-120801", (String) object.get("msg"));
    }
}
