package com.wanmi.sbc.order.trade.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.api.request.purchase.Purchase4DistributionSimplifyRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemConfirmSettlementRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemModifyGoodsNumRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemSnapshotMarkupRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemSnapshotRequest;
import com.wanmi.sbc.order.api.response.purchase.Purchase4DistributionResponse;
import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.purchase.Purchase;
import com.wanmi.sbc.order.purchase.PurchaseService;
import com.wanmi.sbc.order.trade.model.entity.TradeGrouponCommitForm;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.model.root.TradeItemSnapshot;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>订单商品操作Service</p>
 * Created by of628-wenzhi on 2017-07-13-上午10:48.
 */
@Slf4j
@Service
public class TradeItemService {

    @Autowired
    private TradeItemSnapshotService tradeItemSnapshotService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;
    @Autowired
    private RedissonClient redissonClient;


    @Autowired
    private VerifyService verifyService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private TradeGoodsService tradeGoodsService;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;


    /**
     * 获取用户已确认订单的商品快照
     *
     * @param terminalToken 客户id
     * @return 按照商家、店铺分组后的商品快照，只包含skuId与购买数量
     */
    public List<TradeItemGroup> find(String terminalToken) {
        TradeItemSnapshot tradeItemSnapshot = tradeItemSnapshotService.getTradeItemSnapshot(terminalToken);
        if (tradeItemSnapshot == null) {
            throw new SbcRuntimeException("K-050201");
        }
        return tradeItemSnapshot.getItemGroups();
    }


    /**
     * 保存订单商品快照
     *
     * @param request            客户id、是否开店礼包
     * @param tradeItems         商品快照，只包含skuId与购买数量，营销id（用于确认订单页面展示营销活动信息）
     * @param tradeMarketingList
     * @param skuList            快照商品详细信息，包含所属商家，店铺等信息
     */
    @GlobalTransactional
    public void snapshot(TradeItemSnapshotRequest request, List<TradeItem> tradeItems, List<TradeMarketingDTO> tradeMarketingList,
                         List<GoodsInfoDTO> skuList) {
        String customerId = request.getCustomerId();
        //验证积分商品的积分与会员可用积分
        if (Boolean.TRUE.equals(request.getPointGoodsFlag())) {
            Long sumPoint = tradeItems.stream().mapToLong(t -> t.getNum() *
                    skuList.stream()
                            .filter(s -> s.getGoodsInfoId().equals(t.getSkuId()) && s.getBuyPoint() != null && s.getBuyPoint() > 0)
                            .mapToLong(GoodsInfoDTO::getBuyPoint)
                            .findFirst().orElse(0L)).sum();
            if (sumPoint > 0 && (!verifyService.verifyBuyPoints(sumPoint, customerId))) {
                throw new SbcRuntimeException("K-050317");
            }
        }

        //商品按店铺分组
        Map<Long, Map<String, List<GoodsInfoDTO>>> skuMap = skuList.stream().collect(
                Collectors.groupingBy(GoodsInfoDTO::getStoreId,
                        Collectors.groupingBy(GoodsInfoDTO::getGoodsInfoId)));
        List<TradeItemGroup> itemGroups = new ArrayList<>();
        skuMap.forEach((key, value) -> {
            //获取商品所属商家，店铺信息
            StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(key)
                    .build())
                    .getContext().getStoreVO();
            //周期购商品使用单品运费
            DefaultFlag freightTemplateType = Objects.nonNull(request.getCycleBuyInfoDTO()) ? DefaultFlag.YES : store.getFreightTemplateType();
            Supplier supplier = Supplier.builder()
                    .storeId(store.getStoreId())
                    .storeName(store.getStoreName())
                    .isSelf(store.getCompanyType() == BoolFlag.NO)
                    .supplierCode(store.getCompanyInfo().getCompanyCode())
                    .supplierId(store.getCompanyInfo().getCompanyInfoId())
                    .supplierName(store.getCompanyInfo().getSupplierName())
                    .freightTemplateType(freightTemplateType)
                    .build();
            //分组后的商品快照
            List<TradeItem> items = tradeItems.stream().filter(i -> value.containsKey(i.getSkuId()))
                    .collect(Collectors.toList());
            //分组后的营销快照
            List<String> ids = items.stream().map(TradeItem::getSkuId).collect(Collectors.toList());
            List<TradeMarketingDTO> marketings = tradeMarketingList.stream().filter(i -> i.getSkuIds().stream()
                    .anyMatch(ids::contains)).collect(Collectors.toList());

            items.forEach(tradeItem -> {
                List<Long> marketingIds = marketings.stream().filter(i -> i.getSkuIds().contains(tradeItem.getSkuId()))
                        .map(TradeMarketingDTO::getMarketingId).collect(Collectors.toList());
                List<Long> marketingLevelIds = marketings.stream().filter(i -> i.getSkuIds().contains(tradeItem.getSkuId()))
                        .map(TradeMarketingDTO::getMarketingLevelId).collect(Collectors.toList());
                tradeItem.setMarketingIds(marketingIds);
                tradeItem.setMarketingLevelIds(marketingLevelIds);
            });
            TradeGrouponCommitForm grouponForm = null;
            if (Objects.nonNull(request.getOpenGroupon())) {
                // 当为拼团单时，设置拼团form
                grouponForm = new TradeGrouponCommitForm();
                grouponForm.setOpenGroupon(request.getOpenGroupon());
                grouponForm.setGrouponNo(request.getGrouponNo());
            }
            IteratorUtils.zip(skuList, items,
                    (a, b) ->
                            a.getGoodsInfoId().equals(b.getSkuId())
                    ,
                    (c, d) -> {
                        d.setBuyPoint(c.getBuyPoint());
                    });

            if (StringUtils.isNotBlank(request.getSnapshotType())) {
                itemGroups.add(new TradeItemGroup(items, supplier, marketings, request.getStoreBagsFlag(), request
                        .getSnapshotType(), grouponForm, request.getSuitMarketingFlag(), request.getCycleBuyInfoDTO(),request.getSuitScene()));
            } else {
                itemGroups.add(new TradeItemGroup(items, supplier, marketings, request.getStoreBagsFlag(), null, grouponForm, request.getSuitMarketingFlag(), request.getCycleBuyInfoDTO(),request.getSuitScene()));
            }
        });

        //生成快照
        TradeItemSnapshot snapshot = TradeItemSnapshot.builder()
                .id(UUIDUtil.getUUID())
                .buyerId(customerId)
                .itemGroups(itemGroups)
                .snapshotType(request.getSnapshotType())
                .terminalToken(request.getTerminalToken())
                .purchaseBuy(request.getPurchaseBuy())
                .build();
        tradeItemSnapshotService.addTradeItemSnapshot(snapshot);

    }

    /**
     * 保存订单商品快照
     *
     * @param terminalToken    会员id
     * @param marketingGiftDTO 营销信息
     */
    // @LcnTransaction
    // @TccTransaction
    @GlobalTransactional
    @Transactional
    public void fullGiftSnapshot(String terminalToken, TradeMarketingDTO marketingGiftDTO) {
        RLock rLock = redissonClient.getFairLock(RedisKeyConstant.CUSTOMER_TRADE_SNAPSHOT_LOCK_KEY + terminalToken);
        try {
            if (rLock.tryLock(10, 10, TimeUnit.SECONDS)) {
                //快照生成采用幂等操作
                TradeItemSnapshot tradeItemSnapshot = tradeItemSnapshotService.getTradeItemSnapshot(terminalToken);

                if (Objects.isNull(tradeItemSnapshot)
                        || CollectionUtils.isEmpty(tradeItemSnapshot.getItemGroups())
                        || tradeItemSnapshot.getItemGroups().size() != 1) {
                    return;
                }
                TradeItemGroup tradeItemGroup = tradeItemSnapshot.getItemGroups().get(0);
                if (Objects.isNull(tradeItemGroup) || CollectionUtils.isEmpty(tradeItemGroup.getTradeMarketingList())) {
                    return;
                }
                if (Objects.nonNull(marketingGiftDTO) && CollectionUtils.isNotEmpty(marketingGiftDTO.getGiftSkuIds())) {
                    //符合以上条件修改快照
                    tradeItemGroup.getTradeMarketingList().forEach(m -> {
                        if (m.getMarketingId().equals(marketingGiftDTO.getMarketingId())
                                && org.apache.commons.collections4.CollectionUtils.isEqualCollection(m.getSkuIds(), marketingGiftDTO.getSkuIds())) {
                            m.setMarketingLevelId(marketingGiftDTO.getMarketingLevelId());
                            m.setGiftSkuIds(marketingGiftDTO.getGiftSkuIds());
                        }
                    });
                } else {
                    //满赠赠品不选，快照里的相关满赠营销信息删掉
                    List<TradeMarketingDTO> marketingDTOSOthers =
                            tradeItemGroup.getTradeMarketingList().stream().filter(m ->
                                    !(m.getMarketingId().equals(marketingGiftDTO.getMarketingId())
                                            && org.apache.commons.collections4.CollectionUtils.isEqualCollection(m.getSkuIds(), marketingGiftDTO.getSkuIds()))
                            ).collect(Collectors.toList());
                    List<TradeMarketingDTO> marketingDTOSDelete =
                            tradeItemGroup.getTradeMarketingList().stream().filter(m ->
                                    m.getMarketingId().equals(marketingGiftDTO.getMarketingId())
                                            && org.apache.commons.collections4.CollectionUtils.isEqualCollection(m.getSkuIds(), marketingGiftDTO.getSkuIds())
                            ).collect(Collectors.toList());
                    List<Long> deleteMarketingIds =
                            marketingDTOSDelete.stream().map(TradeMarketingDTO::getMarketingId).collect(Collectors.toList());
                    List<Long> deleteMarketingLevelIds =
                            marketingDTOSDelete.stream().map(TradeMarketingDTO::getMarketingLevelId).collect(Collectors.toList());
                    tradeItemGroup.getTradeItems().forEach(m -> {
                        m.setMarketingIds(m.getMarketingIds().stream().filter(t -> !deleteMarketingIds.contains(t)).collect(Collectors.toList()));
                        m.setMarketingLevelIds(m.getMarketingLevelIds().stream().filter(t -> !deleteMarketingLevelIds.contains(t)).collect(Collectors.toList()));
                    });
                    tradeItemGroup.setTradeMarketingList(marketingDTOSOthers);
                }
                tradeItemSnapshotService.updateTradeItemSnapshot(tradeItemSnapshot);
            }
        } catch (Exception e) {
            log.error("订单快照操作失败{}", e);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 换购 保存订单商品快照
     *
     * @param request
     */
    @GlobalTransactional
    @Transactional
    public void markupSnapshot(final TradeItemSnapshotMarkupRequest request) {
        final String terminalToken = request.getTerminalToken();
        final List<TradeMarketingDTO> tradeMarketingList = request.getTradeMarketingDTO();
        final List<TradeItemDTO> tradeItems = request.getTradeItems();
        RLock rLock = redissonClient.getFairLock(RedisKeyConstant.CUSTOMER_TRADE_SNAPSHOT_LOCK_KEY + terminalToken);
        try {
            if (rLock.tryLock(10, 10, TimeUnit.SECONDS)) {
                //快照生成采用幂等操作
                TradeItemSnapshot tradeItemSnapshot = tradeItemSnapshotService.getTradeItemSnapshot(terminalToken);

                if (Objects.isNull(tradeItemSnapshot)
                        || CollectionUtils.isEmpty(tradeItemSnapshot.getItemGroups())) {
                    return;
                }
                for (TradeMarketingDTO tradeMarketingDTO : tradeMarketingList) {
                    for (TradeItemGroup itemGroup : tradeItemSnapshot.getItemGroups()) {
                        Long storeId = tradeMarketingDTO.getStoreId();
                        if (!itemGroup.getSupplier().getStoreId().equals(storeId)) {
                            continue;
                        }
                        TradeItemGroup tradeItemGroup = itemGroup;
                        // 营销活动商品的处理
                        if (CollectionUtils.isNotEmpty(tradeItemGroup.getTradeMarketingList())) {
                            //符合以上条件修改快照
                            AtomicBoolean isFindMarketing = new AtomicBoolean(true);
                            tradeItemGroup.getTradeMarketingList().forEach(m -> {
                                if (m.getMarketingId().equals(tradeMarketingDTO.getMarketingId())
                                        && m.getMarketingLevelId().equals(tradeMarketingDTO.getMarketingLevelId())
                                        && CollectionUtils.isEqualCollection(m.getSkuIds(), tradeMarketingDTO.getSkuIds())) {
                                    isFindMarketing.set(false);
                                    m.setMarketingLevelId(tradeMarketingDTO.getMarketingLevelId());
                                    if (!m.getMarkupSkuIds().contains(tradeMarketingDTO.getMarkupSkuIds().get(0))) {
                                        m.getMarkupSkuIds().add(tradeMarketingDTO.getMarkupSkuIds().get(0));
                                    }
                                }
                            });
                            if (isFindMarketing.get()) {
                                tradeItemGroup.getTradeMarketingList().add(tradeMarketingDTO);
                            }
                        } else {
                            // 加价购换购商品处理
                            tradeItemGroup.getTradeMarketingList().add(tradeMarketingDTO);
                        }
                    }
                }
                tradeItemSnapshotService.updateTradeItemSnapshot(tradeItemSnapshot);
            }
        } catch (Exception e) {
            log.error("订单快照操作失败{}", e);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 更新订单商品快照中的商品数量
     *
     * @param request 客户id、是否开店礼包
     */
    @GlobalTransactional
    @Transactional
    public TradeItemSnapshot modifyGoodsNum(TradeItemModifyGoodsNumRequest request) {
        String customerId = request.getCustomerId();
        List<GoodsInfoDTO> skuList = request.getSkuList();
        //验证积分商品的积分与会员可用积分
        if (skuList.get(0).getBuyPoint() != null && skuList.get(0).getBuyPoint() > 0) {
            Long sumPoint = request.getNum() * skuList.get(0).getBuyPoint();
            if (sumPoint > 0 && (!verifyService.verifyBuyPoints(sumPoint, customerId))) {
                throw new SbcRuntimeException("K-050317");
            }
        }
        //快照生成采用幂等操作
        TradeItemSnapshot tradeItemSnapshot = tradeItemSnapshotService.getTradeItemSnapshot(request.getTerminalToken());
        if (Objects.isNull(tradeItemSnapshot) || CollectionUtils.isEmpty(tradeItemSnapshot.getItemGroups())) {
            throw new SbcRuntimeException("K-050213");
        }
        tradeItemSnapshot.getItemGroups().stream()
                .filter(i -> CollectionUtils.isNotEmpty(i.getTradeItems()))
                .forEach(i -> i.getTradeItems().forEach(j -> {
                    if (j.getSkuId().equalsIgnoreCase(request.getSkuId())) {
                        if (Objects.nonNull(j.getIsBookingSaleGoods()) && j.getIsBookingSaleGoods() && j.getBookingType() == BookingType.EARNEST_MONEY) {
                            j.setEarnestPrice(j.getEarnestPrice().divide(BigDecimal.valueOf(j.getNum())).multiply(BigDecimal.valueOf(request.getNum())));
                            j.setSwellPrice(j.getSwellPrice().divide(BigDecimal.valueOf(j.getNum())).multiply(BigDecimal.valueOf(request.getNum())));
                        }
                        j.setNum(request.getNum());
                    }
                }));

        tradeItemSnapshotService.updateTradeItemSnapshot(tradeItemSnapshot);
        return tradeItemSnapshot;
    }

    /**
     * 删除订单商品快照
     *
     * @param terminalToken 用户终端token
     */
    public void remove(String terminalToken) {
        tradeItemSnapshotService.deleteTradeItemSnapshot(terminalToken);
    }


    /**
     * 计算平摊价
     *
     * @param tradeItems 订单商品
     * @param newTotal   新的总价
     */
    void clacSplitPrice(List<TradeItem> tradeItems, BigDecimal newTotal) {
        // 1.如果新的总价为0或空，设置所有商品均摊价为0
        if (newTotal == null || BigDecimal.ZERO.equals(newTotal)) {
            tradeItems.forEach(tradeItem -> tradeItem.setSplitPrice(BigDecimal.ZERO));
            return;
        }

        // 2.计算商品旧的总价
        BigDecimal total = this.calcSkusTotalPrice(tradeItems);

        // 3.计算商品均摊价
        this.calcSplitPrice(tradeItems, newTotal, total);
    }

    /**
     * 计算商品集合的均摊总价
     */
    BigDecimal calcSkusTotalPrice(List<TradeItem> tradeItems) {
        if (Objects.nonNull(tradeItems.get(0).getIsBookingSaleGoods()) && tradeItems.get(0).getIsBookingSaleGoods()) {
            TradeItem tradeItem = tradeItems.get(0);
            if (tradeItem.getBookingType() == BookingType.EARNEST_MONEY && Objects.nonNull(tradeItem.getTailPrice())) {
                return tradeItem.getTailPrice();
            }
        }
        return tradeItems.stream()
                .filter(tradeItem -> tradeItem.getSplitPrice() != null && tradeItem.getSplitPrice().compareTo(BigDecimal.ZERO) > 0)
                .map(TradeItem::getSplitPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    /**
     * 计算商品集合的积分抵扣均摊总价
     */
    BigDecimal calcSkusTotalPointsPrice(List<TradeItem> tradeItems) {
        return tradeItems.stream()
                .filter(tradeItem -> tradeItem.getPointsPrice() != null && tradeItem.getPointsPrice().compareTo(BigDecimal.ZERO) > 0)
                .map(TradeItem::getPointsPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    /**
     * 计算商品集合的知豆抵扣均摊总价
     */
    BigDecimal calcSkusTotalKnowledgePrice(List<TradeItem> tradeItems) {
        return tradeItems.stream()
                .filter(tradeItem -> tradeItem.getKnowledgePrice() != null && tradeItem.getKnowledgePrice().compareTo(BigDecimal.ZERO) > 0)
                .map(TradeItem::getKnowledgePrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    /**
     * 计算商品集合的积分抵扣均摊总数
     */
    Long calcSkusTotalPoints(List<TradeItem> tradeItems) {
        return tradeItems.stream()
                .filter(tradeItem -> tradeItem.getPoints() != null && tradeItem.getPoints() > 0)
                .map(TradeItem::getPoints)
                .reduce(0L, (a, b) -> a + b);
    }

    /**
     * 计算商品集合的知豆抵扣均摊总数
     */
    Long calcSkusTotalKnowledge(List<TradeItem> tradeItems) {
        return tradeItems.stream()
                .filter(tradeItem -> tradeItem.getKnowledge() != null && tradeItem.getKnowledge() > 0)
                .map(TradeItem::getKnowledge)
                .reduce(0L, (a, b) -> a + b);
    }


    /**
     * 计算商品均摊价
     *
     * @param tradeItems 待计算的商品列表
     * @param newTotal   新的总价
     * @param total      旧的商品总价
     */
    void calcSplitPrice(List<TradeItem> tradeItems, BigDecimal newTotal, BigDecimal total) {
        //内部总价为零或相等不用修改
        if (total.equals(newTotal)) {
            return;
        }
        // 尾款情况重新计算实际总价
        if (CollectionUtils.isNotEmpty(tradeItems)) {
            TradeItem tradeItem = tradeItems.get(0);
            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.EARNEST_MONEY
                    && total.equals(tradeItem.getTailPrice())) {
                newTotal = tradeItem.getEarnestPrice().add(newTotal);
                total = tradeItem.getEarnestPrice().add(total);
            }
        }

        int size = tradeItems.size();
        BigDecimal splitPriceTotal = BigDecimal.ZERO;//累积平摊价，将剩余扣给最后一个元素
        Long totalNum = tradeItems.stream().map(tradeItem -> tradeItem.getNum()).reduce(0L, Long::sum);

        for (int i = 0; i < size; i++) {
            TradeItem tradeItem = tradeItems.get(i);
            if (i == size - 1) {
                tradeItem.setSplitPrice(newTotal.subtract(splitPriceTotal));
            } else {
                BigDecimal splitPrice = tradeItem.getSplitPrice() != null ? tradeItem.getSplitPrice() : BigDecimal.ZERO;
                //全是零元商品按数量均摊
                if (BigDecimal.ZERO.equals(total)) {
                    tradeItem.setSplitPrice(
                            newTotal.multiply(BigDecimal.valueOf(tradeItem.getNum()))
                                    .divide(BigDecimal.valueOf(totalNum), 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    tradeItem.setSplitPrice(
                            splitPrice
                                    .divide(total, 10, BigDecimal.ROUND_DOWN)
                                    .multiply(newTotal)
                                    .setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                splitPriceTotal = splitPriceTotal.add(tradeItem.getSplitPrice());
            }
        }
    }

    /**
     * 计算积分抵扣均摊价、均摊数量
     *
     * @param tradeItems       待计算的商品列表
     * @param pointsPriceTotal 积分抵扣总额
     * @param pointsTotal      积分抵扣总数
     */
    void calcPoints(List<TradeItem> tradeItems, BigDecimal pointsPriceTotal, Long pointsTotal, BigDecimal pointWorth) {
        BigDecimal totalPrice = tradeItems.stream()
                .filter(tradeItem -> tradeItem.getSplitPrice() != null && tradeItem.getSplitPrice().compareTo(BigDecimal.ZERO) > 0)
                .map(TradeItem::getSplitPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        int size = tradeItems.size();
        //累积积分平摊价，将剩余扣给最后一个元素
        BigDecimal splitPriceTotal = BigDecimal.ZERO;
        //累积积分数量，将剩余扣给最后一个元素
        Long splitPointsTotal = 0L;

        // 积分和名单商品不能使用积分，也不参与分摊
        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
        goodsBlackListPageProviderRequest.setBusinessCategoryColl(Collections.singletonList(GoodsBlackListCategoryEnum.POINT_NOT_SPLIT.getCode()));
        BaseResponse<GoodsBlackListPageProviderResponse> goodsBlackListPageProviderResponseBaseResponse = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest);
        GoodsBlackListPageProviderResponse context = goodsBlackListPageProviderResponseBaseResponse.getContext();
        if (context.getPointNotSplitBlackListModel() != null && !CollectionUtils.isEmpty(context.getPointNotSplitBlackListModel().getGoodsIdList())) {
            List<String> blackListGoodsId = context.getPointNotSplitBlackListModel().getGoodsIdList();
            for (TradeItem tradeItem : tradeItems) {
                if(blackListGoodsId.contains(tradeItem.getSpuId())) {
                    totalPrice = totalPrice.subtract(tradeItem.getSplitPrice());
                }
            }
        }
        if(pointsPriceTotal.compareTo(totalPrice) > 0){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "积分超过使用限制");
        }

        for (int i = 0; i < size; i++) {
            TradeItem tradeItem = tradeItems.get(i);
            BigDecimal surplusPointsPrice = pointsPriceTotal.subtract(splitPriceTotal);
            if (i == size - 1) {
                tradeItem.setPointsPrice(surplusPointsPrice);
                tradeItem.setPoints(pointsTotal - splitPointsTotal);
            } else {
                BigDecimal splitPrice = tradeItem.getSplitPrice() != null ? tradeItem.getSplitPrice() : BigDecimal.ZERO;
                BigDecimal pointsPrice = splitPrice.divide(totalPrice, 10, BigDecimal.ROUND_DOWN)
                        .multiply(pointsPriceTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal points = pointsPrice.multiply(pointWorth);
                if (surplusPointsPrice.compareTo(BigDecimal.ZERO) <= 0) {
                    pointsPrice = BigDecimal.ZERO;
                    points = BigDecimal.ZERO;
                }
                tradeItem.setPointsPrice(pointsPrice);
                splitPriceTotal = splitPriceTotal.add(pointsPrice);
                tradeItem.setPoints(points.longValue());
                splitPointsTotal = splitPointsTotal + tradeItem.getPoints();
            }
        }
    }

    /**
     * 计算知豆抵扣均摊价、均摊数量
     *
     * @param tradeItems          待计算的商品列表
     * @param knowledgePriceTotal 知豆抵扣总额
     * @param knowledgeTotal      知豆抵扣总数
     */
    void calcKnowledge(List<TradeItem> tradeItems, BigDecimal knowledgePriceTotal, Long knowledgeTotal, BigDecimal knowledgeWorth) {
        BigDecimal totalPrice = tradeItems.stream()
                .filter(tradeItem -> tradeItem.getSplitPrice() != null && tradeItem.getSplitPrice().compareTo(BigDecimal.ZERO) > 0)
                .map(TradeItem::getSplitPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        int size = tradeItems.size();
        //累积积分平摊价，将剩余扣给最后一个元素
        BigDecimal splitPriceTotal = BigDecimal.ZERO;
        //累积积分数量，将剩余扣给最后一个元素
        Long splitKnowledgeTotal = 0L;

        for (int i = 0; i < size; i++) {
            TradeItem tradeItem = tradeItems.get(i);
            BigDecimal subtract = knowledgePriceTotal.subtract(splitPriceTotal);
            if (i == size - 1) {
                tradeItem.setKnowledgePrice(subtract);
                tradeItem.setPointsPrice(subtract); //TODO 这里考虑后续删除，
                tradeItem.setKnowledge(knowledgeTotal - splitKnowledgeTotal);
            } else {
                BigDecimal splitPrice = tradeItem.getSplitPrice() != null ? tradeItem.getSplitPrice() : BigDecimal.ZERO;
                BigDecimal knowLedgePrice = splitPrice.divide(totalPrice, 10, BigDecimal.ROUND_DOWN)
                        .multiply(knowledgePriceTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal knowLedge = knowLedgePrice.multiply(knowledgeWorth);
                if (subtract.compareTo(BigDecimal.ZERO) <= 0) {
                    knowLedgePrice = BigDecimal.ZERO;
                    knowLedge = BigDecimal.ZERO;
                }
                tradeItem.setPointsPrice(knowLedgePrice);
                tradeItem.setKnowledgePrice(knowLedgePrice);
                splitPriceTotal = splitPriceTotal.add(knowLedgePrice);
                tradeItem.setKnowledge(knowLedge.longValue());
                splitKnowledgeTotal = splitKnowledgeTotal + tradeItem.getKnowledge();
            }

        }
    }

    /**
     * 确认结算
     *
     * @param request
     */
    @GlobalTransactional
    @Transactional
    public void confirmSettlement(TradeItemConfirmSettlementRequest request) {
        //验证采购单
        List<String> skuIds = request.getSkuIds();
        String customerId = request.getCustomerId();
        String inviteeId = request.getInviteeId();

        // 积分换购活动只能一件商品
        List<TradeMarketingDTO> tradeMarketingList1 = request.getTradeMarketingList();
        Iterator<TradeMarketingDTO> it = tradeMarketingList1.iterator();
        while (it.hasNext()) {
            TradeMarketingDTO next = it.next();
            if(next.getMarketingSubType() != null && next.getMarketingSubType() == 10){
                if(request.getTradeItems().size() > 1 || request.getTradeItems().get(0).getNum() > 1){
                    request.setTradeMarketingList(new ArrayList<>());
                    break;
                }
            }
        }

        //查询是否购买付费会员卡
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList = paidCardCustomerRelQueryProvider
                .listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder()
                        .customerId(customerId)
                        .delFlag(DeleteFlag.NO)
                        .endTimeFlag(LocalDateTime.now())
                        .build())
                .getContext();
        PaidCardVO paidCardVO = new PaidCardVO();
        if (CollectionUtils.isNotEmpty(paidCardCustomerRelVOList)) {
            paidCardVO = paidCardCustomerRelVOList.stream()
                    .map(PaidCardCustomerRelVO::getPaidCardVO)
                    .min(Comparator.comparing(PaidCardVO::getDiscountRate)).get();
        }

        List<Purchase> purchaseList = purchaseService.queryPurchase(customerId, skuIds, inviteeId);

        if (CollectionUtils.isEmpty(purchaseList)) {
            throw new SbcRuntimeException("K-050205");
        }
        List<String> existIds = purchaseList.stream().map(Purchase::getGoodsInfoId).collect(Collectors.toList());
        if (skuIds.stream().anyMatch(skuId -> !existIds.contains(skuId))) {
            throw new SbcRuntimeException("K-050205");
        }

        //验证用户
        CustomerSimplifyOrderCommitVO customer = verifyService.simplifyById(customerId);

        GoodsInfoResponse response = tradeGoodsService.getGoodsResponse(skuIds, customer);
        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos();
//        Map<String, String> goodMap = goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsId, GoodsInfoVO::getGoodsInfoId));
//        Map<String, Integer> cpsSpecialMap = response.getGoodses().stream().filter(good -> good.getCpsSpecial() != null).collect(Collectors.toMap(goods -> goodMap.get(goods.getGoodsId()), GoodsVO::getCpsSpecial));

        Map<String, GoodsVO> goodsMap = response.getGoodses().stream().filter(goods -> goods.getCpsSpecial() != null)
                .collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        Map<String, Integer> cpsSpecialMap = goodsInfoVOList.stream()
                .collect(Collectors.toMap(goodsInfo -> goodsInfo.getGoodsInfoId(), goodsInfo2 -> goodsMap.get(goodsInfo2.getGoodsId()).getCpsSpecial()));

        ChannelType channelType = request.getChannelType();
        DistributeChannel distributeChannel = request.getDistributeChannel();
        //根据开关重新设置分销商品标识
        tradeGoodsService.checkDistributionSwitch(goodsInfoVOList, channelType);
        //社交分销业务
        Purchase4DistributionSimplifyRequest purchase4DistributionRequest = new Purchase4DistributionSimplifyRequest();
        purchase4DistributionRequest.setGoodsInfos(response.getGoodsInfos());
        purchase4DistributionRequest.setGoodsIntervalPrices(response.getGoodsIntervalPrices());
        purchase4DistributionRequest.setCustomer(customer);
        purchase4DistributionRequest.setDistributeChannel(distributeChannel);
        Purchase4DistributionResponse purchase4DistributionResponse = tradeGoodsService.distribution
                (purchase4DistributionRequest);
        response.setGoodsInfos(purchase4DistributionResponse.getGoodsInfos());
        response.setGoodsIntervalPrices(purchase4DistributionResponse.getGoodsIntervalPrices());
        //验证分销商品状态
        tradeGoodsService.validShopGoods(purchase4DistributionResponse.getGoodsInfos());

        List<TradeItem> tradeItems = KsBeanUtil.convert(request.getTradeItems(), TradeItem.class);
        //获取付费会员价
        if (Objects.nonNull(paidCardVO.getDiscountRate())) {
            for (GoodsInfoVO goodsInfoVO : response.getGoodsInfos()) {
                goodsInfoVO.setSalePrice(goodsInfoVO.getMarketPrice().multiply(paidCardVO.getDiscountRate()));
            }
            if (CollectionUtils.isNotEmpty(tradeItems)) {
                for (TradeItem tradeItem : tradeItems) {
                    if (Objects.nonNull(tradeItem.getPrice())) {
                        tradeItem.setPrice(tradeItem.getPrice().multiply(paidCardVO.getDiscountRate()));
                    }
                }
            }
        }
        verifyService.verifyGoods(tradeItems, Collections.emptyList(), KsBeanUtil.convert(response, TradeGoodsListVO.class), null, false, request.getAreaId());


        verifyService.verifyStore(response.getGoodsInfos().stream().map(GoodsInfoVO::getStoreId).collect(Collectors.toList()));

        Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
        tradeItems.stream().forEach(tradeItem -> {
            tradeItem.setCpsSpecial(cpsSpecialMap.get(tradeItem.getSkuId()));
            tradeItem.setGoodsType(GoodsType.fromValue(goodsInfoVOMap.get(tradeItem.getSkuId()).getGoodsType()));
            tradeItem.setVirtualCouponId(goodsInfoVOMap.get(tradeItem.getSkuId()).getVirtualCouponId());
            tradeItem.setBuyPoint(goodsInfoVOMap.get(tradeItem.getSkuId()).getBuyPoint());
            tradeItem.setStoreId(goodsInfoVOMap.get(tradeItem.getSkuId()).getStoreId());
        });
        List<TradeMarketingDTO> tradeMarketingList = request.getTradeMarketingList();
        verifyService.verifyTradeMarketing(request.getTradeMarketingList(), Collections.emptyList(), tradeItems, customerId, request.isForceConfirm());

        tradeItems.stream().forEach(tradeItem -> {
            tradeMarketingList.stream().forEach(tradeMarketingDTO -> {
                        if (tradeMarketingDTO.getSkuIds().contains(tradeItem.getSkuId())) {
                            tradeItem.getMarketingIds().add(tradeMarketingDTO.getMarketingId());
                            tradeItem.getMarketingLevelIds().add(tradeMarketingDTO.getMarketingLevelId());
                        }
                    }
            );
        });

        // 校验商品限售信息
        TradeItemGroup tradeItemGroupVOS = new TradeItemGroup();
        tradeItemGroupVOS.setTradeItems(tradeItems);
        tradeGoodsService.validateRestrictedGoods(tradeItemGroupVOS, customer);

        //普通商品不能参与预售预约活动
        List<String> skuIdList = tradeItems.stream()
                .filter(i ->
                        (!Boolean.TRUE.equals(i.getIsBookingSaleGoods()))
                                && (!Boolean.TRUE.equals(i.getIsAppointmentSaleGoods()))
                                && (Objects.isNull(i.getBuyPoint()) || i.getBuyPoint() == 0))
                .map(TradeItem::getSkuId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(skuIdList)) {
            appointmentSaleQueryProvider.containAppointmentSaleAndBookingSale(AppointmentSaleInProgressRequest.builder().goodsInfoIdList(skuIdList).build());
        }

        // 预约活动校验是否有资格
        List<TradeItem> excludeBuyPointList = tradeItems.stream()
                .filter(tradeItem -> (Objects.isNull(tradeItem.getBuyPoint()) || tradeItem.getBuyPoint() == 0)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(excludeBuyPointList)) {
            tradeItemGroupVOS.setTradeItems(excludeBuyPointList);
            tradeGoodsService.validateAppointmentQualification(Collections.singletonList(tradeItemGroupVOS), customerId);
        }


        /**
         *  校验预售活动资格，初始化价格
         */
        tradeItems = tradeGoodsService.fillActivityPrice(tradeItems, goodsInfoVOList, customerId);

        for (TradeItem tradeItem : tradeItems) {
            BaseResponse<String> priceByGoodsId = goodsIntervalPriceProvider.findPriceByGoodsId(tradeItem.getSkuId());
            if(priceByGoodsId.getContext() != null){
                tradeItem.setPropPrice(Double.valueOf(priceByGoodsId.getContext()));
            }
        }
        List<TradeItemDTO> tradeItemDTOs = KsBeanUtil.convert(tradeItems, TradeItemDTO.class);
        TradeItemSnapshotRequest snapshotRequest = TradeItemSnapshotRequest.builder().customerId(customerId).pointGoodsFlag(true)
                .tradeItems(tradeItemDTOs)
                .tradeMarketingList(request.getTradeMarketingList())
                .skuList(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
                .terminalToken(request.getTerminalToken())
                .build();

        this.snapshot(snapshotRequest, tradeItems, request.getTradeMarketingList(), KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class));
    }

    /**
     * 保存订单商品快照
     *
     * @param terminalToken 会员id
     * @param cycleBuyInfo  周期购信息
     */
    public void cycleBuyGiftSnapshot(String terminalToken, CycleBuyInfoDTO cycleBuyInfo) {
        RLock rLock = redissonClient.getFairLock(RedisKeyConstant.CUSTOMER_TRADE_SNAPSHOT_LOCK_KEY + terminalToken);
        try {
            if (rLock.tryLock(10, 10, TimeUnit.SECONDS)) {
                //快照生成采用幂等操作
                TradeItemSnapshot tradeItemSnapshot = tradeItemSnapshotService.getTradeItemSnapshot(terminalToken);

                if (Objects.isNull(tradeItemSnapshot)
                        || CollectionUtils.isEmpty(tradeItemSnapshot.getItemGroups())
                        || tradeItemSnapshot.getItemGroups().size() != NumberUtils.INTEGER_ONE) {
                    return;
                }
                TradeItemGroup tradeItemGroup = tradeItemSnapshot.getItemGroups().get(NumberUtils.INTEGER_ZERO);
                if (Objects.isNull(tradeItemGroup) || Objects.isNull(tradeItemGroup.getCycleBuyInfo())) {
                    return;
                }

                tradeItemGroup.setCycleBuyInfo(cycleBuyInfo);
                tradeItemSnapshotService.updateTradeItemSnapshot(tradeItemSnapshot);
            }
        } catch (Exception e) {
            log.error("订单快照操作失败{}", e);
        } finally {
            rLock.unlock();
        }
    }

}
