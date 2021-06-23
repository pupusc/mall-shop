package com.wanmi.sbc.erp.api.provider;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.request.*;
import com.wanmi.sbc.erp.api.response.DeliveryStatusResponse;
import com.wanmi.sbc.erp.api.response.ReturnTradeResponse;
import com.wanmi.sbc.erp.api.response.SyncGoodsInfoResponse;
import com.wanmi.sbc.erp.api.response.WareHouseListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @program: sbc-background
 * @description: 管易云ERP接口服务
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 18:01
 **/
@FeignClient(value = "${application.erp.name}", contextId = "GuanyierpProvider")
public interface GuanyierpProvider {

    /**
     * 同步ERP商品库存
     * @param erpSynGoodsStockRequest
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/sync-goods-stock")
    BaseResponse<SyncGoodsInfoResponse> syncGoodsStock(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest);

    /**
     * 同步商品信息
     * @param erpSynGoodsStockRequest
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/sync-goods-info")
    BaseResponse<SyncGoodsInfoResponse> syncGoodsInfo(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest);

    /**
     * 推动订单
     * @param erpPushTradeRequest
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/auto-push-trade")
    BaseResponse autoPushTrade(@RequestBody @Valid PushTradeRequest erpPushTradeRequest);

    /**
     * 获取仓库集合
     * @param wareHouseQueryRequest
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/get-warehouse-list")
    BaseResponse<WareHouseListResponse> getWareHouseList(@RequestBody @Valid WareHouseQueryRequest wareHouseQueryRequest);

    /**
     * 发货单查询
     * @param request
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/get-delivery-status")
    BaseResponse<DeliveryStatusResponse> getDeliveryStatus(@RequestBody @Valid DeliveryQueryRequest request);

    /**
     * 退款中止发货
     * @param request
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/refund-trade")
    BaseResponse RefundTrade(@RequestBody @Valid RefundTradeRequest request);

    /**
     * 创建退货单
     * @param requst
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/create-return-order")
    BaseResponse createReturnOrder(@RequestBody @Valid ReturnTradeCreateRequst requst);

    /**
     * 退货单查询
     * @param request
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/get-return-trade-status")
    BaseResponse<ReturnTradeResponse> getReturnTradeStatus(@RequestBody @Valid  ReturnTradeQueryRequest request);

    /**
     * 历史发货单查询
     * @param request
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/get-history-delivery-status")
    BaseResponse<DeliveryStatusResponse> getHistoryDeliveryStatus(@RequestBody @Valid HistoryDeliveryInfoRequest request);
}
