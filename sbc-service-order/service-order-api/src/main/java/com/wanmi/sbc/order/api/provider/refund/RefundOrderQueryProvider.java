package com.wanmi.sbc.order.api.provider.refund;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.refund.*;
import com.wanmi.sbc.order.api.request.trade.TradeAccountRecordRequest;
import com.wanmi.sbc.order.api.response.refund.*;
import com.wanmi.sbc.order.api.response.trade.TradeAccountRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 13:46
 * @version: 1.0
 */
@FeignClient(value = "${application.order.name}", contextId = "RefundOrderQueryProvider")
public interface RefundOrderQueryProvider {

    /**
     * 查询退款单
     * @param refundOrderPageRequest {@link RefundOrderPageRequest }
     * @return {@link RefundOrderPageResponse }
    */
    @PostMapping("/order/${application.order.version}/refund/page")
    BaseResponse<RefundOrderPageResponse> page(@RequestBody @Valid RefundOrderPageRequest refundOrderPageRequest);

    /**
     * 查询不带分页的退款单
     * @param refundOrderWithoutPageRequest {@link RefundOrderWithoutPageRequest }
     * @return {@link RefundOrderWithoutPageResponse }
    */
    @PostMapping("/order/${application.order.version}/refund/list")
    BaseResponse<RefundOrderWithoutPageResponse> list(@RequestBody @Valid RefundOrderWithoutPageRequest refundOrderWithoutPageRequest);

    /**
     * 根据退单编号查询退款单 （查不到抛异常）
     * @param refundOrderByReturnOrderCodeRequest 包含：退单编号 {@link RefundOrderByReturnOrderCodeRequest }
     * @return  {@link RefundOrderByReturnCodeResponse }
    */
    @PostMapping("/order/${application.order.version}/refund/get-by-return-code")
    BaseResponse<RefundOrderByReturnCodeResponse> getByReturnOrderCode(@RequestBody @Valid RefundOrderByReturnOrderCodeRequest refundOrderByReturnOrderCodeRequest);

    /**
     * 根据退单ID查询退款单
     * @param refundOrderByIdRequest 包含：退单ID {@link RefundOrderByIdRequest }
     * @return  {@link RefundOrderByIdResponse }
     */
    @PostMapping("/order/${application.order.version}/refund/get-by-id")
    BaseResponse<RefundOrderByIdResponse> getById(@RequestBody @Valid RefundOrderByIdRequest refundOrderByIdRequest);

    /**
     * 根据退单编号查询退款单
     * @param refundOrderResponseByReturnOrderCodeRequest {@link RefundOrderResponseByReturnOrderCodeRequest }
     * @return {@link RefundOrderResponse }
    */
    @PostMapping("/order/${application.order.version}/refund/get-refund-order-resp-by-return-code")
    BaseResponse<RefundOrderListReponse> getRefundOrderRespByReturnOrderCode(@RequestBody @Valid RefundOrderResponseByReturnOrderCodeRequest refundOrderResponseByReturnOrderCodeRequest);

    /**
     * 合计退款金额
     * @param refundOrderRequest {@link RefundOrderRequest }
     * @return {@link RefundOrderGetSumReturnPriceResponse }
    */
    @PostMapping("/order/${application.order.version}/refund/get-sum-return-price")
    BaseResponse<RefundOrderGetSumReturnPriceResponse> getSumReturnPrice(@RequestBody @Valid RefundOrderRequest refundOrderRequest);

    /**
     * 根据退单编号查询退款单（查不到不抛异常）
     * @param refundOrderByReturnOrderNoRequest 包含：退单编号 {@link RefundOrderByReturnOrderNoRequest }
     * @return  {@link RefundOrderByReturnOrderNoResponse }
     */
    @PostMapping("/order/${application.order.version}/refund/get-by-return-order-no")
    BaseResponse<RefundOrderByReturnOrderNoResponse> getByReturnOrderNo(@RequestBody @Valid RefundOrderByReturnOrderNoRequest refundOrderByReturnOrderNoRequest);


    /**
     * 查询财务对账
     */
    @PostMapping("/order/${application.order.version}/refund/account-record")
    BaseResponse<RefundOrderAccountRecordResponse> getRefundOrderAccountRecord(@RequestBody @Valid RefundOrderAccountRecordRequest request);


}
