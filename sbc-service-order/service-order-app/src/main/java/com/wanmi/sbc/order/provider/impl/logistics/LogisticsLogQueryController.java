package com.wanmi.sbc.order.provider.impl.logistics;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.logistics.LogisticsLogQueryProvider;
import com.wanmi.sbc.order.api.request.logistics.LogisticsLogSimpleListByCustomerIdRequest;
import com.wanmi.sbc.order.api.response.logistics.LogisticsLogSimpleListByCustomerIdResponse;
import com.wanmi.sbc.order.bean.vo.LogisticsLogSimpleVO;
import com.wanmi.sbc.order.logistics.model.root.LogisticsLog;
import com.wanmi.sbc.order.logistics.request.LogisticsLogQueryRequest;
import com.wanmi.sbc.order.logistics.service.LogisticsLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>服务信息服务</p>
 */
@Slf4j
@RestController
public class LogisticsLogQueryController implements LogisticsLogQueryProvider {

    @Autowired
    private LogisticsLogService logisticsLogService;

    @Override
    public BaseResponse<LogisticsLogSimpleListByCustomerIdResponse> listByCustomerId(@RequestBody
                                                                                             LogisticsLogSimpleListByCustomerIdRequest
                                                                                             request) {
        List<LogisticsLog> logs = logisticsLogService.query(LogisticsLogQueryRequest.builder()
                .customerId(request.getCustomerId()).hasDetailsFlag(Boolean.TRUE).customerShowLimit(Boolean.TRUE).build());

        List<LogisticsLogSimpleVO> vos = logs.stream().map(log -> {
            LogisticsLogSimpleVO vo = new LogisticsLogSimpleVO();
            vo.setId(log.getId());
            vo.setGoodsImg(log.getGoodsImg());
            vo.setGoodsName(log.getGoodsName());
            vo.setState(log.getState());
            vo.setDeliverId(log.getDeliverId());
            vo.setOrderNo(log.getOrderNo());
            if(CollectionUtils.isNotEmpty(log.getLogisticsLogDetails())) {
                vo.setContext(log.getLogisticsLogDetails().get(0).getContext());
                vo.setTime(log.getLogisticsLogDetails().get(0).getTime());
            }
            return vo;
        }).collect(Collectors.toList());

        return BaseResponse.success(LogisticsLogSimpleListByCustomerIdResponse.builder().logisticsList(vos).build());
    }
}
