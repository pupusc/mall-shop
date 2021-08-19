package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.MiniProgramQrCodeRequest;
import com.wanmi.sbc.setting.api.request.MiniProgramSetRequest;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigAddRequest;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigModifyRequest;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigSaveRopRequest;
import com.wanmi.sbc.setting.api.response.MiniProgramSetGetResponse;
import com.wanmi.sbc.setting.api.response.SupplierOrderAuditResponse;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigModifyResponse;
import com.wanmi.sbc.setting.api.response.baseconfig.BossLogoResponse;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import com.wanmi.sbc.setting.bean.vo.BaseConfigVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 基本设置服务
 * Created by CHENLI on 2017/5/12.
 */
@Api(tags = "BaseConfigController", description = "基本设置服务 Api")
@RestController
public class BaseConfigController {
    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private BaseConfigQueryProvider baseConfigQueryProvider;

    @Autowired
    private BaseConfigSaveProvider baseConfigSaveProvider;

    /**
     * 查询基本设置
     * @return
     */
    @ApiOperation(value = "查询基本设置")
    @RequestMapping(value = "/baseConfig", method = RequestMethod.GET)
    public BaseResponse<BaseConfigRopResponse> findBaseConfig(){
          return baseConfigQueryProvider.getBaseConfig();
//        CompositeResponse<BaseConfigRopResponse> response =  sdkClient.buildClientRequest()
//                .get(BaseConfigRopResponse.class, "baseConfig.query", "1.0.0");
//        return BaseResponse.success( response.getSuccessResponse() );

    }

    @ApiOperation(value = "查询平台Logo")
    @RequestMapping(value = "/bosslogo", method = RequestMethod.GET)
    public BaseResponse<String> queryBossLogo() {
        BossLogoResponse response = baseConfigQueryProvider.queryBossLogo().getContext();
        return BaseResponse.success(response.getPcLogo());
    }

    /**
     * 保存基本设置
     * @param saveRopRequest
     * @return
     */
    @ApiOperation(value = "保存基本设置")
    @RequestMapping(value = "/baseConfig", method = RequestMethod.POST)
    public BaseResponse<BaseConfigRopResponse> saveBaseConfig(@Valid @RequestBody BaseConfigSaveRopRequest saveRopRequest) {
        BaseConfigAddRequest addRequest=new BaseConfigAddRequest();
        KsBeanUtil.copyPropertiesThird(saveRopRequest,addRequest);
        BaseConfigVO baseConfigVO =  baseConfigSaveProvider.add(addRequest).getContext().getBaseConfigVO();
        operateLogMQUtil.convertAndSend("设置", "新增基本设置", "新增基本设置");
        return BaseResponse.success(KsBeanUtil.convert(baseConfigVO, BaseConfigRopResponse.class));
    }

    /**
     * 修改基本设置
     * @param
     * @return
     */
    @ApiOperation(value = "修改基本设置")
    @RequestMapping(value = "/baseConfig", method = RequestMethod.PUT)
    public BaseResponse<BaseConfigRopResponse> updateBaseConfig(@RequestBody BaseConfigSaveRopRequest updateRopRequest){
        if(StringUtils.isEmpty(updateRopRequest.getBaseConfigId())){
            throw new SbcRuntimeException("K-000009");
        }
        BaseConfigModifyRequest modifyRequest=new BaseConfigModifyRequest();
        KsBeanUtil.copyPropertiesThird(updateRopRequest,modifyRequest);
        BaseConfigModifyResponse response =  baseConfigSaveProvider.modify(modifyRequest).getContext();
        operateLogMQUtil.convertAndSend("设置", "编辑基本设置", "编辑基本设置");
        return BaseResponse.success(KsBeanUtil.convert(response.getBaseConfigVO(), BaseConfigRopResponse.class));
    }

    /**
     * 获取平台boss的小程序码（与PC商城展示的一样）
     * @return
     */
    @ApiOperation(value = "获取平台boss的小程序码（与PC商城展示的一样）")
    @RequestMapping(value = "/getS2bBossQrcode", method = RequestMethod.POST)
    public BaseResponse<String>  getS2bBossQrcode(){
        MiniProgramQrCodeRequest request = new MiniProgramQrCodeRequest();
        //参数先这样，待发布后再修改，否则传入未经发布的页面，会报错。
        request.setPage("pages/index/index");
        request.setScene("123");
        return wechatAuthProvider.getWxaCodeUnlimit(request);
    }

    /**
     * 获取商家boss的二维码（扫码进入以后显示的是店铺首页）
     * @return
     */
    @ApiOperation(value = "获取商家boss的二维码（扫码进入以后显示的是店铺首页）")
    @RequestMapping(value = "/getS2bSupplierQrcode/{storeId}", method = RequestMethod.POST)
    public BaseResponse<String> getS2bSupplierQrcode(@PathVariable String storeId){
        MiniProgramQrCodeRequest request = new MiniProgramQrCodeRequest();
        request.setPage("pages/sharepage/sharepage");
        request.setScene("/store-main/"+storeId);
        return wechatAuthProvider.getWxaCodeUnlimit(request);
    }

    /**
     * 获取小程序配置
     * @return
     */
    @ApiOperation(value = "获取小程序配置")
    @RequestMapping(value = "/getMiniProgramSet", method = RequestMethod.GET)
    public BaseResponse<MiniProgramSetGetResponse> getMiniProgramSet(){
        return wechatAuthProvider.getMiniProgramSet();
    }

    /**
     * 修改
     * @param request
     * @return
     */
    @ApiOperation(value = "修改小程序配置")
    @RequestMapping(value = "/updateMiniProgramSet", method = RequestMethod.PUT)
    public BaseResponse updateMiniProgramSet(@RequestBody  MiniProgramSetRequest request) {
        return wechatAuthProvider.updateMiniProgramSet(request);
    }

    /**
     * 获取订单是否需要审核
     * @return
     */
    @ApiOperation(value = "获取订单是否需要审核")
    @RequestMapping(value = "/getSupplierOrderAudit", method = RequestMethod.GET)
    public BaseResponse<SupplierOrderAuditResponse> getSupplierOrderAudit(){
        return auditQueryProvider.isSupplierOrderAudit();
    }
}
