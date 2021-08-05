package com.wanmi.sbc.linkedmall.provider.impl.returnOrder;

import com.aliyuncs.linkedmall.model.v20180116.InitApplyRefundResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcInitApplyRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcQueryRefundApplicationDetailRequest;
import com.wanmi.sbc.linkedmall.api.response.returnorder.SbcQueryRefundApplicationDetailResponse;
import com.wanmi.sbc.linkedmall.returnorder.LinkedMallReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LinkedMallReturnOrderQueryController implements LinkedMallReturnOrderQueryProvider {
    @Autowired
    LinkedMallReturnOrderService linkedMallReturnOrderService;
    @Override
    public BaseResponse<InitApplyRefundResponse.InitApplyRefundData> initApplyRefund(@RequestBody @Valid SbcInitApplyRefundRequest sbcInitApplyRefundRequest) {
        return BaseResponse.success(linkedMallReturnOrderService.initApplyRefund(sbcInitApplyRefundRequest));
    }

    @Override
    public BaseResponse<SbcQueryRefundApplicationDetailResponse> queryRefundApplicationDetail(@RequestBody @Valid SbcQueryRefundApplicationDetailRequest sbcQueryRefundApplicationDetailRequest) {
        return BaseResponse.success(new SbcQueryRefundApplicationDetailResponse(linkedMallReturnOrderService.queryRefundApplicationDetail(sbcQueryRefundApplicationDetailRequest)));
    }
}
