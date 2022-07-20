package com.wanmi.sbc.order.trade.service;


import com.alibaba.fastjson.JSON;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectSwitchQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchByCompanyInfoIdResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerSimplifyByIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointLockRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCheckRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaQueryRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerSimplifyByIdResponse;
import com.wanmi.sbc.customer.api.response.store.StoreCustomerRelaResponse;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsRestrictedErrorTips;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsSaveProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponCodeProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleIsInProgressRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsBatchStockAndSalesVolumeRequest;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponCodeLinkOrderRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponCodeUnLinkOrderRequest;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleIsInProgressResponse;
import com.wanmi.sbc.goods.api.response.cate.ContractCateListResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.constant.CycleBuyErrorCode;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPlusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPlusStockDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.marketing.api.provider.buyoutprice.MarketingBuyoutPriceQueryProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoQueryProvider;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingFullDiscountQueryProvider;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingFullReductionQueryProvider;
import com.wanmi.sbc.marketing.api.provider.gift.FullGiftQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingScopeQueryProvider;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeValidOrderCommitRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullReductionByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftLevelListByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingMapGetByGoodsIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingScopeListInvalidMarketingRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodeValidOrderCommitResponse;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.*;
import com.wanmi.sbc.order.api.request.flashsale.FlashSaleGoodsOrderCancelReturnStockRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.common.GoodsStockService;
import com.wanmi.sbc.order.constant.OrderErrorCode;
import com.wanmi.sbc.order.mq.FlashSaleGoodsOrderCancelReturnStockService;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Invoice;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.request.VirtualCouponUpdateRequest;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressListRequest;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 校验Service
 * Created by jinwei on 22/3/2017.
 */
@Slf4j
@Service
public class VerifyService {

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private StoreProvider storeProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private InvoiceProjectSwitchQueryProvider invoiceProjectSwitchQueryProvider;

    @Autowired
    private MarketingFullDiscountQueryProvider marketingFullDiscountQueryProvider;

    @Autowired
    private MarketingFullReductionQueryProvider marketingFullReductionQueryProvider;

    @Autowired
    private MarketingBuyoutPriceQueryProvider marketingBuyoutPriceQueryProvider;

    @Autowired
    private FullGiftQueryProvider fullGiftQueryProvider;

    @Autowired
    private CycleBuyQueryProvider cycleBuyQueryProvider;

    @Autowired
    private MarketingScopeQueryProvider marketingScopeQueryProvider;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private CouponCodeQueryProvider couponCodeQueryProvider;


    @Autowired
    private CouponInfoQueryProvider couponInfoQueryProvider;

    @Autowired
    private VirtualCouponCodeProvider virtualCouponCodeProvider;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private FlashSaleGoodsSaveProvider flashSaleGoodsSaveProvider;

    @Autowired
    private FlashSaleGoodsOrderCancelReturnStockService flashSaleGoodsOrderCancelReturnStockService;

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private TradeCustomerService tradeCustomerService;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private ThirdAddressQueryProvider thirdAddressQueryProvider;
    @Autowired
    private ExternalProvider externalProvider;


    /**
     * 校验购买商品,计算商品价格
     * 1.校验商品，删库存除，上下架状态
     * 2.验证购买商品的起订量,限定量
     * 3.根据isFull,填充商品信息与levelPrice(第一步价格计算)
     *
     * @param tradeItems        订单商品数据，仅包含skuId与购买数量
     * @param oldTradeItems     旧订单商品数据，可以为emptyList，用于编辑订单的场景，由于旧订单商品库存已先还回但事务未提交，因此在处理中将库存做逻辑叠加
     * @param goodsInfoResponse 关联商品信息
     * @param isFull            是否填充订单商品信息与设价(区间价/已经算好的会员价)
     * @param areaId            区域地址码，查区域库存
     */
    public List<TradeItem> verifyGoods(List<TradeItem> tradeItems, List<TradeItem> oldTradeItems, TradeGoodsListVO goodsInfoResponse, Long storeId,
                                       boolean isFull, String areaId) {
        log.info("VerifyService verifyGoods goodsInfoResponse:{}" , JSON.toJSONString(goodsInfoResponse));

        List<GoodsInfoVO> goodsInfos = goodsInfoResponse.getGoodsInfos();
        Map<String, GoodsVO> goodsMap = goodsInfoResponse.getGoodses().stream().collect(Collectors.toMap
                (GoodsVO::getGoodsId, Function.identity()));
        Map<String, GoodsInfoVO> goodsInfoMap = goodsInfos.stream().collect(Collectors.toMap
                (GoodsInfoVO::getGoodsInfoId, Function.identity()));
        Map<String, Long> oldTradeItemMap = oldTradeItems.stream().collect(Collectors.toMap(TradeItem::getSkuId, TradeItem::getNum));
        tradeItems
                .forEach(tradeItem -> {
                    GoodsInfoVO goodsInfo = goodsInfoMap.get(tradeItem.getSkuId());
                    GoodsVO goods = goodsMap.get(goodsInfo.getGoodsId());
                    Long oldNum = oldTradeItemMap.getOrDefault(tradeItem.getSkuId(), 0L);
                    //1. 校验商品库存，删除，上下架状态
                    if (goodsInfo.getDelFlag().equals(DeleteFlag.YES) || goodsInfo.getAddedFlag().equals(0)
                            || goods.getAuditStatus().equals(CheckStatus.FORBADE) || Objects.equals(DefaultFlag.NO.toValue(),
                            buildGoodsInfoVendibility(goodsInfo))) {
                        throw new SbcRuntimeException("K-050117");
                    }
                    //校验供应商商品库存
                    if (StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId()) && Objects.isNull(goodsInfo.getThirdPlatformType())) {
                        GoodsInfoByIdResponse providerGoodsInfo = goodsInfoQueryProvider.getById(new GoodsInfoByIdRequest(goodsInfo.getProviderGoodsInfoId(), null)).getContext();
                        //购买数量大于供应商库存
                        if (tradeItem.getNum() > (providerGoodsInfo.getStock() + oldNum)) {
                            throw new SbcRuntimeException("K-050116");
                        }
                    }
                    //如果是linkedmall商品，实时查库存,根据区域码查库存
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
                        String thirdAddrId = null;
                        if (areaId != null) {
                            List<ThirdAddressVO> thirdAddressList = thirdAddressQueryProvider.list(ThirdAddressListRequest.builder()
                                    .platformAddrIdList(Collections.singletonList(Objects.toString(areaId)))
                                    .thirdFlag(ThirdPlatformType.LINKED_MALL)
                                    .build()).getContext().getThirdAddressList();
                            if (CollectionUtils.isNotEmpty(thirdAddressList)) {
                                thirdAddrId = thirdAddressList.get(0).getThirdAddrId();
                            }
                        }
                        String thirdPlatformSpuId = goodsInfo.getThirdPlatformSpuId();
                        List<QueryItemInventoryResponse.Item> stocks =
                                linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(thirdPlatformSpuId)), thirdAddrId == null ? "0" : thirdAddrId, null)).getContext();
                        if (stocks != null) {
                            Optional<QueryItemInventoryResponse.Item> optional = stocks.stream()
                                    .filter(v -> String.valueOf(v.getItemId()).equals(thirdPlatformSpuId)).findFirst();
                            if (optional.isPresent()) {
                                String thirdPlatformSkuId = goodsInfo.getThirdPlatformSkuId();
                                Optional<QueryItemInventoryResponse.Item.Sku> skuStock = optional.get().getSkuList().stream()
                                        .filter(v -> String.valueOf(v.getSkuId()).equals(thirdPlatformSkuId)).findFirst();
                                if (skuStock.isPresent()) {
                                    Long quantity = skuStock.get().getInventory().getQuantity();
                                    if (quantity < 1) {
                                        throw new SbcRuntimeException("K-050143");
                                    }
                                } else {
                                    throw new SbcRuntimeException("K-050143");
                                }
                            }

                        }
                    }
//                    if (!(Objects.nonNull(tradeItem.getIsFlashSaleGoods()) && tradeItem.getIsFlashSaleGoods())) {
                    verifyGoodsInternal(tradeItem, goodsInfo, oldNum);
//                    }
                    if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods()) {
                        BookingSaleIsInProgressResponse response = bookingSaleQueryProvider.isInProgress(BookingSaleIsInProgressRequest
                                .builder().goodsInfoId(tradeItem.getSkuId()).build()).getContext();
                        if (Objects.isNull(response) || Objects.isNull(response.getBookingSaleVO())) {
                            throw new SbcRuntimeException("K-000009");
                        }
                        BookingSaleVO bookingSaleVO = response.getBookingSaleVO();
                        if (!bookingSaleVO.getId().equals(tradeItem.getBookingSaleId())) {
                            throw new SbcRuntimeException("K-000009");
                        }
                        if (Objects.nonNull(bookingSaleVO.getBookingSaleGoods().getBookingCount())
                                && Objects.nonNull(bookingSaleVO.getBookingSaleGoods().getCanBookingCount())) {
                            if (tradeItem.getNum() > (bookingSaleVO.getBookingSaleGoods().getCanBookingCount())) {
                                throw new SbcRuntimeException(GoodsErrorCode.MORE_THAN_RESTRICTED_NUM,
                                        String.format(GoodsRestrictedErrorTips.GOODS_PURCHASE_MOST_NUMBER,
                                                bookingSaleVO.getBookingSaleGoods().getCanBookingCount()));
                            }
                        }


                    }
                    if (isFull) {
                        //3. 填充订单项基本数据
                        merge(tradeItem, goodsInfo, goods, storeId);
                        //4. 填充订单项区间价->levelPrice
                        calcPrice(tradeItem, goodsInfo, goods, goodsInfoResponse.getGoodsIntervalPrices());
                    }
                });

        return tradeItems;
    }

    private Integer buildGoodsInfoVendibility(GoodsInfoVO goodsInfo) {

        Integer vendibility = Constants.yes;

        String providerGoodsInfoId = goodsInfo.getProviderGoodsInfoId();

        if (StringUtils.isNotBlank(providerGoodsInfoId)) {
            if (Constants.no.equals(goodsInfo.getVendibility())) {
                vendibility = Constants.no;
            }
        }
        return vendibility;
    }

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    /**
     * * 校验购买积分商品
     * 1.校验积分商品库存，删除，上下架状态
     *
     * @param goodsInfoResponse 关联商品信息
     * @param storeId           店铺ID
     * @return
     */
    public TradeItem verifyPointsGoods(TradeItem tradeItem, TradeGoodsListVO goodsInfoResponse, PointsGoodsVO
            pointsGoodsVO, Long storeId) {
        GoodsInfoVO goodsInfo = goodsInfoResponse.getGoodsInfos().get(0);
        GoodsVO goods = goodsInfoResponse.getGoodses().get(0);

        // 1.验证积分商品(校验积分商品库存，删除，启用停用状态，兑换时间)
        if (DeleteFlag.YES.equals(pointsGoodsVO.getDelFlag()) || EnableStatus.DISABLE.equals(pointsGoodsVO.getStatus())) {
            throw new SbcRuntimeException("K-120001");
        }
        if (pointsGoodsVO.getStock() < tradeItem.getNum() || goodsInfo.getStock() < tradeItem.getNum()) {
            throw new SbcRuntimeException("K-120002");
        }
        if (pointsGoodsVO.getEndTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-120003");
        }
        // 2.填充订单商品信息
        merge(tradeItem, goodsInfo, goods, storeId);
        if (Objects.isNull(tradeItem.getPrice())) {
            tradeItem.setPrice(goodsInfo.getMarketPrice());
        }
        return tradeItem;
    }


    /**
     * 为tradeItem 填充商品基本信息
     *
     * @param tradeItems        订单商品数据，仅包含skuId/价格
     * @param goodsInfoResponse 关联商品信息
     */
    public List<TradeItem> mergeGoodsInfo(List<TradeItem> tradeItems, TradeGetGoodsResponse goodsInfoResponse) {
        if (CollectionUtils.isEmpty(tradeItems)) {
            return Collections.emptyList();
        }
        List<GoodsInfoVO> goodsInfos = goodsInfoResponse.getGoodsInfos();
        Map<String, GoodsVO> goodsMap = goodsInfoResponse.getGoodses().stream().collect(Collectors.toMap
                (GoodsVO::getGoodsId, Function.identity()));
        Map<String, GoodsInfoVO> goodsInfoMap = goodsInfos.stream().collect(Collectors.toMap
                (GoodsInfoVO::getGoodsInfoId, Function.identity()));
        tradeItems
                .forEach(tradeItem -> {
                    GoodsInfoVO goodsInfo = goodsInfoMap.get(tradeItem.getSkuId());
                    GoodsVO goods = goodsMap.get(goodsInfo.getGoodsId());
                    //为tradeItem填充商品基本信息
                    merge(tradeItem, goodsInfo, goods, null);
                });
        return tradeItems;
    }

    public void verifyStore(List<Long> storeIds) {
        StoreCheckRequest request = new StoreCheckRequest();
        request.setIds(storeIds);
        if (!storeProvider.checkStore(request).getContext().getResult()) {
            throw new SbcRuntimeException("K-050117");
        }
    }

    /**
     * 为tradeItem填充商品基本信息
     * 主要是合并价格和名称这些字段
     *
     * @param tradeItem
     * @param goodsInfo
     * @param goods
     */
    void merge(TradeItem tradeItem, GoodsInfoVO goodsInfo, GoodsVO goods, Long storeId) {
        tradeItem.setSkuName(goodsInfo.getGoodsInfoName());
        tradeItem.setSpuName(goods.getGoodsName());
        tradeItem.setPic(goodsInfo.getGoodsInfoImg());
        tradeItem.setBrand(goods.getBrandId());
        tradeItem.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
        tradeItem.setCateId(goods.getCateId());
        tradeItem.setSkuNo(goodsInfo.getGoodsInfoNo());
        tradeItem.setSpuId(goods.getGoodsId());
        tradeItem.setUnit(goods.getGoodsUnit());
        tradeItem.setGoodsWeight(goods.getGoodsWeight());
        tradeItem.setGoodsCubage(goods.getGoodsCubage());
        tradeItem.setFreightTempId(goods.getFreightTempId());
        tradeItem.setStoreId(storeId);
        tradeItem.setDistributionGoodsAudit(goodsInfo.getDistributionGoodsAudit());
        tradeItem.setCommissionRate(goodsInfo.getCommissionRate());
        tradeItem.setDistributionCommission(goodsInfo.getDistributionCommission());
        tradeItem.setOriginalPrice(BigDecimal.ZERO);
        tradeItem.setLevelPrice(BigDecimal.ZERO);
        tradeItem.setSplitPrice(BigDecimal.ZERO);
        tradeItem.setCateTopId(goodsInfo.getCateTopId());
        tradeItem.setEnterPriseAuditState(goodsInfo.getEnterPriseAuditState());
        tradeItem.setEnterPrisePrice(goodsInfo.getEnterPrisePrice());
        tradeItem.setBuyPoint(goodsInfo.getBuyPoint());
        tradeItem.setThirdPlatformSpuId(goodsInfo.getThirdPlatformSpuId());
        tradeItem.setThirdPlatformSkuId(goodsInfo.getThirdPlatformSkuId());
        tradeItem.setGoodsSource(goodsInfo.getGoodsSource());
        tradeItem.setProviderId(goodsInfo.getProviderId());
        tradeItem.setThirdPlatformType(goodsInfo.getThirdPlatformType());
        tradeItem.setSupplyPrice(goodsInfo.getSupplyPrice());
        tradeItem.setGoodsType(GoodsType.fromValue(goodsInfo.getGoodsType()));
        tradeItem.setVirtualCouponId(goodsInfo.getVirtualCouponId());
        tradeItem.setCycleNum(goodsInfo.getCycleNum());
        tradeItem.setCostPrice(goodsInfo.getCostPrice());
        tradeItem.setStock(goodsInfo.getStock());
        log.info("set costprice");
        //tradeItem.setPrice(goodsInfo.getMarketPrice());
        if (StringUtils.isBlank(tradeItem.getSpecDetails())) {
            tradeItem.setSpecDetails(goodsInfo.getSpecText());
        }
        if (osUtil.isS2b() && storeId != null) {
            BaseResponse<ContractCateListResponse> baseResponse = tradeCacheService.queryContractCateList(storeId, goods.getCateId());
            ContractCateListResponse contractCateListResponse = baseResponse.getContext();
            if (Objects.nonNull(contractCateListResponse)) {
                List<ContractCateVO> cates = contractCateListResponse.getContractCateList();
                if (CollectionUtils.isNotEmpty(cates)) {
                    ContractCateVO cateResponse = cates.get(0);
                    tradeItem.setCateName(cateResponse.getCateName());
                    tradeItem.setCateRate(cateResponse.getCateRate() != null ? cateResponse.getCateRate() : cateResponse.getPlatformCateRate());
                }

            }
        }
    }

    /**
     * 设置订单项商品的订货区间价
     * 若无区间价,则设置为会员价(在前面使用插件已经算好的salePrice)
     * 【商品价格计算第①步】: 商品的 客户级别价格 (完成客户级别价格/客户指定价/订货区间价计算) -> levelPrice
     *
     * @param tradeItem
     * @param goodsInfo
     * @param goods
     * @param goodsIntervalPrices
     */
    void calcPrice(TradeItem tradeItem, GoodsInfoVO goodsInfo, GoodsVO goods, List<GoodsIntervalPriceVO>
            goodsIntervalPrices) {
        // 订货区间设价
        if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
            Long buyNum = tradeItem.getNum();
            Optional<GoodsIntervalPriceVO> first = goodsIntervalPrices.stream()
                    .filter(item -> item.getGoodsInfoId().equals(tradeItem.getSkuId()))
                    .filter(intervalPrice -> buyNum >= intervalPrice.getCount())
                    .max(Comparator.comparingLong(GoodsIntervalPriceVO::getCount));
            if (first.isPresent()) {
                GoodsIntervalPriceVO goodsIntervalPrice = first.get();
                tradeItem.setLevelPrice(goodsIntervalPrice.getPrice());
                tradeItem.setOriginalPrice(goodsInfo.getMarketPrice());
                if (goods.getCpsSpecial() != null && goods.getCpsSpecial() == 1) {
                    tradeItem.setLevelPrice(goodsInfo.getMarketPrice());
                }
                //判断是否为秒杀抢购商品
                if (!(Objects.nonNull(tradeItem.getIsFlashSaleGoods()) && tradeItem.getIsFlashSaleGoods())
                        && !(Objects.nonNull(tradeItem.getIsAppointmentSaleGoods()) && tradeItem.getIsAppointmentSaleGoods())
                        && !(Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods())) {
                    tradeItem.setPrice(goodsIntervalPrice.getPrice());
                    if (goods.getCpsSpecial() != null && goods.getCpsSpecial() == 1) {
                        tradeItem.setPrice(goodsInfo.getMarketPrice());
                    }
                    tradeItem.setSplitPrice(tradeItem.getLevelPrice().multiply(
                            new BigDecimal(tradeItem.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP)
                    );
                } else {
                    tradeItem.setSplitPrice(tradeItem.getPrice().multiply(
                            new BigDecimal(tradeItem.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
                }

                return;
            }
        }
        if (goods.getCpsSpecial() != null && goods.getCpsSpecial() == 1) {
            tradeItem.setLevelPrice(goodsInfo.getMarketPrice());
            tradeItem.setOriginalPrice(goodsInfo.getMarketPrice() == null ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
        } else {
            tradeItem.setLevelPrice(goodsInfo.getSalePrice());
            tradeItem.setOriginalPrice(goodsInfo.getMarketPrice() == null ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
        }
        //判断是否为秒杀抢购商品
        if (!(Objects.nonNull(tradeItem.getIsFlashSaleGoods()) && tradeItem.getIsFlashSaleGoods())
                && !(Objects.nonNull(tradeItem.getIsAppointmentSaleGoods()) && tradeItem.getIsAppointmentSaleGoods())
                // 全款预售指定预售价、定金预售价格以市场价计算
                && !(Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods())) {
            if (goods.getCpsSpecial() != null && goods.getCpsSpecial() == 1) {
                tradeItem.setPrice(goodsInfo.getMarketPrice());
            } else {
                tradeItem.setPrice(goodsInfo.getSalePrice());
            }
            tradeItem.setSplitPrice(tradeItem.getLevelPrice().multiply(
                    new BigDecimal(tradeItem.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP)
            );
        } else {
            tradeItem.setSplitPrice(tradeItem.getPrice().multiply(
                    new BigDecimal(tradeItem.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
    }

    /**
     * 校验起订量,限定量
     *
     * @param tradeItem
     * @param goodsInfo
     * @param oldNum
     * @return
     */
    public void verifyGoodsInternal(TradeItem tradeItem, GoodsInfoVO goodsInfo, Long oldNum) {
        if (!(StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId()) && Objects.isNull(goodsInfo.getThirdPlatformType()))) {
            //非供应商商品，校验购买数量大于库存
            if (tradeItem.getNum() > (goodsInfo.getStock() + oldNum)) {
                throw new SbcRuntimeException("K-050116");
            }
        }
        // 起订量
        if (goodsInfo.getCount() != null) {
            //起订量大于库存
            if (goodsInfo.getCount() > goodsInfo.getStock()) {
                throw new SbcRuntimeException("K-050116");
            }
            //购买数量小于起订量
            if (goodsInfo.getCount() > tradeItem.getNum()) {
                throw new SbcRuntimeException("K-050140", new Object[]{goodsInfo.getGoodsInfoName(), goodsInfo.getCount(), tradeItem.getNum()});
            }
        }

        // 限定量
        if (goodsInfo.getMaxCount() != null && goodsInfo.getMaxCount() < tradeItem.getNum()) {
            throw new SbcRuntimeException("K-050140", new Object[]{goodsInfo.getGoodsInfoName(), goodsInfo.getMaxCount(), tradeItem.getNum()});
        }

    }

    /**
     * 减库存 duanlsh
     *
     * @param tradeItems
     */
    @Transactional
    @GlobalTransactional
    public void subSkuListStock(List<TradeItem> tradeItems) {
        updateSkuListStock(tradeItems, true);
    }

    /**
     * 扣减秒杀商品库存和增加销量
     *
     * @param tradeItems
     */
    @Transactional
    @GlobalTransactional
    public void batchFlashSaleGoodsStockAndSalesVolume(List<TradeItem> tradeItems) {
        tradeItems.forEach(tradeItem -> {
            //扣减秒杀商品库存和增加销量
            FlashSaleGoodsBatchStockAndSalesVolumeRequest request = new FlashSaleGoodsBatchStockAndSalesVolumeRequest();
            request.setId(tradeItem.getFlashSaleGoodsId());
            request.setNum(tradeItem.getNum().intValue());
            flashSaleGoodsSaveProvider.batchStockAndSalesVolume(request);
        });
    }

    /**
     * 增加秒杀商品库存和减少销量
     *
     * @param tradeItems
     */
    @Transactional
    @GlobalTransactional
    public void addFlashSaleGoodsStock(List<TradeItem> tradeItems, String customerId) {
        tradeItems.forEach(tradeItem -> {
            //增加秒杀商品库存同时减少销量
            FlashSaleGoodsBatchStockAndSalesVolumeRequest request = new FlashSaleGoodsBatchStockAndSalesVolumeRequest();
            request.setId(tradeItem.getFlashSaleGoodsId());
            request.setNum(tradeItem.getNum().intValue());
            flashSaleGoodsSaveProvider.subStockAndSalesVolume(request);
            //还redis库存
            FlashSaleGoodsOrderCancelReturnStockRequest returnStockRequest = new FlashSaleGoodsOrderCancelReturnStockRequest();
            returnStockRequest.setCustomerId(customerId);
            returnStockRequest.setFlashSaleGoodsId(tradeItem.getFlashSaleGoodsId());
            returnStockRequest.setFlashSaleGoodsNum(tradeItem.getNum().intValue());
            flashSaleGoodsOrderCancelReturnStockService.flashSaleGoodsOrderCancelReturnStock(returnStockRequest);
        });
    }


    /**
     * 加库存
     *
     * @param tradeItems
     */
    public void addSkuListStock(List<TradeItem> tradeItems) {
        updateSkuListStock(tradeItems, false);
    }

    /**
     * 释放冻结数量
     * @param tradeItems
     */
    public void releaseFrozenStock(List<TradeItem> tradeItems){
        List<GoodsInfoMinusStockDTO> stockList = new ArrayList<>();
        for (TradeItem tradeItem : tradeItems) {
            GoodsInfoMinusStockDTO goodsInfoMinusStockDTO = new GoodsInfoMinusStockDTO();
            goodsInfoMinusStockDTO.setStock(tradeItem.getNum());
            goodsInfoMinusStockDTO.setGoodsInfoId(tradeItem.getSkuId());
            stockList.add(goodsInfoMinusStockDTO);
        }
        goodsInfoProvider.decryFreezeStock(stockList);
    }

    /**
     * 减卡券库存
     *
     * @param tradeItems
     */
    @Transactional
    @GlobalTransactional
    public void subCouponSkuListStock(List<TradeItem> tradeItems, String tradeId, Operator operator) {
        updateCouponSkuListStock(VirtualCouponUpdateRequest.builder()
                .tradeItems(tradeItems).subFlag(true).operator(operator).tradeId(tradeId).build());
    }

    /**
     * 加卡券库存
     *
     * @param tradeItems
     */
    @Transactional
    @GlobalTransactional
    public void addCouponSkuListStock(List<TradeItem> tradeItems, String tradeId, Operator operator) {
        updateCouponSkuListStock(VirtualCouponUpdateRequest.builder()
                .tradeItems(tradeItems).subFlag(false).operator(operator).tradeId(tradeId).build());
    }

    /**
     * 卡券库存变动
     *
     * @param virtualCouponUpdateRequest 订单项
     */
    private void updateCouponSkuListStock(VirtualCouponUpdateRequest virtualCouponUpdateRequest) {
        List<TradeItem> tradeItems = virtualCouponUpdateRequest.getTradeItems();
        if (CollectionUtils.isEmpty(tradeItems)) {
            return;
        }
        tradeItems = tradeItems.stream()
                .filter(v -> !ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tradeItems)) {
            return;
        }
        if (virtualCouponUpdateRequest.isSubFlag()) {
            List<Long> virtualCouponIdList = new ArrayList<>();
            tradeItems.stream().filter(tradeItem -> Objects.nonNull(tradeItem.getGoodsType())
                    && GoodsType.VIRTUAL_COUPON.equals(tradeItem.getGoodsType())).forEach(tradeItem -> {
                for (int i = 0; i < tradeItem.getNum(); i++) {
                    virtualCouponIdList.add(tradeItem.getVirtualCouponId());
                }
            });
            if (CollectionUtils.isEmpty(virtualCouponIdList)) {
                return;
            }
            log.info("订单编号 {}, 锁定卡券库存,卡券编号 {}", virtualCouponUpdateRequest.getTradeId(), virtualCouponIdList);
            // 扣减卡券库存 扣减失败，订单失败
            List<VirtualCouponCodeVO> virtualCouponCodeVOList = null;
            try {
                virtualCouponCodeVOList = virtualCouponCodeProvider.linkOrder(VirtualCouponCodeLinkOrderRequest.builder()
                        .tid(virtualCouponUpdateRequest.getTradeId()).couponIds(virtualCouponIdList).updatePerson(virtualCouponUpdateRequest.getOperator().getName()).build()).getContext().getVirtualCouponCodeVOList();
            } catch (Exception e) {
                log.error("扣减卡券库存失败 {}", e);
                throw new SbcRuntimeException(CommonErrorCode.DATA_OUT_LIIT, "卡券不足");
            }
            Map<Long, List<VirtualCouponCodeVO>> virtualCouponMap = virtualCouponCodeVOList.stream()
                    .collect(Collectors.groupingBy(virtualCouponCodeVO -> virtualCouponCodeVO.getCouponId()));
            tradeItems.stream().forEach(tradeItem -> {
                if (virtualCouponMap.containsKey(tradeItem.getVirtualCouponId())) {
                    if (Objects.isNull(tradeItem.getVirtualCoupons())) {
                        tradeItem.setVirtualCoupons(new ArrayList<>());
                    }
                    tradeItem.getVirtualCoupons().addAll(
                            KsBeanUtil.convert(virtualCouponMap.get(tradeItem.getVirtualCouponId()), TradeItem.VirtualCoupon.class));
                }
            });
        } else {
            List<VirtualCouponCodeVO> virtualCouponCodeVOS = tradeItems.stream()
                    .filter(tradeItem -> Objects.nonNull(tradeItem.getGoodsType())
                            && GoodsType.VIRTUAL_COUPON.equals(tradeItem.getGoodsType()))
                    .flatMap(tradeItem -> tradeItem.getVirtualCoupons().stream())
                    .map(virtualCoupon -> {
                        VirtualCouponCodeVO virtualCouponCodeVO = new VirtualCouponCodeVO();
                        virtualCouponCodeVO.setCouponId(virtualCoupon.getCouponId());
                        virtualCouponCodeVO.setId(virtualCoupon.getId());
                        virtualCouponCodeVO.setUpdatePerson(virtualCouponUpdateRequest.getOperator().getName());
                        return virtualCouponCodeVO;
                    }).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(virtualCouponCodeVOS)) {
                log.info("订单编号 {}, 没有可以释放卡券库存,", virtualCouponUpdateRequest.getTradeId());
                return;
            }
            try {
                virtualCouponCodeProvider.unlinkOrder(VirtualCouponCodeUnLinkOrderRequest.builder().codeVOList(virtualCouponCodeVOS).build());
            } catch (Exception e) {
                log.error("释放卡券库存失败 {}", e);
                throw new SbcRuntimeException(CommonErrorCode.DATA_OUT_LIIT, "释放卡券库存失败");
            }
        }
    }

    /**
     * 库存变动
     *
     * @param tradeItems 订单项
     * @param subFlag    扣库存标识 true:减库存  false:加库存
     */
    private void updateSkuListStock(List<TradeItem> tradeItems, boolean subFlag) {
        if (CollectionUtils.isEmpty(tradeItems)) {
            return;
        }
        tradeItems = tradeItems.stream()
                .filter(v -> !ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tradeItems)) {
            return;
        }
        //批量查询商品
        List<String> skuIds = tradeItems.stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        Map<String, GoodsInfoVO> goodsInfoMap = goodsInfoQueryProvider.listByIds(new GoodsInfoListByIdsRequest(skuIds, null))
                .getContext().getGoodsInfos().stream()
                .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
        if (goodsInfoMap.size() > 0) {
            if (subFlag) {
                List<GoodsMinusStockDTO> spuStockList = new ArrayList<>();
                List<GoodsInfoMinusStockDTO> stockList = tradeItems.stream().map(tradeItem -> {
                    GoodsInfoVO goodsInfoVO = goodsInfoMap.get(tradeItem.getSkuId());
                    GoodsInfoMinusStockDTO dto = new GoodsInfoMinusStockDTO();
                    dto.setStock(tradeItem.getNum());
                    //是供应商商品扣减供应商商品库存
                    boolean isProviderSku = StringUtils.isNotBlank(goodsInfoVO.getProviderGoodsInfoId()) && Objects.isNull(
                            goodsInfoVO.getThirdPlatformType());
                    String spuId = isProviderSku ? goodsInfoVO.getGoodsId() : tradeItem.getSpuId();
                    spuStockList.add(new GoodsMinusStockDTO(tradeItem.getNum(), spuId));
                    dto.setGoodsInfoId(isProviderSku
                            ? goodsInfoVO.getProviderGoodsInfoId() : tradeItem.getSkuId());
                    return dto;
                }).collect(Collectors.toList());
                goodsInfoProvider.batchMinusStock(GoodsInfoBatchMinusStockRequest.builder().stockList(stockList).build());
                //spu减库存
                goodsStockService.batchSubStock(spuStockList);
            } else {
                List<GoodsPlusStockDTO> spuStockList = new ArrayList<>();
                List<GoodsInfoPlusStockDTO> stockList = tradeItems.stream().map(tradeItem -> {
                    GoodsInfoVO goodsInfoVO = goodsInfoMap.get(tradeItem.getSkuId());
                    GoodsInfoPlusStockDTO dto = new GoodsInfoPlusStockDTO();
                    dto.setStock(tradeItem.getNum());
                    boolean isProviderSku = StringUtils.isNotBlank(goodsInfoVO.getProviderGoodsInfoId()) && Objects.isNull(
                            goodsInfoVO.getThirdPlatformType());
                    String spuId = isProviderSku ? goodsInfoVO.getGoodsId() : tradeItem.getSpuId();
                    spuStockList.add(new GoodsPlusStockDTO(tradeItem.getNum(), spuId));
                    dto.setGoodsInfoId(isProviderSku
                            ? goodsInfoVO.getProviderGoodsInfoId() : tradeItem.getSkuId());
                    return dto;
                }).collect(Collectors.toList());
                goodsInfoProvider.batchPlusStock(GoodsInfoBatchPlusStockRequest.builder().stockList(stockList).build());
                //spu加库存
                goodsStockService.batchAddStock(spuStockList);
            }
        }
    }

    public CustomerGetByIdResponse verifyCustomer(String customerId) {
        // 客户信息
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();

        if (customer == null) {
            throw new SbcRuntimeException("K-010014");
        }

        if (customer.getCustomerDetail().getCustomerStatus() == CustomerStatus.DISABLE) {
            throw new SbcRuntimeException("K-010015");
        }
        return customer;
    }

    public CustomerSimplifyByIdResponse simplifyById(String customerId) {
        // 客户信息
        CustomerSimplifyByIdResponse customer = customerQueryProvider.simplifyById(new CustomerSimplifyByIdRequest(customerId)).getContext();

        if (customer == null) {
            throw new SbcRuntimeException("K-010014");
        }

        if (customer.getCustomerDetail().getCustomerStatus() == CustomerStatus.DISABLE) {
            throw new SbcRuntimeException("K-010015");
        }
        return customer;
    }

    /**
     * 校验可使用积分
     *
     * @param tradeCommitRequest
     */
    public SystemPointsConfigQueryResponse verifyPoints(List<Trade> trades, TradeCommitRequest tradeCommitRequest, SystemPointsConfigQueryResponse pointsConfig) {
        //积分抵扣使用起始值
        Long overPointsAvailable = pointsConfig.getOverPointsAvailable();

        String customerId = tradeCommitRequest.getCustomer().getCustomerId();
        //订单使用积分
        Long points = tradeCommitRequest.getPoints();

        //查询会员可用积分
//        Long pointsAvailable = customerQueryProvider.getPointsAvailable(new CustomerPointsAvailableByIdRequest
//                (customerId)).getContext().getPointsAvailable();
        Long pointsAvailable =
                externalProvider.getByUserNoPoint(FanDengPointRequest.builder()
                        .userNo(tradeCommitRequest.getCustomer().getFanDengUserNo())
                        .build()).getContext().getCurrentPoint();

        if (pointsAvailable == null) {
            pointsAvailable = 0L;
        }

        //你的积分不足无法下单
        if (PointsUsageFlag.GOODS.equals(pointsConfig.getPointsUsageFlag())) {
            if (points > pointsAvailable) {
                throw new SbcRuntimeException("K-050317");
            }
        }

        //订单使用积分超出会员可用积分
        if (points.compareTo(pointsAvailable) > 0) {
            throw new SbcRuntimeException("K-050314");
        }

        //会员可用积分未满足积分抵扣使用值
        if (pointsAvailable.compareTo(overPointsAvailable) < 0) {
            throw new SbcRuntimeException("K-050315");
        }

        //订单使用积分超出积分抵扣限额
        //订单总金额
        BigDecimal totalTradePrice;
        Trade trade = trades.get(0);
        // 预售尾款结算时候积分处理
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY
                && Objects.nonNull(trade.getTradePrice().getTailPrice())) {
            totalTradePrice = trade.getTradePrice().getTailPrice();
        } else {
            totalTradePrice = trades.stream().map(t -> t.getTradePrice().getTotalPrice()).reduce(BigDecimal::add).get();
        }
        //积分抵扣限额只考虑按照订单抵扣的
        if (PointsUsageFlag.ORDER.equals(pointsConfig.getPointsUsageFlag())) {
            BigDecimal pointWorth = BigDecimal.valueOf(pointsConfig.getPointsWorth());
            BigDecimal maxDeductionRate = pointsConfig.getMaxDeductionRate().divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_DOWN);
            Long maxPoints = totalTradePrice.multiply(maxDeductionRate).multiply(pointWorth).setScale(0,
                    BigDecimal.ROUND_DOWN).longValue();
            if (points.compareTo(maxPoints) > 0) {
                throw new SbcRuntimeException("K-050316");
            }
        }
        // 调用积分锁定
        String deductCode = externalProvider.pointLock(FanDengPointLockRequest.builder()
                .desc("提交订单锁定(订单号:"+trade.getId()+")")
                .point(points)
                .userNo(tradeCommitRequest.
                        getCustomer().getFanDengUserNo())
                .sourceId(trade.getId())
                .sourceType(1)
                .build()).getContext().getDeductionCode();
        trade.setDeductCode(deductCode);
        return pointsConfig;
    }

    /**
     * 验证失效的营销信息(目前包括失效的赠品、满系活动、优惠券)
     */
    public void verifyInvalidMarketings(TradeCommitRequest tradeCommitRequest, List<TradeItemGroup> tradeItemGroups, Map<Long, CommonLevelVO> storeLevelMap) {
        String customerId = tradeCommitRequest.getCustomer().getCustomerId();

        // 1.验证赠品、满系活动
        // 1.1.从订单快照中获取下单时选择的营销、商品信息
        List<TradeMarketingDTO> tradeMarketings = tradeItemGroups.stream()
                .flatMap(group -> group.getTradeMarketingList().stream()).collect(Collectors.toList());
        List<TradeItem> tradeItems = tradeItemGroups.stream()
                .flatMap(group -> group.getTradeItems().stream()).collect(Collectors.toList());

        // 1.2.验证失效赠品、满系活动
        String validInfo = this.verifyTradeMarketing(
                tradeMarketings, Collections.emptyList(), tradeItems, customerId, storeLevelMap);

        // 1.3.将失效内容更新至request中的订单快照
        List<Long> tradeMarketingIds = tradeMarketings.stream()
                .map(TradeMarketingDTO::getMarketingId).collect(Collectors.toList());
        tradeItemGroups.stream().forEach(group -> {
            group.setTradeMarketingList(
                    group.getTradeMarketingList().stream()
                            .filter(item -> tradeMarketingIds.contains(item.getMarketingId()))
                            .collect(Collectors.toList())
            );
        });

        // 2.6.如果存在提示信息、且为非强制提交，则抛出异常
        if (StringUtils.isNotEmpty(validInfo) && !tradeCommitRequest.isForceCommit()) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "很抱歉，" + validInfo);
        }

        // 2.验证优惠券
        List<String> couponCodeIds = new ArrayList<>();
        tradeCommitRequest.getStoreCommitInfoList().forEach(item -> {
            if (Objects.nonNull(item.getCouponCodeId())) {
                couponCodeIds.add(item.getCouponCodeId());
            }
        });
        if (Objects.nonNull(tradeCommitRequest.getCommonCodeId())) {
            couponCodeIds.add(tradeCommitRequest.getCommonCodeId());
        }

        if (CollectionUtils.isNotEmpty(couponCodeIds)) {
            CouponCodeValidOrderCommitRequest validOrderCommitRequest = CouponCodeValidOrderCommitRequest.builder()
                    .customerId(customerId).couponCodeIds(couponCodeIds).build();
            CouponCodeValidOrderCommitResponse validOrderCommitResponse = couponCodeQueryProvider.validOrderCommit(validOrderCommitRequest).getContext();

            // 2.5.从request对象中移除过期的优惠券
            List<String> invalidCodeIds = validOrderCommitResponse.getInvalidCodeIds();
            validInfo = validInfo + validOrderCommitResponse.getValidInfo();
            if (invalidCodeIds.contains(tradeCommitRequest.getCommonCodeId())) {
                tradeCommitRequest.setCommonCodeId(null);
            }
            tradeCommitRequest.getStoreCommitInfoList().forEach(item -> {
                if (invalidCodeIds.contains(item.getCouponCodeId())) {
                    item.setCouponCodeId(null);
                }
            });
        }

        // 2.6.如果存在提示信息、且为非强制提交，则抛出异常
        if (StringUtils.isNotEmpty(validInfo) && !tradeCommitRequest.isForceCommit()) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "很抱歉，" + validInfo);
        }

    }

    /**
     * 带客下单校验customer跟supplier的关系
     *
     * @param customerId customerId
     * @param companyId  companyId
     */
    public void verifyCustomerWithSupplier(String customerId, Long companyId) {
        StoreCustomerRelaQueryRequest request = new StoreCustomerRelaQueryRequest();
        request.setCustomerId(customerId);
        request.setCompanyInfoId(companyId);
        request.setQueryPlatform(false);

        StoreCustomerRelaResponse storeCustomerRela = storeCustomerQueryProvider.getCustomerRelated(request).getContext();

        if (Objects.isNull(storeCustomerRela)) {
            throw new SbcRuntimeException("K-010201");
        }
    }

    /**
     * 营销活动校验（通过抛出异常返回结果）
     */
    public void verifyTradeMarketing(List<TradeMarketingDTO> tradeMarketingList, List<TradeItem> oldGifts, List<TradeItem> tradeItems,
                                     String customerId, boolean isFoceCommit) {
        List<Long> storeIds = tradeItems.stream().map(tradeItem -> tradeItem.getStoreId()).distinct().collect(Collectors.toList());
        Map<Long, CommonLevelVO> storeLevelMap = tradeCustomerService.listCustomerLevelMapByCustomerIdAndIds(storeIds, customerId);
        String validInfo = this.verifyTradeMarketing(tradeMarketingList, oldGifts, tradeItems, customerId, storeLevelMap);
        if (StringUtils.isNotEmpty(validInfo) && !isFoceCommit) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "很抱歉，" + validInfo);
        }
    }

    /**
     * 营销活动校验（通过字符串方式返回结果）
     *
     * @param tradeMarketingList
     * @param oldGifts           旧订单赠品数据，用于编辑订单的场景，由于旧订单赠品库存已先还回但事务未提交，因此在处理中将库存做逻辑叠加
     * @param tradeItems
     * @param customerId
     */
    public String verifyTradeMarketing(List<TradeMarketingDTO> tradeMarketingList, List<TradeItem> oldGifts,
                                       List<TradeItem> tradeItems, String customerId, Map<Long, CommonLevelVO> storeLevelMap) {
        //校验营销活动
        List<MarketingVO> invalidMarketings = verifyMarketing(tradeMarketingList, customerId, storeLevelMap);
        List<TradeMarketingDTO> tpMarketingList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(invalidMarketings)) {
            List<Long> invalidIds = invalidMarketings.stream().map(MarketingVO::getMarketingId).collect(Collectors.toList());
            tpMarketingList = tradeMarketingList.stream().filter(i -> invalidIds.contains(i.getMarketingId()))
                    .collect(Collectors.toList());
            tradeMarketingList.removeAll(tpMarketingList);
        }

        //校验无效赠品
        String info = "";
        List<String> giftIds = tradeMarketingList.stream()
                .filter(r -> CollectionUtils.isNotEmpty(r.getGiftSkuIds()))
                .flatMap(r -> r.getGiftSkuIds().stream()).distinct()
                .collect(Collectors.toList());
        info += verifyGoodsInfo(giftIds, tradeMarketingList, oldGifts, tradeItems, true);

        // 校验加价购加购商品
        List<String> markupIds = tradeMarketingList.stream()
                .filter(r -> CollectionUtils.isNotEmpty(r.getMarkupSkuIds()))
                .flatMap(r -> r.getMarkupSkuIds().stream()).distinct()
                .collect(Collectors.toList());
        info += verifyGoodsInfo(markupIds, tradeMarketingList, oldGifts, tradeItems, false);


        if (!tpMarketingList.isEmpty()) {
            //无效营销活动提示
            info = info + wraperInvalidMarketingInfo(tpMarketingList, invalidMarketings);
        }

        return info;
    }

    /**
     * 校验没有营销活动的商品下单时是否有新的活动
     * @param tradeItems
     * @param tradeMarketingList
     */
    public void verifyItemMarketing(List<TradeItem> tradeItems, List<TradeMarketingDTO> tradeMarketingList){
        //筛选出没有营销活动的商品id
        List<TradeItem> noMarketingItems = tradeItems.stream().filter(tradeItem -> {
            Optional<TradeMarketingDTO> optional = tradeMarketingList.stream().filter(m -> m.getSkuIds().contains(tradeItem.getSkuId())).findFirst();
            if (CollectionUtils.isEmpty(tradeMarketingList) || !optional.isPresent()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        List<String> goodsInfoIds = noMarketingItems.stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        //查询没有营销活动的商品此时是否有新的活动
        HashMap<String, List<MarketingForEndVO>> marketingMap = marketingQueryProvider.getMarketingMapByGoodsId(MarketingMapGetByGoodsIdRequest.builder()
                .goodsInfoIdList(goodsInfoIds)
                .deleteFlag(DeleteFlag.NO)
                .cascadeLevel(true)
                .marketingStatus(MarketingStatus.STARTED).build()).getContext().getListMap();

        noMarketingItems.forEach(tradeItem -> {
            //实时查询的营销活动
            List<MarketingForEndVO> marketingForEndVOS = marketingMap.get(tradeItem.getSkuId());

            if(CollectionUtils.isNotEmpty(marketingForEndVOS)) {

                //减->折->赠->打包一口价->第二件半价->加价购
                marketingForEndVOS = marketingForEndVOS.stream()
                        .filter(m -> !MarketingType.SUITS.equals(m.getMarketingType()))
                        .sorted(Comparator.comparing(MarketingVO::getMarketingType))
                        .collect(Collectors.toList());

                if(CollectionUtils.isNotEmpty(marketingForEndVOS)) {
                    marketingForEndVOS.forEach(marketingForEndVO -> {

                        BigDecimal sum = tradeItem.getPrice().multiply(BigDecimal.valueOf(tradeItem.getNum()));

                        switch (marketingForEndVO.getSubType()) {
                            //满数量减
                            case REDUCTION_FULL_COUNT:
                                MarketingFullReductionLevelVO countLevelVO = marketingForEndVO.getFullReductionLevelList().stream()
                                        .min(Comparator.comparing(MarketingFullReductionLevelVO::getFullCount)).get();
                                if(tradeItem.getNum().compareTo(countLevelVO.getFullCount()) >= NumberUtils.LONG_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            //满金额减
                            case REDUCTION_FULL_AMOUNT:
                                MarketingFullReductionLevelVO amoutLevelVO = marketingForEndVO.getFullReductionLevelList().stream()
                                        .min(Comparator.comparing(MarketingFullReductionLevelVO::getFullAmount)).get();
                                if(sum.compareTo(amoutLevelVO.getFullAmount()) >= NumberUtils.INTEGER_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            //满数量折
                            case DISCOUNT_FULL_COUNT:
                                MarketingFullDiscountLevelVO countDiscountLevelVO = marketingForEndVO.getFullDiscountLevelList().stream()
                                        .min(Comparator.comparing(MarketingFullDiscountLevelVO::getFullCount)).get();
                                if(tradeItem.getNum().compareTo(countDiscountLevelVO.getFullCount()) >= NumberUtils.LONG_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            //满金额折
                            case DISCOUNT_FULL_AMOUNT:
                                MarketingFullDiscountLevelVO amoutDiscountLevelVO = marketingForEndVO.getFullDiscountLevelList().stream()
                                        .min(Comparator.comparing(MarketingFullDiscountLevelVO::getFullAmount)).get();
                                if(sum.compareTo(amoutDiscountLevelVO.getFullAmount()) >= NumberUtils.INTEGER_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            //满数量赠
                            case GIFT_FULL_COUNT:
                                MarketingFullGiftLevelVO countGiftLevelVO = marketingForEndVO.getFullGiftLevelList().stream()
                                        .min(Comparator.comparing(MarketingFullGiftLevelVO::getFullCount)).get();
                                if(tradeItem.getNum().compareTo(countGiftLevelVO.getFullCount()) >= NumberUtils.LONG_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            //满金额减
                            case GIFT_FULL_AMOUNT:
                                MarketingFullGiftLevelVO amoutGiftLevelVO = marketingForEndVO.getFullGiftLevelList().stream()
                                        .min(Comparator.comparing(MarketingFullGiftLevelVO::getFullAmount)).get();
                                if(sum.compareTo(amoutGiftLevelVO.getFullAmount()) >= NumberUtils.INTEGER_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            //打包一口价
                            case BUYOUT_PRICE:
                                MarketingBuyoutPriceLevelVO marketingBuyoutPriceLevelVO = marketingForEndVO.getBuyoutPriceLevelList().stream()
                                        .min(Comparator.comparing(MarketingBuyoutPriceLevelVO::getFullAmount)).get();
                                if(sum.compareTo(marketingBuyoutPriceLevelVO.getFullAmount()) >= NumberUtils.INTEGER_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            //第二件半价
                            case HALF_PRICE_SECOND_PIECE:
                                MarketingHalfPriceSecondPieceLevelVO halfLevelVO = marketingForEndVO.getHalfPriceSecondPieceLevel().stream()
                                        .min(Comparator.comparing(MarketingHalfPriceSecondPieceLevelVO::getNumber)).get();
                                if(tradeItem.getNum().compareTo(halfLevelVO.getNumber()) >= NumberUtils.INTEGER_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            //加价购
                            case MARKUP:
                                MarkupLevelVO markupLevelVO = marketingForEndVO.getMarkupLevelList().stream().min(Comparator.comparing(MarkupLevelVO::getLevelAmount)).get();
                                if(sum.compareTo(markupLevelVO.getLevelAmount()) >= NumberUtils.INTEGER_ZERO) {
                                    throw new SbcRuntimeException(OrderErrorCode.MARKETING_CHANGE);
                                }
                                break;
                            default:
                        }
                    });
                }
            }
        });
    }

    /**
     * 校验赠品或加架构换购的商品
     *
     * @param giftIds
     * @param tradeMarketingList
     * @param oldGifts
     * @param tradeItems
     */
    private String verifyGoodsInfo(List<String> giftIds, List<TradeMarketingDTO> tradeMarketingList, List<TradeItem> oldGifts,
                                   List<TradeItem> tradeItems, boolean giftFlag) {
        StringBuilder info = new StringBuilder();
        if (CollectionUtils.isEmpty(giftIds)) {
            return info.toString();
        }
        List<GoodsInfoVO> invalidGifts = new ArrayList<>();
        GoodsInfoViewByIdsResponse gifts = getGoodsResponse(giftIds);
        //与赠品相同的商品列表
        List<TradeItem> sameItems = tradeItems.stream().filter(i -> giftIds.contains(i.getSkuId())).collect(Collectors.toList());
        invalidGifts.addAll(verifyGiftSku(giftIds, sameItems, gifts, oldGifts));
        if (!invalidGifts.isEmpty()) {
            final List<String> tpGiftList = invalidGifts.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            tradeMarketingList.stream().filter(m -> CollectionUtils.isNotEmpty(m.getGiftSkuIds())).forEach(
                    i -> {
                        List<String> ids = i.getGiftSkuIds().stream().filter(tpGiftList::contains).collect(Collectors.toList());
                        i.getGiftSkuIds().removeAll(ids);
                    }
            );
        }

        if (!invalidGifts.isEmpty()) {
            //无效赠品提示
            String tmp = "赠品%s";
            if (!giftFlag) {
                tmp = "换购商品%s";
            }
            List<String> skuInfo = new ArrayList<>();
            String finalTmp = tmp;
            invalidGifts.forEach(
                    i -> skuInfo.add(String.format(finalTmp, i.getGoodsInfoName() + (i.getSpecText() == null ? "" : i.getSpecText())))
            );
            info.append(StringUtils.join(skuInfo, "、") + "已失效或缺货！");
        }
        return info.toString();
    }

    /**
     * 获取订单商品详情,不包含区间价，会员级别价信息
     */
    public GoodsInfoViewByIdsResponse getGoodsResponse(List<String> skuIds) {
        GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                .goodsInfoIds(skuIds)
                .isHavSpecText(Constants.yes)
                .build();

        return goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();
    }

    /**
     * 订单营销信息校验，返回失效的营销活动
     *
     * @param tradeMarketingList 订单营销信息
     */
    private List<MarketingVO> verifyMarketing(List<TradeMarketingDTO> tradeMarketingList, String customerId, Map<Long, CommonLevelVO> storeLevelMap) {
        if (CollectionUtils.isEmpty(tradeMarketingList)) {
            return Collections.emptyList();
        }
        //获取商户设置的营销活动信息
        List<Long> marketingIds = tradeMarketingList.stream().map(TradeMarketingDTO::getMarketingId).distinct()
                .collect(Collectors.toList());
        //请求信息根据营销活动分组
        Map<Long, List<TradeMarketingDTO>> marketingGroup = tradeMarketingList.stream().collect(Collectors.groupingBy
                (TradeMarketingDTO::getMarketingId));
        Map<Long, List<String>> skuGroup = marketingGroup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                i -> i.getValue().stream().flatMap(m -> m.getSkuIds().stream()).collect(Collectors.toList())));
        Map<Long, Long> levelMap = new HashMap<>();
        storeLevelMap.forEach((storeId, commonLevelVO) -> levelMap.put(storeId, commonLevelVO.getLevelId()));


        MarketingScopeListInvalidMarketingRequest request = new MarketingScopeListInvalidMarketingRequest();
        request.setMarketingIds(marketingIds);
        request.setSkuGroup(skuGroup);
        request.setCustomerId(customerId);
        request.setLevelMap(levelMap);
        return marketingScopeQueryProvider.listInvalidMarketing(request).getContext().getMarketingList();
    }

    /**
     * 校验营销赠品是否有效，返回无效赠品信息
     *
     * @param giftIds           赠品id集合
     * @param sameItems         与赠品有重复的商品列表
     * @param goodsInfoResponse 赠品基础信息
     * @param oldGifts          旧订单赠品数据，用于编辑订单的场景，由于旧订单赠品库存已先还回但事务未提交，因此在处理中将库存做逻辑叠加
     * @return
     */
    private List<GoodsInfoVO> verifyGiftSku(List<String> giftIds, List<TradeItem> sameItems,
                                            GoodsInfoViewByIdsResponse goodsInfoResponse, List<TradeItem> oldGifts) {
        List<GoodsInfoVO> result = new ArrayList<>();
        List<GoodsInfoVO> goodsInfos = goodsInfoResponse.getGoodsInfos();
        Map<String, GoodsVO> goodsMap = goodsInfoResponse.getGoodses().stream().collect(Collectors.toMap
                (GoodsVO::getGoodsId, Function.identity()));
        Map<String, GoodsInfoVO> goodsInfoMap = goodsInfos.stream().collect(Collectors.toMap
                (GoodsInfoVO::getGoodsInfoId, Function.identity()));
        Map<String, List<TradeItem>> sameItemMap = sameItems.stream().collect(Collectors.groupingBy(TradeItem::getSkuId));
        Map<String, Long> oldGiftMap = oldGifts.stream().collect(Collectors.toMap(TradeItem::getSkuId, TradeItem::getNum));
        giftIds.forEach(id -> {
            GoodsInfoVO goodsInfo = goodsInfoMap.get(id);
            GoodsVO goods = goodsMap.get(goodsInfo.getGoodsId());
            List<TradeItem> sameList = sameItemMap.get(id);
            Long oldNum = oldGiftMap.getOrDefault(id, 0L);
            long num = 0;
            if (sameList != null) {
                num = sameList.stream().map(TradeItem::getNum).reduce(0L, (a, b) -> a + b);
            }
            //校验赠品库存，删除，上下架状态
            if (goodsInfo.getDelFlag() == DeleteFlag.YES || goodsInfo.getAddedFlag() == 0
                    || goods.getAuditStatus() == CheckStatus.FORBADE || Objects.equals(DefaultFlag.NO.toValue(), buildGoodsInfoVendibility(goodsInfo))) {
                result.add(goodsInfo);
            }
            Long giftStock = 0L;
            //校验商品库存
            if (StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId()) && Objects.isNull(goodsInfo.getThirdPlatformType())) {
                GoodsInfoByIdResponse providerGoodsInfo = goodsInfoQueryProvider.getById(new GoodsInfoByIdRequest(goodsInfo.getProviderGoodsInfoId()
                        , null)).getContext();
                //供应商商品，取供应商商品库存
                giftStock = providerGoodsInfo.getStock();
            } else {
                //商家商品，取商家商品库存
                giftStock = goodsInfo.getStock();
            }
            //订单商品和赠品存在重复商品，且减完订单商品数量后库存为0，优先订单商品，赠品不送
            if (giftStock + oldNum - num == 0) {
                result.add(goodsInfo);
            }
        });

        return result.stream().filter(IteratorUtils.distinctByKey(GoodsInfoVO::getGoodsInfoId)).collect(Collectors
                .toList());
    }

    /**
     * 封装并返回无效营销活动描述信息
     *
     * @param tradeMarketingList
     * @param invalidMarketings
     * @return
     */
    private String wraperInvalidMarketingInfo(List<TradeMarketingDTO> tradeMarketingList, List<MarketingVO> invalidMarketings) {
        Map<Long, List<TradeMarketingDTO>> marketingMap = tradeMarketingList.stream().collect(
                Collectors.groupingBy(TradeMarketingDTO::getMarketingId));
        List<String> infoList = new ArrayList<>();
        invalidMarketings.forEach(
                i -> {
                    List<Long> reqLevelList = marketingMap.get(i.getMarketingId()).stream().map(TradeMarketingDTO::getMarketingLevelId)
                            .distinct().collect(Collectors.toList());
                    MarketingSubType subType = i.getSubType();
                    reqLevelList.forEach(
                            l -> {
                                String info = "";
                                DecimalFormat fm = new DecimalFormat("#.##");
                                switch (i.getMarketingType()) {
                                    case REDUCTION:
                                        Map<Long, MarketingFullReductionLevelVO> reductionMap = marketingFullReductionQueryProvider.listByMarketingId
                                                (new MarketingFullReductionByMarketingIdRequest(i.getMarketingId())).getContext().getMarketingFullReductionLevelVOList().stream().collect(Collectors.toMap(MarketingFullReductionLevelVO::
                                                getReductionLevelId, Function.identity()));

                                        MarketingFullReductionLevelVO reductionLevel = reductionMap.get(l);
                                        if (reductionLevel == null) {
                                            throw new SbcRuntimeException("K-050221");
                                        }
                                        info = "满%s减%s活动";
                                        if (subType == MarketingSubType.REDUCTION_FULL_AMOUNT) {
                                            info = String.format(info, fm.format(reductionLevel.getFullAmount()), reductionLevel.getReduction());
                                        } else if (subType == MarketingSubType.REDUCTION_FULL_COUNT) {
                                            info = String.format(info, reductionLevel.getFullCount() + "件", reductionLevel.getReduction());
                                        }
                                        break;
                                    case DISCOUNT:
                                        Map<Long, MarketingFullDiscountLevelVO> discountMap = marketingFullDiscountQueryProvider.listByMarketingId
                                                (new MarketingFullDiscountByMarketingIdRequest(i.getMarketingId())).getContext().getMarketingFullDiscountLevelVOList().stream().collect(Collectors.toMap(MarketingFullDiscountLevelVO::
                                                getDiscountLevelId, Function.identity()));
                                        MarketingFullDiscountLevelVO discountLevel = discountMap.get(l);
                                        if (discountLevel == null) {
                                            throw new SbcRuntimeException("K-050221");
                                        }
                                        info = "满%s享%s折活动";
                                        BigDecimal discount = discountLevel.getDiscount().multiply(BigDecimal.TEN)
                                                .setScale(1, BigDecimal.ROUND_HALF_UP);
                                        if (subType == MarketingSubType.DISCOUNT_FULL_AMOUNT) {
                                            info = String.format(info, fm.format(discountLevel.getFullAmount()), discount);
                                        } else if (subType == MarketingSubType.DISCOUNT_FULL_COUNT) {
                                            info = String.format(info, discountLevel.getFullCount() + "件", discount);
                                        }
                                        break;
                                    case GIFT:
                                        FullGiftLevelListByMarketingIdRequest fullGiftLevelListByMarketingIdRequest = new FullGiftLevelListByMarketingIdRequest();
                                        fullGiftLevelListByMarketingIdRequest.setMarketingId(i.getMarketingId());
                                        Map<Long, MarketingFullGiftLevelVO> giftMap = fullGiftQueryProvider.listLevelByMarketingId
                                                (fullGiftLevelListByMarketingIdRequest).getContext().getFullGiftLevelVOList().stream().collect(Collectors.toMap(MarketingFullGiftLevelVO::
                                                getGiftLevelId, Function.identity()));
                                        MarketingFullGiftLevelVO giftLevel = giftMap.get(l);
                                        if (giftLevel == null) {
                                            throw new SbcRuntimeException("K-050221");
                                        }
                                        info = "满%s获赠品活动";
                                        if (subType == MarketingSubType.GIFT_FULL_AMOUNT) {
                                            info = String.format(info, fm.format(giftLevel.getFullAmount()));
                                        } else if (subType == MarketingSubType.GIFT_FULL_COUNT) {
                                            info = String.format(info, giftLevel.getFullCount() + "件");
                                        }
                                        break;
                                    case BUYOUT_PRICE:
                                        MarketingBuyoutPriceIdRequest request = new MarketingBuyoutPriceIdRequest();
                                        request.setMarketingId(i.getMarketingId());
                                        Map<Long, MarketingBuyoutPriceLevelVO> levelMap =
                                                marketingBuyoutPriceQueryProvider.details(request).getContext().getMarketingBuyoutPriceLevelVO().stream().collect(Collectors.toMap(MarketingBuyoutPriceLevelVO::getReductionLevelId, c -> c));
                                        MarketingBuyoutPriceLevelVO level = levelMap.get(l);
                                        if (level == null) {
                                            throw new SbcRuntimeException("K-050221");
                                        }
                                        info = "%s件%s元";
                                        info = String.format(info, level.getChoiceCount(), fm.format(level.getFullAmount()));
                                        break;
                                    case HALF_PRICE_SECOND_PIECE:
                                        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
                                        marketingGetByIdRequest.setMarketingId(i.getMarketingId());
                                        Map<Long, MarketingHalfPriceSecondPieceLevelVO> halfPriceSecondPieceLevelMap = marketingQueryProvider.getByIdForSupplier(marketingGetByIdRequest).getContext().getMarketingForEndVO().getHalfPriceSecondPieceLevel().stream().collect(Collectors.toMap(MarketingHalfPriceSecondPieceLevelVO::getId, c -> c));
                                        MarketingHalfPriceSecondPieceLevelVO halfPriceSecondPieceLevel = halfPriceSecondPieceLevelMap.get(l);
                                        if (halfPriceSecondPieceLevel == null) {
                                            throw new SbcRuntimeException("K-050221");
                                        }
                                        if (halfPriceSecondPieceLevel.getDiscount().intValue() == 0) {
                                            info = "买" + (halfPriceSecondPieceLevel.getNumber() - 1) + "送1";
                                        } else {
                                            info = "第" + halfPriceSecondPieceLevel.getNumber() + "件" + halfPriceSecondPieceLevel.getDiscount() + "折";
                                        }
                                        break;
                                    case POINT_BUY:
                                        info = "积分换购活动";
                                        break;
                                    case SUITS:
                                        throw new SbcRuntimeException("K-600019");
                                    default:
                                        break;
                                }
                                infoList.add(info);
                            }
                    );

                });
        return infoList.isEmpty() ? "" : StringUtils.join(infoList, "、") + "已失效！";
    }

    /**
     * 校验订单开票规则
     *
     * @param invoice  订单开票信息
     * @param supplier 商家店铺信息
     */
    void verifyInvoice(Invoice invoice, Supplier supplier) {
        InvoiceProjectSwitchByCompanyInfoIdRequest request = new InvoiceProjectSwitchByCompanyInfoIdRequest();
        request.setCompanyInfoId(supplier.getSupplierId());
        BaseResponse<InvoiceProjectSwitchByCompanyInfoIdResponse> baseResponse = invoiceProjectSwitchQueryProvider.getByCompanyInfoId(request);
        InvoiceProjectSwitchByCompanyInfoIdResponse response = baseResponse.getContext();
        log.info("InvoiceProjectSwitchByCompanyInfoIdResponse===>{},invoice=========>{}", response, invoice);
        if ((response.getIsSupportInvoice().equals(DefaultFlag.NO) && !invoice.getType().equals(-1)) ||
                (response.getIsPaperInvoice().equals(DefaultFlag.NO) && invoice.getType().equals(0)) ||
                (response.getIsValueAddedTaxInvoice().equals(DefaultFlag.NO) && invoice.getType().equals(1))) {
            throw new SbcRuntimeException("K-050209", new String[]{supplier.getStoreName()});
        }
    }


    /**
     * 验证商品抵扣性积分
     *
     * @param points
     * @param customerId
     */
    public boolean verifyBuyPoints(Long points, String customerId) {
        //查询会员可用积分
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId))
                .getContext();
        //会员积分余额
        Long pointsAvailable = Objects.isNull(customer.getPointsAvailable()) ? 0 : customer.getPointsAvailable();
        //订单使用积分超出会员可用积分
        return pointsAvailable.compareTo(points) >= 0;
    }


    /**
     * 验证周期购活动
     * 不验证赠品时，无需设置gifts
     * 不验证配送信息时，无需设置deliveryCycle、rule、deliveryPlan
     *
     * @param goodsId
     * @param gifts
     */
    public void verifyCycleBuy(String goodsId, List<String> gifts, DeliveryCycle deliveryCycle, String rule
            , DeliveryPlan deliveryPlan, boolean forceCommit) {

        //验证活动是否存在
        CycleBuyVO cycleBuyVO = cycleBuyQueryProvider.getByGoodsId(CycleBuyByGoodsIdRequest.builder().goodsId(goodsId).build()).getContext().getCycleBuyVO();
        if (Objects.isNull(cycleBuyVO)) {
            throw new SbcRuntimeException(CycleBuyErrorCode.INVALID);
        }

        //验证配送信息
        if (DeliveryPlan.CUSTOMER.equals(deliveryPlan)) {
            if (!cycleBuyVO.getDeliveryPlan().equals(deliveryPlan) || !cycleBuyVO.getDeliveryCycle().equals(deliveryCycle) || !cycleBuyVO.getSendDateRule().contains(rule)) {
                throw new SbcRuntimeException(CycleBuyErrorCode.ACTIVITY_UPDATED);
            }
        } else if (DeliveryPlan.BUSINESS.equals(deliveryPlan) && !cycleBuyVO.getDeliveryPlan().equals(deliveryPlan)) {
            throw new SbcRuntimeException(CycleBuyErrorCode.ACTIVITY_UPDATED);
        }

        //验证赠品
        if (CollectionUtils.isNotEmpty(gifts)) {
            GoodsInfoViewByIdsResponse goodsResponse = getGoodsResponse(gifts);
            List<String> actGifts = cycleBuyVO.getCycleBuyGiftVOList().stream().map(CycleBuyGiftVO::getGoodsInfoId).collect(Collectors.toList());
            //验证活动赠品是否变更
            gifts.forEach(id -> {
                if (!actGifts.contains(id)) {
                    throw new SbcRuntimeException(CycleBuyErrorCode.ACTIVITY_UPDATED);
                }
            });

            //校验赠品有效性
            List<GoodsInfoVO> invalidGifts = verifyGiftSku(gifts, Collections.emptyList(), goodsResponse, Collections.emptyList());

            //组装提示信息
            String info = "";
            if (!invalidGifts.isEmpty()) {
                // 强制提交
                if (forceCommit) {
                    List<String> invalidSkuId= invalidGifts.stream().map(g -> g.getGoodsInfoId()).collect(Collectors.toList());
                    gifts.removeAll(invalidSkuId);
                } else {
                    String tmp = "赠品%s";
                    List<String> skuInfo = new ArrayList<>();
                    invalidGifts.forEach(
                            i -> skuInfo.add(String.format(tmp, i.getGoodsInfoName() + (i.getSpecText() == null ? "" : i.getSpecText())))
                    );
                    info = info + StringUtils.join(skuInfo, "、") + "已失效或缺货！";

                    if (StringUtils.isNotBlank(info)) {
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "很抱歉，" + info);
                    }
                }
            }

        }
    }

    /**
     * 校验加价购活动是否满足
     *
     * @param trade
     */
    public void verifyMarkup(Trade trade) {
        if (CollectionUtils.isEmpty(trade.getTradeMarketings())) {
            return;
        }
        List<TradeMarketingVO> markupMarketing = trade.getTradeMarketings().stream()
                .filter(i -> MarketingSubType.MARKUP.equals(i.getSubType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(markupMarketing)) {
            return;
        }
        List<TradeItem> tradeItems = trade.getTradeItems().stream()
                .filter(i -> Boolean.TRUE.equals(i.getIsMarkupGoods())).collect(Collectors.toList());

        // 没有换购商品 直接返回
        if (CollectionUtils.isEmpty(tradeItems)) {
            return;
        }
        HashMap<String, BigDecimal> markupAmountMap = new HashMap<>();
        markupMarketing.stream().forEach(l -> {
            l.getMarkupLevel().getMarkupLevelDetailList().stream().forEach(d -> {
                markupAmountMap.put(d.getGoodsInfoId(), l.getMarkupLevel().getLevelAmount());
            });
        });


        TradePrice tradePrice = trade.getTradePrice();
        BigDecimal goodsPrice = tradePrice.getGoodsPrice()
                .subtract(Objects.isNull(tradePrice.getMarkupPrice()) ? BigDecimal.ZERO : tradePrice.getMarkupPrice());
        markupAmountMap.entrySet().stream().forEach(entry -> {
            if (entry.getValue().compareTo(goodsPrice) > 0) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "换购商品不满足换购条件,请重新选择商品下单!");
            }
        });

    }
}
