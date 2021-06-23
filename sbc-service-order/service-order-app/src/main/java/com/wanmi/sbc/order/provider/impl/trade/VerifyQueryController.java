package com.wanmi.sbc.order.provider.impl.trade;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.dto.GoodsDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.*;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.mapper.TradeGoodsListMapper;
import com.wanmi.sbc.order.trade.model.mapper.TradeItemMapper;
import com.wanmi.sbc.order.trade.service.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-04
 */
@Validated
@RestController
public class VerifyQueryController implements VerifyQueryProvider {

    @Autowired
    private VerifyService verifyService;
    @Autowired
    private TradeItemMapper tradeItemMapper;
    @Autowired
    private TradeGoodsListMapper tradeGoodsListMapper;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    /**
     * @param verifyGoodsRequest {@link VerifyGoodsRequest} 包含以下参数：
     *                           tradeItems        订单商品数据，仅包含skuId与购买数量
     *                           oldTradeItems     旧订单商品数据，可以为emptyList，用于编辑订单的场景，由于旧订单商品库存已先还回但事务未提交，因此在处理中将库存做逻辑叠加
     *                           goodsInfoResponse 关联商品信息
     *                           storeId           店铺ID
     *                           isFull            是否填充订单商品信息与设价(区间价/已经算好的会员价)
     * @return
     */
    @Override
    public BaseResponse<VerifyGoodsResponse> verifyGoods(@RequestBody @Valid VerifyGoodsRequest verifyGoodsRequest) {
        //如果是linkedmall商品，实时查库存
        TradeGoodsInfoPageDTO goodsInfoResponse = verifyGoodsRequest.getGoodsInfoResponse();
        List<Long> itemIds = goodsInfoResponse.getGoodses().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        }
        if (stocks != null) {
            for (GoodsDTO goodsDTO : goodsInfoResponse.getGoodses()) {
                if (ThirdPlatformType.LINKED_MALL.equals(goodsDTO.getThirdPlatformType())) {
                    Optional<QueryItemInventoryResponse.Item> optional = stocks.stream()
                            .filter(v -> v.getItemId().equals(Long.valueOf(goodsDTO.getThirdPlatformSpuId())))
                            .findFirst();
                    if (optional.isPresent()) {

                        Long totalStock = optional.get().getSkuList().stream()
                                .map(v -> v.getInventory().getQuantity())
                                .reduce(0L, ((aLong, aLong2) -> aLong + aLong2));
                        goodsDTO.setStock(totalStock);
                    }
                }
            }
            for (GoodsInfoDTO goodsInfo : goodsInfoResponse.getGoodsInfos()) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
                        Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream().filter(v -> String.valueOf(spuStock.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId())).findFirst();
                        if (stock.isPresent()) {
                            Long quantity = stock.get().getInventory().getQuantity();
                            goodsInfo.setStock(quantity);
                            if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
        }
        List<TradeItem> tradeItems = verifyService.verifyGoods(tradeItemMapper.tradeItemDTOsToTradeItems(verifyGoodsRequest.getTradeItems()),
                tradeItemMapper.tradeItemDTOsToTradeItems(verifyGoodsRequest.getOldTradeItems()),
                tradeGoodsListMapper.tradeGoodsInfoPageDTOToTradeGoodsListVO(verifyGoodsRequest.getGoodsInfoResponse()),
                verifyGoodsRequest.getStoreId(), verifyGoodsRequest.getIsFull(),null);
        VerifyGoodsResponse verifyGoodsResponse = new VerifyGoodsResponse();
        verifyGoodsResponse.setTradeItems(tradeItemMapper.tradeItemsToTradeItemVOs(tradeItems));
        return BaseResponse.success(verifyGoodsResponse);
    }

    @Override
    public BaseResponse<VerifyPointsGoodsResponse> verifyPointsGoods(@RequestBody @Valid VerifyPointsGoodsRequest verifyPointsGoodsRequest) {
        TradeItem tradeItem = verifyService.verifyPointsGoods(KsBeanUtil.convert(verifyPointsGoodsRequest.getTradeItem(), TradeItem.class),
                KsBeanUtil.convert(verifyPointsGoodsRequest.getGoodsInfoResponse(), TradeGoodsListVO.class),
                verifyPointsGoodsRequest.getPointsGoodsVO(), verifyPointsGoodsRequest.getStoreId());
        VerifyPointsGoodsResponse verifyPointsGoodsResponse = new VerifyPointsGoodsResponse();
        verifyPointsGoodsResponse.setTradeItem(KsBeanUtil.convert(tradeItem, TradeItemVO.class));
        return BaseResponse.success(verifyPointsGoodsResponse);
    }

    /**
     * @param mergeGoodsInfoRequest {@link MergeGoodsInfoRequest}  包含以下参数：
     *                              tradeItems        订单商品数据，仅包含skuId/价格
     *                              goodsInfoResponse 关联商品信息
     * @return
     */
    @Override
    public BaseResponse<MergeGoodsInfoResponse> mergeGoodsInfo(@RequestBody @Valid MergeGoodsInfoRequest mergeGoodsInfoRequest) {
        List<TradeItem> tradeItems = verifyService.mergeGoodsInfo(KsBeanUtil.convertList(mergeGoodsInfoRequest.getTradeItems(), TradeItem.class),
                KsBeanUtil.convert(mergeGoodsInfoRequest.getGoodsInfoResponse(), TradeGetGoodsResponse.class));
        MergeGoodsInfoResponse mergeGoodsInfoResponse = new MergeGoodsInfoResponse();
        mergeGoodsInfoResponse.setTradeItems(KsBeanUtil.convertList(tradeItems, TradeItemVO.class));
        return BaseResponse.success(mergeGoodsInfoResponse);
    }

    /**
     * @param verifyStoreRequest {@link VerifyStoreRequest} 包含多个店铺ID集合
     * @return
     */
    @Override
    public BaseResponse verifyStore(@RequestBody @Valid VerifyStoreRequest verifyStoreRequest) {
        verifyService.verifyStore(verifyStoreRequest.getStoreIds());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param verifyTradeMarketingRequest {@link VerifyTradeMarketingRequest}
     * @return
     */
    @Override
    public BaseResponse<VerifyTradeMarketingResponse> verifyTradeMarketing(@RequestBody @Valid VerifyTradeMarketingRequest verifyTradeMarketingRequest) {
        verifyService.verifyTradeMarketing(verifyTradeMarketingRequest.getTradeMarketingList(),
                KsBeanUtil.convertList(verifyTradeMarketingRequest.getOldGifts(), TradeItem.class),
                KsBeanUtil.convertList(verifyTradeMarketingRequest.getTradeItems(), TradeItem.class), verifyTradeMarketingRequest.getCustomerId(), verifyTradeMarketingRequest.getIsFoceCommit());
        return BaseResponse.success(new VerifyTradeMarketingResponse(verifyTradeMarketingRequest.getTradeMarketingList()));
    }

    @Override
    public BaseResponse<VerifyTradeMarketingResponse> verifyTradeCycleBuy(@Valid @RequestBody VerifyCycleBuyRequest verifyCycleBuyRequest) {
        verifyService.verifyCycleBuy(verifyCycleBuyRequest.getGoodsId(), verifyCycleBuyRequest.getGifts(),
                verifyCycleBuyRequest.getDeliveryCycle(), verifyCycleBuyRequest.getSendDateRule(), verifyCycleBuyRequest.getDeliveryPlan(),false);
        return BaseResponse.SUCCESSFUL();
    }
}
