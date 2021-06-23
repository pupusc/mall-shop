package com.wanmi.sbc.customerserver;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.provider.onlineservice.OnlineServiceQueryProvider;
import com.wanmi.sbc.setting.api.provider.onlineservice.OnlineServiceSaveProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.ConfigContextModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceByIdRequest;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceListRequest;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceModifyRequest;
import com.wanmi.sbc.setting.api.request.systemconfig.SystemConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceByIdResponse;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceListResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Api(description = "客服API", tags = "CustomerServiceController")
@RestController
@RequestMapping("/customerService")
public class CustomerServiceController {


    @Autowired
    private OnlineServiceQueryProvider onlineServiceQueryProvider;

    @Autowired
    private OnlineServiceSaveProvider onlineServiceSaveProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private SystemConfigSaveProvider systemConfigSaveProvider;

    /**
     * 查询qq客服配置明细
     *
     * @return
     */
    @ApiOperation(value = "查询qq客服配置明细")
    @ApiImplicitParam(paramType = "path", dataType = "Long",
            name = "storeId", value = "店铺id", required = true)
    @RequestMapping(value = {"/qq/detail/{storeId}"}, method = RequestMethod.GET)
    public BaseResponse<OnlineServiceListResponse> qqDetail(@PathVariable Long storeId) {
//    public BaseResponse<QQOnline> qqDetail(@PathVariable Long storeId) {
//        QQOnline response = qqServerService.qqDetail(storeId);
//        return BaseResponse.success(response);

        return onlineServiceQueryProvider.list(OnlineServiceListRequest.builder().storeId(storeId).build());

    }

    /**
     * 查询qq客服开关
     *
     * @return
     */
    @ApiOperation(value = "查询qq客服开关")
    @ApiImplicitParam(paramType = "path", dataType = "Long",
            name = "storeId", value = "店铺id", required = true)
    @RequestMapping(value = {"/qq/switch/{storeId}"}, method = RequestMethod.GET)
    public BaseResponse<ConfigResponse> qqSwitch(@PathVariable Long storeId) {
//    public BaseResponse<QQOnlineServerRopResponse> qqSwitch(@PathVariable Long storeId) {
//        QQOnlineServerRopResponse response = qqServerService.qqSwitch(storeId);
//        return BaseResponse.success(response);

        OnlineServiceByIdResponse onlineServiceByIdResponse = onlineServiceQueryProvider.getById(
                OnlineServiceByIdRequest.builder().storeId(storeId).build()).getContext();

        SystemConfigResponse response =
                systemConfigQueryProvider.list(SystemConfigQueryRequest.builder().configKey(ConfigKey.ONLINESERVICE.toValue())
                        .configType(ConfigType.ALIYUN_ONLINE_SERVICE.toValue()).delFlag(DeleteFlag.NO).build()).getContext();
        SystemConfigVO systemConfigVO = new SystemConfigVO();
        if (!response.getSystemConfigVOList().isEmpty()){
            systemConfigVO = response.getSystemConfigVOList().get(0);
        }

        // 改造 bff
        return BaseResponse.success(ConfigResponse.builder().onlineServiceVO(onlineServiceByIdResponse.getOnlineServiceVO())
                .systemConfigVO(systemConfigVO).build());
    }

    /**
     * 保存qq客服配置明细
     *
     * @return
     */
    @ApiOperation(value = "保存qq客服配置明细")
    @RequestMapping(value = {"/qq/saveDetail"}, method = RequestMethod.POST)
    public BaseResponse qqSaveDetail(@RequestBody OnlineServiceModifyRequest ropRequest) {
//    public BaseResponse qqSaveDetail(@RequestBody QQOnline ropRequest) {
//        BaseResponse baseResponse = qqServerService.qqSaveDetail(ropRequest);

//        return baseResponse;
        SystemConfigResponse response =
                systemConfigQueryProvider.list(SystemConfigQueryRequest.builder().configKey(ConfigKey.ONLINESERVICE.toValue())
                        .configType(ConfigType.ALIYUN_ONLINE_SERVICE.toValue()).delFlag(DeleteFlag.NO).build()).getContext();
        if (!response.getSystemConfigVOList().isEmpty()){
            SystemConfigVO systemConfigVO = response.getSystemConfigVOList().get(0);
            if (ropRequest.getQqOnlineServerRop().getServerStatus().toValue()== 1 && systemConfigVO.getStatus() == 1){
                return BaseResponse.FAILED();
            }
        }
        operateLogMQUtil.convertAndSend("设置", "编辑在线客服", "编辑在线客服");

        return onlineServiceSaveProvider.modify(ropRequest);

    }

    /**
     *  查询阿里云客服配置
     * @return
     */
    @ApiOperation(value = "查询阿里云客服配置")
    @PostMapping("/aliyun/detail")
    public BaseResponse queryAliyun(){
        SystemConfigResponse response =
                systemConfigQueryProvider.list(SystemConfigQueryRequest.builder().configKey(ConfigKey.ONLINESERVICE.toValue())
                .configType(ConfigType.ALIYUN_ONLINE_SERVICE.toValue()).delFlag(DeleteFlag.NO).build()).getContext();
        if (!response.getSystemConfigVOList().isEmpty()){
            return BaseResponse.success(response.getSystemConfigVOList().get(0));
        }
        return BaseResponse.FAILED();
    }

    @ApiOperation(value = "修改阿里云客服配置")
    @PostMapping("/aliyun/modify")
    public BaseResponse modifyAliyun(@RequestBody ConfigContextModifyByTypeAndKeyRequest request){

        // 不知道为什么有店铺ID。目前都是写死的0
        OnlineServiceByIdResponse onlineServiceByIdResponse = onlineServiceQueryProvider.getById(
                OnlineServiceByIdRequest.builder().storeId(0L).build()).getContext();
        if (Objects.nonNull(onlineServiceByIdResponse) && onlineServiceByIdResponse.getOnlineServiceVO()
                .getServerStatus().toValue() == 1 && request.getStatus() == 1){
            return BaseResponse.FAILED();
        }
        request.setConfigKey(ConfigKey.ONLINESERVICE);
        request.setConfigType(ConfigType.ALIYUN_ONLINE_SERVICE);
        systemConfigSaveProvider.modify(request);
        return BaseResponse.SUCCESSFUL();
    }

}
