package com.wanmi.sbc.order.provider.impl.yzorder;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.yzorder.YzOrderProvider;
import com.wanmi.sbc.order.api.request.yzorder.YzOrderAddBatchRequest;
import com.wanmi.sbc.order.api.request.yzorder.YzOrderPageQueryRequest;
import com.wanmi.sbc.order.api.response.yzorder.YzOrderListResponse;
import com.wanmi.sbc.order.api.response.yzorder.YzOrderPageResponse;
import com.wanmi.sbc.order.bean.dto.yzorder.YzOrderDTO;
import com.wanmi.sbc.order.yzorder.model.root.YzOrder;
import com.wanmi.sbc.order.yzorder.request.YzOrderQueryRequest;
import com.wanmi.sbc.order.yzorder.service.YzOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
public class YzOrderController implements YzOrderProvider {

    @Autowired
    private YzOrderService yzOrderService;

    @Override
    public BaseResponse addBatch(@RequestBody @Valid YzOrderAddBatchRequest request) {
        List<YzOrder> list = request.getYzOrders().stream().map(dto -> KsBeanUtil.convert(dto, YzOrder.class)).collect(Collectors.toList());
        yzOrderService.addAll(list, request.getUpdateFlag());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<YzOrderPageResponse> page(@RequestBody @Valid YzOrderPageQueryRequest request) {
        Page<YzOrder> page = yzOrderService.page(KsBeanUtil.convert(request, YzOrderQueryRequest.class));
        MicroServicePage<YzOrderDTO> yzOrderDTOS = KsBeanUtil.convertPage(page, YzOrderDTO.class);
        return BaseResponse.success(YzOrderPageResponse.builder().yzOrderPage(yzOrderDTOS).build());
    }

    @Override
    public BaseResponse convertType(@RequestBody @Valid YzOrderPageQueryRequest request) {
        YzOrderQueryRequest queryRequest = KsBeanUtil.convert(request, YzOrderQueryRequest.class);
        yzOrderService.convert(queryRequest, Boolean.FALSE);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse compare(@RequestBody @Valid YzOrderPageQueryRequest request) {
        YzOrderQueryRequest queryRequest = KsBeanUtil.convert(request, YzOrderQueryRequest.class);
        yzOrderService.compare(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<YzOrderListResponse> getYzOrderIdList(@RequestBody @Valid YzOrderPageQueryRequest request) {
        YzOrderQueryRequest queryRequest = KsBeanUtil.convert(request, YzOrderQueryRequest.class);
        List<String> yzOrderIds = yzOrderService.getYzOrderIds(queryRequest);
        return BaseResponse.success(YzOrderListResponse.builder().ids(yzOrderIds).build());
    }

    @Override
    public BaseResponse convert(@RequestBody @Valid YzOrderPageQueryRequest request) {
        yzOrderService.convert2(request);
        yzOrderService.convertAgain();
        return BaseResponse.SUCCESSFUL();
    }
}
