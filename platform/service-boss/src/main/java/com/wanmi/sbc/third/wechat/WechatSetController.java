package com.wanmi.sbc.third.wechat;

import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.request.PayGatewaySaveByTerminalTypeRequest;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.provider.wechatloginset.WechatLoginSetQueryProvider;
import com.wanmi.sbc.setting.api.provider.wechatloginset.WechatLoginSetSaveProvider;
import com.wanmi.sbc.setting.api.request.MiniProgramSetRequest;
import com.wanmi.sbc.setting.api.request.wechat.WechatSetRequest;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatLoginSetAddRequest;
import com.wanmi.sbc.setting.api.response.wechat.WechatSetResponse;
import com.wanmi.sbc.setting.api.response.wechatloginset.WechatLoginSetResponse;
import com.wanmi.sbc.third.WechatSetService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Api(tags = "WechatSetController", description = "微信设置服务")
@RestController
@RequestMapping("/third/wechatSet")
public class WechatSetController {

    @Autowired
    private WechatLoginSetSaveProvider wechatLoginSetSaveProvider;

    @Autowired
    private WechatLoginSetQueryProvider wechatLoginSetQueryProvider;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private WechatSetService wechatSetService;

    /**
     * 获取微信授信配置
     *
     * @return
     */
    @ApiOperation(value = "获取微信设置")
    @GetMapping
    public BaseResponse<WechatSetResponse> get() {
        return BaseResponse.success(wechatSetService.get(Arrays.asList(TerminalType.PC, TerminalType.H5, TerminalType.MINI, TerminalType.APP)));
    }

    /**
     * 保存微信授信配置
     *
     * @return
     */
    @ApiOperation(value = "保存微信设置")
    @GlobalTransactional
    @MultiSubmit
    @PutMapping
    public BaseResponse set(@RequestBody @Valid WechatSetRequest request) {
        Long storeId = Constants.BOSS_DEFAULT_STORE_ID;
        if(TerminalType.PC.equals(request.getTerminalType())){
            WechatLoginSetAddRequest addRequest = new WechatLoginSetAddRequest();
            WechatLoginSetResponse setResponse = wechatLoginSetQueryProvider.getInfo().getContext();
            KsBeanUtil.copyPropertiesThird(setResponse, addRequest);
            addRequest.setPcAppId(request.getAppId());
            addRequest.setPcAppSecret(request.getSecret());
            addRequest.setPcServerStatus(request.getStatus());
            addRequest.setOperatePerson(commonUtil.getOperatorId());
            addRequest.setStoreId(storeId);
            wechatLoginSetSaveProvider.add(addRequest);
            operateLogMQUtil.convertAndSend("设置", "微信设置", "编辑微信PC接口");
        }else if(TerminalType.H5.equals(request.getTerminalType())){
            //更新支付配置里的微信公众号
            PayGatewaySaveByTerminalTypeRequest saveRequest = new PayGatewaySaveByTerminalTypeRequest();
            saveRequest.setStoreId(storeId);
            saveRequest.setAppId(request.getAppId());
            saveRequest.setSecret(request.getSecret());
            saveRequest.setTerminalType(com.wanmi.sbc.pay.bean.enums.TerminalType.H5);
            saveRequest.setPayGatewayEnum(PayGatewayEnum.WECHAT);
            payProvider.savePayGatewayByTerminalType(saveRequest);
            //同步LoginSet
            WechatLoginSetAddRequest addRequest = new WechatLoginSetAddRequest();
            WechatLoginSetResponse setResponse = wechatLoginSetQueryProvider.getInfo().getContext();
            KsBeanUtil.copyPropertiesThird(setResponse, addRequest);
            addRequest.setMobileAppId(request.getAppId());
            addRequest.setMobileAppSecret(request.getSecret());
            addRequest.setMobileServerStatus(request.getStatus());
            addRequest.setOperatePerson(commonUtil.getOperatorId());
            addRequest.setStoreId(storeId);
            wechatLoginSetSaveProvider.add(addRequest);
            operateLogMQUtil.convertAndSend("设置", "微信设置", "编辑微信H5接口");
        }else if(TerminalType.MINI.equals(request.getTerminalType())){
            MiniProgramSetRequest setRequest = new MiniProgramSetRequest();
            setRequest.setAppId(request.getAppId());
            setRequest.setAppSecret(request.getSecret());
            setRequest.setStatus(request.getStatus().toValue());
            wechatAuthProvider.updateMiniProgramSet(setRequest);
            operateLogMQUtil.convertAndSend("设置", "微信设置", "编辑微信小程序接口");
        }else if(TerminalType.APP.equals(request.getTerminalType())){
            //更新支付配置里的开放平台APP
            PayGatewaySaveByTerminalTypeRequest saveRequest = new PayGatewaySaveByTerminalTypeRequest();
            saveRequest.setStoreId(storeId);
            saveRequest.setAppId(request.getAppId());
            saveRequest.setSecret(request.getSecret());
            saveRequest.setTerminalType(com.wanmi.sbc.pay.bean.enums.TerminalType.APP);
            saveRequest.setPayGatewayEnum(PayGatewayEnum.WECHAT);
            payProvider.savePayGatewayByTerminalType(saveRequest);

            //同步LoginSet
            WechatLoginSetAddRequest addRequest = new WechatLoginSetAddRequest();
            WechatLoginSetResponse setResponse = wechatLoginSetQueryProvider.getInfo().getContext();
            KsBeanUtil.copyPropertiesThird(setResponse, addRequest);
            addRequest.setOperatePerson(commonUtil.getOperatorId());
            addRequest.setStoreId(storeId);
            addRequest.setAppServerStatus(request.getStatus());
            wechatLoginSetSaveProvider.add(addRequest);
            operateLogMQUtil.convertAndSend("设置", "微信设置", "编辑微信APP接口");
        }
        return BaseResponse.SUCCESSFUL();
    }

}
