package com.wanmi.sbc.customerserver;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.onlineservice.OnlineServiceQueryProvider;
import com.wanmi.sbc.setting.api.provider.onlineservice.OnlineServiceSaveProvider;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceByIdRequest;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceListRequest;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceModifyRequest;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceByIdResponse;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceListResponse;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "QQServiceController", description = "QQ服务 API")
@RestController
@RequestMapping("/customerService")
public class QQServiceController {


    @Autowired
    private OnlineServiceQueryProvider onlineServiceQueryProvider;

    @Autowired
    private OnlineServiceSaveProvider onlineServiceSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 查询qq客服配置明细
     *
     * @return
     */
    @ApiOperation(value = "查询qq客服配置明细")
    @RequestMapping(value = {"/qq/detail"}, method = RequestMethod.GET)
    public BaseResponse<OnlineServiceListResponse> qqDetail() {
//    public BaseResponse<QQOnline> qqDetail() {
//        QQOnline response = qqServerService.qqDetail(commonUtil.getStoreId());
//        return BaseResponse.success(response);

        return onlineServiceQueryProvider.list(OnlineServiceListRequest.builder().storeId(commonUtil.getStoreId()).build());
    }

    /**
     * 查询qq客服开关
     *
     * @return
     */
    @ApiOperation(value = "查询qq客服开关")
    @RequestMapping(value = {"/qq/switch"}, method = RequestMethod.GET)
    public BaseResponse<OnlineServiceVO> qqSwitch() {
//    public BaseResponse<QQOnlineServerRopResponse> qqSwitch() {
//        QQOnlineServerRopResponse response = qqServerService.qqSwitch(commonUtil.getStoreId());
//        return BaseResponse.success(response);

        OnlineServiceByIdResponse onlineServiceByIdResponse = onlineServiceQueryProvider.getById(
                OnlineServiceByIdRequest.builder().storeId(commonUtil.getStoreId()).build()).getContext();

        // 改造 bff
        return BaseResponse.success(onlineServiceByIdResponse.getOnlineServiceVO());
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

//       BaseResponse baseResponse =  qqServerService.qqSaveDetail(ropRequest);
//        return  baseResponse;
        ropRequest.setStoreId(commonUtil.getStoreId());
        operateLogMQUtil.convertAndSend("设置","编辑在线客服","编辑在线客服");
        return onlineServiceSaveProvider.modify(ropRequest);
    }
}
