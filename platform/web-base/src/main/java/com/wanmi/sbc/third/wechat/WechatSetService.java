package com.wanmi.sbc.third.wechat;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.SpecialSymbols;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.GatewayConfigByGatewayRequest;
import com.wanmi.sbc.pay.api.response.PayGatewayConfigResponse;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.provider.wechatloginset.WechatLoginSetQueryProvider;
import com.wanmi.sbc.setting.api.response.MiniProgramSetGetResponse;
import com.wanmi.sbc.setting.api.response.wechat.WechatSetResponse;
import com.wanmi.sbc.setting.api.response.wechatloginset.WechatLoginSetResponse;
import com.wanmi.sbc.third.wechat.response.WeChatTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品缓存服务
 * Created by daiyitian on 2017/4/11.
 */
@Slf4j
@Service
public class WechatSetService {


    @Autowired
    private WechatLoginSetQueryProvider wechatLoginSetQueryProvider;

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    private static String TOKEN_KEY = CacheKeyConstant.WE_CHAT + SpecialSymbols.COLON + "TOKEN";


    /**
     * 获取状态
     * @param terminalType 终端类型
     * @return 状态
     */
    public DefaultFlag getStatus(TerminalType terminalType){
        if(TerminalType.APP.equals(terminalType)){
            return this.get(TerminalType.APP).getAppServerStatus();
        }else if(TerminalType.MINI.equals(terminalType)){
            return this.get(TerminalType.MINI).getMiniProgramServerStatus();
        }else if(TerminalType.PC.equals(terminalType)){
            return this.get(TerminalType.PC).getPcServerStatus();
        }
        return this.get(TerminalType.H5).getMobileServerStatus();
    }

    /**
     * 获取微信设置配置
     * @param terminalType 终端类型
     * @return
     */
    public WechatSetResponse get(TerminalType terminalType){
        return get(Collections.singletonList(terminalType));
    }

    /**
     * 获取微信设置配置
     * @return
     */
    public WechatSetResponse get(List<TerminalType> terminalTypes) {
        if (CollectionUtils.isEmpty(terminalTypes)) {
            return null;
        }
        WechatSetResponse response = new WechatSetResponse();

        //获取PC的配置和开关  H5/APP的开关
        if (terminalTypes.contains(TerminalType.PC) || terminalTypes.contains(TerminalType.H5) || terminalTypes.contains(TerminalType.APP)) {
            WechatLoginSetResponse setResponse = wechatLoginSetQueryProvider.getInfo().getContext();
            KsBeanUtil.copyPropertiesThird(setResponse, response);
        }

        //填充H5/app的appId、密钥
        if (terminalTypes.contains(TerminalType.H5) || terminalTypes.contains(TerminalType.APP)) {
            GatewayConfigByGatewayRequest gatewayConfigByGatewayRequest = new GatewayConfigByGatewayRequest();
            gatewayConfigByGatewayRequest.setGatewayEnum(PayGatewayEnum.WECHAT);
            gatewayConfigByGatewayRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
            PayGatewayConfigResponse payGatewayConfig = payQueryProvider.getGatewayConfigByGateway(gatewayConfigByGatewayRequest).getContext();
            if (payGatewayConfig != null) {
                response.setMobileAppId(payGatewayConfig.getAppId());
                response.setMobileAppSecret(payGatewayConfig.getSecret());
                response.setAppAppId(payGatewayConfig.getOpenPlatformAppId());
                response.setAppAppSecret(payGatewayConfig.getOpenPlatformSecret());
            }
        }

        //填充小程序的appId、密钥、开关
        if (terminalTypes.contains(TerminalType.MINI)) {
            MiniProgramSetGetResponse miniResponse = wechatAuthProvider.getMiniProgramSet().getContext();
            if (StringUtils.isNotBlank(miniResponse.getContext())) {
                JSONObject configJson = JSONObject.parseObject(miniResponse.getContext());
                response.setMiniProgramAppAppId(configJson.getString("appId"));
                response.setMiniProgramAppSecret(configJson.getString("appSecret"));
                response.setMiniProgramServerStatus(DefaultFlag.fromValue(miniResponse.getStatus()));
            }
        }
        return response;
    }


    /**
     * 获取token，另外也起到校验作用
     * @param appId appId
     * @param appSecret 密钥
     * @return
     */
    public String getToken(String appId, String appSecret) {
        String token = redisService.getString(TOKEN_KEY.concat(appId));
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("secret", appSecret);
        WeChatTokenResponse response = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}", WeChatTokenResponse.class, map);
        if (response == null || response.getErrcode() != null) {
            log.error("获得微信token异常：请求参数：{},response:{}", map.toString(), response);
            throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
        }
        token = response.getAccess_token();
        redisService.setString(TOKEN_KEY.concat(appId), token, response.getExpires_in() - 200);
        return token;
    }
}
