package com.wanmi.sbc.wx.mini.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.wx.mini.bean.request.WxAddProductRequest;
import com.wanmi.sbc.wx.mini.bean.response.WxAccessTokenResponse;
import com.wanmi.sbc.wx.mini.bean.response.WxAddProductResponse;
import com.wanmi.sbc.wx.mini.bean.response.WxResponseBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WxService {

    private static final String ACCESS_TOKEN_REDIS_KEY = "WX_ACCESS_TOKEN";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String ADD_PRODUCT_URL = "https://api.weixin.qq.com/shop/spu/add";
    private static final String UPDATE_PRODUCT_WITHOUT_AUDIT_URL = "https://api.weixin.qq.com/shop/spu/update_without_audit";

    @Value("${wx.appid}")
    private String wxAppid;
    @Value("${wx.appsecret}")
    private String wxAppsecret;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    public boolean uploadGoodsToWx(WxAddProductRequest addProductRequest){

        String accessToken = getAccessToken();
        String url = ADD_PRODUCT_URL.concat("?access_token=").concat(accessToken);

        String reqJsonStr = JSONObject.toJSONString(addProductRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, headers);
        WxAddProductResponse wxAddProductResponse = sendRequest(url, HttpMethod.POST, entity, WxAddProductResponse.class);

        if(!wxAddProductResponse.isSuccess()){
            log.error("添加微信商品失败:{}", wxAddProductResponse.getErrmsg());
            return false;
        }
        return true;
    }

    public boolean updateGoodsWithoutAudit(){
        return false;
    }

    private String getAccessToken(){

        Object token = redisTemplate.opsForValue().get(ACCESS_TOKEN_REDIS_KEY);
        if(token != null) return (String) token;

        String requestUrl = ACCESS_TOKEN_URL.concat("?grant_type=client_credential").concat("&appid=").concat(wxAppid)
                .concat("&secret=").concat(wxAppsecret);
        WxAccessTokenResponse wxAccessTokenResponse = sendRequest(requestUrl, HttpMethod.GET, null, WxAccessTokenResponse.class);
        redisTemplate.opsForValue().set(ACCESS_TOKEN_REDIS_KEY, wxAccessTokenResponse.getAccessToken(), wxAccessTokenResponse.getExpiresIn() - 30L, TimeUnit.SECONDS);
        return wxAccessTokenResponse.getAccessToken();
    }

    private <T> T sendRequest(String url, HttpMethod method, HttpEntity httpEntity, Class<T> clazz){
        log.info("请求地址:{},参数:{}", url, httpEntity == null?"": JSONObject.toJSON(httpEntity.getBody()));
        ResponseEntity<WxResponseBase> exchange = restTemplate.exchange(url, method, httpEntity, WxResponseBase.class);
        WxResponseBase response = exchange.getBody();
        log.info("响应:{}", JSONObject.toJSON(response));
        if(!response.isSuccess()){
            log.error("微信获取token失败:{}", response.getErrmsg());
            return null;
        }
        return (T) clazz;
    }
}
