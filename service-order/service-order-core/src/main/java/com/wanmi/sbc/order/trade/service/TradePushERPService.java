package com.wanmi.sbc.order.trade.service;

import com.mongodb.client.result.UpdateResult;
import com.sbc.wanmi.erp.bean.dto.ERPTradeItemDTO;
import com.sbc.wanmi.erp.bean.dto.ERPTradePaymentDTO;
import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import com.sbc.wanmi.erp.bean.enums.ERPTradePayChannel;
import com.sbc.wanmi.erp.bean.enums.ERPTradePushStatus;
import com.sbc.wanmi.erp.bean.vo.DeliveryInfoVO;
import com.sbc.wanmi.erp.bean.vo.DeliveryItemVO;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.erp.api.request.PushTradeRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByIdAndSkuIdsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdAndSkuIdsResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeStatusSyncRequest;
import com.wanmi.sbc.order.bean.enums.*;
import com.wanmi.sbc.order.bean.vo.LogisticsVO;
import com.wanmi.sbc.order.bean.vo.ShippingItemVO;
import com.wanmi.sbc.order.bean.vo.TradeDeliverVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.logistics.model.root.LogisticsLog;
import com.wanmi.sbc.order.logistics.service.LogisticsLogService;
import com.wanmi.sbc.order.mq.ProviderTradeOrderService;
import com.wanmi.sbc.order.orderinvoice.request.OrderInvoiceModifyOrderStatusRequest;
import com.wanmi.sbc.order.orderinvoice.service.OrderInvoiceService;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.*;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import com.wanmi.sbc.setting.api.provider.erplogisticsmapping.ErpLogisticsMappingQueryProvider;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.erplogisticsmapping.ErpLogisticsMappingByErpLogisticsCodeRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressListResponse;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import com.wanmi.sbc.setting.bean.vo.ErpLogisticsMappingVO;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import io.seata.common.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: sbc-background
 * @description: 订单推送ERP服务
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-30 14:27
 **/
@Service
@Slf4j
public class TradePushERPService {

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private GuanyierpProvider guanyierpProvider;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private LogisticsLogService logisticsLogService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;


    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private ErpLogisticsMappingQueryProvider erpLogisticsMappingQueryProvider;

    @Autowired
    private TradeRepository tradeRepository;


    @Autowired
    private OrderInvoiceService orderInvoiceService;


    @Value("${default.providerId}")
    private Long defaultProviderId;


    @Autowired
    private ProviderTradeOrderService providerTradeOrderService;
    /**
     * 查询订单
     *
     * @param tid
     */
    public Trade detail(String tid) {
        return tradeRepository.findById(tid).orElse(null);
    }

    /**
     * 推送订单到erp系统
     *
     * @param providerTrade
     * @return
     */
    public BaseResponse pushOrderToERP(ProviderTrade providerTrade) {
        try {
            Optional<PushTradeRequest> erpPushTradeRequest = this.buildERPOrder(providerTrade, null, false);
            if (!erpPushTradeRequest.isPresent()) {
                log.info("erp订单推送参数组装失败:{}", providerTrade.getId());
                return BaseResponse.FAILED();
            }
            BaseResponse baseResponse = this.differentiatedDelivery(providerTrade,erpPushTradeRequest.get());
            if(!Objects.equals(providerTrade.getSupplier().getStoreId(),defaultProviderId)){
                return BaseResponse.SUCCESSFUL();
            }
            if (baseResponse.getCode().equals(CommonErrorCode.FAILED)) {
                //推送订单失败,更新订单推送状态
                this.updateTradeInfo(providerTrade.getId(), true, false, providerTrade.getTradeState().getPushCount() + 1, baseResponse.getMessage(), LocalDateTime.now(),"");
            } else {
                this.updateTradeInfo(providerTrade.getId(), true, true, providerTrade.getTradeState().getPushCount() + 1, baseResponse.getMessage(), LocalDateTime.now(),"");
                return BaseResponse.SUCCESSFUL();
            }
        } catch (Exception e) {
            log.error("推送订单{}发生异常", providerTrade.getId(), e);
            //推送订单失败,更新订单推送状态
            this.updateTradeInfo(providerTrade.getId(), true, false, providerTrade.getTradeState().getPushCount() + 1,
                    "订单推送erp出现异常",
                    LocalDateTime.now(),"");
        }
        return BaseResponse.FAILED();
    }


    /**
     * 推送订单到erp系统--区分已发货和未发货接口的调用
     *
     * @param providerTrade
     * @return
     */
    public BaseResponse differentiatedDelivery(ProviderTrade providerTrade, PushTradeRequest request) {

        Trade parentTrade = detail(providerTrade.getParentId());

        List<TradeItem> totalTradeItems=Stream.of( providerTrade.getTradeItems(), providerTrade.getGifts()).flatMap(Collection::stream).collect(Collectors.toList());

        //判断有没有实体商品，如果没有直接调用已发货已发货
        int size = totalTradeItems.stream().filter(tradeItem -> tradeItem.getGoodsType()==GoodsType.REAL_GOODS).collect(Collectors.toList()).size();

        BaseResponse baseResponse=null;
        //根据供应商发货,非原erp发货则发消息
        if(!Objects.equals(providerTrade.getSupplier().getStoreId(),defaultProviderId)){
            providerTradeOrderService.sendMQForProviderTrade(request);
            return BaseResponse.success("推送mq消息成功");
        }
        if (parentTrade.getCycleBuyFlag()) {
            log.info("=======周期购订单已发货接口========",providerTrade);
            baseResponse = guanyierpProvider.autoPushTradeDelivered(request);
        }else {
            //普通订单如果没有实物商品调用已发货接口
            if (size==0) {
                log.info("=======已发货接口========",providerTrade);
                baseResponse = guanyierpProvider.autoPushTradeDelivered(request);
            } else {
                log.info("=======待发货接口========",providerTrade);
                baseResponse = guanyierpProvider.autoPushTrade(request);
            }
        }
        return baseResponse;
    }


    /**
     * 构建接口调用参数
     *
     * @param trade
     * @return
     */
    private Optional<PushTradeRequest>     buildERPOrder(ProviderTrade trade, Integer cycleNum, boolean isFirstCycle) {

        //查询主单信息
        Trade parentTrade = detail(trade.getParentId());
        trade.setPayWay(parentTrade.getPayWay());
        trade.getTradeState().setPayTime(parentTrade.getTradeState().getPayTime());

        //订单主商品
        List<TradeItem> tradeItems = trade.getTradeItems();
        //订单赠品
        List<TradeItem> gifts = trade.getGifts();

        if (CollectionUtils.isEmpty(tradeItems)) {
            log.error("#ERP订单信息组装失败{},没有商品", trade.getId());
            return Optional.empty();
        }
        //订单购买用户
        Buyer buyer = trade.getBuyer();
        //订单状态信息
        TradeState tradeState = trade.getTradeState();

        //收货人信息
        Consignee consignee = trade.getConsignee();
        //商家信息
        Supplier supplier = trade.getSupplier();

        List<ERPTradeItemDTO> erpTradeItemDTOS;
        //判断是否是周期购订单，
        if (trade.getCycleBuyFlag()) {
            //组装订单商品明细,将赠品合并到主商品集合推送到ERP
            //判断是否是第一期
            if (Objects.nonNull(isFirstCycle) && isFirstCycle) {
                List<TradeItem> totalTradeItemList =
                        Stream.of(tradeItems, gifts).flatMap(Collection::stream).collect(Collectors.toList());
                erpTradeItemDTOS = this.buildTradeItems(totalTradeItemList, Objects.nonNull(trade.getYzTid()),cycleNum,trade);
            } else {
                List<TradeItem> totalTradeItemList =
                        Stream.of(tradeItems, gifts).flatMap(Collection::stream).collect(Collectors.toList());
                log.info("==========已发货接口推送:{}============",totalTradeItemList);
                erpTradeItemDTOS = this.buildTradeItems(totalTradeItemList,Objects.nonNull(trade.getYzTid()),cycleNum,trade);
            }

        } else {
            //组装订单商品明细,将赠品合并到主商品集合推送到ERP
            List<TradeItem> totalTradeItemList =
                    Stream.of(tradeItems, gifts).flatMap(Collection::stream).collect(Collectors.toList());
            erpTradeItemDTOS = this.buildTradeItems(totalTradeItemList, null,cycleNum,trade);
        }

        //组装订单支付对象
        List<ERPTradePaymentDTO> erpTradePaymentDTOList = this.assemblyOrderAmount(trade,cycleNum);


        Map<Enum, String> addrMap = new HashMap<>();
        //排除周期购有赞老订单
        if (trade.getParentId().startsWith(GeneratorService._PREFIX_YOUZAN_TRADE_ID)) {
            //todo 增加地址省市区信息字段
            addrMap.put(AddrLevel.PROVINCE, trade.getConsignee().getProvinceName());
            addrMap.put(AddrLevel.CITY, trade.getConsignee().getCityName());
            addrMap.put(AddrLevel.DISTRICT, trade.getConsignee().getAreaName());
        } else {
            //提取平台地址数据
            PlatformAddressListRequest platformAddressListRequest =
                    PlatformAddressListRequest.builder().addrIdList(Arrays.asList(consignee.getProvinceId().toString(),
                            consignee.getCityId().toString()
                            , consignee.getAreaId().toString())).build();
            //不填充叶子节点
            platformAddressListRequest.setLeafFlag(false);
            BaseResponse<PlatformAddressListResponse> platformAddressListResponseBaseResponse = platformAddressQueryProvider.list(platformAddressListRequest);
            List<PlatformAddressVO> platformAddressVOList =
                    platformAddressListResponseBaseResponse.getContext().getPlatformAddressVOList();
            platformAddressVOList.stream().forEach(platformAddressVO -> {
                addrMap.put(platformAddressVO.getAddrLevel(), platformAddressVO.getAddrName());
            });
        }

        //tips: 商城供应商店铺编号一定需要和erp系统店铺code一致,否则接口调用失败
        PushTradeRequest pushTradeRequest = PushTradeRequest.builder()
                .shopCode(supplier.getSupplierCode())//店铺代码
                .vipCode(buyer.getAccount())//会员代码
                .dealDatetime(DateUtil.format(tradeState.getCreateTime(), DateUtil.FMT_TIME_1))//下单时间
                .platformCode(trade.getCycleBuyFlag() && Objects.nonNull(cycleNum) ? trade.getId() + "-" + cycleNum :
                        trade.getId())//订单号
                .receiverPhone(Objects.nonNull(trade.getDirectChargeMobile()) ? trade.getDirectChargeMobile() : null)//虚拟商品直充手机号
                .details(erpTradeItemDTOS)
                .payments(erpTradePaymentDTOList)
                .receiverName(consignee.getName())
                .receiverMobile(consignee.getPhone())
                .receiverProvince(addrMap.get(AddrLevel.PROVINCE))
                .receiverCity(addrMap.get(AddrLevel.CITY))
                .receiverDistrict(addrMap.get(AddrLevel.DISTRICT))
                .receiverAddress(consignee.getDetailAddress())
                .sellerMemo((trade.getCycleBuyFlag() && Objects.isNull(cycleNum)) ? "周期购订单" : null)
                .buyerMemo(Objects.nonNull(trade.getBuyerRemark()) ? trade.getBuyerRemark() : null)
                .build();
        return Optional.of(pushTradeRequest);
    }



    /**
     * 组装推送erp相关的金额业务
     * @param trade
     * @return
     */
    private List<ERPTradePaymentDTO> assemblyOrderAmount(ProviderTrade trade, Integer cycleNum) {

        //订单购买用户
        Buyer buyer = trade.getBuyer();
        //订单价格信息
        TradePrice tradePrice = trade.getTradePrice();
        //订单状态信息
        TradeState tradeState = trade.getTradeState();

        //积分价格
        BigDecimal pointsPrice=tradePrice.getPointsPrice();
        //现金价格
        BigDecimal totalPrice=tradePrice.getTotalPrice();

        log.info("========周期购平摊计算======pointsPrice:{},totalPrice:{},cycleNum:{}",pointsPrice,totalPrice,cycleNum);

        if (Objects.nonNull(cycleNum)) {
            if (Objects.nonNull(pointsPrice)) {
                pointsPrice=pointsPrice.divide(BigDecimal.valueOf(trade.getTradeCycleBuyInfo().getCycleNum()), 2, BigDecimal.ROUND_HALF_UP);
                log.info("========周期购平摊计算=======pointsPrice===={}",pointsPrice);
            }
            if (Objects.nonNull(totalPrice)) {
                totalPrice=totalPrice.divide(BigDecimal.valueOf(trade.getTradeCycleBuyInfo().getCycleNum()), 2, BigDecimal.ROUND_HALF_UP);
                log.info("========周期购平摊计算======totalPrice====={}",totalPrice);
            }
         }
        List<ERPTradePaymentDTO> erpTradePaymentDTOList = new ArrayList<>();

        if (Objects.nonNull(tradePrice.getPointsPrice()) && tradePrice.getPointsPrice().compareTo(BigDecimal.ZERO) != 0 && tradePrice.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
                   ERPTradePaymentDTO erpTradePaymentPointDTO = ERPTradePaymentDTO.builder()
                        .account(buyer.getAccount())
                        .paytime(Objects.nonNull(tradeState.getPayTime()) ? tradeState.getPayTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() : null)
                        .payment(String.valueOf(pointsPrice))//积分兑换金额
                        .payTypeCode(ERPTradePayChannel.JFZF.getStateId())
                        .build();
                erpTradePaymentDTOList.add(erpTradePaymentPointDTO);
            } else if (Objects.nonNull(tradePrice.getPointsPrice()) && tradePrice.getPointsPrice().compareTo(BigDecimal.ZERO) != 0 && tradePrice.getTotalPrice().compareTo(BigDecimal.ZERO) != 0) {
                //积分支付
                ERPTradePaymentDTO erpTradePaymentPointDTO = ERPTradePaymentDTO.builder()
                        .account(buyer.getAccount())
                        .paytime(Objects.nonNull(tradeState.getPayTime()) ? tradeState.getPayTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() : null)
                        .payment(String.valueOf(pointsPrice))//积分兑换金额
                        .payTypeCode(ERPTradePayChannel.JFZF.getStateId())
                        .build();
                //现金支付
                ERPTradePaymentDTO erpTradePaymentDTO = ERPTradePaymentDTO.builder()
                        .account(buyer.getAccount())
                        .paytime(Objects.nonNull(tradeState.getPayTime()) ? tradeState.getPayTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() : null)
                        .payment(String.valueOf(totalPrice))
                        .build();
                if (!Objects.isNull(trade.getPayWay())) {
                    switch (trade.getPayWay()) {
                        case WECHAT:
                            erpTradePaymentDTO.setPayTypeCode(ERPTradePayChannel.weixin.getStateId());
                            break;
                        case ALIPAY:
                            erpTradePaymentDTO.setPayTypeCode(ERPTradePayChannel.aliPay.getStateId());
                            break;
                        default:
                            erpTradePaymentDTO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
                            break;
                    }
                }
                erpTradePaymentDTOList.add(erpTradePaymentDTO);
                erpTradePaymentDTOList.add(erpTradePaymentPointDTO);
            } else {
                //现金
                ERPTradePaymentDTO erpTradePaymentDTO = ERPTradePaymentDTO.builder()
                        .account(buyer.getAccount())
                        .paytime(Objects.nonNull(tradeState.getPayTime()) ? tradeState.getPayTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() : null)
                        .payment(String.valueOf(totalPrice))
                        .build();
                if (!Objects.isNull(trade.getPayWay())) {
                    switch (trade.getPayWay()) {
                        case WECHAT:
                            erpTradePaymentDTO.setPayTypeCode(ERPTradePayChannel.weixin.getStateId());
                            break;
                        case ALIPAY:
                            erpTradePaymentDTO.setPayTypeCode(ERPTradePayChannel.aliPay.getStateId());
                            break;
                        default:
                            erpTradePaymentDTO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
                            break;
                    }
                }
                erpTradePaymentDTOList.add(erpTradePaymentDTO);
            }
        return erpTradePaymentDTOList;
    }


    /**
     * 构建订单商品列表
     *
     * @param tradeItems
     * @return
     */
    private List<ERPTradeItemDTO> buildTradeItems(List<TradeItem> tradeItems,Boolean yzTrade, Integer cycleNum,ProviderTrade trade) {
        List<ERPTradeItemDTO> items = new ArrayList<>(tradeItems.size());

            BigDecimal originPrice=trade.getTradePrice().getOriginPrice();

            //计算商品的个数
            Long num=trade.getTradeItems().stream().mapToLong(TradeItem::getNum).sum();

            //订单总价格，周期购推送的订单需要按照期数做平摊
            BigDecimal singlePrice = originPrice.divide(BigDecimal.valueOf(num), 2, BigDecimal.ROUND_HALF_UP);
            if (Objects.nonNull(cycleNum)) {
                singlePrice = singlePrice.divide(BigDecimal.valueOf(trade.getTradeCycleBuyInfo().getCycleNum()), 2, BigDecimal.ROUND_HALF_UP);
            }

            List<String> skuIds= trade.getGifts().stream().map(TradeItem::getSkuId).collect(Collectors.toList());

            for (TradeItem tradeItem:tradeItems){
                if (Objects.nonNull(yzTrade) && yzTrade) {
                    GoodsViewByIdAndSkuIdsRequest goodsViewByIdAndSkuIdsRequest = new GoodsViewByIdAndSkuIdsRequest();
                    goodsViewByIdAndSkuIdsRequest.setGoodsId(tradeItem.getSpuId());
                    goodsViewByIdAndSkuIdsRequest.setSkuIds(Arrays.asList(tradeItem.getSkuId()));
                    GoodsViewByIdAndSkuIdsResponse goodsViewByIdAndSkuIdsResponse = goodsQueryProvider.getViewByIdAndSkuIds(goodsViewByIdAndSkuIdsRequest).getContext();
                    List<GoodsInfoVO> goodsInfos = goodsViewByIdAndSkuIdsResponse.getGoodsInfos();
                    for (GoodsInfoVO goodsInfoVO:goodsInfos){
                        if (Objects.equals(tradeItem.getSkuId(), goodsInfoVO.getGoodsInfoId())) {
                            ERPTradeItemDTO erpTradeItemDTO = ERPTradeItemDTO.builder()
                                    .itemCode(goodsInfoVO.getErpGoodsNo())
                                    .skuCode(Objects.nonNull(goodsInfoVO.getErpGoodsInfoNo()) ? goodsInfoVO.getErpGoodsInfoNo() : null)
                                    .price((CollectionUtils.isNotEmpty(skuIds) && skuIds.contains(tradeItem.getSkuId())) ? "0": singlePrice.toString())
                                    .costPrice(tradeItem.getCostPrice())
                                    .qty(tradeItem.getNum().intValue())
                                    .refund(0)
                                    .oid(tradeItem.getOid())
                                    .build();
                            items.add(erpTradeItemDTO);
                        }
                    }

                } else {
                    //订单总价格，周期购推送的订单需要按照期数做平摊
                    ERPTradeItemDTO erpTradeItemDTO = ERPTradeItemDTO.builder()
                            .itemCode(tradeItem.getErpSpuNo())
                            .skuCode(Objects.nonNull(tradeItem.getErpSkuNo()) ? tradeItem.getErpSkuNo() : null)
                            .price(skuIds.contains(tradeItem.getSkuId()) ? "0": singlePrice.toString())
                            .costPrice(tradeItem.getCostPrice())
                            .qty(tradeItem.getNum().intValue())
                            .refund(0)
                            .oid(tradeItem.getOid())
                            .build();
                    log.info("============TradeItem:{}===============",tradeItem);
                    items.add(erpTradeItemDTO);
                }
            }
        return items;
    }


    /**
     * 根据
     *
     * @param tid
     * @param isPush
     * @param pushCount
     * @param response
     * @param pushTime
     * @return
     */
    public boolean updateTradeInfo(String tid, boolean isPush, boolean pushStatus, int pushCount, String response,
                                   LocalDateTime pushTime,String deliveryOrderId) {
        log.info("updateTradeInfo===============>tid:{},isPush:{},pushStatus:{},pushCount:{},response:{}",
                tid, isPush, pushStatus, pushCount, response);
        Update update = new Update();
        if (isPush) {
            if (pushStatus) {
                update.set("tradeState.erpTradeState", ERPTradePushStatus.PUSHED_SUCCESS.toValue());
                update.set("deliveryOrderId",deliveryOrderId);
            } else {
                update.set("tradeState.erpTradeState", ERPTradePushStatus.PUSHED_FAIL.toValue());
            }
        }
        if (pushTime == null) {
            pushTime = LocalDateTime.now();
        }
        update.set("tradeState.pushTime", pushTime);

        if (pushCount >= 0) {
            update.set("tradeState.pushCount", pushCount);
        }
        update.set("tradeState.pushResponse", response);
        UpdateResult result = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(tid)), update, ProviderTrade.class);
        log.info("UpdateResult===============>:{}", result);
        return result.getModifiedCount() >= 0;
    }


    /**
     * 推送周期购订单到erp系统
     *
     * @param providerTrade
     * @return
     */
    public BaseResponse pushCycleOrderToERP(ProviderTrade providerTrade, DeliverCalendar pushDeliverCalendar, Integer cycleNum, boolean isFirstCycle) {
        Optional<PushTradeRequest> erpPushTradeRequest = this.buildERPOrder(providerTrade, cycleNum, isFirstCycle);
        if (!erpPushTradeRequest.isPresent()) {
            log.info("erp订单推送参数组装失败:{}", providerTrade.getId());
            return BaseResponse.FAILED();
        }
        try {
            BaseResponse baseResponse = guanyierpProvider.autoPushTrade(erpPushTradeRequest.get());
            if (baseResponse.getCode().equals(CommonErrorCode.FAILED)) {
                this.updateCycleTradeInfo(providerTrade, false, pushDeliverCalendar, cycleNum);
            } else {
                this.updateCycleTradeInfo(providerTrade, true, pushDeliverCalendar, cycleNum);
                return BaseResponse.SUCCESSFUL();
            }
        } catch (Exception e) {
            log.error("推送订单{}发生异常", providerTrade.getId(), e);
            //推送订单失败,更新订单推送状态
            this.updateCycleTradeInfo(providerTrade, false, pushDeliverCalendar, cycleNum);

        }
        return BaseResponse.FAILED();
    }


    /**
     * 修改周期购订单信息
     *
     * @param providerTrade
     * @param isPush              推送状态 true:推送成功，false：推送失败
     * @param pushDeliverCalendar
     */
    public void updateCycleTradeInfo(ProviderTrade providerTrade, boolean isPush, DeliverCalendar pushDeliverCalendar, Integer cycleNum) {
        //周期购订单更新父订单、子订单发货日历
        Trade trade = tradeService.detail(providerTrade.getParentId());
        log.info("=============修改主单之前的之前的信息：{}，修改子单之前的信息：{}==============", trade, providerTrade);
        TradeCycleBuyInfo proviDertradeCycleBuyInfo = providerTrade.getTradeCycleBuyInfo();
        TradeCycleBuyInfo tradeCycleBuyInfo = trade.getTradeCycleBuyInfo();
        if (isPush) {
            proviDertradeCycleBuyInfo.getDeliverCalendar().forEach(deliverCalendar -> {
                if (deliverCalendar.getDeliverDate().isEqual(pushDeliverCalendar.getDeliverDate())) {
                    deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.PUSHED);
                    deliverCalendar.setErpTradeCode(providerTrade.getId() + "-" + cycleNum);
                }
            });
            //更新主订单的发货日历状态
            tradeCycleBuyInfo.getDeliverCalendar().forEach(deliverCalendar -> {
                if (deliverCalendar.getDeliverDate().isEqual(pushDeliverCalendar.getDeliverDate())) {
                    deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.PUSHED);
                    deliverCalendar.setErpTradeCode(providerTrade.getId() + "-" + cycleNum);
                }
            });
        } else {
            proviDertradeCycleBuyInfo.getDeliverCalendar().forEach(deliverCalendar -> {
                if (deliverCalendar.getDeliverDate().isEqual(pushDeliverCalendar.getDeliverDate())) {
                    if (deliverCalendar.getPushCount() > 2) {
                        deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.PUSHED_FAIL);
                    } else {
                        deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);
                    }
                    deliverCalendar.setErpTradeCode(providerTrade.getId() + "-" + cycleNum);
                    deliverCalendar.setPushCount(deliverCalendar.getPushCount() + 1);
                }
            });
            //更新主订单的发货日历状态
            tradeCycleBuyInfo.getDeliverCalendar().forEach(deliverCalendar -> {
                if (deliverCalendar.getDeliverDate().isEqual(pushDeliverCalendar.getDeliverDate())) {
                    if (deliverCalendar.getPushCount() > 2) {
                        deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.PUSHED_FAIL);
                    } else {
                        deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);
                    }
                    deliverCalendar.setErpTradeCode(providerTrade.getId() + "-" + cycleNum);
                    deliverCalendar.setPushCount(deliverCalendar.getPushCount() + 1);
                }
            });
        }
        //更新主订单的发货日历状态
        tradeService.updateTrade(trade);

        providerTradeService.updateProviderTrade(providerTrade);

        log.info("=============修改后主单信息：{}，修改后子单信息：{}==============", trade, providerTrade);
    }

    /**
     * 更新订单发货状态
     *
     * @param providerTrade
     */
    public void syncDeliveryStatus(ProviderTrade providerTrade,List<DeliveryInfoVO> deliveryInfoVOList) {
        if(!Objects.equals(providerTrade.getSupplier().getStoreId(),defaultProviderId) && CollectionUtils.isEmpty(deliveryInfoVOList)){
            this.syncDeliveryStatusProduct(providerTrade);
            return;
        }
        try {
            //周期购订单和普通订单分开处理
            if (providerTrade.getCycleBuyFlag()) {
                this.updateCycleBuyDeliveryStatus(providerTrade,deliveryInfoVOList);
            } else {
                DeliveryQueryRequest deliveryQueryRequest = new DeliveryQueryRequest();
                deliveryQueryRequest.setTid(providerTrade.getId());
                //设置查询erp发货状态
                deliveryQueryRequest.setDelivery(Constants.yes);
                // 是否是历史订单
                if (CollectionUtils.isEmpty(deliveryInfoVOList)){
                    deliveryInfoVOList = guanyierpProvider
                            .getDeliveryStatus(deliveryQueryRequest).getContext().getDeliveryInfoVOList();
                }
                if (CollectionUtils.isNotEmpty(deliveryInfoVOList)) {
                    this.fillERPTradeDelivers(providerTrade, deliveryInfoVOList);
                } else {
                    // 扫描次数小于3的加1
                    if (!ObjectUtils.isEmpty(providerTrade.getTradeState())) {
                        TradeState tradeState = providerTrade.getTradeState();
                        if (tradeState.getScanCount() < ScanCount.COUNT_THREE.toValue()) {
                            tradeState.setScanCount(tradeState.getScanCount() + ScanCount.COUNT_ONE.toValue());
                        }
                    }
                    providerTradeService.updateProviderTrade(providerTrade);

                }
            }
        } catch (Exception e) {
            log.error("#批量同步发货状态异常:{}", e);
        }

    }

    /**
     * 非erp发货状态更新，消息
     * @param providerTrade
     */
    public void syncDeliveryStatusProduct(ProviderTrade providerTrade){
        try {
            //周期购订单和普通订单分开处理
            if (providerTrade.getCycleBuyFlag()) {
                //todo 周期购同步
                this.sendMQForCycleDeliveryStatus(providerTrade);
            } else {
                DeliveryQueryRequest deliveryQueryRequest = new DeliveryQueryRequest();
                deliveryQueryRequest.setTid(providerTrade.getId());
                providerTradeOrderService.sendMQForDeliveryStatus(deliveryQueryRequest);
            }
        } catch (Exception e) {
            log.error("#批量同步发货状态异常:{}", e);
        }

    }

    private void sendMQForCycleDeliveryStatus(ProviderTrade providerTrade){
        List<DeliverCalendar> deliverCalendars = providerTrade.getTradeCycleBuyInfo().getDeliverCalendar();
        if (CollectionUtils.isNotEmpty(deliverCalendars)) {
            //筛选已推送的发货日历
            List<DeliverCalendar> deliveryList =
                    deliverCalendars.stream()
                            .filter(deliverCalendar ->
                                    deliverCalendar.getCycleDeliverStatus().equals(CycleDeliverStatus.PUSHED) && Objects.nonNull(deliverCalendar.getErpTradeCode()))
                            .collect(Collectors.toList());
            deliveryList.forEach(deliverCalendar -> {
                DeliveryQueryRequest deliveryQueryRequest = new DeliveryQueryRequest();
                deliveryQueryRequest.setTid(providerTrade.getId());
                providerTradeOrderService.sendMQForDeliveryStatus(deliveryQueryRequest);
            });
        }

    }



    /**
     * 周期购订单每一期发货
     *
     * @param providerTrade
     */
    public void updateCycleBuyDeliveryStatus(ProviderTrade providerTrade,List<DeliveryInfoVO> deliveryInfoVOListVo) {
        //周期购订单更新父订单、子订单发货日历
        Trade trade = tradeService.detail(providerTrade.getParentId());
        List<DeliverCalendar> deliverCalendars = providerTrade.getTradeCycleBuyInfo().getDeliverCalendar();
        //总期数
        Integer cycleNum = providerTrade.getTradeCycleBuyInfo().getCycleNum();
        if (CollectionUtils.isNotEmpty(deliverCalendars)) {
            //筛选已推送的发货日历
            List<DeliverCalendar> deliveryList =
                    deliverCalendars.stream()
                            .filter(deliverCalendar ->
                                    deliverCalendar.getCycleDeliverStatus().equals(CycleDeliverStatus.PUSHED) && Objects.nonNull(deliverCalendar.getErpTradeCode()))
                            .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(deliveryList)) {
                // 扫描次数小于3的加1
                if (!ObjectUtils.isEmpty(providerTrade.getTradeState())) {
                    TradeState tradeState = providerTrade.getTradeState();
                    if (tradeState.getScanCount() < ScanCount.COUNT_THREE.toValue()) {
                        tradeState.setScanCount(tradeState.getScanCount() + ScanCount.COUNT_ONE.toValue());
                    }
                }

                providerTradeService.updateProviderTrade(providerTrade);
            }

            deliveryList.forEach(deliverCalendar -> {
                DeliveryQueryRequest deliveryQueryRequest = new DeliveryQueryRequest();
                deliveryQueryRequest.setTid(deliverCalendar.getErpTradeCode());
                // 是否是历史订单
                List<DeliveryInfoVO> deliveryInfoVOList = new ArrayList<>();
                if (CollectionUtils.isEmpty(deliveryInfoVOListVo)){
                    deliveryInfoVOList = guanyierpProvider
                            .getDeliveryStatus(deliveryQueryRequest).getContext().getDeliveryInfoVOList();
                }else {
                    deliveryInfoVOList = deliveryInfoVOListVo;
                }
                if (CollectionUtils.isNotEmpty(deliveryInfoVOList)) {
                    //计算erp发货单已发的商品个数
                    for (DeliveryInfoVO deliveryInfoVO : deliveryInfoVOList) {
                        if (deliveryInfoVO.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)) {
                            //获取发货的期数
                            String cycle = deliverCalendar.getErpTradeCode().substring(deliverCalendar.getErpTradeCode().length() - 1);
                            log.info("=================周期购订单修改发货状态，查询erp发货单==========={}=========", deliveryInfoVO);
                            if (CollectionUtils.isEmpty(providerTrade.getTradeDelivers())) {
                                if (Objects.equals(cycleNum.toString(), cycle)) {

                                    List<DeliverCalendar> calendar = providerTrade.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliverCalendar1 -> Objects.nonNull(deliverCalendar1.getErpTradeCode()) && deliverCalendar1.getErpTradeCode().equals(deliverCalendar.getErpTradeCode())).collect(Collectors.toList());
                                    calendar.forEach(calendar1 -> {
                                        calendar1.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                                    });
                                    providerTrade.getTradeState().setFlowState(FlowState.DELIVERED);
                                    providerTrade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);


                                    List<DeliverCalendar> tradeCalendar = trade.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliver -> Objects.nonNull(deliver.getErpTradeCode()) && deliver.getErpTradeCode().equals(deliverCalendar.getErpTradeCode())).collect(Collectors.toList());
                                    tradeCalendar.forEach(calendar1 -> {
                                        calendar1.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                                    });

                                    trade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
                                    trade.getTradeState().setFlowState(FlowState.DELIVERED);


                                    //设置发货时间
                                    log.info("=========TradeDelivers:{}，platformCode:{}======",deliveryInfoVO.getDeliverTime(),deliveryInfoVO.getPlatformCode());
                                    providerTrade.getTradeState().setDeliverTime(deliveryInfoVO.getDeliverTime());
                                    trade.getTradeState().setDeliverTime(deliveryInfoVO.getDeliverTime());

                                    //组装发货记录等信息
                                    this.setCycleTradeDelivers(providerTrade, KsBeanUtil.convert(trade, TradeVO.class), deliveryInfoVO, Integer.valueOf(cycle));
                                } else {
                                    List<DeliverCalendar> calendar = providerTrade.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliverCalendar1 -> Objects.nonNull(deliverCalendar1.getErpTradeCode()) && deliverCalendar1.getErpTradeCode().equals(deliverCalendar.getErpTradeCode())).collect(Collectors.toList());
                                    calendar.forEach(calendar1 -> {
                                        calendar1.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                                    });
                                    providerTrade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
                                    providerTrade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);

                                    List<DeliverCalendar> tradeCalendar = trade.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliver -> Objects.nonNull(deliver.getErpTradeCode()) && deliver.getErpTradeCode().equals(deliverCalendar.getErpTradeCode())).collect(Collectors.toList());
                                    tradeCalendar.forEach(calendar1 -> {
                                        calendar1.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                                    });

                                    trade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
                                    trade.getTradeState().setFlowState(FlowState.DELIVERED_PART);

                                    //组装发货记录等信息
                                    this.setCycleTradeDelivers(providerTrade, KsBeanUtil.convert(trade, TradeVO.class), deliveryInfoVO, Integer.valueOf(cycle));
                                }

                            } else {
                                providerTrade.getTradeDelivers().forEach(tradeDeliver -> {
                                    if (Objects.nonNull(tradeDeliver.getDeliverId()) && !Objects.equals(tradeDeliver.getDeliverId(), deliveryInfoVO.getCode())) {
                                        //最后一期修改订单状态
                                        if (Objects.equals(cycleNum.toString(), cycle)) {

                                            List<DeliverCalendar> calendar = providerTrade.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliverCalendar1 -> Objects.nonNull(deliverCalendar1.getErpTradeCode()) && deliverCalendar1.getErpTradeCode().equals(deliverCalendar.getErpTradeCode())).collect(Collectors.toList());
                                            calendar.forEach(calendar1 -> {
                                                calendar1.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                                            });

                                            providerTrade.getTradeState().setFlowState(FlowState.DELIVERED);
                                            providerTrade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);


                                            trade.getTradeCycleBuyInfo().getDeliverCalendar().stream()
                                                    .filter(deliverCalendar1 -> Objects.nonNull(deliverCalendar1.getErpTradeCode()) && deliverCalendar1.getErpTradeCode().equals(deliverCalendar.getErpTradeCode())).forEach(deliverCalendar1 -> {
                                                deliverCalendar1.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                                            });

                                            trade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
                                            trade.getTradeState().setFlowState(FlowState.DELIVERED);

                                            //设置发货时间
                                            log.info("=========TradeDelivers:{}，platformCode:{}======",deliveryInfoVO.getDeliverTime(),deliveryInfoVO.getPlatformCode());
                                            providerTrade.getTradeState().setDeliverTime(deliveryInfoVO.getDeliverTime());
                                            trade.getTradeState().setDeliverTime(deliveryInfoVO.getDeliverTime());

                                            //组装发货记录等信息
                                            this.setCycleTradeDelivers(providerTrade, KsBeanUtil.convert(trade, TradeVO.class), deliveryInfoVO, Integer.valueOf(cycle));
                                        } else {
                                            List<DeliverCalendar> calendar = providerTrade.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliverCalendar1 -> Objects.nonNull(deliverCalendar1.getErpTradeCode()) && deliverCalendar1.getErpTradeCode().equals(deliverCalendar.getErpTradeCode())).collect(Collectors.toList());
                                            calendar.forEach(calendar1 -> {
                                                calendar1.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                                            });

                                            providerTrade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
                                            providerTrade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);


                                            trade.getTradeCycleBuyInfo().getDeliverCalendar().stream()
                                                    .filter(deliverCalendar1 -> Objects.nonNull(deliverCalendar1.getErpTradeCode()) && deliverCalendar1.getErpTradeCode().equals(deliverCalendar.getErpTradeCode())).forEach(deliverCalendar1 -> {
                                                deliverCalendar1.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                                            });
                                            trade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
                                            trade.getTradeState().setFlowState(FlowState.DELIVERED_PART);

                                            //组装发货记录等信息
                                            this.setCycleTradeDelivers(providerTrade, KsBeanUtil.convert(trade, TradeVO.class), deliveryInfoVO, Integer.valueOf(cycle));
                                        }
                                    }
                                });
                            }
                        } else {
                            // 返回数据只要不是已发货,其余状态下,扫描次数小于3的加1
                            if (!ObjectUtils.isEmpty(providerTrade.getTradeState())) {
                                TradeState tradeState = providerTrade.getTradeState();
                                if (tradeState.getScanCount() < ScanCount.COUNT_THREE.toValue()) {
                                    tradeState.setScanCount(tradeState.getScanCount() + ScanCount.COUNT_ONE.toValue());
                                }
                            }

                            providerTradeService.updateProviderTrade(providerTrade);

                        }
                    }
                } else {
                    // erp返回空数据
                    // 扫描次数小于3的加1
                    if (!ObjectUtils.isEmpty(providerTrade.getTradeState())) {
                        TradeState tradeState = providerTrade.getTradeState();
                        if (tradeState.getScanCount() < ScanCount.COUNT_THREE.toValue()) {
                            tradeState.setScanCount(tradeState.getScanCount() + ScanCount.COUNT_ONE.toValue());
                        }
                    }

                    providerTradeService.updateProviderTrade(providerTrade);

                }
            });
        }
    }


    /**
     * 更新订单发货信息
     *
     * @param providerTrade
     */
    public void setCycleTradeDelivers(ProviderTrade providerTrade, TradeVO tradeVO, DeliveryInfoVO deliveryInfoVO, Integer cycleNum) {
        log.info("=================周期购订单修改发货状态==========={}=========", providerTrade);

        //总期数
        Integer cycle = providerTrade.getTradeCycleBuyInfo().getCycleNum();

        //主品的发货清单
        List<ShippingItemVO> shippingItems = new ArrayList<>();
        //赠品的发货清单
        List<ShippingItemVO> giftListShippingItems = new ArrayList<>();
        //发货记录
        TradeDeliverVO tradeDeliverVO = null;


        //查询快递100对应的物流编码
        ErpLogisticsMappingVO erpLogisticsMappingVO = null;
        if (Objects.nonNull(deliveryInfoVO.getExpressCode())) {
            erpLogisticsMappingVO = erpLogisticsMappingQueryProvider.getByErpLogisticsCode(ErpLogisticsMappingByErpLogisticsCodeRequest.builder().erpLogisticsCode(deliveryInfoVO.getExpressCode()).build()).getContext().getErpLogisticsMappingVO();
        }

        // 物流信息
        LogisticsVO logistics = LogisticsVO.builder()
                .logisticCompanyName(deliveryInfoVO.getExpressName())
                .logisticNo(deliveryInfoVO.getExpressNo())
                .logisticStandardCode((Objects.nonNull(erpLogisticsMappingVO) && Objects.nonNull(erpLogisticsMappingVO.getWmLogisticsCode())) ? erpLogisticsMappingVO.getWmLogisticsCode() : null)
                .build();

        //组装主品发货清单
        deliveryInfoVO.getItemVOList().stream().forEach(deliveryItemVO -> {
            providerTrade.getTradeItems().forEach(tradeItemVO -> {
                if (Objects.equals(tradeItemVO.getOid(), deliveryItemVO.getOid())) {
                    ShippingItemVO shippingItemVO = new ShippingItemVO();
                    shippingItemVO.setItemName(tradeItemVO.getSkuName());
                    shippingItemVO.setItemNum(deliveryItemVO.getQty());
                    shippingItemVO.setSkuId(tradeItemVO.getSkuId());
                    shippingItemVO.setSkuNo(tradeItemVO.getSkuNo());
                    shippingItemVO.setUnit(tradeItemVO.getUnit());
                    shippingItemVO.setSpuId(tradeItemVO.getSpuId());
                    shippingItemVO.setSpecDetails(tradeItemVO.getSpecDetails());
                    shippingItems.add(shippingItemVO);

                    //1. 增加发货数量
                    Long hasNum = tradeItemVO.getDeliveredNum();
                    if (hasNum != null) {
                        hasNum += deliveryItemVO.getQty();
                    } else {
                        hasNum = deliveryItemVO.getQty();
                    }
                    tradeItemVO.setDeliveredNum(hasNum);
                    tradeItemVO.setDeliverStatus(Objects.equals(cycle, cycleNum) ? DeliverStatus.SHIPPED : DeliverStatus.PART_SHIPPED);
                }
            });
            //判断赠品是否发货
            providerTrade.getGifts().forEach(tradeItemVO -> {
                if (Objects.equals(tradeItemVO.getOid(), deliveryItemVO.getOid()) && !tradeItemVO.getGoodsType().equals(GoodsType.VIRTUAL_COUPON) && !tradeItemVO.getGoodsType().equals(GoodsType.VIRTUAL_GOODS)) {
                    ShippingItemVO shippingItemVO = new ShippingItemVO();
                    shippingItemVO.setItemName(tradeItemVO.getSkuName());
                    shippingItemVO.setItemNum(deliveryItemVO.getQty());
                    shippingItemVO.setSkuId(tradeItemVO.getSkuId());
                    shippingItemVO.setSkuNo(tradeItemVO.getSkuNo());
                    shippingItemVO.setUnit(tradeItemVO.getUnit());
                    shippingItemVO.setSpuId(tradeItemVO.getSpuId());
                    shippingItemVO.setSpecDetails(tradeItemVO.getSpecDetails());
                    giftListShippingItems.add(shippingItemVO);

                    //1. 增加发货数量
                    Long hasNum = tradeItemVO.getDeliveredNum();
                    if (hasNum != null) {
                        hasNum += deliveryItemVO.getQty();
                    } else {
                        hasNum = deliveryItemVO.getQty();
                    }
                    tradeItemVO.setDeliveredNum(hasNum);
                    tradeItemVO.setDeliverStatus(Objects.equals(cycle, cycleNum) ? DeliverStatus.SHIPPED : DeliverStatus.PART_SHIPPED);
                }
            });
            //更新主单的发货数量和发货状态
            tradeVO.getTradeItems().stream().forEach(tradeItemVO -> {
                if (Objects.equals(tradeItemVO.getOid(), deliveryItemVO.getOid())) {
                    // 增加发货数量、发货状态
                    Long hasNum = tradeItemVO.getDeliveredNum();
                    if (hasNum != null) {
                        hasNum += deliveryItemVO.getQty();
                    } else {
                        hasNum = deliveryItemVO.getQty();
                    }
                    tradeItemVO.setDeliveredNum(hasNum);
                    tradeItemVO.setDeliverStatus(Objects.equals(cycle, cycleNum) ? DeliverStatus.SHIPPED : DeliverStatus.PART_SHIPPED);
                }
            });

            //更新主单赠品的发货数量和发货状态
            tradeVO.getGifts().stream().forEach(tradeItemVO -> {
                if (Objects.equals(tradeItemVO.getOid(), deliveryItemVO.getOid()) && !tradeItemVO.getGoodsType().equals(GoodsType.VIRTUAL_COUPON) && !tradeItemVO.getGoodsType().equals(GoodsType.VIRTUAL_GOODS)) {
                    // 增加发货数量、发货状态
                    Long hasNum = tradeItemVO.getDeliveredNum();
                    if (hasNum != null) {
                        hasNum += deliveryItemVO.getQty();
                    } else {
                        hasNum = deliveryItemVO.getQty();
                    }
                    //同步已发货数量、发货状态
                    tradeItemVO.setDeliveredNum(hasNum);
                    tradeItemVO.setDeliverStatus(Objects.equals(cycle, cycleNum) ? DeliverStatus.SHIPPED : DeliverStatus.PART_SHIPPED);
                }
            });
        });

        //判断本次发货是否有赠品,组装赠品发货清单
        if (CollectionUtils.isNotEmpty(giftListShippingItems)) {
            tradeDeliverVO = TradeDeliverVO.builder()
                    .tradeId(providerTrade.getId())
                    .deliverId(deliveryInfoVO.getCode())
                    .deliverTime(deliveryInfoVO.getDeliverTime())
                    .shippingItems(shippingItems)
                    .giftItemList(giftListShippingItems)
                    .logistics(logistics)
                    .shipperType(ShipperType.PROVIDER)
                    .providerName(providerTrade.getSupplier().getSupplierName())
                    .status(Objects.equals(cycle, cycleNum) ? DeliverStatus.SHIPPED : DeliverStatus.PART_SHIPPED)
                    .cycleNum(cycleNum)
                    .build();

        } else {
            tradeDeliverVO = TradeDeliverVO.builder()
                    .tradeId(providerTrade.getId())
                    .deliverId(deliveryInfoVO.getCode())
                    .deliverTime(deliveryInfoVO.getDeliverTime())
                    .shippingItems(shippingItems)
                    .logistics(logistics)
                    .shipperType(ShipperType.PROVIDER)
                    .providerName(providerTrade.getSupplier().getSupplierName())
                    .status(Objects.equals(cycle, cycleNum) ? DeliverStatus.SHIPPED : DeliverStatus.PART_SHIPPED)
                    .cycleNum(cycleNum)
                    .build();
        }

        //组装操作记录
        Operator system = Operator.builder().name("system").account("system").platform(Platform.PLATFORM).build();
        TradeEventLog tradeEventLog = TradeEventLog
                .builder()
                .operator(system)
                .eventType(Objects.equals(cycle, cycleNum) ? FlowState.DELIVERED.getDescription() : FlowState.DELIVERED_PART.getDescription())
                .eventDetail(Objects.equals(cycle, cycleNum) ? String.format("订单[%s],已全部发货,发货人:%s", tradeVO.getId(), "system") : String.format("订单[%s],已部分发货,发货人:%s", tradeVO.getId(), "system"))
                .eventTime(LocalDateTime.now())
                .build();

        // 添加日志
        tradeEventLog.setEventDetail(String.format("同步ERP订单%s发货清单", providerTrade.getId()));
        providerTrade.appendTradeEventLog(tradeEventLog);
        // 同步provideTrade商品/赠品对应发货数和发货状态,发货清单
        providerTrade.addTradeDeliver(KsBeanUtil.convert(tradeDeliverVO, TradeDeliver.class));


        // 周期购添加物流信息
        // 物流信息表写入
        this.writeLogistics(providerTrade);

        Trade trade1 = KsBeanUtil.convert(tradeVO, Trade.class);
        // 添加日志
        tradeEventLog.setEventDetail(String.format("同步ERP订单%s发货清单", trade1.getId()));
        trade1.appendTradeEventLog(tradeEventLog);
        // 同步Trade商品对应发货数和发货状态,发货清单
        trade1.addTradeDeliver(KsBeanUtil.convert(tradeDeliverVO, TradeDeliver.class));

        providerTradeService.updateProviderTrade(providerTrade);

        tradeService.updateTrade(trade1);


        //同步变更订单开票的订单状态，排除不开票的订单
        if (!Objects.equals(trade1.getInvoice().getType(),-1)) {
            FlowState flowState= trade1.getTradeState().getFlowState();
            //更新订单状态
            OrderInvoiceModifyOrderStatusRequest orderInvoiceModifyOrderStatusRequest=new OrderInvoiceModifyOrderStatusRequest();
            orderInvoiceModifyOrderStatusRequest.setOrderNo(trade1.getId());
            orderInvoiceModifyOrderStatusRequest.setOrderStatus(flowState);
            if (PayState.NOT_PAID==trade1.getTradeState().getPayState()) {
                orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.NOTPAY);
            } else  if (PayState.PAID==trade1.getTradeState().getPayState()) {
                orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.PAYED);
            }else  if (PayState.UNCONFIRMED==trade1.getTradeState().getPayState()) {
                orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.TOCONFIRM);
            }
            log.info("订单开票更新订单状态，订单编号 {},订单当前状态：{}，更新之后的状态：{}", trade1.getId(),trade1.getTradeState().getFlowState(),flowState);
            orderInvoiceService.updateOrderStatus(orderInvoiceModifyOrderStatusRequest);
        }

    }


    /**
     * 更新订单发货信息
     *
     * @param providerTrade
     * @param deliveryInfoVOList
     */
    public void fillERPTradeDelivers(ProviderTrade providerTrade, List<DeliveryInfoVO> deliveryInfoVOList) {
        List<TradeDeliverVO> tradeDeliverVOs = new ArrayList<>();
        List<TradeDeliverVO> tradeDeliverVOList = new ArrayList<>();
        TradeVO tradeVO = KsBeanUtil.convert(tradeService.detail(providerTrade.getParentId()), TradeVO.class);
        Operator system = Operator.builder().name("system").account("system").platform(Platform.PLATFORM).build();
        TradeEventLog tradeEventLog = TradeEventLog
                .builder()
                .operator(system)
                .eventType("ERP订单发货清单")
                .eventTime(LocalDateTime.now())
                .build();
        //根据主单号查询所有发货单并以此判断主单是部分发货还是全部发货
        List<ProviderTrade> providerTrades = providerTradeService.findListByParentId(tradeVO.getId());

        //修改订单发货状态(排除发货单是未发货状态的)
        deliveryInfoVOList.stream().
                filter(vo -> vo.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE) || vo.getDeliveryStatus().equals(DeliveryStatus.PART_DELIVERY)).forEach(deliveryInfoVO -> {
            Map<String, DeliveryItemVO> tradeItemMap = new HashMap<>();
            Map<String, DeliveryItemVO> giftItemMap = new HashMap<>();
            List<ShippingItemVO> shippingItems = new ArrayList<>();
            List<ShippingItemVO> giftItems = new ArrayList<>();
            long count = providerTrade.getTradeDelivers().stream()
                    .filter(tradeDeliver -> !ObjectUtils.isEmpty(tradeDeliver)
                            && Objects.equals(tradeDeliver.getDeliverId(),deliveryInfoVO.getCode())).count();
            if (count == 0) {
                //修改订单主商品发货状态
                providerTrade.getTradeItems().forEach(tradeItem -> {
                    if (Objects.nonNull(tradeItem.getCombinedCommodity()) && tradeItem.getCombinedCommodity()) {
                        //todo 临时根据商品的skuId查询Erp商品编码
                        Optional<DeliveryItemVO> deliveryItemVOOptional = deliveryInfoVO.getItemVOList().stream()
                                .filter(vo -> vo.getOid().equals(tradeItem.getOid())
                                        && !tradeItem.getGoodsType().equals(GoodsType.VIRTUAL_COUPON)
                                        && !tradeItem.getGoodsType().equals(GoodsType.VIRTUAL_GOODS)).findFirst();
                        if (deliveryItemVOOptional.isPresent() && tradeItem.getDeliverStatus()==DeliverStatus.NOT_YET_SHIPPED) {
                            DeliveryItemVO deliveryItemVO = deliveryItemVOOptional.get();
                            tradeItem.setDeliveredNum(tradeItem.getNum());
                            tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
                            deliveryItemVO.setItemName(tradeItem.getSkuName());
                            deliveryItemVO.setSkuId(tradeItem.getSkuId());
                            deliveryItemVO.setSkuCode(tradeItem.getSkuNo());
                            tradeItemMap.put(tradeItem.getSkuId(), deliveryItemVO);
                        }
                    }


                    if (Objects.isNull(tradeItem.getCombinedCommodity())  && deliveryInfoVO.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)){
                        //todo 临时根据商品的skuId查询Erp商品编码
                        Optional<DeliveryItemVO> deliveryItemVOOptional = deliveryInfoVO.getItemVOList().stream()
                                .filter(vo -> vo.getOid().equals(tradeItem.getOid())
                                        && !tradeItem.getGoodsType().equals(GoodsType.VIRTUAL_COUPON)
                                        && !tradeItem.getGoodsType().equals(GoodsType.VIRTUAL_GOODS)).findFirst();
                        if (deliveryItemVOOptional.isPresent()) {
                            DeliveryItemVO deliveryItemVO = deliveryItemVOOptional.get();
                            tradeItem.setDeliveredNum(tradeItem.getDeliveredNum() + deliveryItemVO.getQty());
                            if (tradeItem.getDeliveredNum() < tradeItem.getNum() && tradeItem.getDeliveredNum() > 0) {
                                tradeItem.setDeliverStatus(DeliverStatus.PART_SHIPPED);
                            } else if (tradeItem.getDeliveredNum() >= tradeItem.getNum()) {
                                tradeItem.setDeliveredNum(tradeItem.getNum());
                                tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
                            } else {
                                tradeItem.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
                            }
                            deliveryItemVO.setItemName(tradeItem.getSkuName());
                            deliveryItemVO.setSkuId(tradeItem.getSkuId());
                            deliveryItemVO.setSkuCode(tradeItem.getSkuNo());
                            tradeItemMap.put(tradeItem.getSkuId(), deliveryItemVO);
                        }
                    }
                });

                //修改订单赠品发货状态
                providerTrade.getGifts().forEach(giftItem -> {

                    if (Objects.nonNull(giftItem.getCombinedCommodity()) && giftItem.getCombinedCommodity()) {
                        Optional<DeliveryItemVO> deliveryGiftVOOptional = deliveryInfoVO.getItemVOList().stream()
                                .filter(vo -> vo.getOid().equals(giftItem.getOid())
                                        && !giftItem.getGoodsType().equals(GoodsType.VIRTUAL_COUPON)
                                        && !giftItem.getGoodsType().equals(GoodsType.VIRTUAL_GOODS)).findFirst();
                        if (deliveryGiftVOOptional.isPresent() && giftItem.getDeliverStatus()==DeliverStatus.NOT_YET_SHIPPED ) {
                            DeliveryItemVO deliveryItemVO = deliveryGiftVOOptional.get();
                            giftItem.setDeliveredNum(giftItem.getNum());
                            giftItem.setDeliverStatus(DeliverStatus.SHIPPED);
                            deliveryItemVO.setItemName(giftItem.getSkuName());
                            deliveryItemVO.setSkuId(giftItem.getSkuId());
                            deliveryItemVO.setSkuCode(giftItem.getSkuNo());
                            giftItemMap.put(giftItem.getSkuId(), deliveryItemVO);
                        }
                    }

                    if (Objects.isNull(giftItem.getCombinedCommodity())  &&  deliveryInfoVO.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)){
                            Optional<DeliveryItemVO> deliveryGiftVOOptional = deliveryInfoVO.getItemVOList().stream()
                                    .filter(vo -> vo.getOid().equals(giftItem.getOid())
                                            && !giftItem.getGoodsType().equals(GoodsType.VIRTUAL_COUPON)
                                            && !giftItem.getGoodsType().equals(GoodsType.VIRTUAL_GOODS)).findFirst();
                            if (deliveryGiftVOOptional.isPresent()) {
                                DeliveryItemVO deliveryItemVO = deliveryGiftVOOptional.get();
                                giftItem.setDeliveredNum(giftItem.getDeliveredNum() + deliveryItemVO.getQty());
                                if (giftItem.getDeliveredNum() < giftItem.getNum() && giftItem.getDeliveredNum() > 0) {
                                    giftItem.setDeliverStatus(DeliverStatus.PART_SHIPPED);
                                } else if (giftItem.getDeliveredNum() >= giftItem.getNum()) {
                                    giftItem.setDeliveredNum(giftItem.getNum());
                                    giftItem.setDeliverStatus(DeliverStatus.SHIPPED);
                                } else {
                                    giftItem.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
                                }
                                deliveryItemVO.setItemName(giftItem.getSkuName());
                                deliveryItemVO.setSkuId(giftItem.getSkuId());
                                deliveryItemVO.setSkuCode(giftItem.getSkuNo());
                                giftItemMap.put(giftItem.getSkuId(), deliveryItemVO);
                            }
                        }
                });

                //发货记录排除电子卡券和虚拟商品,这类商品直接从商城发货生产发货单
                if (tradeItemMap.size() > 0 || giftItemMap.size() > 0) {

                    //查询快递100对应的物流编码
                    ErpLogisticsMappingVO erpLogisticsMappingVO = null;
                    if (Objects.nonNull(deliveryInfoVO.getExpressCode())) {
                        erpLogisticsMappingVO = erpLogisticsMappingQueryProvider.getByErpLogisticsCode(ErpLogisticsMappingByErpLogisticsCodeRequest.builder().erpLogisticsCode(deliveryInfoVO.getExpressCode()).build()).getContext().getErpLogisticsMappingVO();
                    }

                    //物流信息
                    LogisticsVO logisticsVO = LogisticsVO.builder()
                            .logisticCompanyName(deliveryInfoVO.getExpressName())
                            .logisticNo(deliveryInfoVO.getExpressNo())
                            .logisticStandardCode((Objects.nonNull(erpLogisticsMappingVO) && Objects.nonNull(erpLogisticsMappingVO.getWmLogisticsCode())) ? erpLogisticsMappingVO.getWmLogisticsCode() : null)
                            .build();
                    /**
                     * 组合订单主商品发货记录
                     */
                    tradeItemMap.forEach((skuId, deliveryItemVO) -> {
                        ShippingItemVO shippingItemVO = KsBeanUtil.convert(deliveryItemVO, ShippingItemVO.class);
                        TradeItem item=providerTrade.getTradeItems().stream().filter(tradeItem -> Objects.equals(skuId,tradeItem.getSkuId())).findFirst().orElse(null);
                        if (Objects.nonNull(item) && Objects.nonNull(item.getCombinedCommodity()) && item.getCombinedCommodity()){
                            shippingItemVO.setItemNum(item.getNum());
                        }else {
                            shippingItemVO.setItemNum(deliveryItemVO.getQty());
                        }
                        shippingItemVO.setItemName(deliveryItemVO.getItemName());
                        shippingItemVO.setSkuId(deliveryItemVO.getSkuId());
                        shippingItemVO.setSkuNo(deliveryItemVO.getSkuCode());
                        shippingItems.add(shippingItemVO);
                    });

                    /**
                     * 同步配送赠品信息
                     */
                    log.info("giftItemMap====>:{}", giftItemMap);
                    giftItemMap.forEach((sku, deliveryItemVO) -> {
                        ShippingItemVO shippingItemVO = KsBeanUtil.convert(deliveryItemVO, ShippingItemVO.class);
                        TradeItem gifts=providerTrade.getGifts().stream().filter(tradeGifts -> Objects.equals(sku,tradeGifts.getSkuId())).findFirst().orElse(null);
                        if (Objects.nonNull(gifts) && Objects.nonNull(gifts.getCombinedCommodity()) && gifts.getCombinedCommodity()){
                            shippingItemVO.setItemNum(gifts.getNum());
                        }else {
                            shippingItemVO.setItemNum(deliveryItemVO.getQty());
                        }
                        shippingItemVO.setItemName(deliveryItemVO.getItemName());
                        shippingItemVO.setItemNum(deliveryItemVO.getQty());
                        shippingItemVO.setSkuId(deliveryItemVO.getSkuId());
                        shippingItemVO.setSkuNo(deliveryItemVO.getSkuCode());
                        giftItems.add(shippingItemVO);
                    });

                    /**
                     * 生成新的发货清单
                     */
                    TradeDeliverVO tradeDeliverVO = TradeDeliverVO.builder()
                            .tradeId(providerTrade.getId())
                            //.deliverId(generatorService.generate("TD"))
                            .deliverId(deliveryInfoVO.getCode())
                            .deliverTime(deliveryInfoVO.getDeliverTime())
                            .shippingItems(shippingItems)
                            .giftItemList(giftItems)
                            .logistics(logisticsVO)
                            .providerName(providerTrade.getSupplier().getSupplierName())
                            .shipperType(ShipperType.PROVIDER)
                            .status(DeliverStatus.SHIPPED)
                            .build();
                    tradeDeliverVOList.add(tradeDeliverVO);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(tradeDeliverVOList)) {
            List<DeliverStatus> itemDeliverStatusList = providerTrade.getTradeItems().stream().map(item -> item.getDeliverStatus()).distinct().collect(Collectors.toList());
            List<DeliverStatus> giftDeliverStatusList = providerTrade.getGifts().stream().map(gift -> gift.getDeliverStatus()).distinct().collect(Collectors.toList());
            List<DeliverStatus> totalDeliverStatusList = Stream.of(itemDeliverStatusList, giftDeliverStatusList).flatMap(Collection::stream).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(totalDeliverStatusList) && totalDeliverStatusList.size() == 1 || !providerTrade.getSupplier().getStoreId().equals(defaultProviderId)) {
                if (totalDeliverStatusList.get(0) == DeliverStatus.SHIPPED || !providerTrade.getSupplier().getStoreId().equals(defaultProviderId)) {
                    providerTrade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
                    providerTrade.getTradeState().setFlowState(FlowState.DELIVERED);
                    tradeVO.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
                    tradeVO.getTradeState().setFlowState(FlowState.DELIVERED);

                    log.info("=========TradeDelivers:{}======",tradeDeliverVOList);

                    //设置发货时间
                    TradeDeliverVO tradeDeliver =  tradeDeliverVOList.get(0);
                    providerTrade.getTradeState().setDeliverTime(tradeDeliver.getDeliverTime());
                    tradeVO.getTradeState().setDeliverTime(tradeDeliver.getDeliverTime());
                    if(CollectionUtils.isNotEmpty(providerTrades) && providerTrades.stream().anyMatch(p->!Objects.equals(p.getId(),providerTrade.getId()) && (Objects.equals(p.getTradeState().getDeliverStatus(),DeliverStatus.PART_SHIPPED) || (Objects.equals(p.getTradeState().getDeliverStatus(),DeliverStatus.NOT_YET_SHIPPED) && !Objects.equals(p.getTradeState().getFlowState(),FlowState.VOID))))){
                        tradeVO.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
                        tradeVO.getTradeState().setFlowState(FlowState.DELIVERED_PART);
                        tradeVO.getTradeState().setDeliverTime(null);
                    }
                    if(!providerTrade.getSupplier().getStoreId().equals(defaultProviderId) && providerTrade.getTradeItems().stream().anyMatch(p->p.getDeliverStatus() != DeliverStatus.SHIPPED)){
                        providerTrade.getTradeState().setVirtualAllDelivery(1);
                        tradeVO.getTradeState().setVirtualAllDelivery(1);
                    }


                } else if (totalDeliverStatusList.get(0) == DeliverStatus.PART_SHIPPED) {
                    providerTrade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
                    providerTrade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
                    tradeVO.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
                    tradeVO.getTradeState().setFlowState(FlowState.DELIVERED_PART);
                }
            } else if (CollectionUtils.isNotEmpty(totalDeliverStatusList) && totalDeliverStatusList.stream().anyMatch(deliverStatus ->
                    deliverStatus.getStatusId().equals(DeliverStatus.PART_SHIPPED.getStatusId()) ||
                            deliverStatus.getStatusId().equals(DeliverStatus.NOT_YET_SHIPPED.getStatusId()))) {
                providerTrade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
                providerTrade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
                tradeVO.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
                tradeVO.getTradeState().setFlowState(FlowState.DELIVERED_PART);
            }
            // 添加日志
            tradeEventLog.setEventDetail(String.format("自订单同步ERP订单%s发货清单", providerTrade.getId()));
            List<TradeDeliver> providerTradeDelivers = KsBeanUtil.copyListProperties(tradeDeliverVOList, TradeDeliver.class);
            providerTrade.getTradeDelivers().addAll(providerTradeDelivers);
            providerTrade.appendTradeEventLog(tradeEventLog);
            // 部分发货订单添加扫描次数
            if (!DeliverStatus.SHIPPED.equals(providerTrade.getTradeState().getDeliverStatus())) {
                // 扫描次数小于3的加1
                if (!ObjectUtils.isEmpty(providerTrade.getTradeState())) {
                    TradeState tradeState = providerTrade.getTradeState();
                    if (tradeState.getScanCount() < ScanCount.COUNT_THREE.toValue()) {
                        tradeState.setScanCount(tradeState.getScanCount() + ScanCount.COUNT_ONE.toValue());
                    }
                }
            }

            // 物流信息表写入
            this.writeLogistics(providerTrade);
            providerTradeService.updateProviderTrade(providerTrade);


            // 同步Trade商品对应发货数和发货状态,发货清单
            tradeVO.getTradeDelivers().addAll(tradeDeliverVOs);
            Trade trade = KsBeanUtil.convert(tradeVO, Trade.class);
           // trade.setTradeItems(providerTrade.getTradeItems());
            //trade.setGifts(providerTrade.getGifts());

            // 添加日志
            tradeEventLog.setEventDetail(String.format("主订单同步ERP订单%s发货清单", trade.getId()));
            //更新主订单发货记录
            List<TradeDeliver> tradeDelivers = KsBeanUtil.copyListProperties(tradeDeliverVOList, TradeDeliver.class);
            trade.getTradeDelivers().addAll(tradeDelivers);
            //更新主订单TradeItem/Gift,多供应商不能直接覆盖-update
            List<TradeItem> tradeItems = new ArrayList<>();
            List<TradeItem> gifts = new ArrayList<>();
            tradeItems.addAll(trade.getTradeItems().stream().filter(p-> !providerTrade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList()).contains(p.getSkuId())).collect(Collectors.toList()));
            gifts.addAll(trade.getGifts().stream().filter(p-> !providerTrade.getGifts().stream().map(TradeItem::getSkuId).collect(Collectors.toList()).contains(p.getSkuId())).collect(Collectors.toList()));
            tradeItems.addAll(providerTrade.getTradeItems());
            gifts.addAll(providerTrade.getGifts());
            trade.setTradeItems(tradeItems);
            trade.setGifts(gifts);

            trade.appendTradeEventLog(tradeEventLog);
            tradeService.updateTrade(trade);


            //同步变更订单开票的订单状态，排除不开票的订单
            if (!Objects.equals(trade.getInvoice().getType(),-1)) {
                FlowState flowState= trade.getTradeState().getFlowState();
                //更新订单状态
                OrderInvoiceModifyOrderStatusRequest orderInvoiceModifyOrderStatusRequest=new OrderInvoiceModifyOrderStatusRequest();
                orderInvoiceModifyOrderStatusRequest.setOrderNo(trade.getId());
                orderInvoiceModifyOrderStatusRequest.setOrderStatus(flowState);
                if (PayState.NOT_PAID==trade.getTradeState().getPayState()) {
                    orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.NOTPAY);
                } else  if (PayState.PAID==trade.getTradeState().getPayState()) {
                    orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.PAYED);
                }else  if (PayState.UNCONFIRMED==trade.getTradeState().getPayState()) {
                    orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.TOCONFIRM);
                }
                log.info("订单开票更新订单状态，订单编号 {},订单当前状态：{}，更新之后的状态：{}", trade.getId(),trade.getTradeState().getFlowState(),flowState);
                orderInvoiceService.updateOrderStatus(orderInvoiceModifyOrderStatusRequest);
            }


        }
    }

    /**
     * 写入物流信息
     *
     * @param providerTrade
     */
    public void writeLogistics(ProviderTrade providerTrade) {

        if (CollectionUtils.isNotEmpty(providerTrade.getTradeDelivers())) {

            TradeDeliver tradeDeliver=null;

            if (providerTrade.getCycleBuyFlag()) {
                 tradeDeliver = providerTrade.getTradeDelivers().get(0);
            }else {
                 tradeDeliver = providerTrade.getTradeDelivers().get(providerTrade.getTradeDelivers().size()-1);
            }

            Logistics  logistics=tradeDeliver.getLogistics();

            //快递订阅
            LogisticsLog logisticsLog = new LogisticsLog();
            logisticsLog.setOrderNo(providerTrade.getParentId());
            logisticsLog.setDeliverId(tradeDeliver.getDeliverId());
            logisticsLog.setStoreId(providerTrade.getSupplier().getStoreId());
            logisticsLog.setCustomerId(providerTrade.getBuyer().getId());
            logisticsLog.setPhone(providerTrade.getConsignee().getPhone());
            logisticsLog.setComOld((Objects.nonNull(logistics) && Objects.nonNull(logistics.getLogisticStandardCode())) ? logistics.getLogisticStandardCode() : null);
            logisticsLog.setLogisticNo((Objects.nonNull(logistics) && Objects.nonNull(logistics.getLogisticNo())) ? logistics.getLogisticNo() : null);
            logisticsLog.setTo(providerTrade.getConsignee().getDetailAddress());
            if (CollectionUtils.isNotEmpty(providerTrade.getTradeItems())) {
                logisticsLog.setGoodsImg(providerTrade.getTradeItems().get(0).getPic());
                logisticsLog.setGoodsName(providerTrade.getTradeItems().get(0).getSkuName());
            }
            logisticsLogService.add(logisticsLog);
        }
    }

    public BaseResponse syncProviderTradeStatus(ProviderTradeStatusSyncRequest request) {
        if(request == null || request.getStatus() == null){
          throw new SbcRuntimeException(CommonErrorCode.FAILED,new Object[]{"入参为空"});
        }
        ProviderTrade providerTrade = providerTradeService.findbyId(request.getPlatformCode());
        if(providerTrade == null){
            throw new SbcRuntimeException(CommonErrorCode.FAILED,new Object[]{"未找到对应订单号"});
        }
        try {
            if (request.getStatus().equals(1)) {
                //推送订单失败,更新订单推送状态
                this.updateTradeInfo(request.getPlatformCode(), true, false, providerTrade.getTradeState().getPushCount() + 1, request.getStatusDesc(), LocalDateTime.now(),"");
            } else {
                this.updateTradeInfo(request.getPlatformCode(), true, true, providerTrade.getTradeState().getPushCount() + 1, "success", LocalDateTime.now(),request.getOrderId());
            }
            return BaseResponse.SUCCESSFUL();
        } catch (Exception e) {
            log.error("更新订单{}发生异常", providerTrade.getId(), e);
            //推送订单失败,更新订单推送状态
            this.updateTradeInfo(providerTrade.getId(), true, false, providerTrade.getTradeState().getPushCount() + 1,
                    "更新订单出现异常",
                    LocalDateTime.now(),"");
        }
        return BaseResponse.FAILED();
    }

    public BaseResponse syncProviderTradeDeliveryStatus(ProviderTradeStatusSyncRequest request) {
        ProviderTrade providerTrade = providerTradeService.findbyId(request.getPlatformCode());
        try {
            List<DeliveryInfoVO> deliveryInfoVOListVo = new ArrayList<>();
            if(request.getOrderStatus() != null && request.getOrderStatus().equals(5)) {
                DeliveryInfoVO deliveryInfoVO = DeliveryInfoVO.builder()
                        .expressName(request.getPost())
                        .deliveryStatus(DeliveryStatus.DELIVERY_COMPLETE)
                        .expressNo(request.getPostNumber())
                        .code(generatorService.generate("TD"))
                        .platformCode(request.getPlatformCode()).build();
                if(request.getPostDate() !=null){
                    try{
                        Instant instant = request.getPostDate().toInstant();
                        ZoneId zoneId = ZoneId.systemDefault();
                        deliveryInfoVO.setDeliverTime(instant.atZone(zoneId).toLocalDateTime());

                    }catch (Exception e){
                        log.warn("syncProviderTradeDeliveryStatus trans time error,request:{}",request,e);
                        deliveryInfoVO.setDeliverTime(LocalDateTime.now());
                    }
                }
                List<DeliveryItemVO> deliveryItemVOS = new ArrayList<>();
                request.getGoodsList().forEach(g -> {
                    if(providerTrade.getTradeItems().stream().anyMatch(p->p.getErpSpuNo().equals(g.getSourceSpbs()) || p.getErpSpuNo().equals(g.getBookId()))) {
                        DeliveryItemVO deliveryItemVO = new DeliveryItemVO();
                        deliveryItemVO.setQty(g.getBookSendNum().longValue());
                        deliveryItemVO.setOid(providerTrade.getTradeItems().stream().filter(p -> p.getErpSpuNo().equals(g.getSourceSpbs()) ||  p.getErpSpuNo().equals(g.getBookId())).findFirst().get().getOid());
                        deliveryItemVOS.add(deliveryItemVO);
                    }
                });
                deliveryInfoVO.setItemVOList(deliveryItemVOS);
                deliveryInfoVOListVo.add(deliveryInfoVO);
            }

            if (CollectionUtils.isNotEmpty(deliveryInfoVOListVo)) {
                    this.fillERPTradeDelivers(providerTrade, deliveryInfoVOListVo);
            } else {
                    // 扫描次数小于3的加1
                    if (!ObjectUtils.isEmpty(providerTrade.getTradeState())) {
                        TradeState tradeState = providerTrade.getTradeState();
                        if (tradeState.getScanCount() < ScanCount.COUNT_THREE.toValue()) {
                            tradeState.setScanCount(tradeState.getScanCount() + ScanCount.COUNT_ONE.toValue());
                        }
                    }
                    providerTradeService.updateProviderTrade(providerTrade);

                }
        } catch (Exception e) {
            log.error("#同步发货状态异常:{}", e);
        }
        return BaseResponse.SUCCESSFUL();
    }

}
