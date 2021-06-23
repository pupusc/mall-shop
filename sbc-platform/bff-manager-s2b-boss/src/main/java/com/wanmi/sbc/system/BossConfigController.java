package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.provider.AuditProvider;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigContextModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.ConfigStatusModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueStatusModifyRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * S2B 平台端-审核开关配置
 * Created by wj on 2017/12/06.
 */
@Api(tags = "BossConfigController", description = "S2B 平台端-审核开关配置API")
@RestController
@RequestMapping("/boss/config")
public class BossConfigController {

    @Resource
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private AuditProvider auditProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @ApiOperation(value = "查询审核开关状态")
    @RequestMapping(value = "/audit/list", method = RequestMethod.GET)
    public BaseResponse<List<ConfigVO>> listConfigs() {
        return BaseResponse.success(auditQueryProvider.listAuditConfig().getContext().getConfigVOList());
    }

    @ApiOperation(value = "查询商品配置")
    @RequestMapping(value = "/audit/list-goods-configs",method = RequestMethod.GET)
    public BaseResponse<List<ConfigVO>> queryGoodsSettingConfigs() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigKey(ConfigKey.GOODS_SETTING.toString());
        return BaseResponse.success(auditQueryProvider.getByConfigKey(request).getContext().getConfigVOList());
    }

    /**
     * 开启商品审核开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "开启商品审核开关")
    @RequestMapping(value = "/audit/goods/open", method = RequestMethod.POST)
    public BaseResponse openAuditGoods() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.SUPPLIERGOODSAUDIT);
        request.setStatus(1);

        auditProvider.modifyStatusByTypeAndKey(request);

        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：商品审核设为开");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 关闭商品审核开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "关闭商品审核开关")
    @RequestMapping(value = "/audit/goods/close", method = RequestMethod.POST)
    public BaseResponse closeAuditGoods() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.SUPPLIERGOODSAUDIT);
        request.setStatus(0);

        auditProvider.modifyStatusByTypeAndKey(request);

        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：商品审核设为关");
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：自营商品审核设为关");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 开启自营商品审核开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "开启自营商品审核开关")
    @RequestMapping(value = "/audit/goods/self/open", method = RequestMethod.POST)
    public BaseResponse openAuditSelfGoods() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.BOSSGOODSAUDIT);
        request.setStatus(1);

        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：自营商品审核设为开");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 关闭自营商品审核开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "关闭自营商品审核开关")
    @RequestMapping(value = "/audit/goods/self/close", method = RequestMethod.POST)
    public BaseResponse closeAuditSelfGoods() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.BOSSGOODSAUDIT);
        request.setStatus(0);

        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：自营商品审核设为关");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 开启订单审核开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "开启订单审核开关")
    @RequestMapping(value = "/audit/order/open", method = RequestMethod.POST)
    public BaseResponse openAuditOrder() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.ORDERAUDIT);
        request.setStatus(1);

        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：订单审核设为开");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 关闭订单审核开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "关闭订单审核开关")
    @RequestMapping(value = "/audit/order/close", method = RequestMethod.POST)
    public BaseResponse closeAuditOrder() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.ORDERAUDIT);
        request.setStatus(0);

        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：订单审核设为关");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 开启客户审核开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "开启客户审核开关")
    @RequestMapping(value = "/audit/customer/open", method = RequestMethod.POST)
    public BaseResponse openAuditCustomer() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.CUSTOMERAUDIT);
        request.setStatus(1);

        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：客户审核设为开");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 关闭客户审核开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "关闭客户审核开关")
    @RequestMapping(value = "/audit/customer/close", method = RequestMethod.POST)
    public BaseResponse closeAuditCustomer() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.CUSTOMERAUDIT);
        request.setStatus(0);

        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：客户审核设为关");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 开启客户信息完善开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "开启客户信息完善开关")
    @RequestMapping(value = "/audit/customer-info/open", method = RequestMethod.POST)
    public BaseResponse openAuditCustomerInfo() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.CUSTOMERINFOAUDIT);
        request.setStatus(1);
        auditProvider.modifyStatusByTypeAndKey(request);

        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：客户信息完善设为开");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 关闭客户信息完善开关
     * 关闭客户信息完善开关时，客户审核开关一起关闭
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "关闭客户信息完善开关-------关闭客户信息完善开关时，客户审核开关一起关闭")
    @RequestMapping(value = "/audit/customer-info/close", method = RequestMethod.POST)
    public BaseResponse closeAuditCustomerInfo() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.CUSTOMERINFOAUDIT);
        request.setStatus(0);
        auditProvider.modifyStatusByTypeAndKey(request);

        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：客户信息完善设为关");
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：客户审核设为关");
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 开启用户审核（即访问需登录）
     *
     * @return
     */
    @ApiOperation(value = "开启用户审核（即访问需登录）")
    @RequestMapping(value = "/audit/usersetting/open", method = RequestMethod.POST)
    public BaseResponse openUserSetting() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.USERAUDIT);
        request.setStatus(1);
        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：用户设置设为开");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 关闭用户审核（即访问商城无需登录）
     *
     * @return
     */
    @ApiOperation(value = "关闭用户审核（即访问商城无需登录）")
    @RequestMapping(value = "/audit/usersetting/close", method = RequestMethod.POST)
    public BaseResponse closeUserSetting() {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.USERAUDIT);
        request.setStatus(0);
        auditProvider.modifyStatusByTypeAndKey(request);

        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：用户设置设为关");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * pc端商品列表大小图默认展示设置
     *
     * @return
     */
    @ApiOperation(value = "pc端商品列表大小图默认展示设置")
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "status", value = "状态", required = true)
    @RequestMapping(value = "/audit/imgdisplayforpc/{status}", method = RequestMethod.POST)
    public BaseResponse setDisplayImgForPc(@PathVariable Integer status) {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.PC_GOODS_IMAGE_SWITCH);
        request.setStatus(status);
        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改商品设置", "修改商品设置：PC商城商品列表默认展示设为" + (status == 1 ? "大图列表" : "小图列表"));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * PC商城商品列表展示维度SKU或者SPU设置
     *
     * @return
     */
    @ApiOperation(value = "PC商城商品列表展示维度SKU或者SPU设置")
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "status", value = "状态", required = true)
    @RequestMapping(value = "/audit/specdisplayforpc/{status}", method = RequestMethod.POST)
    public BaseResponse setDisplaySpecForPc(@PathVariable Integer status) {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.PC_GOODS_SPEC_SWITCH);
        request.setStatus(status);
        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改商品设置", "修改商品设置：PC商城商品列表展示维度设为" + (status == 1 ? "SPU维度" : "SKU维度"));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 移动端商品列表大小图默认展示设置
     *
     * @return
     */
    @ApiOperation(value = "移动端商品列表大小图默认展示设置")
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "status", value = "状态", required = true)
    @RequestMapping(value = "/audit/imgdisplayformobile/{status}", method = RequestMethod.POST)
    public BaseResponse setDisplayImgForMobile(@PathVariable Integer status) {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.MOBILE_GOODS_IMAGE_SWITCH);
        request.setStatus(status);
        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改商品设置", "修改商品设置：移动商城商品列表默认展示设为" + (status == 1 ? "大图列表" : "小图列表"));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 移动端商城商品列表展示维度SKU或者SPU设置
     *
     * @return
     */
    @ApiOperation(value = "移动端商城商品列表展示维度SKU或者SPU设置")
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "status", value = "状态", required = true)
    @RequestMapping(value = "/audit/specdisplayformobile/{status}", method = RequestMethod.POST)
    public BaseResponse setDisplaySpecForMobile(@PathVariable Integer status) {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.MOBILE_GOODS_SPEC_SWITCH);
        request.setStatus(status);
        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改商品设置", "修改商品设置：移动商城商品列表展示维度设为" + (status == 1 ? "SPU维度" : "SKU维度"));
        return BaseResponse.SUCCESSFUL();
    }



    /**
     * 商品评价开关设置
     */
    @ApiOperation(value = "商品评价开关设置")
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "status", value = "状态", required = true)
    @RequestMapping(value = "/audit/goods-evaluate/{status}", method = RequestMethod.POST)
    public BaseResponse setGoodsEvaluateSwitch(@PathVariable Integer status) {
        ConfigStatusModifyByTypeAndKeyRequest request = new ConfigStatusModifyByTypeAndKeyRequest();
        request.setConfigKey(ConfigKey.GOODS_SETTING);
        request.setConfigType(ConfigType.GOODS_EVALUATE_SETTING);
        request.setStatus(status);
        auditProvider.modifyStatusByTypeAndKey(request);
        operateLogMQUtil.convertAndSend("设置", "修改商品设置", "修改商品设置：商品评价" + (status == 1 ? "开放" : "关闭") + "展示");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 小程序分享设置
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "小程序分享设置")
    @RequestMapping(value = "/audit/modify-share-little-program", method = RequestMethod.POST)
    public BaseResponse modifyShareLittleProgram(@RequestBody ConfigContextModifyByTypeAndKeyRequest request) {
        request.setConfigKey(ConfigKey.S2BAUDIT);
        request.setConfigType(ConfigType.APPLET_SHARE_SETTING);
        auditProvider.modifyShareLittleProgram(request);
        return BaseResponse.SUCCESSFUL();
    }
}
