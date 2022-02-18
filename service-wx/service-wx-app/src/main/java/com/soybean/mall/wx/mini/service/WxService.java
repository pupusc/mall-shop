package com.soybean.mall.wx.mini.service;

import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.wx.mini.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.bean.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WxService {

    private static final String ACCESS_TOKEN_REDIS_KEY = "WX_ACCESS_TOKEN";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String ADD_PRODUCT_URL = "https://api.weixin.qq.com/shop/spu/add";
    private static final String DELETE_PRODUCT_URL = "https://api.weixin.qq.com/shop/spu/del";
    private static final String UPDATE_PRODUCT_WITHOUT_AUDIT_URL = "https://api.weixin.qq.com/shop/spu/update_without_audit";
    private static final String GET_PHONE_NUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";
    private static final String GET_OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Value("${wx.mini.appid}")
    private String wxAppid;
    @Value("${wx.mini.appsecret}")
    private String wxAppsecret;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    public String getPhoneNumber(String code){
        String url = GET_PHONE_NUMBER_URL.concat("?access_token=").concat(getAccessToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"code\":\"" + code + "\"}", headers);
        WxGetPhoneNumberResponse wxGetPhoneNumberResponse = sendRequest(url, HttpMethod.POST, entity, WxGetPhoneNumberResponse.class);
        if(wxGetPhoneNumberResponse.isSuccess()){
            return wxGetPhoneNumberResponse.getPhoneInfo().getPurePhoneNumber();
        }
        return null;
    }

    public WxGetOPenIdResponse getOpenId(String code){
        String url = GET_OPEN_ID_URL.concat("?appid=").concat(wxAppid).concat("&secret=").concat(wxAppsecret).concat("&js_code=")
                .concat(code).concat("&grant_type=authorization_code");
        WxGetOPenIdResponse wxGetOPenIdResponse = sendRequest(url, HttpMethod.GET, null, WxGetOPenIdResponse.class);
        if(wxGetOPenIdResponse.isSuccess()){
            return wxGetOPenIdResponse;
        }
        return null;
    }

    public boolean uploadGoodsToWx(WxAddProductRequest addProductRequest){
        String accessToken = getAccessToken();
        String url = ADD_PRODUCT_URL.concat("?access_token=").concat(accessToken);

        String reqJsonStr = JSONObject.toJSONString(addProductRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, headers);
        WxAddProductResponse wxAddProductResponse = sendRequest(url, HttpMethod.POST, entity, WxAddProductResponse.class);

        if(!wxAddProductResponse.isSuccess()){
            return false;
        }
        return true;
    }

    public boolean deleteGoods(WxDeleteProductRequest wxDeleteProductRequest){
        String url = DELETE_PRODUCT_URL.concat("?access_token=").concat(getAccessToken());
        String reqJsonStr = JSONObject.toJSONString(wxDeleteProductRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, headers);
        WxResponseBase wxResponseBase = sendRequest(url, HttpMethod.POST, entity, WxResponseBase.class);
        if(wxResponseBase.isSuccess()) return true;
        return false;
    }

    public boolean updateGoodsWithoutAudit(){
        return false;
    }

    public String getAccessToken(){

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
            log.error("微信api请求失败:{}", response.getErrmsg());
            return null;
        }
        return (T) clazz;
    }
}
