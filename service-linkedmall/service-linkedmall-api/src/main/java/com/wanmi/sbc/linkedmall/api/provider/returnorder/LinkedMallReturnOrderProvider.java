package com.wanmi.sbc.linkedmall.api.provider.returnorder;


import com.aliyuncs.linkedmall.model.v20180116.ApplyRefundResponse;
import com.aliyuncs.linkedmall.model.v20180116.CancelRefundResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcApplyRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcCancelRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcSubmitReturnGoodLogisticsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.linkedmall.name}",contextId = "LinkedMallReturnOrderProvider")
public interface LinkedMallReturnOrderProvider {

    @PostMapping("/linkedmall/${application.linkedmall.version}/applyRefund")
    BaseResponse<ApplyRefundResponse.RefundApplicationData> applyRefund(@RequestBody @Valid SbcApplyRefundRequest sbcApplyRefundRequest);


    @PostMapping("/linkedmall/${application.linkedmall.version}/cancelRefund")
    BaseResponse<CancelRefundResponse.RefundApplicationData> cancelRefund(@RequestBody @Valid SbcCancelRefundRequest sbcCancelRefundRequest);

    @PostMapping("/linkedmall/${application.linkedmall.version}/submitReturnGoodLogistic")
    BaseResponse submitReturnGoodLogistic(@RequestBody @Valid SbcSubmitReturnGoodLogisticsRequest sbcSubmitReturnGoodLogisticsRequest);
}
