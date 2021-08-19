package com.wanmi.sbc.order.api.provider.payorder;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.bean.dto.StringRequest;
import com.wanmi.sbc.order.api.request.payorder.*;
import com.wanmi.sbc.order.api.response.payorder.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p> 支付单查询服务</p>
 * author: sunpeng
 * Date: 2018-12-5
 */
@FeignClient(value = "${application.order.name}", contextId = "PayOrderQueryProvider")
public interface PayOrderQueryProvider {

    /**
     * pay模块无法引入tradeService，此处将OrderList传到controller，判断trade是否过了账期
     * @param request  {@link  FindPayOrderByPayOrderIdsRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/find-payorder-bypayorderids")
    BaseResponse<FindPayOrderByPayOrderIdsResponse> findPayOrderByPayOrderIds(@RequestBody @Valid FindPayOrderByPayOrderIdsRequest request);

    /**
     * 根据订单编号查询支付单，支付单状态.
     * @param request  {@link  StringRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/find-payorder-bypayordercode")
    BaseResponse<FindPayOrderByOrderCodeResponse> findPayOrderByOrderCode(@RequestBody @Valid FindPayOrderByOrderCodeRequest request);

    /**
     * 根据订单编号批量查询支付单，支付单状态.
     * @param request  {@link  StringRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/find-payorder-bypayordercodes")
    BaseResponse<FindPayOrderByOrderCodesResponse> findPayOrderByOrderCodes(@RequestBody @Valid FindPayOrderByOrderCodesRequest request);



    /**
     * 查找支付单
     * @param request  {@link StringRequest }
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/find-payorder")
    BaseResponse<FindPayOrderResponse> findPayOrder(@RequestBody @Valid FindPayOrderRequest request);

    /**
     * 根据父订单号查询子订单对应的付款单集合
     * @param request  {@link StringRequest }
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/find-payorder-list")
    BaseResponse<FindPayOrderListResponse> findPayOrderList(@RequestBody @Valid FindPayOrderListRequest request);

    /**
     * 查找支付单 数组
     * @param request  {@link  FindPayOrdersRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/find-payorders")
    BaseResponse<FindPayOrdersResponse> findPayOrders(@RequestBody @Valid FindPayOrdersRequest request);

    /**
     * 查找支付单 未分页
     * @param request  {@link  FindPayOrdersWithNoPageRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/find-payorders-withnopage")
    BaseResponse<FindPayOrdersWithNoPageResponse> findPayOrdersWithNoPage(@RequestBody @Valid FindPayOrdersWithNoPageRequest request);

    /**
     * 通过订单编号列表查询支付单
     * @param request  {@link  FindByOrderNosRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/find-payordernos")
    BaseResponse<FindByOrderNosResponse> findByOrderNos(@RequestBody @Valid FindByOrderNosRequest request);

    /**
     *  合计收款金额
     * @param request  {@link  SumPayOrderPriceRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/sum-payorderprice")
    BaseResponse<SumPayOrderPriceResponse> sumPayOrderPrice(@RequestBody @Valid SumPayOrderPriceRequest request);

}
