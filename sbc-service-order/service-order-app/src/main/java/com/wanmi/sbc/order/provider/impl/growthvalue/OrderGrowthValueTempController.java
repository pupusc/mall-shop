package com.wanmi.sbc.order.provider.impl.growthvalue;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.growthvalue.OrderGrowthValueTempProvider;
import com.wanmi.sbc.order.api.request.growthvalue.OrderGrowthValueTempQueryRequest;
import com.wanmi.sbc.order.api.response.growthvalue.OrderGrowthValueTempListResponse;
import com.wanmi.sbc.order.api.response.growthvalue.OrderGrowthValueTempPageResponse;
import com.wanmi.sbc.order.bean.vo.OrderGrowthValueTempVO;
import com.wanmi.sbc.order.growthvalue.model.root.OrderGrowthValueTemp;
import com.wanmi.sbc.order.growthvalue.service.OrderGrowthValueTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员权益处理订单成长值 临时表查询服务接口实现</p>
 */
@RestController
@Validated
public class OrderGrowthValueTempController implements OrderGrowthValueTempProvider {
    @Autowired
    private OrderGrowthValueTempService orderGrowthValueTempService;

    @Override
    public BaseResponse<OrderGrowthValueTempPageResponse> page(@RequestBody @Valid OrderGrowthValueTempQueryRequest orderGrowthValueTempPageReq) {
        Page<OrderGrowthValueTemp> orderGrowthValueTempPage = orderGrowthValueTempService.page(orderGrowthValueTempPageReq);
        Page<OrderGrowthValueTempVO> newPage = orderGrowthValueTempPage.map(entity -> orderGrowthValueTempService.wrapperVo(entity));
        MicroServicePage<OrderGrowthValueTempVO> microPage = new MicroServicePage<>(newPage, orderGrowthValueTempPageReq.getPageable());
        OrderGrowthValueTempPageResponse finalRes = new OrderGrowthValueTempPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<OrderGrowthValueTempListResponse> list(@RequestBody @Valid OrderGrowthValueTempQueryRequest orderGrowthValueTempListReq) {
        List<OrderGrowthValueTemp> orderGrowthValueTempList = orderGrowthValueTempService.list(orderGrowthValueTempListReq);
        List<OrderGrowthValueTempVO> newList = orderGrowthValueTempList.stream().map(entity -> orderGrowthValueTempService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new OrderGrowthValueTempListResponse(newList));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid OrderGrowthValueTempQueryRequest orderGrowthValueTempDelByIdRequest) {
        orderGrowthValueTempService.deleteById(orderGrowthValueTempDelByIdRequest.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid OrderGrowthValueTempQueryRequest orderGrowthValueTempDelByIdListRequest) {
        orderGrowthValueTempService.deleteByIdList(orderGrowthValueTempDelByIdListRequest.getIdList());
        return BaseResponse.SUCCESSFUL();
    }


}
