package com.wanmi.sbc.third.login.api;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.third.login.response.GetAccessTokeResponse;
import com.wanmi.sbc.third.login.response.GetWeChatUserInfoResponse;
import com.wanmi.sbc.third.login.response.LittleProgramAuthResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WechatApi {

    private static final Logger log = LoggerFactory.getLogger(WechatApi.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 公众号应用,APP应用通过code获取openId
     * 1.公众号JSAPI支付所需要的openId
     * 2.微信登陆
     * 3.绑定微信
     * @param appId
     * @param secret
     * @param code
     * @return
     */
    public GetAccessTokeResponse getWeChatAccessToken(String appId, String secret, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appId", appId);
        map.put("secret", secret);
        map.put("code", code);
        String result = restTemplate.getForObject("https://api.weixin.qq" +
                        ".com/sns/oauth2/access_token?appid={appId}&secret={secret}&code={code}&grant_type=authorization_code",
                String.class, map);
        GetAccessTokeResponse resp = JSONObject.parseObject(result, GetAccessTokeResponse.class);
        if (resp.getErrcode() != null) {
            log.error("微信通过code授权获取openId异常，返回信息：" + resp.toString());
//            throw new RuntimeException("微信通过code授权获取openId异常");
        }
        return resp;
    }

    /**
     * 小程序应用通过code获取openId
     * 1.小程序支付所需要的openId
     * @param appId
     * @param secret
     * @param code
     * @return
     */
    public LittleProgramAuthResponse getLittleProgramAccessToken(String appId, String secret, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appId", appId);
        map.put("secret", secret);
        map.put("code", code);
        String result = restTemplate.getForObject("https://api.weixin.qq" +
                        ".com/sns/jscode2session?appid={appId}&secret={secret}&js_code={code}&grant_type=authorization_code",
                String.class, map);
        LittleProgramAuthResponse resp = JSONObject.parseObject(result, LittleProgramAuthResponse.class);
        if (resp.getErrcode() != null) {
            log.error("小程序通过code授权获取openId异常，返回信息：" + result);
            throw new RuntimeException("小程序通过code授权获取openId异常");
        }
        return resp;
    }

    public GetWeChatUserInfoResponse getUserInfo(GetAccessTokeResponse accessTokeResponse) {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessTokeResponse.getAccess_token());
        map.put("openId", accessTokeResponse.getOpenid());
        String result = restTemplate.getForObject("https://api.weixin.qq" +
                ".com/sns/userinfo?access_token={accessToken}&openid={openId}", String.class, map);
        GetWeChatUserInfoResponse resp = JSONObject.parseObject(result, GetWeChatUserInfoResponse.class);
        if (StringUtils.isNotBlank(resp.getErrcode())) {
            log.error("微信授权登录获得微信用户信息异常，返回信息：" + resp.toString());
            throw new RuntimeException("微信授权登录获得微信用户信息异常");
        }
        String name = resp.getNickname();
        try {
            resp.setNickname(new String(name.getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            resp.setNickname("");
        }
        return resp;
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("appId", "wxf4106f3700c9b79c");
        map.put("secret", "0e05eff58de371192d6f64f009663aea");
        map.put("code", "081itHiR1lKGw91PhOgR16AyiR1itHiN");
        String s = restTemplate.getForObject("https://api.weixin.qq" +
                        ".com/sns/oauth2/access_token?appid={appId}&secret={secret}&code={code}&grant_type=authorization_code",
                String.class, map);
        GetAccessTokeResponse a = JSONObject.parseObject(s, GetAccessTokeResponse.class);
        System.out.println(a.toString());
    }
}
