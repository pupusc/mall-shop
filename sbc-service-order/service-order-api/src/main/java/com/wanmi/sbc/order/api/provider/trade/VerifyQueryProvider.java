package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.MergeGoodsInfoResponse;
import com.wanmi.sbc.order.api.response.trade.VerifyGoodsResponse;
import com.wanmi.sbc.order.api.response.trade.VerifyPointsGoodsResponse;
import com.wanmi.sbc.order.api.response.trade.VerifyTradeMarketingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 10:04
 * @version: 1.0
 */
@FeignClient(value = "${application.order.name}", contextId = "VerifyQueryProvider")
public interface VerifyQueryProvider {

    /**
     * 校验购买商品,计算商品价格
     * 1.校验商品库存，删除，上下架状态
     * 2.验证购买商品的起订量,限定量
     * 3.根据isFull,填充商品信息与levelPrice(第一步价格计算)
     * @param verifyGoodsRequest   {@link VerifyGoodsRequest} 包含以下参数：
     *                            tradeItems        订单商品数据，仅包含skuId与购买数量
     *                            oldTradeItems     旧订单商品数据，可以为emptyList，用于编辑订单的场景，由于旧订单商品库存已先还回但事务未提交，因此在处理中将库存做逻辑叠加
     *                            goodsInfoResponse 关联商品信息
     *                            storeId           店铺ID
     *                            isFull            是否填充订单商品信息与设价(区间价/已经算好的会员价)
     * @return  {@link VerifyGoodsResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/verify-goods")
    BaseResponse<VerifyGoodsResponse> verifyGoods(@RequestBody @Valid VerifyGoodsRequest verifyGoodsRequest);

    /**
     * 校验购买积分商品
     * 1.校验积分商品库存，删除，上下架状态
     * @param verifyPointsGoodsRequest   {@link VerifyPointsGoodsRequest} 包含以下参数：
     *                            tradeItems        订单商品数据，仅包含skuId与购买数量
     *                            goodsInfoResponse 关联商品信息
     *                            storeId           店铺ID
     * @return  {@link VerifyPointsGoodsResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/verify-points-goods")
    BaseResponse<VerifyPointsGoodsResponse> verifyPointsGoods(@RequestBody @Valid VerifyPointsGoodsRequest verifyPointsGoodsRequest);

    /**
     * 为tradeItem 填充商品基本信息
     * @param mergeGoodsInfoRequest {@link MergeGoodsInfoRequest}  包含以下参数：
     *                                                           tradeItems        订单商品数据，仅包含skuId/价格
     *                                                           goodsInfoResponse 关联商品信息
     * @return {@link MergeGoodsInfoResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/merge-goods-info")
    BaseResponse<MergeGoodsInfoResponse> mergeGoodsInfo(@RequestBody @Valid MergeGoodsInfoRequest mergeGoodsInfoRequest);

    /**
     * 验证店铺
     * @param verifyStoreRequest  {@link VerifyStoreRequest} 包含多个店铺ID集合
     * @return  {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/verify-store")
    BaseResponse verifyStore(@RequestBody @Valid VerifyStoreRequest verifyStoreRequest);

    /**
     * 营销活动校验
     * @param verifyTradeMarketingRequest  {@link VerifyTradeMarketingRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/verify-trade-marketing")
    BaseResponse<VerifyTradeMarketingResponse> verifyTradeMarketing(@RequestBody @Valid VerifyTradeMarketingRequest verifyTradeMarketingRequest);

    /**
     * 周期购活动校验
     * @param verifyCycleBuyRequest  {@link VerifyCycleBuyRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/verify-trade-cycle-buy")
    BaseResponse<VerifyTradeMarketingResponse> verifyTradeCycleBuy(@RequestBody @Valid VerifyCycleBuyRequest verifyCycleBuyRequest);
}
