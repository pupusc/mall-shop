package com.wanmi.sbc.order.provider.impl.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.order.api.provider.groupon.GrouponProvider;
import com.wanmi.sbc.order.api.request.groupon.*;
import com.wanmi.sbc.order.api.response.groupon.GrouponDetailQueryResponse;
import com.wanmi.sbc.order.api.response.groupon.GrouponDetailWithGoodsResponse;
import com.wanmi.sbc.order.api.response.groupon.GrouponOrderStatusGetByOrderIdResponse;
import com.wanmi.sbc.order.api.response.groupon.GrouponOrderValidResponse;
import com.wanmi.sbc.order.bean.dto.GrouponDetailDTO;
import com.wanmi.sbc.order.bean.enums.GrouponOrderCheckStatus;
import com.wanmi.sbc.order.groupon.service.GrouponOrderService;
import com.wanmi.sbc.order.groupon.service.GrouponService;
import com.wanmi.sbc.order.trade.model.entity.GrouponTradeValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-03 10:44
 */
@Validated
@RestController
public class GrouponController implements GrouponProvider {

    @Autowired
    private GrouponService grouponService;

    @Autowired
    private GrouponOrderService grouponOrderService;

    @Override
    public BaseResponse<GrouponDetailQueryResponse> getGrouponDetail(@RequestBody @Valid  GrouponDetailQueryRequest
                                                                                 grouponDetailQueryRequest) {
        GrouponDetailDTO grouponDetail = KsBeanUtil.convert(grouponDetailQueryRequest,
                GrouponDetailDTO.class);
       return BaseResponse.success(grouponService.getGrouponDetail(grouponDetail)) ;
    }

    /**
     * 处理商品起订量限定量等信息
     * @param grouponDetailWithGoodsRequest
     * @return
     */
    @Override
    public BaseResponse<GrouponDetailWithGoodsResponse> getGrouponDetailWithGoodsInfos(@RequestBody @Valid
                                                                                           GrouponDetailWithGoodsRequest
                                                                             grouponDetailWithGoodsRequest) {
        GrouponDetailDTO grouponDetail = KsBeanUtil.convert(grouponDetailWithGoodsRequest,
                GrouponDetailDTO.class);
        return BaseResponse.success(grouponService.getGrouponDetailWithGoodsInfo(grouponDetail)) ;
    }

    @Override
    public BaseResponse<GrouponOrderStatusGetByOrderIdResponse> getGrouponOrderStatusByOrderId(@RequestBody @Valid GrouponOrderStatusGetByOrderIdRequest request) {
        GrouponOrderCheckStatus status =  grouponOrderService.checkGrouponOrderBeforePay(request.getOrderId());
        return BaseResponse.success(new GrouponOrderStatusGetByOrderIdResponse(status));
    }



    @Override
    public BaseResponse<GrouponOrderValidResponse> validGrouponOrderBeforeCommit(@RequestBody @Valid GrouponOrderValidRequest request) {
        GrouponGoodsInfoVO result = grouponOrderService.validGrouponImmediately(
                KsBeanUtil.convert(request, GrouponTradeValid.class));
        return BaseResponse.success(new GrouponOrderValidResponse(result));
    }

    @Override
    public BaseResponse getGrouponActivityByGrouponNo(@RequestBody @Valid GrouponActivityQueryRequest request){
        grouponService.getGrouponActivityByGrouponNo(request.getGrouponNo());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse grouponInstanceQuery(@RequestBody @Valid GrouponRecentActivityQueryRequest request){
        grouponService.grouponInstanceQuery(request.getActivityId());
        return BaseResponse.SUCCESSFUL();
    }


}
