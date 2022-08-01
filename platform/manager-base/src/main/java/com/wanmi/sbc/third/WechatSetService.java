package com.wanmi.sbc.third;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPlusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPlusStockDTO;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.GatewayConfigByGatewayRequest;
import com.wanmi.sbc.pay.api.response.PayGatewayConfigResponse;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.redis.RedisHIncrBean;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.provider.wechatloginset.WechatLoginSetQueryProvider;
import com.wanmi.sbc.setting.api.response.MiniProgramSetGetResponse;
import com.wanmi.sbc.setting.api.response.wechat.WechatSetResponse;
import com.wanmi.sbc.setting.api.response.wechatloginset.WechatLoginSetResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 获取所有微信配置
     * @return
     */
    public WechatSetResponse get(List<TerminalType> terminalTypes){
        if(CollectionUtils.isEmpty(terminalTypes)){
            return null;
        }
        WechatSetResponse response = new WechatSetResponse();

        //获取PC的配置和开关  H5/APP的开关
        if(terminalTypes.contains(TerminalType.PC) || terminalTypes.contains(TerminalType.H5) || terminalTypes.contains(TerminalType.APP)) {
            WechatLoginSetResponse setResponse = wechatLoginSetQueryProvider.getInfo().getContext();
            KsBeanUtil.copyPropertiesThird(setResponse, response);
        }

        //填充H5/app的appId、密钥
        if(terminalTypes.contains(TerminalType.H5) || terminalTypes.contains(TerminalType.APP)) {
            GatewayConfigByGatewayRequest gatewayConfigByGatewayRequest = new GatewayConfigByGatewayRequest();
            gatewayConfigByGatewayRequest.setGatewayEnum(PayGatewayEnum.WECHAT);
            gatewayConfigByGatewayRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
            PayGatewayConfigResponse payGatewayConfig = payQueryProvider.getGatewayConfigByGateway(gatewayConfigByGatewayRequest).getContext();
            response.setMobileAppId(payGatewayConfig.getAppId());
            response.setMobileAppSecret(payGatewayConfig.getSecret());
            response.setAppAppId(payGatewayConfig.getOpenPlatformAppId());
            response.setAppAppSecret(payGatewayConfig.getOpenPlatformSecret());
        }

        //填充小程序的appId、密钥、开关
        if(terminalTypes.contains(TerminalType.MINI)) {
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
}
