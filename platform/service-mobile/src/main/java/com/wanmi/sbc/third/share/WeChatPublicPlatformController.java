package com.wanmi.sbc.third.share;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.SpecialSymbols;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.GatewayConfigByGatewayRequest;
import com.wanmi.sbc.pay.api.response.PayGatewayConfigResponse;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.provider.wechatloginset.WechatLoginSetQueryProvider;
import com.wanmi.sbc.setting.api.provider.wechatshareset.WechatShareSetQueryProvider;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetInfoRequest;
import com.wanmi.sbc.setting.api.response.MiniProgramSetGetResponse;
import com.wanmi.sbc.setting.api.response.wechat.WechatSetResponse;
import com.wanmi.sbc.setting.api.response.wechatloginset.WechatLoginSetResponse;
import com.wanmi.sbc.setting.api.response.wechatshareset.WechatShareSetInfoResponse;
import com.wanmi.sbc.third.share.request.GetSignRequest;
import com.wanmi.sbc.third.share.response.TicketResponse;
import com.wanmi.sbc.third.share.response.WeChatTicketResponse;
import com.wanmi.sbc.third.wechat.WechatSetService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/third/share")
@Api(tags = "WeChatPublicPlatformController", description = "微信公众平台Controller")
public class WeChatPublicPlatformController {

    private static final Logger log = LoggerFactory.getLogger(WeChatPublicPlatformController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private WechatShareSetQueryProvider wechatShareSetQueryProvider;

    @Autowired
    private WechatLoginSetQueryProvider wechatLoginSetQueryProvider;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private WechatSetService wechatSetService;

    @Autowired
    private CommonUtil commonUtil;

    private static String TICKET_KEY = CacheKeyConstant.WE_CHAT + SpecialSymbols.COLON + "TICKET";

    @ApiOperation(value = "获得微信sign")
    @RequestMapping(value = {"/weChat/getSign"}, method = RequestMethod.POST)
    public BaseResponse<TicketResponse> getSign(@Validated @RequestBody GetSignRequest request) {
        //获取appId,secret
        String appId = null;
        String appSecret = null;

        TerminalType terminalType = null;
        if(StringUtils.isNotBlank(request.getTerminalType())) {
            terminalType = TerminalType.valueOf(request.getTerminalType());
        }
        //小程序设置
        if (TerminalType.MINI.equals(terminalType)) {
            MiniProgramSetGetResponse miniSet = wechatAuthProvider.getMiniProgramSet().getContext();
            if (miniSet != null && Constants.yes.equals(miniSet.getStatus())) {
                JSONObject configJson = JSONObject.parseObject(miniSet.getContext());
                appId = configJson.getString("appId");
                appSecret = configJson.getString("appSecret");
            }
        } else if (TerminalType.H5.equals(terminalType)) { //H5
            WechatLoginSetResponse setResponse = wechatLoginSetQueryProvider.getInfo().getContext();
            if (setResponse != null && DefaultFlag.YES.equals(setResponse.getMobileServerStatus())) {
                GatewayConfigByGatewayRequest gatewayConfigByGatewayRequest = new GatewayConfigByGatewayRequest();
                gatewayConfigByGatewayRequest.setGatewayEnum(PayGatewayEnum.WECHAT);
                gatewayConfigByGatewayRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
                PayGatewayConfigResponse payGatewayConfig = payQueryProvider.getGatewayConfigByGatewayUnException(gatewayConfigByGatewayRequest).getContext();
                if (payGatewayConfig != null) {
                    appId = payGatewayConfig.getAppId();
                    appSecret = payGatewayConfig.getSecret();
                }
            }
        } else {
            WechatShareSetInfoResponse infoResponse = wechatShareSetQueryProvider.getInfo(WechatShareSetInfoRequest.builder().
                    operatePerson(commonUtil.getOperatorId()).build()).getContext();
            appId = infoResponse.getShareAppId();
            appSecret = infoResponse.getShareAppSecret();
        }

        if(appId == null) {
            return BaseResponse.success(new TicketResponse());
        }

        String ticket = this.getTicket(wechatSetService.getToken(appId,appSecret));
        TicketResponse response = WeChatSign.sign(ticket, request.getUrl());
        response.setAppId(appId);
        return BaseResponse.success(response);

    }

    /**
     * 目前提供给APP的装载微信分享SDK，填充appId
     * @param terminalType
     * @return
     */
    @ApiOperation(value = "获取微信AppId", notes = "channel: APP")
    @ApiImplicitParam(paramType = "path", name = "channel", value = "类型终端", required = true)
    @RequestMapping(value = {"/weChat/{terminalType}"}, method = RequestMethod.GET)
    public BaseResponse<WechatSetResponse> getAppIdByTerminalType(@PathVariable String terminalType) {
        WechatSetResponse response = new WechatSetResponse();
        if (TerminalType.APP.name().equalsIgnoreCase(terminalType)) {
            WechatSetResponse setResponse = wechatSetService.get(TerminalType.APP);
            //防止泄露Secret
            response.setAppAppId(setResponse.getAppAppId());
            response.setAppServerStatus(setResponse.getAppServerStatus());
        }
        return BaseResponse.success(response);
    }

    private String getTicket(String token) {
        String ticket = redisService.getString(TICKET_KEY);
        if (!StringUtils.isBlank(ticket)) {
            return ticket;
        }
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        WeChatTicketResponse response = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={access_token}&type=jsapi", WeChatTicketResponse.class, map);
        if (response == null || response.getErrcode() != 0) {
            log.error("获得微信ticket异常：请求参数：" + map.toString());
            throw new RuntimeException("获得微信ticket异常");
        }
        ticket = response.getTicket();
        redisService.setString(TICKET_KEY, ticket, response.getExpires_in() - 200);
        return ticket;
    }
}
