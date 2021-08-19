package com.wanmi.sbc.util;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SpecialSymbols;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;

import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.provider.wechatshareset.WechatShareSetQueryProvider;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetInfoByStoreIdRequest;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetInfoRequest;
import com.wanmi.sbc.setting.api.response.wechatshareset.WechatShareSetInfoResponse;
import com.wanmi.sbc.setting.api.response.wechatshareset.WechatTokenResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class MiniProgramUtil {
    private static final Logger log = LoggerFactory.getLogger(MiniProgramUtil.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private   RedisService redisService;

    @Autowired
    private   WechatShareSetQueryProvider wechatShareSetQueryProvider;

    @Autowired
    private  CommonUtil commonUtil;

    private static String TOKEN_KEY = CacheKeyConstant.WE_CHAT + SpecialSymbols.COLON + "TOKEN";
    private String appId ="";

    private  String appSecret="";



     //微信公众号access_token
    public  void getSign() {
        //获取appId,secret
        Long storeId = commonUtil.getStoreId();
        if (Objects.nonNull(storeId)) {
            BaseResponse<WechatShareSetInfoResponse> setInfoResponse = wechatShareSetQueryProvider.getInfoByStoreId(WechatShareSetInfoByStoreIdRequest
                    .builder()
                    .storeId(storeId)
                    .build());
            if (Objects.nonNull(setInfoResponse)
                    && Objects.nonNull(setInfoResponse.getContext())) {
                appId = setInfoResponse.getContext().getShareAppId();
                appSecret = setInfoResponse.getContext().getShareAppSecret();
            } else {
                throw new SbcRuntimeException(CommonErrorCode.METHOD_NOT_ALLOWED);
            }
        } else {
            WechatShareSetInfoResponse infoResponse = wechatShareSetQueryProvider.getInfo(WechatShareSetInfoRequest.builder().
                    operatePerson(commonUtil.getOperatorId()).build()).getContext();
            appId = infoResponse.getShareAppId();
            appSecret = infoResponse.getShareAppSecret();
        }
    }


    public  String getToken() {
     return wechatAuthProvider.getMiniProgramAccessToken().getContext().getAccessToken();
       /* String token = redisService.getString(TOKEN_KEY);
        if (!StringUtils.isBlank(token)) {
            return token;
        }*/
        //获取小程序参数配置
/*        String result = wechatAuthProvider.getMiniProgramSet().getContext().getContext();
        JSONObject jsonObject = JSONObject.parseObject(result);
        appId = jsonObject.getString("appId");
        appSecret = jsonObject.getString("appSecret");
        String token ="";
        // getSign();
        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("secret", appSecret);
        WechatTokenResponse response = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}", WechatTokenResponse.class, map);
        if (response.getErrcode() != null) {
            log.error("获得微信token异常：请求参数：{},response:{}", map.toString(), response);
            throw new SbcRuntimeException(response.getErrcode().toString(),"小程序参数错误!");
        }
        token = response.getAccess_token();
        redisService.setString(TOKEN_KEY, token, response.getExpires_in() - 200);
        return token;*/
    }




   /* *//**
     * 获取小程序直播回放视频
     * @param roomId
     * @return
     *//*
    public  ResponseEntity<ReplayVO> getLiveReplay(Integer roomId) {
        String accessToken = getToken();
        String url = String.format(liveRoomListUrl,accessToken);
        Map<String, Object> params = new HashMap<>();
        params.put("start",0);
        params.put("limit",10);
        params.put("action","get_replay");
        params.put("room_id",roomId);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResponseEntity<ReplayVO> responseEntity = restTemplate.postForEntity(url,json, ReplayVO.class);
        return responseEntity;
    }*/
}

