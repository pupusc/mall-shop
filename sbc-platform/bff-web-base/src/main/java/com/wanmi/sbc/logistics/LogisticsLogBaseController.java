package com.wanmi.sbc.logistics;

import com.aliyuncs.linkedmall.model.v20180116.QueryLogisticsResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.order.SbcLogisticsQueryRequest;
import com.wanmi.sbc.linkedmall.api.response.order.SbcLogisticsQueryResponse;
import com.wanmi.sbc.logistics.response.LogisticsLinkedMallResponse;
import com.wanmi.sbc.order.api.provider.logistics.LogisticsLogQueryProvider;
import com.wanmi.sbc.order.api.request.logistics.LogisticsLogSimpleListByCustomerIdRequest;
import com.wanmi.sbc.order.api.response.logistics.LogisticsLogSimpleListByCustomerIdResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "LogisticsLogBaseController", description = "物流信息 API")
@RestController
@RequestMapping("/logisticsLog")
@Validated
public class LogisticsLogBaseController {

    @Autowired
    private LogisticsLogQueryProvider logisticsLogQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private LinkedMallOrderQueryProvider linkedMallOrderQueryProvider;

    /**
     * 获取物流信息
     * @return
     */
    @ApiOperation(value = "获取物流信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResponse<LogisticsLogSimpleListByCustomerIdResponse> list(){
        String customerId = commonUtil.getOperatorId();
        return logisticsLogQueryProvider.listByCustomerId(LogisticsLogSimpleListByCustomerIdRequest.builder()
                .customerId(customerId).build());
    }

    /**
     * 查询linkedmall订单的物流详情
     */
    @ApiOperation(value = "查询linkedmall订单的物流详情", notes = "返回: 物流详情")
    @RequestMapping(value = "/linkedMall/deliveryInfos", method = RequestMethod.POST)
    public BaseResponse<LogisticsLinkedMallResponse> logistics4LinkedMall(@RequestBody SbcLogisticsQueryRequest request) {
        QueryLogisticsResponse.DataItem dataItem = linkedMallOrderQueryProvider.getOrderLogistics(request).getContext().getDataItems().get(0);
        List<QueryLogisticsResponse.DataItem.LogisticsDetailListItem> logisticsDetail = dataItem.getLogisticsDetailList();
        LogisticsLinkedMallResponse logisticsLinkedMallResponse = new LogisticsLinkedMallResponse();
        logisticsLinkedMallResponse.setLogisticCompanyName(dataItem.getLogisticsCompanyName());
        logisticsLinkedMallResponse.setLogisticNo(dataItem.getMailNo());
        logisticsLinkedMallResponse.setLogisticStandardCode(dataItem.getLogisticsCompanyCode());
        if (logisticsDetail.size()>1) {
            logisticsLinkedMallResponse.setDeliveryTime(logisticsDetail.get(logisticsDetail.size()-2).getOcurrTimeStr());
        }else {
            logisticsLinkedMallResponse.setDeliveryTime("");
        }
        ArrayList<Map<String, String>> logisticsDetailList = new ArrayList<>();
        for (QueryLogisticsResponse.DataItem.LogisticsDetailListItem logisticsDetailListItem : logisticsDetail) {
            HashMap<String, String> map = new HashMap<>();
            map.put("time",logisticsDetailListItem.getOcurrTimeStr());
            map.put("context",logisticsDetailListItem.getStanderdDesc());
            logisticsDetailList.add(map);
        }
        logisticsLinkedMallResponse.setLogisticsDetailList(logisticsDetailList);
        return BaseResponse.success(logisticsLinkedMallResponse);
    }
}
