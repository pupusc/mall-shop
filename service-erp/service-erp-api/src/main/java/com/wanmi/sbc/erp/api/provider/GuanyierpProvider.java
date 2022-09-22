package com.wanmi.sbc.erp.api.provider;
import com.sbc.wanmi.erp.bean.vo.ERPGoodsInfoVO;
import com.sbc.wanmi.erp.bean.vo.ErpStockVo;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.request.*;
import com.wanmi.sbc.erp.api.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: sbc-background
 * @description: 管易云ERP接口服务
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 18:01
 **/
@FeignClient(value = "${application.erp.name}", contextId = "GuanyierpProvider")
public interface GuanyierpProvider {

//    /**
//     * 同步ERP商品库存
//     * @param erpSynGoodsStockRequest
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/sync-goods-stock")
//    BaseResponse<SyncGoodsInfoResponse> syncGoodsStock(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest);

//    /**
//     * 获取商品库存
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/get-updated-stock")
//    BaseResponse<ErpStockVo> getUpdatedStock(@RequestParam("startTime") String startTime, @RequestParam("erpGoodInfoNo") String erpGoodInfoNo,
//                                             @RequestParam(value = "pageNum", defaultValue = "1") String pageNum,
//                                             @RequestParam(value = "pageSize",defaultValue = "20") String pageSize);

//    @PostMapping("/erp/${application.erp.version}/guanyierp/list-ware-hose-stock")
//    BaseResponse<ErpStockVo> listWareHoseStock(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("erpGoodNo") String erpGoodNo);

//    /**
//     * 获取Goods信息
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/get-erp-goods")
//    BaseResponse<List<ERPGoodsInfoVO>> getErpGoodsInfoWithoutStock(@RequestParam("erpGoodsNum") String erpGoodsNum);

    /**
     * 同步商品信息
     * @param erpSynGoodsStockRequest
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/sync-goods-info")
    BaseResponse<SyncGoodsInfoResponse> syncGoodsInfo(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest);

//    /**
//     * 推动订单
//     * @param erpPushTradeRequest
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/auto-push-trade")
//    BaseResponse autoPushTrade(@RequestBody @Valid PushTradeRequest erpPushTradeRequest);


//    /**
//     * 推动订单--已发货状态
//     * @param erpPushTradeRequest
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/auto-push-trade-delivered")
//    BaseResponse autoPushTradeDelivered(@RequestBody @Valid PushTradeRequest erpPushTradeRequest);

//    /**
//     * 获取仓库集合
//     * @param wareHouseQueryRequest
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/get-warehouse-list")
//    BaseResponse<WareHouseListResponse> getWareHouseList(@RequestBody @Valid WareHouseQueryRequest wareHouseQueryRequest);

    /**
     * 发货单查询
     * @param request
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/get-delivery-status")
    BaseResponse<DeliveryStatusResponse> getDeliveryStatus(@RequestBody @Valid DeliveryQueryRequest request);

//    /**
//     * 退款商品终止发货
//     * @param request
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/refund-trade-item")
//    BaseResponse refundTradeItem(@RequestBody @Valid RefundTradeRequest request);


//    /**
//     * 退款订单拦截
//     * @param request
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/refund-trade-order")
//    BaseResponse refundTradeOrder(@RequestBody @Valid RefundTradeRequest request);

//    /**
//     * 退款中止发货
//     * @param request
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/refund-trade")
//    BaseResponse RefundTrade(@RequestBody @Valid RefundTradeRequest request);

    /**
     * 创建退货单
     * @param requst
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/create-return-order")
    BaseResponse createReturnOrder(@RequestBody @Valid ReturnTradeCreateRequst requst);

//    /**
//     * 退货单查询
//     * @param request
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/get-return-trade-status")
//    BaseResponse<ReturnTradeResponse> getReturnTradeStatus(@RequestBody @Valid  ReturnTradeQueryRequest request);

//    /**
//     * 历史发货单查询
//     * @param request
//     * @return
//     */
//    @PostMapping("/erp/${application.erp.version}/guanyierp/get-history-delivery-status")
//    BaseResponse<DeliveryStatusResponse> getHistoryDeliveryStatus(@RequestBody @Valid HistoryDeliveryInfoRequest request);


    /**
     * 订单查询
     * @param request
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/guanyierp/get-trade-info")
    BaseResponse<QueryTradeResponse> getTradeInfo(@RequestBody @Valid TradeQueryRequest request);
}
