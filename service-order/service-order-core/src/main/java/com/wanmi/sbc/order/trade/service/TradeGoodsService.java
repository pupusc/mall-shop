package com.wanmi.sbc.order.trade.service;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DistributionCommissionUtils;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerEnableByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByCustomerIdAndStoreIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaListByConditionRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerEnableByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelByCustomerIdAndStoreIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.RushToAppointmentSaleGoodsRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleIsInProgressRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoPartColsByIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByCustomerIdRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleInProcessResponse;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleMergeInProcessResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleIsInProgressResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordQueryRequest;
import com.wanmi.sbc.order.api.request.purchase.Purchase4DistributionSimplifyRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.response.purchase.Purchase4DistributionResponse;
import com.wanmi.sbc.order.appointmentrecord.model.root.AppointmentRecord;
import com.wanmi.sbc.order.appointmentrecord.service.AppointmentRecordService;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.cache.DistributionCacheService;
import com.wanmi.sbc.order.purchase.PurchaseService;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.request.TradeParams;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 存放订单与商品服务相关的接口方法
 * @author wanggang
 */
@Service
public class TradeGoodsService {

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;

    @Autowired
    private AppointmentRecordService appointmentRecordService;

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private DistributorLevelQueryProvider distributorLevelQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    /**
     * 1.根据 订单项List 获取商品信息List
     * 2.设置该商品信息List中会用到的区间价信息
     * 3.修改商品信息List中的会员价(salePrice)
     * @param trade 订单
     * @return 商品信息List
     */
    public TradeGoodsListVO getGoodsInfoResponse(Trade trade, TradeParams tradeParams) {
        return getGoodsInfoResponse(trade,tradeParams.getCustomer(),tradeParams.getGoodsInfoViewByIdsResponse());
    }

    /**
     * 1.根据 订单项List 获取商品信息List
     * 2.设置该商品信息List中会用到的区间价信息
     * 3.修改商品信息List中的会员价(salePrice)
     * @param trade 订单
     * @return 商品信息List
     */
    public TradeGoodsListVO getGoodsInfoResponse(Trade trade) {
        //1. 获取sku
        Buyer b = trade.getBuyer();
        GoodsInfoViewByIdsResponse idsResponse = tradeCacheService.getGoodsInfoViewByIds(IteratorUtils.collectKey(trade.getTradeItems(), TradeItem::getSkuId));
        CustomerSimplifyOrderCommitVO customerVO = verifyService.simplifyById(b.getId());

        return getGoodsInfoResponse(trade,customerVO,idsResponse);
    }


    /**
     * 1.根据 订单项List 获取商品信息List
     * 2.设置该商品信息List中会用到的区间价信息
     * 3.修改商品信息List中的会员价(salePrice)
     * @param trade 订单
     * @return 商品信息List
     */
    public TradeGoodsListVO getGoodsInfoResponse(Trade trade, CustomerSimplifyOrderCommitVO customerVO, GoodsInfoViewByIdsResponse idsResponse ) {
        TradeGoodsListVO response = new TradeGoodsListVO();
        response.setGoodsInfos(idsResponse.getGoodsInfos());
        response.setGoodses(idsResponse.getGoodses());
        List<GoodsInfoDTO> goodsInfoDTOList = KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class);
        GoodsIntervalPriceByCustomerIdResponse intervalPriceResponse =
                goodsIntervalPriceProvider.putByCustomerId(
                        GoodsIntervalPriceByCustomerIdRequest.builder().goodsInfoDTOList(goodsInfoDTOList)
                                .customerId(customerVO.getCustomerId()).build()).getContext();
        //计算区间价
        response.setGoodsIntervalPrices(intervalPriceResponse.getGoodsIntervalPriceVOList());
        response.setGoodsInfos(intervalPriceResponse.getGoodsInfoVOList());
        // 商品是否可以分销状态-不要覆盖了
        response.getGoodsInfos().forEach(goodsInfoVO -> {
            goodsInfoVO.setDistributionGoodsAudit(trade.getTradeItems().stream().filter(tradeItem -> Objects.equals(goodsInfoVO.getGoodsInfoId(), tradeItem.getSkuId())).findFirst().orElse(new TradeItem()).getDistributionGoodsAudit());
        });

        //填充实时的校验字段
        this.fillPartCols(response.getGoodsInfos());

        //目前只计算商品的客户级别价格/客户指定价
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customerVO, CustomerDTO.class));
        filterRequest.setCommitFlag(Boolean.TRUE);
        //秒杀从购物车走普通商品提交
        if (Objects.isNull(trade.getIsFlashSaleGoods()) || !trade.getIsFlashSaleGoods()) {
            filterRequest.setIsFlashSaleMarketing(true);
        }


        response.setGoodsInfos(marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList());
        return response;
    }

    /**
     * 增加预售活动过期校验
     *
     * @param tradeItemGroups
     * @param tradeCommitRequest
     */
    void validateBookingQualification(List<TradeItemGroup> tradeItemGroups, TradeCommitRequest tradeCommitRequest) {
        tradeItemGroups.get(0).getTradeItems().forEach(item -> {
            if (item.getIsBookingSaleGoods()) {
                if (StringUtils.isEmpty(tradeCommitRequest.getTailNoticeMobile()) && item.getBookingType() == BookingType.EARNEST_MONEY) {
                    throw new SbcRuntimeException("K-000009");
                }
                BookingSaleIsInProgressResponse bookingResponse = bookingSaleQueryProvider.isInProgress(BookingSaleIsInProgressRequest.builder().goodsInfoId(item.getSkuId()).build()).getContext();
                if (Objects.isNull(bookingResponse) || Objects.isNull(bookingResponse.getBookingSaleVO())) {
                    throw new SbcRuntimeException("K-600010");
                }
                BookingSaleVO bookingSaleVO = bookingResponse.getBookingSaleVO();
                if (!bookingSaleVO.getId().equals(item.getBookingSaleId())) {
                    throw new SbcRuntimeException("K-000009");
                }
                if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ONE) &&
                        (bookingSaleVO.getHandSelEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getHandSelStartTime().isAfter(LocalDateTime.now()))) {
                    throw new SbcRuntimeException("K-170003");
                }
                if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ZERO) &&
                        (bookingSaleVO.getBookingEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getBookingStartTime().isAfter(LocalDateTime.now()))) {
                    throw new SbcRuntimeException("K-170003");
                }
                tradeCommitRequest.setIsBookingSaleGoods(Boolean.TRUE);
            }
        });
    }

    /**
     * 预约活动校验是否有资格
     *
     */
    void validateAppointmentQualification(AppointmentSaleMergeInProcessResponse response, int purchaseNum, String customerId) {

        if (Objects.isNull(response)){
            return;
        }
        int actualNum = 0;
        if (Objects.nonNull(response) || CollectionUtils.isNotEmpty(response.getAppointmentSaleVOList())) {
            actualNum = response.getAppointmentSaleVOList().size();
        }

        //包含预约中商品, 校验不通过
        if (actualNum > purchaseNum) {
            throw new SbcRuntimeException("K-600017");
        }

        //预约活动失效
        if (purchaseNum > actualNum) {
            throw new SbcRuntimeException("K-600009");
        }

        if (Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getAppointmentSaleVOList())) {
            response.getAppointmentSaleVOList().forEach(a -> {
                if (!(a.getSnapUpStartTime().isBefore(LocalDateTime.now()) && a.getSnapUpEndTime().isAfter(LocalDateTime.now()))) {
                    throw new SbcRuntimeException("K-600009");
                }
                if (a.getAppointmentType().equals(NumberUtils.INTEGER_ONE)) {
                    if(a.getJoinLevel().equals("-3")) {
                        //企业会员
                        CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
                        customerGetByIdRequest.setCustomerId(customerId);
                        CustomerGetByIdResponse customerResponse = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
                        if(!EnterpriseCheckState.CHECKED.equals(customerResponse.getEnterpriseCheckState())) {
                            throw new SbcRuntimeException("K-160003");
                        }
                    } else if(a.getJoinLevel().equals("-2")){
                        //付费会员
                        List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
                                .list(PaidCardCustomerRelListRequest.builder().customerId(customerId).build())
                                .getContext().getPaidCardCustomerRelVOList();
                        if(CollectionUtils.isEmpty(relVOList)) {
                            throw new SbcRuntimeException("K-160003");
                        }
                    } else if (!a.getJoinLevel().equals("-1")) {
                        CustomerLevelByCustomerIdAndStoreIdResponse levelResponse = customerLevelQueryProvider
                                .getCustomerLevelByCustomerIdAndStoreId(CustomerLevelByCustomerIdAndStoreIdRequest.builder().customerId(customerId).storeId(a.getStoreId()).build())
                                .getContext();
                        if (Objects.nonNull(levelResponse) && Objects.nonNull(levelResponse.getLevelId())) {
                            if (!a.getJoinLevel().equals("0") && !Arrays.asList(a.getJoinLevel().split(",")).contains(levelResponse.getLevelId().toString())) {
                                throw new SbcRuntimeException("K-160003");
                            }
                        } else {
                            throw new SbcRuntimeException("K-160004");
                        }
                    }
                } else {
                    AppointmentRecord recordResponse =
                            appointmentRecordService.getAppointmentInfo(AppointmentRecordQueryRequest.builder().
                                    buyerId(customerId)
                                    .goodsInfoId(a.getAppointmentSaleGood().getGoodsInfoId()).appointmentSaleId(a.getId()).build());
                    if (Objects.isNull(recordResponse)) {
                        throw new SbcRuntimeException("K-180001");
                    }
                }
            });
        }
    }

    /**
     * 校验是下单商品是否参加了预约，预售活动，参加了则提示重新下单(积分价商品优先预约、预售活动)
     * 应用场景：普通商品在提交订单之前成为了预约、预售商品，此时给与前端提示
     *
     * @param tradeItemGroups
     */
    void containAppointmentSaleAndBookingSale(List<TradeItemGroup> tradeItemGroups) {

        List<String> skuIds = new ArrayList<>();
        tradeItemGroups.forEach(tradeItemGroup -> {
            skuIds.addAll(tradeItemGroup.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList()));
        });
        GoodsInfoViewByIdsResponse response = tradeCacheService.getGoodsInfoViewByIds(skuIds);
        List<String> needValidSkuIds = new ArrayList<>();
        tradeItemGroups.forEach(tradeItemGroup -> needValidSkuIds.addAll(tradeItemGroup.getTradeItems()
                .stream()
                .filter(i -> Objects.nonNull(i.getIsAppointmentSaleGoods()) && !i.getIsAppointmentSaleGoods()
                        && Objects.nonNull(i.getIsBookingSaleGoods()) && !i.getIsBookingSaleGoods())
                .map(TradeItem::getSkuId).collect(Collectors.toList())));
        //积分价商品不需要校验
        needValidSkuIds.removeAll(response.getGoodsInfos().stream()
                .filter(goodsInfoVO -> Objects.nonNull(goodsInfoVO.getBuyPoint()) && goodsInfoVO.getBuyPoint() > 0)
                .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(needValidSkuIds)) {
            return;
        }
        appointmentSaleQueryProvider.containAppointmentSaleAndBookingSale(AppointmentSaleInProgressRequest.builder().goodsInfoIdList(needValidSkuIds).build());
    }

    /**
     * 校验商品限售信息
     *
     * @param tradeItemGroupVO
     */
    public void validateRestrictedGoods(TradeItemGroup tradeItemGroupVO, CustomerSimplifyOrderCommitVO customer) {
        Boolean openGroup = false;
        if (Objects.nonNull(tradeItemGroupVO.getGrouponForm()) && Objects.nonNull(tradeItemGroupVO.getGrouponForm().getOpenGroupon())) {
            openGroup = tradeItemGroupVO.getGrouponForm().getOpenGroupon();
        }
        Boolean storeBagsFlag = false;
        if (DefaultFlag.YES.equals(tradeItemGroupVO.getStoreBagsFlag())) {
            storeBagsFlag = true;
        }
        //组装请求的数据
        List<TradeItem> tradeItemVOS = tradeItemGroupVO.getTradeItems();
        List<GoodsRestrictedValidateVO> list = KsBeanUtil.convert(tradeItemVOS, GoodsRestrictedValidateVO.class);

        CustomerVO customerVO = KsBeanUtil.convert(customer,CustomerVO.class);
        goodsRestrictedSaleQueryProvider.validateOrderRestricted(GoodsRestrictedBatchValidateRequest.builder()
                .goodsRestrictedValidateVOS(list)
                .snapshotType(tradeItemGroupVO.getSnapshotType())
                .customerVO(customerVO)
                .openGroupon(openGroup)
                .storeBagsFlag(storeBagsFlag)
                .build());
    }

    /**
     *
     * @param skuIds
     * @param customer
     * @return
     */
    public GoodsInfoResponse getGoodsResponse(List<String> skuIds, CustomerSimplifyOrderCommitVO customer) {

        GoodsInfoViewByIdsResponse response = tradeCacheService.getGoodsInfoViewByIds(skuIds);
        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos();
        List<GoodsInfoDTO> goodsInfoDTOList = KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class);
        GoodsIntervalPriceByCustomerIdResponse priceResponse =
                goodsIntervalPriceProvider.putByCustomerId(
                        GoodsIntervalPriceByCustomerIdRequest.builder().goodsInfoDTOList(goodsInfoDTOList)
                                .customerId(customer.getCustomerId()).build()).getContext();
        response.setGoodsInfos(priceResponse.getGoodsInfoVOList());

        //填充实时的校验字段
        this.fillPartCols(response.getGoodsInfos());

        if (StringUtils.isNotBlank(customer.getCustomerId())) {
            //计算会员价
            response.setGoodsInfos(
                    marketingLevelPluginProvider.goodsListFilter(MarketingLevelGoodsListFilterRequest.builder()
                            .goodsInfos(KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class))
                            .customerDTO(KsBeanUtil.convert(customer, CustomerDTO.class)).build())
                            .getContext().getGoodsInfoVOList());
        }

        return GoodsInfoResponse.builder().goodsInfos(response.getGoodsInfos())
                .goodses(response.getGoodses())
                .goodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList())
                .build();

    }

    /**
     * 根据开关重新设置分销商品标识
     * @param goodsInfoList
     */
    public void checkDistributionSwitch(List<GoodsInfoVO> goodsInfoList,ChannelType channelType) {
        //需要叠加访问端Pc\app不体现分销业务
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        goodsInfoList.forEach(goodsInfoVO -> {
            Boolean distributionFlag = Objects.equals(ChannelType.PC_MALL, channelType) || DefaultFlag.NO.equals(openFlag) || DefaultFlag.NO.equals(distributionCacheService.queryStoreOpenFlag(String.valueOf(goodsInfoVO.getStoreId())));
            // 排除积分价商品
            Boolean pointsFlag = !(Objects.isNull(goodsInfoVO.getBuyPoint()) || (goodsInfoVO.getBuyPoint().compareTo(0L) == 0));
            // 排除预售、预约
            Boolean appointmentFlag = Objects.nonNull(goodsInfoVO.getAppointmentSaleVO());
            Boolean bookingFlag = Objects.nonNull(goodsInfoVO.getBookingSaleVO());
            if (distributionFlag||pointsFlag||appointmentFlag||bookingFlag) {
                goodsInfoVO.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
            }
        });
    }

    public Purchase4DistributionResponse distribution(@RequestBody @Valid Purchase4DistributionSimplifyRequest
                                                                            request) {
        DistributeChannel channel = request.getDistributeChannel();
        List<GoodsInfoVO> goodsInfoVOList = request.getGoodsInfos();
        List<GoodsInfoVO> goodsInfoComList = request.getGoodsInfos();
        List<GoodsIntervalPriceVO> goodsIntervalPrices = request.getGoodsIntervalPrices();
        CustomerSimplifyOrderCommitVO customer = request.getCustomer();
        Purchase4DistributionResponse response = Purchase4DistributionResponse.builder().goodsInfos(goodsInfoVOList)
                .goodsInfoComList(goodsInfoComList).build();

        //1.如果为社交分销渠道
        if (null != channel && !Objects.equals(channel.getChannelType(), ChannelType.PC_MALL)) {
            response.setSelfBuying(Boolean.FALSE);
            //分销商品
            List<GoodsInfoVO> goodsInfoDistributionList = goodsInfoVOList.stream().filter(goodsInfo -> Objects
                    .equals(goodsInfo.getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)).collect
                    (Collectors.toList());
            //验证自购
            if ((Objects.equals(channel.getInviteeId(), Constants.PURCHASE_DEFAULT) || Objects.isNull(channel
                    .getInviteeId())) && Objects.nonNull(customer)) {
                DistributionCustomerEnableByCustomerIdResponse customerEnableByCustomerIdResponse =
                        distributionCustomerQueryProvider.checkEnableByCustomerId
                                (DistributionCustomerEnableByCustomerIdRequest.builder().customerId(customer
                                        .getCustomerId()).build()).getContext();
                response.setSelfBuying(customerEnableByCustomerIdResponse.getDistributionEnable() && CollectionUtils
                        .isNotEmpty(goodsInfoDistributionList));
            }
            //排除分销商品
            if (channel.getChannelType() == ChannelType.SHOP) {
                goodsInfoComList = new ArrayList<>();
            } else {
                goodsInfoComList = goodsInfoVOList.stream().filter(goodsInfo -> !Objects.equals(goodsInfo
                        .getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)).collect(Collectors.toList());
            }
            //3.分销商品去除阶梯价等信息
            goodsIntervalPrices = setDistributorPrice(goodsInfoVOList, goodsIntervalPrices);

            //2.如果为店铺精选购买
            purchaseService.verifyDistributorGoodsInfo(channel, goodsInfoVOList);

            //4.分销价叠加分销员等级
            if (Objects.nonNull(customer)) {
                BaseResponse<DistributorLevelByCustomerIdResponse> resultBaseResponse =
                        distributorLevelQueryProvider.getByCustomerId(new DistributorLevelByCustomerIdRequest
                                (customer.getCustomerId()));
                DistributorLevelVO distributorLevelVO = Objects.isNull(resultBaseResponse) ? null :
                        resultBaseResponse.getContext().getDistributorLevelVO();
                if (Objects.nonNull(distributorLevelVO) && Objects.nonNull(distributorLevelVO.getCommissionRate())) {
                    goodsInfoVOList.stream().forEach(goodsInfoVO -> {
                        if (DistributionGoodsAudit.CHECKED.equals(goodsInfoVO.getDistributionGoodsAudit())) {
                            BigDecimal commissionRate = distributorLevelVO.getCommissionRate();
                            BigDecimal distributionCommission = goodsInfoVO.getDistributionCommission();
                            distributionCommission = DistributionCommissionUtils.calDistributionCommission(distributionCommission,commissionRate);
                            goodsInfoVO.setDistributionCommission(distributionCommission);
                        }
                    });
                }
            }
        }
        response.setGoodsInfoComList(goodsInfoComList);
        response.setGoodsInfos(goodsInfoVOList);
        response.setGoodsIntervalPrices(goodsIntervalPrices);
        return response;
    }

    /**
     * 分销商品去除阶梯价等信息
     *
     * @param goodsInfoVOList
     */
    private List<GoodsIntervalPriceVO> setDistributorPrice(List<GoodsInfoVO> goodsInfoVOList,
                                                           List<GoodsIntervalPriceVO> goodsIntervalPrices) {
        //        分销商品
        List<GoodsInfoVO> goodsInfoDistributionList = goodsInfoVOList.stream().filter(goodsInfo -> Objects.equals
                (goodsInfo.getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)).collect(Collectors.toList());
        List<String> skuIdList = goodsInfoDistributionList.stream().map(GoodsInfoVO::getGoodsInfoId).collect
                (Collectors.toList());

        goodsInfoVOList.forEach(goodsInfo -> {
            if (skuIdList.contains(goodsInfo.getGoodsInfoId())) {
                goodsInfo.setIntervalPriceIds(null);
                goodsInfo.setIntervalMinPrice(null);
                goodsInfo.setIntervalMaxPrice(null);
                goodsInfo.setCount(null);
                goodsInfo.setMaxCount(null);
            }
        });
        return goodsIntervalPrices.stream().filter(intervalPrice -> !skuIdList.contains(intervalPrice.getGoodsInfoId
                ())).collect(Collectors.toList());
    }

    protected void validShopGoods(List<GoodsInfoVO> goodsInfoVOS) {
        goodsInfoVOS.stream().forEach(goodsInfo -> {
            if (goodsInfo.getGoodsStatus() == GoodsStatus.INVALID) {
                throw new SbcRuntimeException("K-050117");
            }
        });
    }

    /**
     * 校验活动初始化价格
     *
     * @param tradeItems
     * @return
     */
    public List<TradeItem> fillActivityPrice(List<TradeItem> tradeItems, List<GoodsInfoVO> goodsInfoVOList,String customerId) {
        Map<String, BigDecimal> skuMap =
                goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId,
                        GoodsInfoVO::getMarketPrice));
        return tradeItems.stream().map(item -> {
            if (item.getIsAppointmentSaleGoods()) {
                AppointmentSaleVO appointmentSaleVO =
                        appointmentSaleQueryProvider.getAppointmentSaleRelaInfo(RushToAppointmentSaleGoodsRequest.builder().appointmentSaleId(item.getAppointmentSaleId()).
                                skuId(item.getSkuId()).build()).getContext().getAppointmentSaleVO();
                if (Objects.isNull(appointmentSaleVO)) {
                    throw new SbcRuntimeException("K-170003");
                }
                if (appointmentSaleVO.getSnapUpEndTime().isAfter(LocalDateTime.now()) && appointmentSaleVO.getSnapUpStartTime().isBefore(LocalDateTime.now())) {
                    item.setPrice(Objects.isNull(appointmentSaleVO.getAppointmentSaleGood().getPrice()) ?
                            appointmentSaleVO.getAppointmentSaleGood().getGoodsInfoVO().getMarketPrice()
                            : appointmentSaleVO.getAppointmentSaleGood().getPrice());
                }
                return item;
            }
            if (item.getIsBookingSaleGoods()) {
                BookingSaleIsInProgressResponse bookingResponse =
                        bookingSaleQueryProvider.isInProgress(BookingSaleIsInProgressRequest.builder().goodsInfoId(item.getSkuId()).build()).getContext();
                if (Objects.isNull(bookingResponse) || Objects.isNull(bookingResponse.getBookingSaleVO())) {
                    throw new SbcRuntimeException("K-000009");
                }
                BookingSaleVO bookingSaleVO = bookingResponse.getBookingSaleVO();
                if (!bookingSaleVO.getId().equals(item.getBookingSaleId())) {
                    throw new SbcRuntimeException("K-000009");
                }
                if (bookingSaleVO.getPauseFlag() == 1) {
                    throw new SbcRuntimeException("K-170003");
                }
                if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ONE)) {
                    if (bookingSaleVO.getHandSelEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getHandSelStartTime().isAfter(LocalDateTime.now())) {
                        throw new SbcRuntimeException("K-170003");
                    }
                    item.setPrice(skuMap.get(bookingSaleVO.getBookingSaleGoods().getGoodsInfoId()));
                    item.setBookingType(BookingType.EARNEST_MONEY);
                    BigDecimal handSelPrice = bookingSaleVO.getBookingSaleGoods().getHandSelPrice();
                    BigDecimal inflationPrice = bookingSaleVO.getBookingSaleGoods().getInflationPrice();
                    item.setEarnestPrice(handSelPrice.multiply(BigDecimal.valueOf(item.getNum())));
                    if (Objects.nonNull(inflationPrice)) {
                        item.setSwellPrice(inflationPrice.multiply(BigDecimal.valueOf(item.getNum())));
                    } else {
                        item.setSwellPrice(item.getEarnestPrice());
                    }
                    item.setHandSelStartTime(bookingSaleVO.getHandSelStartTime());
                    item.setHandSelEndTime(bookingSaleVO.getHandSelEndTime());
                    item.setTailStartTime(bookingSaleVO.getTailStartTime());
                    item.setTailEndTime(bookingSaleVO.getTailEndTime());
                }
                if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ZERO)) {
                    if (bookingSaleVO.getBookingEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getBookingStartTime().isAfter(LocalDateTime.now())) {
                        throw new SbcRuntimeException("K-170003");
                    }
                    item.setBookingType(BookingType.FULL_MONEY);
                    item.setPrice(Objects.isNull(bookingSaleVO.getBookingSaleGoods().getBookingPrice()) ?
                            skuMap.get(bookingSaleVO.getBookingSaleGoods().getGoodsInfoId())
                            : bookingSaleVO.getBookingSaleGoods().getBookingPrice());
                }

                if(bookingSaleVO.getJoinLevel().equals("-3")) {
                    //企业会员
                    CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
                    customerGetByIdRequest.setCustomerId(customerId);
                    CustomerGetByIdResponse customerResponse = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
                    if(!EnterpriseCheckState.CHECKED.equals(customerResponse.getEnterpriseCheckState())) {
                        throw new SbcRuntimeException("K-170009");
                    }
                } else if(bookingSaleVO.getJoinLevel().equals("-2")){
                    //付费会员
                    List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
                            .list(PaidCardCustomerRelListRequest.builder().customerId(customerId).build())
                            .getContext().getPaidCardCustomerRelVOList();
                    if(CollectionUtils.isEmpty(relVOList)) {
                        throw new SbcRuntimeException("K-170009");
                    }
                }else if (!bookingSaleVO.getJoinLevel().equals("-1")) {
                    //店铺内客户
                    StoreCustomerRelaListByConditionRequest listByConditionRequest =
                            new StoreCustomerRelaListByConditionRequest();
                    listByConditionRequest.setCustomerId(customerId);
                    listByConditionRequest.setStoreId(bookingSaleVO.getStoreId());
                    List<StoreCustomerRelaVO> relaVOList =
                            storeCustomerQueryProvider.listByCondition(listByConditionRequest).getContext().getRelaVOList();
                    if (Objects.nonNull(relaVOList) && relaVOList.size() > 0) {
                        if (!bookingSaleVO.getJoinLevel().equals("0") && !Arrays.asList(bookingSaleVO.getJoinLevel().split(",")).contains(relaVOList.get(0).getStoreLevelId().toString())) {
                            throw new SbcRuntimeException("K-160003");
                        }
                    } else {
                        throw new SbcRuntimeException("K-160004");
                    }
                }
                return item;
            }
            return item;
        }).collect(Collectors.toList());
    }

    /**
     * 预约活动校验是否有资格
     *
     * @param tradeItemGroups
     */
     void validateAppointmentQualification(List<TradeItemGroup> tradeItemGroups,String customerId) {
        Boolean suitMarketingFlag =
                tradeItemGroups.stream().anyMatch(tradeItemGroupVO -> Objects.nonNull(tradeItemGroupVO.getSuitMarketingFlag()) && tradeItemGroupVO.getSuitMarketingFlag().equals(Boolean.TRUE));
        Boolean isGrouponOrder =
                tradeItemGroups.stream().anyMatch(tradeItemGroupVO -> Objects.nonNull(tradeItemGroupVO.getGrouponForm()) && Objects.nonNull(tradeItemGroupVO.getGrouponForm().getOpenGroupon()));
        if (suitMarketingFlag || isGrouponOrder) {
            return;
        }
        List<String> appointmentSaleSkuIds = new ArrayList<>();
        List<String> allSkuIds = new ArrayList<>();

        tradeItemGroups.forEach(tradeItemGroup -> {
            appointmentSaleSkuIds.addAll(tradeItemGroup.getTradeItems().stream()
                    .filter(i -> Objects.nonNull(i.getIsAppointmentSaleGoods()) && i.getIsAppointmentSaleGoods())
                    .map(TradeItem::getSkuId).collect(Collectors.toList()));
            allSkuIds.addAll(tradeItemGroup.getTradeItems().stream()
                    .filter(i -> Objects.isNull(i.getBuyPoint()) || i.getBuyPoint() == 0)
                    .map(TradeItem::getSkuId).collect(Collectors.toList()));
        });
        if (CollectionUtils.isEmpty(allSkuIds)) {
            return;
        }
        AppointmentSaleInProcessResponse response =
                appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList(
                        AppointmentSaleInProgressRequest.builder().goodsInfoIdList(allSkuIds)
                                .build()).getContext();
        int purchaseNum = appointmentSaleSkuIds.size();
        int actualNum = 0;
        if (Objects.nonNull(response) || CollectionUtils.isNotEmpty(response.getAppointmentSaleVOList())) {
            actualNum = response.getAppointmentSaleVOList().size();
        }

        //包含预约中商品, 校验不通过
        if (actualNum > purchaseNum) {
            throw new SbcRuntimeException("K-600017");
        }

        //预约活动失效
        if (purchaseNum > actualNum) {
            throw new SbcRuntimeException("K-600009");
        }

        if (Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getAppointmentSaleVOList())) {
            response.getAppointmentSaleVOList().forEach(a -> {
                if (!(a.getSnapUpStartTime().isBefore(LocalDateTime.now()) && a.getSnapUpEndTime().isAfter(LocalDateTime.now()))) {
                    throw new SbcRuntimeException("K-600009");
                }
                if (a.getAppointmentType().equals(NumberUtils.INTEGER_ONE)) {
                    // 判断活动是否是全平台客户还是店铺内客户
                    if (!a.getJoinLevel().equals("-1")) {
/*                        StoreCustomerRelaListByConditionRequest listByConditionRequest =
                                new StoreCustomerRelaListByConditionRequest();
                        listByConditionRequest.setCustomerId(customerId);
                        listByConditionRequest.setStoreId(a.getStoreId());
                        List<StoreCustomerRelaVO> relaVOList =
                                storeCustomerQueryProvider.listByCondition(listByConditionRequest).getContext().getRelaVOList();
                        if (Objects.nonNull(relaVOList) && relaVOList.size() > 0) {
                            if (!a.getJoinLevel().equals("0") && !Arrays.asList(a.getJoinLevel().split(",")).contains(relaVOList.get(0).getStoreLevelId().toString())) {
                                throw new SbcRuntimeException("K-160003");
                            }
                        } else {
                            throw new SbcRuntimeException("K-160004");
                        }*/
                        //获取会员在该店铺的等级，自营店铺取平台等级；第三方店铺取店铺等级
                        CustomerLevelByCustomerIdAndStoreIdResponse levelResponse = customerLevelQueryProvider
                                .getCustomerLevelByCustomerIdAndStoreId(CustomerLevelByCustomerIdAndStoreIdRequest.builder().customerId(customerId).storeId(a.getStoreId()).build())
                                .getContext();
                        if (Objects.nonNull(levelResponse) && Objects.nonNull(levelResponse.getLevelId())) {
                            if (!a.getJoinLevel().equals("0") && !Arrays.asList(a.getJoinLevel().split(",")).contains(levelResponse.getLevelId().toString())) {
                                throw new SbcRuntimeException("K-160003");
                            }
                        } else {
                            throw new SbcRuntimeException("K-160004");
                        }
                    }
                } else {
                    AppointmentRecord recordResponse =
                            appointmentRecordService.getAppointmentInfo(AppointmentRecordQueryRequest.builder().
                                    buyerId(customerId)
                                    .goodsInfoId(a.getAppointmentSaleGood().getGoodsInfoId()).appointmentSaleId(a.getId()).build());
                    if (Objects.isNull(recordResponse)) {
                        throw new SbcRuntimeException("K-180001");
                    }
                }
            });
        }
    }

    /**
     * 填充实时的部分字段，上下架状态、删除状态、可售性、审核状态
     * @param skuList
     */
    public void fillPartCols(List<GoodsInfoVO> skuList) {
        List<GoodsInfoVO> skus = goodsInfoQueryProvider.listPartColsByIds(GoodsInfoPartColsByIdsRequest.builder()
                .goodsInfoIds(skuList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList())).build()).getContext().getGoodsInfos();
        if (CollectionUtils.isNotEmpty(skus)) {
            Map<String, GoodsInfoVO> skuMap = skus.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, i -> i));
            skuList.stream().filter(i -> skuMap.containsKey(i.getGoodsInfoId()))
                    .forEach(i -> {
                        GoodsInfoVO vo = skuMap.get(i.getGoodsInfoId());
                        i.setAddedFlag(vo.getAddedFlag());
                        i.setDelFlag(vo.getDelFlag());
                        i.setVendibility(vo.getVendibility());
                        i.setAuditStatus(vo.getAuditStatus());
                        i.setMarketPrice(vo.getMarketPrice());
                        i.setSupplyPrice(vo.getSupplyPrice());
                        i.setBuyPoint(vo.getBuyPoint());
                        i.setGoodsChannelTypeSet(vo.getGoodsChannelTypeSet());
                    });
        }
    }
}
