package com.soybean.mall.wx.mini.service;

import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.wx.mini.common.bean.request.WxUploadImageRequest;
import com.soybean.mall.wx.mini.common.bean.response.WxUploadImageResponse;
import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.soybean.mall.wx.mini.goods.bean.response.*;
import com.soybean.mall.wx.mini.order.bean.request.*;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxPrePayOrderResponse;
import com.wanmi.sbc.common.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
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
    private static final String CREATE_ORDER_URL="https://api.weixin.qq.com/shop/order/add";
    private static final String ORDER_PAY_URL="https://api.weixin.qq.com/shop/order/pay";
    private static final String DELIVERY_SEND_URL="https://api.weixin.qq.com/shop/delivery/send";
    private static final String DELIVERY_RECEIVE_URL="https://api.weixin.qq.com/shop/delivery/recieve";
    private static final String AFTER_SALE_URL="https://api.weixin.qq.com/shop/aftersale/add";
    private static final String UPLOAD_IMG_URL="https://api.weixin.qq.com/shop/img/upload";
    private static final String PRE_PAY_TRANSATION_URL="https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
    private static final HttpHeaders defaultHeader;
    static {
        defaultHeader = new HttpHeaders();
        defaultHeader.setContentType(MediaType.APPLICATION_JSON);
    }

    @Value("${wx.mini.appid}")
    private String wxAppid;
    @Value("${wx.mini.appsecret}")
    private String wxAppsecret;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${wx.mini.merchantId}")
    private String merchantId;

    public String getPhoneNumber(String code){
        String url = GET_PHONE_NUMBER_URL.concat("?access_token=").concat(getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("{\"code\":\"" + code + "\"}", defaultHeader);
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

    public WxAddProductResponse uploadGoodsToWx(WxAddProductRequest addProductRequest){
        String accessToken = getAccessToken();
        String url = ADD_PRODUCT_URL.concat("?access_token=").concat(accessToken);

        String reqJsonStr = JSONObject.toJSONString(addProductRequest);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, defaultHeader);
        return sendRequest(url, HttpMethod.POST, entity, WxAddProductResponse.class);
    }

    public boolean deleteGoods(WxDeleteProductRequest wxDeleteProductRequest){
        String url = DELETE_PRODUCT_URL.concat("?access_token=").concat(getAccessToken());
        String reqJsonStr = JSONObject.toJSONString(wxDeleteProductRequest);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, defaultHeader);
        WxResponseBase wxResponseBase = sendRequest(url, HttpMethod.POST, entity, WxResponseBase.class);
        if(wxResponseBase.isSuccess()) return true;
        return false;
    }

    public boolean updateGoodsWithoutAudit(WxUpdateProductWithoutAuditRequest wxUpdateProductWithoutAuditRequest){
        String url = UPDATE_PRODUCT_WITHOUT_AUDIT_URL.concat("access_token=").concat(getAccessToken());
        String reqJsonStr = JSONObject.toJSONString(wxUpdateProductWithoutAuditRequest);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, defaultHeader);
        WxUpdateProductWithoutAuditRequest response = sendRequest(url, HttpMethod.POST, entity, WxUpdateProductWithoutAuditRequest.class);
        if(response.isSuccess()) return true;
        return false;
    }

    public String getAccessToken(){
        Object token = redisTemplate.opsForValue().get(ACCESS_TOKEN_REDIS_KEY);
        if(token != null) return (String) token;

        String requestUrl = ACCESS_TOKEN_URL.concat("?grant_type=client_credential").concat("&appid=").concat(wxAppid)
                .concat("&secret=").concat(wxAppsecret);
        WxAccessTokenResponse wxAccessTokenResponse = sendRequest(requestUrl, HttpMethod.GET, null, WxAccessTokenResponse.class);
        redisTemplate.opsForValue().set(ACCESS_TOKEN_REDIS_KEY, wxAccessTokenResponse.getAccessToken(), 3500, TimeUnit.SECONDS);
        return wxAccessTokenResponse.getAccessToken();
    }

    private <T extends WxResponseBase> T sendRequest(String url, HttpMethod method, HttpEntity httpEntity, Class<T> clazz){
        log.info("请求地址:{},参数:{}", url, httpEntity == null?"" : JSONObject.toJSONString(httpEntity.getBody()));
        ResponseEntity<String> exchange = restTemplate.exchange(url, method, httpEntity, String.class);
        String response = exchange.getBody();
        log.info("响应:{}", response);
        T t = JSONObject.parseObject(response, clazz);
        if(!t.isSuccess()){
            log.error("微信api请求失败:{}", t.getErrmsg());
        }
        return t;
    }

    public WxUploadImageResponse uploadImg(WxUploadImageRequest wxUploadImageRequest){
        String url = UPLOAD_IMG_URL.concat("?access_token=").concat(getAccessToken());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<>();
        params.add("resp_type", 1);
        params.add("upload_type", 1);
        params.add("img_url", wxUploadImageRequest.getImgUrl());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, header);
        return sendRequest(url, HttpMethod.POST, entity, WxUploadImageResponse.class);
    }

    /**
     * 生成订单并获取ticket
     * @param createOrderRequest
     * @return
     */
    public WxCreateOrderResponse createOrder(WxCreateOrderRequest createOrderRequest){
        String accessToken = getAccessToken();
        String url = CREATE_ORDER_URL.concat("?access_token=").concat(accessToken);

        String reqJsonStr = JSONObject.toJSONString(createOrderRequest);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, defaultHeader);
        return sendRequest(url, HttpMethod.POST, entity, WxCreateOrderResponse.class);
    }

    /**
     * 同步订单支付结果
     * @param request
     * @return
     */
    public WxResponseBase orderPay(WxOrderPayRequest request){
        String accessToken = getAccessToken();
        String url = ORDER_PAY_URL.concat("?access_token=").concat(accessToken);

        String reqJsonStr = JSONObject.toJSONString(request);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, defaultHeader);
        return sendRequest(url, HttpMethod.POST, entity, WxResponseBase.class);
    }

    /**
     * 订单发货
     * @param request
     * @return
     */
    public WxResponseBase deliverySend(WxDeliverySendRequest request){
        String accessToken = getAccessToken();
        String url = DELIVERY_SEND_URL.concat("?access_token=").concat(accessToken);

        String reqJsonStr = JSONObject.toJSONString(request);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, defaultHeader);
        return sendRequest(url, HttpMethod.POST, entity, WxResponseBase.class);
    }

    /**
     * 订单确认收获
     * @param request
     * @return
     */
    public WxResponseBase deliveryReceive(WxDeliveryReceiveRequest request){
        String accessToken = getAccessToken();
        String url = DELIVERY_RECEIVE_URL.concat("?access_token=").concat(accessToken);

        String reqJsonStr = JSONObject.toJSONString(request);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, defaultHeader);
        return sendRequest(url, HttpMethod.POST, entity, WxResponseBase.class);
    }

    /**
     * 售后
     * @param request
     * @return
     */
    public WxResponseBase createAfterSale(WxCreateAfterSaleRequest request){
        String accessToken = getAccessToken();
        String url = AFTER_SALE_URL.concat("?access_token=").concat(accessToken);

        String reqJsonStr = JSONObject.toJSONString(request);
        HttpEntity<String> entity = new HttpEntity<>(reqJsonStr, defaultHeader);
        return sendRequest(url, HttpMethod.POST, entity, WxResponseBase.class);
    }



}
