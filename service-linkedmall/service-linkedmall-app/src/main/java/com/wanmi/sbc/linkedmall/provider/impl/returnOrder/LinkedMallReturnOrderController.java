package com.wanmi.sbc.linkedmall.provider.impl.returnOrder;


import com.aliyuncs.linkedmall.model.v20180116.ApplyRefundResponse;
import com.aliyuncs.linkedmall.model.v20180116.CancelRefundResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderProvider;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcApplyRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcCancelRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcSubmitReturnGoodLogisticsRequest;
import com.wanmi.sbc.linkedmall.returnorder.LinkedMallReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LinkedMallReturnOrderController implements LinkedMallReturnOrderProvider {
    @Autowired
    LinkedMallReturnOrderService linkedMallReturnOrderService;

    @Override
    public BaseResponse<ApplyRefundResponse.RefundApplicationData> applyRefund(@RequestBody @Valid SbcApplyRefundRequest sbcApplyRefundRequest) {
        return BaseResponse.success(linkedMallReturnOrderService.applyRefund(sbcApplyRefundRequest));
    }

    @Override
    public BaseResponse<CancelRefundResponse.RefundApplicationData> cancelRefund(@RequestBody @Valid SbcCancelRefundRequest sbcCancelRefundRequest) {
        return BaseResponse.success(linkedMallReturnOrderService.cancelRefund(sbcCancelRefundRequest));
    }

    @Override
    public BaseResponse submitReturnGoodLogistic(@RequestBody @Valid SbcSubmitReturnGoodLogisticsRequest sbcSubmitReturnGoodLogisticsRequest) {
        return linkedMallReturnOrderService.submitReturnGoodLogistics(sbcSubmitReturnGoodLogisticsRequest);
    }
}
