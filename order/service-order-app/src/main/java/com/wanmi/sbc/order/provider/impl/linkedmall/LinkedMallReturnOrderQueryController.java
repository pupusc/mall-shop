package com.wanmi.sbc.order.provider.impl.linkedmall;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.linkedmall.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.request.linkedmall.LinkedMallReturnOrderReasonRequest;
import com.wanmi.sbc.order.api.response.linkedmall.LinkedMallReturnReasonResponse;
import com.wanmi.sbc.order.returnorder.service.LinkedMallReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>linkedMall退单服务查询接口</p>
 */
@RestController
public class LinkedMallReturnOrderQueryController implements LinkedMallReturnOrderQueryProvider {

    @Autowired
    private LinkedMallReturnOrderService linkedMallReturnOrderService;

    @Override
    public BaseResponse<LinkedMallReturnReasonResponse> listReturnReason(@RequestBody @Valid LinkedMallReturnOrderReasonRequest
                                                                                     request){
        return BaseResponse.success(linkedMallReturnOrderService.reasons(request.getRid()));
    }
}
