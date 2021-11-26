package com.wanmi.sbc.linkedmall.api.provider.returnorder;

import com.aliyuncs.linkedmall.model.v20180116.InitApplyRefundResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcInitApplyRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcQueryRefundApplicationDetailRequest;
import com.wanmi.sbc.linkedmall.api.response.returnorder.SbcQueryRefundApplicationDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.linkedmall.name}",contextId = "LinkedMallReturnOrderQueryProvider")
public interface LinkedMallReturnOrderQueryProvider {

    @PostMapping("/linkedmall/${application.linkedmall.version}/initApplyRefund")
    BaseResponse<InitApplyRefundResponse.InitApplyRefundData> initApplyRefund(@RequestBody @Valid SbcInitApplyRefundRequest sbcInitApplyRefundRequest);

    @PostMapping("/linkedmall/${application.linkedmall.version}/queryRefundApplicationDetail")
    BaseResponse<SbcQueryRefundApplicationDetailResponse> queryRefundApplicationDetail(@RequestBody @Valid SbcQueryRefundApplicationDetailRequest sbcQueryRefundApplicationDetailRequest);

}
