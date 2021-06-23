package com.wanmi.sbc.order.provider.impl.refund;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.refund.RefundBillProvider;
import com.wanmi.sbc.order.api.request.refund.RefundBillAddRequest;
import com.wanmi.sbc.order.api.request.refund.RefundBillDeleteByIdRequest;
import com.wanmi.sbc.order.api.request.refund.RefundBillRequest;
import com.wanmi.sbc.order.api.response.refund.RefundBillAddResponse;
import com.wanmi.sbc.order.api.response.refund.RefundBillDeleteByIdResponse;
import com.wanmi.sbc.order.api.response.refund.RefundBillResponse;
import com.wanmi.sbc.order.refund.model.root.RefundBill;
import com.wanmi.sbc.order.refund.service.RefundBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 14:46
 * @version: 1.0
 */
@Validated
@RestController
public class RefundBillController implements RefundBillProvider{

    @Autowired
    private RefundBillService refundBillService;

    /**
     * 新增流水单
     * @param refundBillAddRequest {@link RefundBillAddRequest }
     * @return {@link RefundBillAddResponse }
    */
    @Override
    public void add(@RequestBody @Valid RefundBillAddRequest refundBillAddRequest){
        refundBillService.save(refundBillAddRequest);
    }

    /**
     * 新增流水单
     * @param refundBillRequest {@link RefundBillRequest }
     * @return {@link RefundBillResponse }
     */
    @Override
    public BaseResponse<RefundBillResponse> addAndModifyRefundOrderReason(@RequestBody @Valid RefundBillRequest refundBillRequest){
        RefundBill refundBill = refundBillService.save(KsBeanUtil.convert(refundBillRequest,RefundBill.class)).orElseGet(() ->new RefundBill());
        return BaseResponse.success(KsBeanUtil.convert(refundBill,RefundBillResponse.class));
    }

    /**
     * 根据退款单ID删除流水
     * @param refundBillDeleteByIdRequest {@link RefundBillDeleteByIdRequest }
     * @return
    */
    @Override
    public BaseResponse<RefundBillDeleteByIdResponse> deleteById(@RequestBody @Valid RefundBillDeleteByIdRequest refundBillDeleteByIdRequest){
        Integer result = refundBillService.deleteByRefundId(refundBillDeleteByIdRequest.getRefundId());
        return BaseResponse.success(new RefundBillDeleteByIdResponse(result));
    }
}
