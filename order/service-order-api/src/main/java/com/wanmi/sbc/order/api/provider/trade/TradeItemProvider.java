package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.TradeItemModifyGoodsNumResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>订单商品服务操作接口</p>
 * @Author: daiyitian
 * @Description: 退单服务操作接口
 * @Date: 2018-12-03 15:40
 */
@FeignClient(value = "${application.order.name}", contextId = "TradeItemProvider")
public interface TradeItemProvider {

    /**
     * 保存订单商品快照
     *
     * @param request 保存订单商品快照请求结构 {@link TradeItemSnapshotRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/item/snapshot")
    BaseResponse snapshot(@RequestBody @Valid TradeItemSnapshotRequest request);

    /**
     * 修改订单商品快照的商品数量
     *
     * @param request 修改订单商品快照的商品数量请求结构 {@link TradeItemModifyGoodsNumRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/item/modify-goods-num")
    BaseResponse<TradeItemModifyGoodsNumResponse> modifyGoodsNum(@RequestBody @Valid TradeItemModifyGoodsNumRequest request);

    /**
     * 根据customerId删除订单商品快照
     *
     * @param request 根据customerId删除订单商品快照请求结构 {@link TradeItemDeleteByCustomerIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/item/delete-by-customer-id")
    BaseResponse deleteByCustomerId(@RequestBody @Valid TradeItemDeleteByCustomerIdRequest request);

    /**
     * 保存订单赠品快照
     *
     * @param request 保存订单商品快照请求结构 {@link TradeItemSnapshotGiftRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/item/snapshot-gift")
    BaseResponse snapshotGift(@RequestBody @Valid TradeItemSnapshotGiftRequest request);
    /**
     * 保存订单赠品快照
     *
     * @param request 保存订单商品快照请求结构 {@link TradeItemSnapshotGiftRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/item/snapshot-markup")
    BaseResponse snapshotMarkup(@RequestBody @Valid TradeItemSnapshotMarkupRequest request);

    /**
     * 确认结算
     *
     * @param request 保存订单商品快照请求结构 {@link TradeItemConfirmSettlementRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/item/confirm-settlement")
    BaseResponse confirmSettlement(@RequestBody @Valid TradeItemConfirmSettlementRequest request);

    /**
     * 周期购订单保存订单赠品快照
     *
     * @param request 保存订单商品快照请求结构 {@link TradeItemSnapshotCycleBuyGiftRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/item/snapshot-cycle-buy-gift")
    BaseResponse snapshotCycleBuyGift(@RequestBody @Valid TradeItemSnapshotCycleBuyGiftRequest request);

}
