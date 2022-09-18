package com.wanmi.sbc.order.thirdplatformtrade.service;

import com.aliyuncs.linkedmall.model.v20180116.*;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderProvider;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.order.*;
import com.wanmi.sbc.linkedmall.api.response.order.SbcCreateOrderAndPayResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcLogisticsQueryResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcOrderListQueryResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcRenderOrderResponse;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradeUpdateRequest;
import com.wanmi.sbc.order.bean.dto.ThirdPlatformTradeUpdateStateDTO;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.ShipperType;
import com.wanmi.sbc.order.bean.vo.*;
import com.wanmi.sbc.order.common.OrderCommonService;
import com.wanmi.sbc.order.thirdplatformtrade.model.entity.LinkedMallGoods;
import com.wanmi.sbc.order.thirdplatformtrade.model.entity.LinkedMallLogisticsDetail;
import com.wanmi.sbc.order.thirdplatformtrade.model.entity.LinkedMallTradeResult;
import com.wanmi.sbc.order.thirdplatformtrade.model.root.LinkedMallTradeLogistics;
import com.wanmi.sbc.order.thirdplatformtrade.model.root.ThirdPlatformTrade;
import com.wanmi.sbc.order.thirdplatformtrade.repository.ThirdPlatformTradeRepository;
import com.wanmi.sbc.order.thirdplatformtrade.request.ThirdPlatformTradeQueryRequest;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import com.wanmi.sbc.order.trade.service.TradeService;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressQueryProvider;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressListRequest;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 第三方渠道-linkedMall订单处理服务层
 * @Description: 第三方渠道-linkedMall订单处理服务层
 * @Autho qiaokang
 * @Date：2020-02-11 22:56
 */
@Service
@Slf4j
public class LinkedMallTradeService {

    @Autowired
    private ThirdPlatformTradeRepository thirdPlatformTradeRepository;

    @Autowired
    private LinkedMallOrderProvider linkedMallOrderProvider;

    @Autowired
    private LinkedMallOrderQueryProvider linkedMallOrderQueryProvider;

    @Autowired
    private ThirdAddressQueryProvider thirdAddressQueryProvider;

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private OrderCommonService orderCommonService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private LinkedMallTradeLogisticsService linkedMallTradeLogisticsService;

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param businessId 业务id
     */
    @Transactional
    public LinkedMallTradeResult add(String businessId) {
        List<Trade> trades = orderCommonService.findTradesByBusinessId(businessId).stream()
                .filter(trade -> ThirdPlatformType.LINKED_MALL.equals(trade.getThirdPlatformType())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(trades)){
            LinkedMallTradeResult result = new LinkedMallTradeResult();
            result.setStatus(0);
            return result;
        }
        Set<String> errorTrade = new HashSet<>();
        List<String> tradeIds = trades.stream().map(Trade::getId).collect(Collectors.toList());
        Map<String, List<ThirdPlatformTrade>> thirdTrades = thirdPlatformTradeRepository.findListByTradeIdIn(tradeIds)
                .stream()
                .filter(t -> ThirdPlatformType.LINKED_MALL.equals(t.getThirdPlatformType()))
                .collect(Collectors.groupingBy(ThirdPlatformTrade::getTradeId));
        for (Trade trade : trades) {
            List<ThirdPlatformTrade> thirdPlatformTrades = thirdTrades.getOrDefault(trade.getId(), Collections.emptyList());
            //更新订单编号
            for (ThirdPlatformTrade thirdTrade : thirdPlatformTrades) {
                SbcCreateOrderRequest request = new SbcCreateOrderRequest();
                request.setFullName(thirdTrade.getConsignee().getName());
                request.setMobile(thirdTrade.getConsignee().getPhone());
                request.setBizUid(thirdTrade.getBuyer().getId());
                request.setOutTradeId(thirdTrade.getId());
                request.setAddressDetail(thirdTrade.getConsignee().getDetailAddress());
                request.setDivisionCode(thirdTrade.getThirdDivisionCode());
                request.setOrderExpireTime(60L * 30);//30分钟
                List<CreateOrderV2Request.ItemList> items = this.zipLinkedMallItem(thirdTrade.getTradeItems(), thirdTrade.getGifts()).stream()
                        .map(i -> {
                            CreateOrderV2Request.ItemList item = new CreateOrderV2Request.ItemList();
                            item.setItemId(NumberUtils.toLong(i.getThirdPlatformSpuId()));
                            item.setQuantity(i.getNum().intValue());
                            item.setSkuId(NumberUtils.toLong(i.getThirdPlatformSkuId()));
                            return item;
                        }).collect(Collectors.toList());
                request.setLmGoodsItems(items);
                try {
                    SbcCreateOrderAndPayResponse response = linkedMallOrderProvider.createOrder(request).getContext();
                    thirdTrade.setThirdPlatformOrderIds(response.getLmOrderList());
                    thirdTrade.setOutOrderIds(response.getOrderIds());
                    if (thirdTrade.getTradeState() == null) {
                        thirdTrade.setTradeState(new TradeState());
                    }
                    thirdTrade.getTradeState().setPayState(PayState.NOT_PAID);
                    thirdPlatformTradeRepository.save(thirdTrade);
                } catch (Exception e) {
                    //其中一个抛错，退出当前循环
                    log.error("创建第三方平台订单抛错", e);
                    errorTrade.add(trade.getId());
                    break;
                }
            }
        }
        LinkedMallTradeResult result = new LinkedMallTradeResult();
        result.setStatus(0);
        result.setAutoRefundTrades(trades.stream().filter(t -> errorTrade.contains(t.getId())).collect(Collectors.toList()));
        result.setSuccessTrades(trades.stream().filter(t -> !errorTrade.contains(t.getId())).collect(Collectors.toList()));
        return result;
    }

    /**
     * linkedMall支付
     *
     * @param tradeId 订单id
     * @return 0：完成  1:自动退款 2：定时观察 3标记支付失败
     */
    @Transactional
    public int pay(String tradeId) {
        List<ThirdPlatformTrade> thirdTrades = thirdPlatformTradeRepository.findListByTradeId(tradeId);
        if(CollectionUtils.isEmpty(thirdTrades)){
            return 1;
        }
        int res = 0;
        int netErr = 0;//外部异常，每笔重试2次，2次之后停止并进入定时观察，
        int len = thirdTrades.size();
        List<String> errProviderTradeIds = new ArrayList<>();
        List<String> netErrProviderTradeIds = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            ThirdPlatformTrade thirdTrade = thirdTrades.get(i);
            if (thirdTrade.getTradeState() == null) {
                thirdTrade.setTradeState(new TradeState());
            }
            SbcPayOrderRequest request = new SbcPayOrderRequest();
            request.setBizUid(thirdTrade.getBuyer().getId());
            request.setOutTradeId(thirdTrade.getId());
            request.setLmOrderId(thirdTrade.getThirdPlatformOrderIds().get(0));
            try {
                linkedMallOrderProvider.payOrder(request);
                thirdTrade.getTradeState().setPayState(PayState.PAID);
                thirdPlatformTradeRepository.save(thirdTrade);
            } catch (SbcRuntimeException e) {
                //业务错误问题
                if (CommonErrorCode.SPECIFIED.equals(e.getErrorCode())) {
                    log.error("第三方子订单id：{}，第三方订单支付失败! tradeId={} error={}", thirdTrade.getId(), tradeId, StringUtils.isNotBlank(e.getResult()) ? e.getResult() : e.getMessage());
                    if (e.getResult().contains("订单未找到")) {
                        i--;
                        continue;
                    }
                    //如果第一个抛错，直接自动退款
                    if (i == 0) {
                        return 1;
                    }
                    thirdTrade.setThirdPlatformPayErrorFlag(true);
                    thirdPlatformTradeRepository.save(thirdTrade);
                    errProviderTradeIds.add(thirdTrade.getParentId());
                } else {
                    //网络超出2次，定时观察
                    if (netErr > 0) {
                        netErr = 0;
                        thirdTrade.getTradeState().setPayState(PayState.UNCONFIRMED);
                        thirdPlatformTradeRepository.save(thirdTrade);
                        netErrProviderTradeIds.add(thirdTrade.getParentId());
                        log.error("第三方子订单id：{}，第三方订单网络异常超出2次，定时观察! tradeId={}", thirdTrade.getId(), tradeId);
                    } else {
                        log.error("第三方子订单id：{}，第三方订单网络异常! tradeId={} error={}", thirdTrade.getId(), tradeId, e.getMessage());
                        i--;
                        netErr++;
                    }
                }
            } catch (Exception e) {
                //网络超出2次，定时观察
                if (netErr > 1) {
                    netErr = 0;
                    thirdTrade.getTradeState().setPayState(PayState.UNCONFIRMED);
                    thirdPlatformTradeRepository.save(thirdTrade);
                    netErrProviderTradeIds.add(thirdTrade.getParentId());
                    log.error("第三方子订单id：{}，第三方订单网络异常超出2次，定时观察! tradeId={}", thirdTrade.getId(), tradeId);
                } else {
                    i--;
                    netErr++;
                    log.error("第三方子订单id：" + thirdTrade.getId() + "，第三方订单网络异常! tradeId=" + tradeId, e);
                }
            }
        }

        //设定providerTrade待确认状态
        if (CollectionUtils.isNotEmpty(netErrProviderTradeIds)) {
            netErrProviderTradeIds.stream().distinct().forEach(providerTradeId -> {
                providerTradeService.updateThirdPlatformPayState(providerTradeId, PayState.UNCONFIRMED);
            });
        }

        //设定providerTrade付款错误状态
        if (CollectionUtils.isNotEmpty(errProviderTradeIds)) {
            errProviderTradeIds.stream().distinct().forEach(providerTradeId -> {
                providerTradeService.updateThirdPlatformPayFlag(providerTradeId, true);
            });
        }

        long errCount = thirdTrades.stream().filter(t -> Boolean.TRUE.equals(t.getThirdPlatformPayErrorFlag())).count();
        if (thirdTrades.stream().anyMatch(t -> t.getTradeState() != null && PayState.UNCONFIRMED.equals(t.getTradeState().getPayState()))) {
            res = 2;
        } else if (errCount == thirdTrades.size()) {
            res = 1;
        } else if (errCount > 0) {
            res = 3;
        }
        return res;
    }

    /**
     * 补偿性判断
     *
     * @param tradeId 订单id
     * @return 0：完成  1:自动退款 2：继续观察 3标记支付失败
     */
    @Transactional
    public LinkedMallTradeResult compensate(String tradeId) {
        Trade trade = tradeService.detail(tradeId);
        LinkedMallTradeResult result = new LinkedMallTradeResult();
        List<String> errProviderTradeIds = new ArrayList<>();
        List<String> netErrProviderTradeIds = new ArrayList<>();
        int res = 0;
        if (trade != null) {
            List<ThirdPlatformTrade> thirdTrades = thirdPlatformTradeRepository.findListByTradeId(tradeId).stream()
                    .filter(t -> ThirdPlatformType.LINKED_MALL.equals(t.getThirdPlatformType())
                            && CollectionUtils.isNotEmpty(t.getThirdPlatformOrderIds()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(thirdTrades)) {
                List<String> ids = thirdTrades.stream().flatMap(t -> t.getThirdPlatformOrderIds().stream()).distinct().collect(Collectors.toList());
                SbcOrderListQueryRequest queryRequest = new SbcOrderListQueryRequest();
                queryRequest.setPageNum(1L);
                queryRequest.setPageSize(20L);
                queryRequest.setLmOrderList(ids);
                queryRequest.setBizUid(trade.getBuyer().getId());
                queryRequest.setAllFlag(true);
                queryRequest.setEnableStatus(-1);
                List<QueryOrderListResponse.LmOrderListItem> items = linkedMallOrderQueryProvider.queryOrderDetail(queryRequest).getContext().getLmOrderListItems();
                if (CollectionUtils.isEmpty(items)) {
                    log.error("订单id：{}，第三方平台订单不存在，自动退款!", tradeId);
                    result.setStatus(1);
                    return result;
                }
                Map<String, QueryOrderListResponse.LmOrderListItem> itemMap = items.stream().collect(Collectors.toMap(i -> Objects.toString(i.getLmOrderId()), i -> i));
                for (ThirdPlatformTrade thirdTrade : thirdTrades) {
                    if (thirdTrade.getTradeState() == null) {
                        thirdTrade.setTradeState(new TradeState());
                    }
                    //未付款或待确认
                    if (PayState.UNCONFIRMED.equals(thirdTrade.getTradeState().getPayState())
                            || PayState.NOT_PAID.equals(thirdTrade.getTradeState().getPayState())) {
                        QueryOrderListResponse.LmOrderListItem item = itemMap.get(thirdTrade.getThirdPlatformOrderIds().get(0));
                        if (item != null) {
                            Integer orderStatus = item.getOrderStatus();
                            //支付成功
                            if (Integer.valueOf(2).equals(orderStatus) || Integer.valueOf(6).equals(orderStatus)) {
                                thirdTrade.getTradeState().setPayState(PayState.PAID);
                                thirdPlatformTradeRepository.save(thirdTrade);
                            } else if (Integer.valueOf(12).equals(orderStatus)) {//待支付
                                SbcPayOrderRequest request = new SbcPayOrderRequest();
                                request.setBizUid(thirdTrade.getBuyer().getId());
                                request.setOutTradeId(thirdTrade.getId());
                                request.setLmOrderId(thirdTrade.getThirdPlatformOrderIds().get(0));
                                try {
                                    linkedMallOrderProvider.payOrder(request);
                                    thirdTrade.getTradeState().setPayState(PayState.PAID);
                                    thirdPlatformTradeRepository.save(thirdTrade);
                                } catch (SbcRuntimeException e) {
                                    //业务性问题
                                    if (CommonErrorCode.SPECIFIED.equals(e.getErrorCode())) {
                                        thirdTrade.setThirdPlatformPayErrorFlag(true);
                                        thirdTrade.getTradeState().setPayState(PayState.NOT_PAID);
                                        thirdPlatformTradeRepository.save(thirdTrade);
                                        errProviderTradeIds.add(thirdTrade.getParentId());
                                        log.error("第三方子订单id={}，第三方订单标记支付失败! tradeId={}，error={}", thirdTrade.getId(), tradeId, e.getMessage());
                                    } else {//外网网络性问题
                                        thirdTrade.getTradeState().setPayState(PayState.UNCONFIRMED);
                                        thirdPlatformTradeRepository.save(thirdTrade);
                                        netErrProviderTradeIds.add(thirdTrade.getParentId());
                                        log.error("第三方子订单id={}，第三方订单网络异常! tradeId={}，error={}", thirdTrade.getId(), tradeId, e.getMessage());
                                    }
                                } catch (Exception e) {//内网网络问题
                                    thirdTrade.getTradeState().setPayState(PayState.UNCONFIRMED);
                                    thirdPlatformTradeRepository.save(thirdTrade);
                                    netErrProviderTradeIds.add(thirdTrade.getParentId());
                                    log.error("第三方子订单id=" + thirdTrade.getId() + "，第三方订单网络异常! tradeId=" + tradeId, e);
                                }
                            }else {
                                thirdTrade.setThirdPlatformPayErrorFlag(true);
                                thirdTrade.getTradeState().setPayState(PayState.NOT_PAID);
                                thirdPlatformTradeRepository.save(thirdTrade);
                                errProviderTradeIds.add(thirdTrade.getParentId());
                                log.error("第三方平台订单id：{}，linkedMall订单支付失败，标记支付失败! --> tradeId：{}", thirdTrade.getId(), tradeId);
                            }
                        } else {
                            thirdTrade.setThirdPlatformPayErrorFlag(true);
                            thirdTrade.getTradeState().setPayState(PayState.NOT_PAID);
                            thirdPlatformTradeRepository.save(thirdTrade);
                            errProviderTradeIds.add(thirdTrade.getParentId());
                            log.error("第三方平台订单id：{}，linkedMall订单不存在，标记支付失败! --> tradeId：{}", thirdTrade.getId(), tradeId);
                        }
                    }
                }

                //设定providerTrade状态
                if (CollectionUtils.isNotEmpty(netErrProviderTradeIds)) {
                    netErrProviderTradeIds.stream().distinct().forEach(providerTradeId -> {
                        providerTradeService.updateThirdPlatformPayState(providerTradeId, PayState.UNCONFIRMED);
                    });
                }

                //设定providerTrade的付款错误状态
                if (CollectionUtils.isNotEmpty(errProviderTradeIds)) {
                    errProviderTradeIds.stream().distinct().forEach(providerTradeId -> {
                        providerTradeService.updateThirdPlatformPayFlag(providerTradeId, true);
                    });
                }

                //设定providerTrade的付款成功状态
                if(CollectionUtils.isEmpty(netErrProviderTradeIds) && CollectionUtils.isEmpty(errProviderTradeIds)){
                    thirdTrades.stream().map(ThirdPlatformTrade::getParentId).distinct().forEach(providerTradeId ->{
                        providerTradeService.updateThirdPlatformPayState(providerTradeId, PayState.PAID);
                        providerTradeService.updateThirdPlatformPayFlag(providerTradeId, false);
                    });
                }

                long errCount = thirdTrades.stream().filter(t -> Boolean.TRUE.equals(t.getThirdPlatformPayErrorFlag())).count();
                if(thirdTrades.stream().anyMatch(t -> t.getTradeState() != null && PayState.UNCONFIRMED.equals(t.getTradeState().getPayState()))){
                    res = 2;
                }else if(errCount == thirdTrades.size()){
                    res = 1;
                    result.setAutoRefundTrades(Collections.singletonList(trade));
                }else if(errCount > 0){
                    res = 3;
                }
            }
        }
        result.setStatus(res);
        return result;
    }

    /**
     * 根据供货商拆单并入库
     *
     * @param trade 订单
     */
    public void splitTrade(ProviderTrade trade) {
        List<ThirdAddressVO> thirdAddressVOS = thirdAddressQueryProvider.list(ThirdAddressListRequest.builder()
                        .platformAddrIdList(Collections.singletonList(Objects.toString(trade.getConsignee().getAreaId())))
                        .thirdFlag(ThirdPlatformType.LINKED_MALL).build()) .getContext().getThirdAddressList();
        String thirdAddressId = null;
        if (CollectionUtils.isNotEmpty(thirdAddressVOS)) {
            thirdAddressId = thirdAddressVOS.get(0).getThirdAddrId();
        }
        if (thirdAddressId == null) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"不支持该收货地区"});
        }

        //去重赠品，合并赠品数量
        if(CollectionUtils.isNotEmpty(trade.getGifts())) {
            Map<String, List<TradeItem>> giftMap = trade.getGifts().stream().collect(Collectors.groupingBy(TradeItem::getSkuId));
            List<TradeItem> gift = new ArrayList<>();
            giftMap.forEach((k,v)->{
                if(giftMap.get(k).size()>1){
                    TradeItem s = KsBeanUtil.convert(giftMap.get(k).get(0), TradeItem.class);
                    s.setNum(giftMap.get(k).stream().mapToLong(TradeItem::getNum).sum());
                    gift.add(s);
                }else{
                    gift.add(giftMap.get(k).get(0));
                }
            });
            trade.setGifts(gift);
        }
        //拆分订单
        splitTrade(trade, thirdAddressId);
    }

    /**
     * 根据供货商拆单并入库
     *
     * @param trade 订单
     */
    private void splitTrade(ProviderTrade trade, String thirdAddressId) {
        SbcRenderOrderRequest request = new SbcRenderOrderRequest();
        request.setDivisionCode(thirdAddressId);
        request.setBizUid(trade.getBuyer().getId());
        request.setAddressDetail(trade.getConsignee().getDetailAddress());
        request.setMobile(trade.getConsignee().getPhone());
        request.setFullName(trade.getConsignee().getName());
        List<RenderOrderRequest.ItemList> items = this.zipLinkedMallItem(trade.getTradeItems(), trade.getGifts()).stream()
                .map(i -> {
                    RenderOrderRequest.ItemList item = new RenderOrderRequest.ItemList();
                    item.setItemId(NumberUtils.toLong(i.getThirdPlatformSpuId()));
                    item.setQuantity(i.getNum().intValue());
                    item.setSkuId(NumberUtils.toLong(i.getThirdPlatformSkuId()));
                    return item;
                }).collect(Collectors.toList());
        request.setLmGoodsItems(items);
        SbcRenderOrderResponse response = linkedMallOrderQueryProvider.initRenderOrder(request).getContext();
        response.getModel().getRenderOrderInfos().forEach(renderOrder -> {
            ThirdPlatformTrade thirdPlatformTrade = KsBeanUtil.convert(trade, ThirdPlatformTrade.class);
            // 用渠道订单parentId作为供应商订单的父id
            thirdPlatformTrade.setParentId(trade.getId());
            thirdPlatformTrade.setTradeId(trade.getParentId());//主订单号
            thirdPlatformTrade.setId(generatorService.generateProviderThirdTid());
            // 筛选当前供应商的订单商品信息
            Map<String, RenderOrderResponse.Model.RenderOrderInfosItem.LmItemInfosItem> itemMap =
                    renderOrder.getLmItemInfos().stream().collect(Collectors.toMap(i -> String.valueOf(i.getSkuId()), i -> i));
            List<TradeItem> providerTradeItems = trade.getTradeItems().stream()
                    .filter(i -> itemMap.containsKey(i.getThirdPlatformSkuId())).collect(Collectors.toList());

            // 筛选当前LM店铺的赠品信息
            List<TradeItem> providerTradeGifts = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(trade.getGifts())) {
                providerTradeGifts.addAll(trade.getGifts().stream()
                        .filter(i -> itemMap.containsKey(i.getThirdPlatformSkuId())).collect(Collectors.toList()));
            }

            // 拆单后，重新计算价格信息
            TradePrice tradePrice = thirdPlatformTrade.getTradePrice();
            // 商品总价
            BigDecimal goodsPrice = BigDecimal.ZERO;

            Long goodsPoint = 0L;

            // 订单总价:实付金额
            BigDecimal orderPrice = BigDecimal.ZERO;
            // 订单供货价总额
            BigDecimal orderSupplyPrice = BigDecimal.ZERO;
            for (TradeItem item : providerTradeItems) {
                // 商品总价
                goodsPrice = goodsPrice.add(Objects.isNull(item.getPrice()) ? BigDecimal.ZERO :
                                item.getPrice().multiply(BigDecimal.valueOf(item.getNum())));
                goodsPoint += (Objects.isNull(item.getPoints())?0:item.getPoints()) * item.getNum();
                // 商品分摊价格
                BigDecimal splitPrice = Objects.isNull(item.getSplitPrice()) ? BigDecimal.ZERO :
                        item.getSplitPrice();
                orderPrice = orderPrice.add(splitPrice);
                // 订单供货价总额
                orderSupplyPrice = orderSupplyPrice.add(item.getTotalSupplyPrice());
            }

            for (TradeItem item : providerTradeGifts) {
                // 订单供货价总额
                orderSupplyPrice = orderSupplyPrice.add(item.getTotalSupplyPrice());
            }

            thirdPlatformTrade.setThirdSellerName(itemMap.get(providerTradeItems.get(0).getThirdPlatformSkuId()).getSellerNick());
            thirdPlatformTrade.setThirdSellerId(String.valueOf(itemMap.get(providerTradeItems.get(0).getThirdPlatformSkuId()).getSellerId()));
            thirdPlatformTrade.setThirdDivisionCode(request.getDivisionCode());

            thirdPlatformTrade.setTradeItems(providerTradeItems);
            thirdPlatformTrade.setGifts(providerTradeGifts);
            // 商品总价
            tradePrice.setGoodsPrice(goodsPrice);
            tradePrice.setOriginPrice(goodsPrice);
            if (goodsPoint > 0) {
                tradePrice.setPoints(goodsPoint);
            }
            // 订单总价
            tradePrice.setTotalPrice(orderPrice);
            tradePrice.setTotalPayCash(orderPrice);
            // 订单供货价总额
            tradePrice.setOrderSupplyPrice(orderSupplyPrice);
            thirdPlatformTrade.setTradePrice(tradePrice);
            this.thirdPlatformTradeRepository.save(thirdPlatformTrade);
        });
    }

    /**
     * 验证是否可售
     *
     * @param trades 多订单
     */
//    public void verify(List<Trade> trades) {
//        Trade trade = trades.stream().filter(t -> ThirdPlatformType.LINKED_MALL.equals(t.getThirdPlatformType())).findFirst().orElse(null);
//        if(Objects.isNull(trade)){
//            return;
//        }
//        List<ThirdAddressVO> thirdAddressVOS =
//                thirdAddressQueryProvider.list(ThirdAddressListRequest.builder().platformAddrIdList(Collections.singletonList(Objects.toString(trade.getConsignee().getAreaId())))
//                        .thirdFlag(ThirdPlatformType.LINKED_MALL).build())
//                        .getContext().getThirdAddressList();
//        String thirdAddrId = null;
//        if (CollectionUtils.isNotEmpty(thirdAddressVOS)) {
//            thirdAddrId = thirdAddressVOS.get(0).getThirdAddrId();
//        }
//        if (thirdAddrId == null) {
//            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"不支持该收货地区"});
//        }
//        SbcRenderOrderRequest request = new SbcRenderOrderRequest();
//        request.setDivisionCode(thirdAddrId);
//        request.setBizUid(trade.getBuyer().getId());
//        request.setAddressDetail(trade.getConsignee().getDetailAddress());
//        request.setMobile(trade.getConsignee().getPhone());
//        request.setFullName(trade.getConsignee().getName());
//        List<RenderOrderRequest.ItemList> items = this.zipLinkedMallItem(trade.getTradeItems(), trade.getGifts()).stream()
//                .map(i -> {
//                    RenderOrderRequest.ItemList item = new RenderOrderRequest.ItemList();
//                    item.setItemId(NumberUtils.toLong(i.getThirdPlatformSpuId()));
//                    item.setQuantity(i.getNum().intValue());
//                    item.setSkuId(NumberUtils.toLong(i.getThirdPlatformSkuId()));
//                    return item;
//                }).collect(Collectors.toList());
//        request.setLmGoodsItems(items);
//        try {
//            SbcRenderOrderResponse response = linkedMallOrderQueryProvider.initRenderOrder(request).getContext();
//            response.getModel().getRenderOrderInfos().stream()
//                    .flatMap(o -> o.getLmItemInfos().stream())
//                    .filter(i -> Boolean.FALSE.equals(i.getCanSell()))
//                    .findFirst().ifPresent(i -> {
//                throw new SbcRuntimeException("K-050117");
//            });
//        }catch (SbcRuntimeException e) {
//            if (CommonErrorCode.SPECIFIED.equals(e.getErrorCode())) {
//                throw new SbcRuntimeException("K-050117");
//            }
//            throw e;
//        }
//    }

    /**
     * 确认收货
     * @param tradeId
     * @param userId
     */
    @GlobalTransactional
    @Transactional
    public void confirmDisburse(String tradeId, String userId) {
        List<ThirdPlatformTrade> tradeList = this.findListByTradeId(tradeId);
        tradeList.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getThirdPlatformOrderIds())
                        && ThirdPlatformType.LINKED_MALL.equals(t.getThirdPlatformType()))
                .forEach(t -> {
                    t.getThirdPlatformOrderIds().forEach(id -> {
                        SbcConfirmDisburseRequest request = new SbcConfirmDisburseRequest();
                        request.setLmOrderId(id);
                        request.setBizUid(userId);
                        linkedMallOrderProvider.confirmDisburse(request);
                    });
                    t.getTradeState().setFlowState(FlowState.COMPLETED);
                    thirdPlatformTradeRepository.save(t);
                });
    }

    /**
     * 根据订单号查询供货商第三方平台订单
     *
     * @param tradeIds 批量订单号
     */
    public List<ThirdPlatformTrade> listByTradeIds(List<String> tradeIds) {
        return thirdPlatformTradeRepository.findListByTradeIdIn(tradeIds);
    }


    public boolean isOpen(){
        ThirdPlatformConfigResponse response = thirdPlatformConfigQueryProvider.get(
                ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build())
                .getContext();
        return response != null && Constants.yes.equals(response.getStatus());
    }

    /**
     * 查询订单
     *
     * @param tid
     */
    public ThirdPlatformTrade detail(String tid) {
        return thirdPlatformTradeRepository.findById(tid).orElse(null);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param trade
     */
    public void updateThirdPlatformTrade(ThirdPlatformTrade trade) {
        thirdPlatformTradeRepository.save(trade);
    }

    /**
     * 根据父订单号查询第三方订单
     *
     * @param parentTid
     */
    public List<ThirdPlatformTrade> findListByParentId(String parentTid) {
        return thirdPlatformTradeRepository.findListByParentId(parentTid);
    }

    /**
     * 根据父订单号批量查询第三方订单
     *
     * @param parentTids
     */
    public List<ThirdPlatformTrade> findListByParentIds(List<String> parentTids ) {
        return thirdPlatformTradeRepository.findListByParentIdIn(parentTids);
    }


    // 填充linkedmall订单中商品对应的子单号
    @Transactional
    public void fillSubLmOrderId(List<TradeVO> tradeVOS, List<QueryOrderListResponse.LmOrderListItem> lmOrderListItems) {
        if(CollectionUtils.isEmpty(tradeVOS) || CollectionUtils.isEmpty(lmOrderListItems)) {
            return;
        }

        // 查询linkedmall订单详情
        Map<Long, QueryOrderListResponse.LmOrderListItem> lmOrderMap = lmOrderListItems.stream()
                .collect(Collectors.toMap(QueryOrderListResponse.LmOrderListItem::getLmOrderId, i -> i));

        if(MapUtils.isEmpty(lmOrderMap)) {
            return;
        }

        List<ThirdPlatformTrade> thirdPlatformTrades = KsBeanUtil.convert(tradeVOS,ThirdPlatformTrade.class);

        // 遍历linkedmall订单，填充linkedmall商品对应子单号
        thirdPlatformTrades.stream()
                .filter(t ->
                        ThirdPlatformType.LINKED_MALL.equals(t.getThirdPlatformType())
                                && CollectionUtils.isNotEmpty(t.getThirdPlatformOrderIds()))
                .forEach(trade -> {
                    AtomicBoolean flag = new AtomicBoolean(false);
                    String lmOrderId = trade.getThirdPlatformOrderIds().get(0);
                    if(CollectionUtils.isNotEmpty(trade.getTradeItems())) {
                        trade.getTradeItems().stream().filter(t -> Objects.isNull(t.getThirdPlatformSubOrderId())).forEach(t -> {
                            lmOrderMap.get(NumberUtils.toLong(lmOrderId)).getSubOrderList().stream().filter(Objects::nonNull)
                                    .filter(s -> t.getThirdPlatformSkuId().equals(String.valueOf(s.getSkuId()))
                                            && t.getThirdPlatformSpuId().equals(String.valueOf(s.getItemId())))
                                    .forEach(s -> {
                                        t.setThirdPlatformSubOrderId(String.valueOf(s.getLmOrderId()));
                                        flag.set(true);
                                    });
                        });
                    }
                    if(CollectionUtils.isNotEmpty(trade.getGifts())) {
                        trade.getGifts().stream().filter(t -> Objects.isNull(t.getThirdPlatformSubOrderId())).forEach(t -> {
                            lmOrderMap.get(NumberUtils.toLong(lmOrderId)).getSubOrderList().stream().filter(Objects::nonNull)
                                    .filter(s -> t.getThirdPlatformSkuId().equals(String.valueOf(s.getSkuId()))
                                            && t.getThirdPlatformSpuId().equals(String.valueOf(s.getItemId())))
                                    .forEach(s -> {
                                        t.setThirdPlatformSubOrderId(String.valueOf(s.getLmOrderId()));
                                        flag.set(true);
                                    });
                        });
                    }
                    if(flag.get()) {
                        this.updateThirdPlatformTrade(trade);
                    }
        });
    }

    /**
     * 根据主订单号查询第三方订单
     *
     * @param tradeId
     */
    public List<ThirdPlatformTrade> findListByTradeId(String tradeId) {
        return thirdPlatformTradeRepository.findListByTradeId(tradeId);
    }

    /**
     * 根据id查询第三方订单
     */
    public ThirdPlatformTrade findById(String id){
        return thirdPlatformTradeRepository.findFirstById(id);
    }

    /**
     * 更新订单
     *
     * @param tradeUpdateRequest
     */
    @GlobalTransactional
    @Transactional
    public void updateThirdPlatformTrade(ThirdPlatformTradeUpdateRequest tradeUpdateRequest) {
        this.updateThirdPlatformTrade(KsBeanUtil.convert(tradeUpdateRequest.getTrade(), ThirdPlatformTrade.class));
    }

    /**
     * 更新订单状态,同时更新父订单、主订单状态
     *
     * @param tradeUpdateStateDTO
     */
    @Transactional
    public void updateThirdPlatformTradeState(ThirdPlatformTradeUpdateStateDTO tradeUpdateStateDTO) {
        // 更新后的状态
        FlowState newFlowState = tradeUpdateStateDTO.getFlowState();
        DeliverStatus newDeliverStatus = tradeUpdateStateDTO.getDeliverStatus();
        PayState newPayState = tradeUpdateStateDTO.getPayState();
        // 获取数据库订单详情
        ThirdPlatformTrade thirdPlatformTrade = thirdPlatformTradeRepository.findFirstById(tradeUpdateStateDTO.getId());
        // 当前数据库中订单状态
        FlowState oldFlowState= thirdPlatformTrade.getTradeState().getFlowState();
        DeliverStatus oldDeliverStatus = thirdPlatformTrade.getTradeState().getDeliverStatus();
        PayState oldPatState = thirdPlatformTrade.getTradeState().getPayState();
        if(FlowState.VOID.equals(oldFlowState) || FlowState.COMPLETED.equals(oldFlowState)) {
            return;
        }
        // 拼装日志详情,更新状态
        StringBuilder eventDetail = new StringBuilder("同步linkedmall订单").append(tradeUpdateStateDTO.getId()).append("状态");
        if(Objects.nonNull(newFlowState) && !newFlowState.equals(oldFlowState)) {
            eventDetail.append(",订单状态从【").append(oldFlowState.getDescription())
                    .append("】扭转为【").append(newFlowState.getDescription()).append("】");
            thirdPlatformTrade.getTradeState().setFlowState(newFlowState);
        }
        if(Objects.nonNull(newDeliverStatus) && !newDeliverStatus.equals(oldDeliverStatus)) {
            eventDetail.append(",发货状态从【").append(oldDeliverStatus.getDescription())
                    .append("】扭转为【").append(newDeliverStatus.getDescription()).append("】");
            thirdPlatformTrade.getTradeState().setDeliverStatus(newDeliverStatus);
        }
        if(PayState.UNCONFIRMED.equals(oldPatState) && Objects.nonNull(newPayState) && !newPayState.equals(oldPatState)) {
            eventDetail.append(",支付状态从【").append(oldPatState.getDescription())
                    .append("】扭转为【").append(newPayState.getDescription()).append("】");
            thirdPlatformTrade.getTradeState().setPayState(newPayState);
        }

        // 1、更新三级订单状态
        Operator system = Operator.builder().name("system").account("system").platform(Platform.PLATFORM).build();
        TradeEventLog tradeEventLog = TradeEventLog
                .builder()
                .operator(system)
                .eventType("同步linkedmall订单状态")
                .eventDetail(eventDetail.toString())
                .eventTime(LocalDateTime.now())
                .build();
        thirdPlatformTrade.appendTradeEventLog(tradeEventLog);
        thirdPlatformTradeRepository.save(thirdPlatformTrade);

        String parentId = tradeUpdateStateDTO.getParentId();
        String tradeId = tradeUpdateStateDTO.getTradeId();

        // 2、获取父订单所有子订单
        ProviderTrade providerTrade = providerTradeService.providerDetail(parentId);
        if(FlowState.VOID.equals(providerTrade.getTradeState().getFlowState()) ||
                FlowState.COMPLETED.equals(providerTrade.getTradeState().getFlowState())) {
            return;
        }
        List<ThirdPlatformTrade> thirdPlatformTrades = thirdPlatformTradeRepository.findListByParentId(parentId);
        // 防止事务未提交，查出来的数据未更新
        thirdPlatformTrades.forEach(trade -> {
            if (trade.getId().equals(tradeUpdateStateDTO.getId())) {
                if(Objects.nonNull(newFlowState)) {
                    trade.getTradeState().setFlowState(newFlowState);
                }
                if(Objects.nonNull(newDeliverStatus)) {
                    trade.getTradeState().setDeliverStatus(newDeliverStatus);
                }
            }
        });
        TradeVO tradeVO = KsBeanUtil.convert(providerTrade, TradeVO.class);
        boolean updateProviderFlag = changeParentTradeState(tradeVO, KsBeanUtil.convert(thirdPlatformTrades,
                TradeVO.class),tradeEventLog);
        //3、更新二级父订单状态
        if(updateProviderFlag) {
            providerTrade = KsBeanUtil.convert(tradeVO, ProviderTrade.class);
            providerTrade.appendTradeEventLog(tradeEventLog);
            providerTradeService.updateProviderTrade(providerTrade);

            //4、获取一级主订单所有子订单
            Trade trade = tradeService.detail(tradeId);
            if(FlowState.VOID.equals(trade.getTradeState().getFlowState()) ||
                    FlowState.COMPLETED.equals(trade.getTradeState().getFlowState())) {
                return;
            }
            List<ProviderTrade> providerTrades = providerTradeService.findListByParentId(tradeId);
            // 防止事务未提交，查出来的数据未更新
            providerTrades.forEach(pTrade -> {
                if (pTrade.getId().equals(parentId)) {
                    if(Objects.nonNull(newFlowState)) {
                        pTrade.getTradeState().setFlowState(newFlowState);
                    }
                    if(Objects.nonNull(newDeliverStatus)) {
                        pTrade.getTradeState().setDeliverStatus(newDeliverStatus);
                    }
                }
            });
            TradeVO convert = KsBeanUtil.convert(trade, TradeVO.class);
            boolean updateTradeFlag = changeParentTradeState(convert, KsBeanUtil.convert(providerTrades,
                    TradeVO.class),tradeEventLog);
            // 5、更新一级主订单状态
            if(updateTradeFlag) {
                trade = KsBeanUtil.convert(convert, Trade.class);
                trade.appendTradeEventLog(tradeEventLog);
                tradeService.updateTrade(trade);
            }
        }
    }

    // 根据子订单状态判断是否更新父订单状态,子订单状态一致时更改父订单状态
    private boolean changeParentTradeState(TradeVO parentTrade, List<TradeVO> sonTrades,TradeEventLog tradeEventLog) {
        boolean updateFlag = false;
        // 1、获取父订单状态
        FlowState pFlowState = parentTrade.getTradeState().getFlowState();
        DeliverStatus pDeliverStatus = parentTrade.getTradeState().getDeliverStatus();
        PayState pPayState = parentTrade.getTradeState().getPayState();
        if(CollectionUtils.isNotEmpty(sonTrades)) {
            // 获取所有子订单去重后的 订单状态集合
            List<FlowState> flowStateList =
                    sonTrades.stream().map(v -> v.getTradeState().getFlowState()).distinct().collect(Collectors.toList());
            // 获取所有子订单去重后的 配送状态集合
            List<DeliverStatus> deliverStatusList =
                    sonTrades.stream().map(v -> v.getTradeState().getDeliverStatus()).distinct().collect(Collectors.toList());
            // 拼装日志详情,更新状态
            StringBuilder eventDetail = new StringBuilder("同步linkedmall订单").append(parentTrade.getId()).append("状态");
            // 所有子订单状态一致且与父订单状态不一致时，更改父订单状态
            if(CollectionUtils.isNotEmpty(flowStateList) && flowStateList.size() == 1 && !flowStateList.get(0).equals(pFlowState)) {
                eventDetail.append(",订单状态从【").append(pFlowState.getDescription())
                        .append("】扭转为【").append(flowStateList.get(0).getDescription()).append("】");
                parentTrade.getTradeState().setFlowState(flowStateList.get(0));
                updateFlag = true;
            }
            if(CollectionUtils.isNotEmpty(deliverStatusList) && deliverStatusList.size() == 1 && !deliverStatusList.get(0).equals(pDeliverStatus)) {
                eventDetail.append(",发货状态从【").append(pDeliverStatus.getDescription())
                        .append("】扭转为【").append(deliverStatusList.get(0).getDescription()).append("】");
                parentTrade.getTradeState().setDeliverStatus(deliverStatusList.get(0));
                updateFlag = true;
            }
            // 如果支付状态为未确认，
            if(PayState.UNCONFIRMED.equals(pPayState) && ThirdPlatformType.LINKED_MALL.equals(parentTrade.getThirdPlatformType())) {
                // 获取所有子订单去重后的 支付状态集合
                List<PayState> payStateList =
                        sonTrades.stream().map(v -> v.getTradeState().getPayState()).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(payStateList) && payStateList.size() == 1 && !payStateList.get(0).equals(pPayState)) {
                    eventDetail.append(",支付状态从【").append(pPayState.getDescription())
                            .append("】扭转为【").append(payStateList.get(0).getDescription()).append("】");
                    parentTrade.getTradeState().setPayState(payStateList.get(0));
                    updateFlag = true;
                }
            }
            tradeEventLog.setEventDetail(eventDetail.toString());
        }
        return updateFlag;
    }

    /**
     * 订单分页
     *
     * @param whereCriteria 条件
     * @param request       参数
     * @return
     */
    public Page<ThirdPlatformTrade> page(Criteria whereCriteria, ThirdPlatformTradeQueryRequest request) {
        long totalSize = this.countNum(whereCriteria, request);
        if (totalSize < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        return new PageImpl<>(mongoTemplate.find(query.with(request.getPageRequest()), ThirdPlatformTrade.class), request
                .getPageable(), totalSize);
    }

    /**
     * 统计数量
     *
     * @param whereCriteria
     * @param request
     * @return
     */
    public long countNum(Criteria whereCriteria, ThirdPlatformTradeQueryRequest request) {
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        long totalSize = mongoTemplate.count(query, ThirdPlatformTrade.class);
        return totalSize;
    }

    /**
     * 填充并保存linkedmall订单配送信息
     * @param tradeVO 订单详细信息
     * @return
     */
    @Transactional
    public TradeVO fillLinkedMallTradeDelivers(TradeVO tradeVO) {
        if(Objects.isNull(tradeVO)) {
            return tradeVO;
        }
        // 是否积分订单
        boolean isPointsTrade = OrderType.POINTS_ORDER.equals(tradeVO.getOrderType());
        if((!isPointsTrade && CollectionUtils.isEmpty(tradeVO.getTradeVOList())) ||
                (isPointsTrade && CollectionUtils.isEmpty(tradeVO.getPointsTradeVOList()))) {
            return tradeVO;
        }

        List<TradeVO> tradeVOList = new ArrayList<>();
        if(!isPointsTrade && CollectionUtils.isNotEmpty(tradeVO.getTradeVOList())) {
            tradeVOList = tradeVO.getTradeVOList();
        }

        if(isPointsTrade && CollectionUtils.isNotEmpty(tradeVO.getPointsTradeVOList())) {
            tradeVOList = KsBeanUtil.convert(tradeVO.getPointsTradeVOList(), TradeVO.class);
        }

        if(CollectionUtils.isNotEmpty(tradeVOList)) {
            tradeVOList = tradeVOList.stream().map(providerTrade -> {
                // 是linkedmall订单,填充linkedmall子单信息
                if(Objects.nonNull(providerTrade.getThirdPlatformType()) && ThirdPlatformType.LINKED_MALL.equals(providerTrade.getThirdPlatformType())) {
                    List<ThirdPlatformTrade> thirdPlatformTrades = this.findListByParentId(providerTrade.getId());
                    List<TradeVO> tradeVOS = KsBeanUtil.convert(thirdPlatformTrades, TradeVO.class);
                    try {
                        // linkedmall订单状态同步
                        this.thirdPlatformTradeStateSync(tradeVOS,providerTrade);
                        // 获取数据库最新的订单信息
                        tradeVOS = KsBeanUtil.convert(thirdPlatformTradeRepository.findListByParentId(providerTrade.getId()), TradeVO.class);
                        providerTrade = KsBeanUtil.convert(providerTradeService.providerDetail(providerTrade.getId()), TradeVO.class);
                        TradeVO trade = KsBeanUtil.convert(tradeService.detail(providerTrade.getParentId()), TradeVO.class);
                        // 填充linkedmall订单物流信息
                        this.fillLinkedMallTradeDelivers(tradeVOS,providerTrade,trade);
                    } catch (Exception e) {
                        log.error("查询订单详情接口，更新linkedmall订单:{},订单状态及发货清单出现异常",providerTrade.getId(),e);
                    }
                    if(isPointsTrade) {
                        providerTrade.setPointsTradeVOList(KsBeanUtil.convert(tradeVOS,PointsTradeVO.class));
                    } else {
                        providerTrade.setTradeVOList(tradeVOS);
                    }
                }
                return providerTrade;
            }).collect(Collectors.toList());
        }

        TradeVO trade = KsBeanUtil.convert(tradeService.detail(tradeVO.getId()), TradeVO.class);

        if(isPointsTrade) {
            trade.setPointsTradeVOList(KsBeanUtil.convert(tradeVOList, PointsTradeVO.class));
        } else {
            trade.setTradeVOList(tradeVOList);
        }

        return trade;
    }

    // linkedmall订单状态同步
    public void thirdPlatformTradeStateSync(List<TradeVO> tradeVOList, TradeVO providerTrade) {
        if(CollectionUtils.isEmpty(tradeVOList)) {
            return;
        }

        // 过滤需要查询linkedmall详情的订单
        List<TradeVO> tradeVOS = tradeVOList.stream()
                .filter(t -> ThirdPlatformType.LINKED_MALL.equals(t.getThirdPlatformType()) &&
                        CollectionUtils.isNotEmpty(t.getThirdPlatformOrderIds()) &&
                        !FlowState.VOID.equals(t.getTradeState().getFlowState()) &&
                        !FlowState.COMPLETED.equals(t.getTradeState().getFlowState()) &&
                        !PayState.NOT_PAID.equals(t.getTradeState().getPayState()))
                .collect(Collectors.toList());

        List<String> lmOrderIds =
                tradeVOS.stream().map(t -> t.getThirdPlatformOrderIds().get(0)).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(tradeVOS) || CollectionUtils.isEmpty(lmOrderIds)) {
            return;
        }

        Map<String, TradeVO> map =
                tradeVOS.stream().collect(Collectors.toMap(t -> t.getThirdPlatformOrderIds().get(0), i -> i));

        List<ThirdPlatformTradeUpdateStateDTO> tradeUpdateStates = new ArrayList<>();
        // 查询linkedmall订单详情
        SbcOrderListQueryResponse response =
                linkedMallOrderQueryProvider.queryOrderDetail(SbcOrderListQueryRequest.builder()
                        .lmOrderList(lmOrderIds).bizUid(providerTrade.getBuyer().getId()).allFlag(Boolean.TRUE).build()).getContext();
        if (Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getLmOrderListItems())) {
            // 填充linkedmall订单商品对应子单号
            this.fillSubLmOrderId(tradeVOList,response.getLmOrderListItems());
            response.getLmOrderListItems().forEach(lmOrderListItem -> {
                // linkedMall 订单状态
                Integer orderStatus = lmOrderListItem.getOrderStatus();
                // linkedMall 物流状态
                Integer logisticsStatus = lmOrderListItem.getLogisticsStatus();
                Long lmOrderId = lmOrderListItem.getLmOrderId();
                if(map.containsKey(Objects.toString(lmOrderId))) {
                    this.fillTradeStatusByLinkedMallOrderStatus(tradeUpdateStates, map.get(Objects.toString(lmOrderId)), orderStatus, logisticsStatus);
                }
            });
        }

        // 更新linkedmall订单状态
        tradeUpdateStates.forEach(this::updateThirdPlatformTradeState);

    }

    // 根据linkedmall订单状态 填充 商城订单状态
    private void fillTradeStatusByLinkedMallOrderStatus(List<ThirdPlatformTradeUpdateStateDTO> tradeUpdateStates, TradeVO tradeVO, Integer orderStatus,
                                                        Integer logisticsStatus) {
        /*
        "orderStatus":"12=待支付，2=已支付，4=已退款关闭，6=交易成功，8=被淘宝关闭 ",
        "logisticsStatus":" 1=未发货 -> 等待卖家发货 2=已发货 -> 等待买家确认收货 3=已收货 -> 交易成功 4=已经退货 -> 交易失败 5=部分收货 -> 交易成功 6=部分发货中
        8=还未创建物流订单",
         */
        FlowState flowState = null;
        DeliverStatus deliverStatus = null;

        if (Integer.valueOf(6).equals(orderStatus) || Integer.valueOf(3).equals(logisticsStatus)) {
            flowState = FlowState.COMPLETED;
        } else if (Integer.valueOf(4).equals(orderStatus)) {
            flowState = FlowState.VOID;
        } else if (Integer.valueOf(8).equals(orderStatus)) {
            flowState = FlowState.VOID;
        } else if (Integer.valueOf(12).equals(orderStatus)) {
            flowState = FlowState.AUDIT;
        } else if (Integer.valueOf(2).equals(orderStatus) && (Integer.valueOf(2).equals(logisticsStatus) ||
                Integer.valueOf(5).equals(logisticsStatus) || Integer.valueOf(6).equals(logisticsStatus))) {
            flowState = FlowState.DELIVERED;
        } else if (Integer.valueOf(2).equals(orderStatus) &&
                (Integer.valueOf(1).equals(logisticsStatus) || Integer.valueOf(8).equals(logisticsStatus))) {
            flowState = FlowState.AUDIT;
        } else if (Integer.valueOf(4).equals(logisticsStatus)) {
            flowState = FlowState.VOID;
        }

        if (Integer.valueOf(1).equals(logisticsStatus) ||
                (Integer.valueOf(8).equals(logisticsStatus) && Integer.valueOf(12).equals(orderStatus))) {
            deliverStatus = DeliverStatus.NOT_YET_SHIPPED;
        } else if (Integer.valueOf(2).equals(logisticsStatus) || Integer.valueOf(3).equals(logisticsStatus)) {
            deliverStatus = DeliverStatus.SHIPPED;
        } else if (Integer.valueOf(5).equals(logisticsStatus) || Integer.valueOf(6).equals(logisticsStatus)) {
            deliverStatus = DeliverStatus.PART_SHIPPED;
        } else if (Integer.valueOf(4).equals(logisticsStatus) || Integer.valueOf(4).equals(orderStatus) ||
                (Integer.valueOf(8).equals(logisticsStatus) && Integer.valueOf(8).equals(orderStatus))) {
            deliverStatus = DeliverStatus.VOID;
        }

        boolean updateFlag = false;
        if (Objects.nonNull(flowState) && !flowState.equals(tradeVO.getTradeState().getFlowState())) {
            tradeVO.getTradeState().setFlowState(flowState);
            updateFlag = true;
        }
        if (Objects.nonNull(deliverStatus) && !deliverStatus.equals(tradeVO.getTradeState().getDeliverStatus())) {
            tradeVO.getTradeState().setDeliverStatus(deliverStatus);
            updateFlag = true;
        }
        if(PayState.UNCONFIRMED.equals(tradeVO.getTradeState().getPayState()) && Integer.valueOf(2).equals(orderStatus)) {
            tradeVO.getTradeState().setPayState(PayState.PAID);
            updateFlag = true;
        }
        if(PayState.UNCONFIRMED.equals(tradeVO.getTradeState().getPayState()) && Integer.valueOf(12).equals(orderStatus)) {
            tradeVO.getTradeState().setPayState(PayState.NOT_PAID);
            updateFlag = true;
        }
        if (updateFlag) {
            ThirdPlatformTradeUpdateStateDTO tradeUpdateStateDTO = new ThirdPlatformTradeUpdateStateDTO();
            tradeUpdateStateDTO.setId(tradeVO.getId());
            tradeUpdateStateDTO.setParentId(tradeVO.getParentId());
            tradeUpdateStateDTO.setTradeId(tradeVO.getTradeId());
            tradeUpdateStateDTO.setFlowState(tradeVO.getTradeState().getFlowState());
            tradeUpdateStateDTO.setDeliverStatus(tradeVO.getTradeState().getDeliverStatus());
            tradeUpdateStateDTO.setPayState(tradeVO.getTradeState().getPayState());
            tradeUpdateStates.add(tradeUpdateStateDTO);
        }
    }

    // 填充并保存linkedmall订单配送信息
    @Transactional
    public void fillLinkedMallTradeDelivers(List<TradeVO> tradeVOS, TradeVO providerTrade, TradeVO trade) {
        if(CollectionUtils.isEmpty(tradeVOS)) {
            return;
        }
        // providerTrade下所有linkedmall二次拆单的订单 对应的 发货清单集合
        List<TradeDeliverVO> tradeDeliverVOs = new ArrayList<>();
        Map<String,TradeItemVO> tradeItemList = new HashMap<>();
        Map<String,TradeItemVO> giftList = new HashMap<>();

        Operator system = Operator.builder().name("system").account("system").platform(Platform.PLATFORM).build();
        TradeEventLog tradeEventLog = TradeEventLog
                .builder()
                .operator(system)
                .eventType("同步linkedmall发货清单")
                .eventTime(LocalDateTime.now())
                .build();

        tradeVOS.forEach(tradeVO -> {
            if(Objects.nonNull(tradeVO.getThirdPlatformType()) && ThirdPlatformType.LINKED_MALL.equals(tradeVO.getThirdPlatformType())
                    && Objects.nonNull(tradeVO.getBuyer()) && StringUtils.isNotBlank(tradeVO.getBuyer().getId())
                    && CollectionUtils.isNotEmpty(tradeVO.getThirdPlatformOrderIds())
                    && PayState.PAID.equals(tradeVO.getTradeState().getPayState()) &&
                    (DeliverStatus.SHIPPED.equals(tradeVO.getTradeState().getDeliverStatus()) ||
                            DeliverStatus.PART_SHIPPED.equals(tradeVO.getTradeState().getDeliverStatus()))) {

                // 当前第三方订单 商品、赠品全部发货完毕，不再同步linkedmall发货信息
                long count = Stream.concat(tradeVO.getTradeItems().stream(), tradeVO.getGifts().stream())
                        .filter(tradeItemVO -> tradeItemVO.getDeliveredNum() < tradeItemVO.getNum()).count();
                if(count == 0) {
                    return;
                }

                String customerId = tradeVO.getBuyer().getId();
                List<String> lmOrderIds = tradeVO.getThirdPlatformOrderIds();

                SbcLogisticsQueryResponse response = linkedMallOrderQueryProvider.getOrderLogistics(SbcLogisticsQueryRequest.builder()
                        .lmOrderId(NumberUtils.toLong(lmOrderIds.get(0)))
                        .bizUid(customerId)
                        .build()).getContext();

                if(Objects.isNull(response) || CollectionUtils.isEmpty(response.getDataItems())) {
                    return;
                }

                // 获取当前订单所有物流单号集合
                List<String> logisticNos = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(tradeVO.getTradeDelivers())) {
                    tradeVO.getTradeDelivers().stream().filter(Objects::nonNull).forEach(v -> {
                        if(Objects.nonNull(v.getLogistics()) && StringUtils.isNotBlank(v.getLogistics().getLogisticNo())) {
                            logisticNos.add(v.getLogistics().getLogisticNo());
                        }
                    });
                }

                // 该笔linkedmall二次拆单的订单 对应的 发货清单集合
                List<TradeDeliverVO> tradeDeliverVOList = new ArrayList<>();

                response.getDataItems().stream()
                        .filter(dataItem -> Objects.nonNull(dataItem) && StringUtils.isNotBlank(dataItem.getMailNo()))
                        .forEach(dataItem -> {

                            // 物流信息存档入库
                            this.saveLinkedMallLogictics(dataItem, tradeVO.getId(), lmOrderIds.get(0), customerId);

                            // 商城订单物流信息中 没有 linkedmall 物流单号 则新增到该订单中
                            if (CollectionUtils.isEmpty(logisticNos) || !logisticNos.contains(dataItem.getMailNo())) {

                                // 同步配送商品信息
                                List<ShippingItemVO> shippingItems = new ArrayList<>();
                                tradeVO.getTradeItems().stream()
                                        .filter(tradeItemVO -> ThirdPlatformType.LINKED_MALL.equals(tradeItemVO.getThirdPlatformType()))
                                        .forEach(tradeItemVO -> {
                                            ShippingItemVO shippingItemVO = KsBeanUtil.convert(tradeItemVO, ShippingItemVO.class);
                                            shippingItemVO.setItemName(tradeItemVO.getSkuName());
                                            shippingItemVO.setItemNum(tradeItemVO.getNum());
                                            shippingItems.add(shippingItemVO);
                                            // 同步已发货数量、发货状态
                                            tradeItemVO.setDeliveredNum(tradeItemVO.getNum());
                                            tradeItemVO.setDeliverStatus(DeliverStatus.SHIPPED);
                                            tradeItemList.put(tradeItemVO.getSkuId(),tradeItemVO);
                                        });

                                // 同步配送赠品信息
                                List<ShippingItemVO> giftItems = new ArrayList<>();
                                tradeVO.getGifts().forEach(tradeItemVO -> {
                                    ShippingItemVO shippingItemVO = KsBeanUtil.convert(tradeItemVO, ShippingItemVO.class);
                                    shippingItemVO.setItemName(tradeItemVO.getSkuName());
                                    shippingItemVO.setItemNum(tradeItemVO.getNum());
                                    giftItems.add(shippingItemVO);
                                    // 同步已发货数量、发货状态
                                    tradeItemVO.setDeliveredNum(tradeItemVO.getNum());
                                    tradeItemVO.setDeliverStatus(DeliverStatus.SHIPPED);
                                    giftList.put(tradeItemVO.getSkuId(),tradeItemVO);
                                });


                                // 同步发货时间,大于两条记录时取菜鸟裹裹的倒数第二条记录的时间作为发货时间
                                LocalDateTime deliverTime = null;
                                if (CollectionUtils.isNotEmpty(dataItem.getLogisticsDetailList())) {
                                    String ocurrTimeStr =
                                            dataItem.getLogisticsDetailList().get(dataItem.getLogisticsDetailList().size() - 1).getOcurrTimeStr();
                                    if (dataItem.getLogisticsDetailList().size() > 1) {
                                        ocurrTimeStr = dataItem.getLogisticsDetailList().get(dataItem.getLogisticsDetailList().size() - 2).getOcurrTimeStr();
                                    }
                                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    deliverTime = LocalDateTime.parse(ocurrTimeStr, df);
                                }

                                // 物流信息
                                LogisticsVO logistics = LogisticsVO.builder()
                                        .logisticCompanyName(dataItem.getLogisticsCompanyName())
                                        .logisticNo(dataItem.getMailNo())
                                        .logisticStandardCode(dataItem.getLogisticsCompanyCode())
                                        // 设置linkedmall物流的订单号、用户id
                                        .thirdPlatformOrderId(lmOrderIds.get(0))
                                        .buyerId(customerId)
                                        .build();

                                if(CollectionUtils.isNotEmpty(tradeVO.getOutOrderIds())) {
                                    // 设置linkedmall物流的淘宝订单号
                                    logistics.setOutOrderId(tradeVO.getOutOrderIds().get(0));
                                }

                                // 生成新的发货清单
                                TradeDeliverVO tradeDeliverVO = TradeDeliverVO.builder()
                                        .tradeId(tradeVO.getId())
                                        .deliverId(generatorService.generate("TD"))
                                        .deliverTime(deliverTime)
                                        .thirdPlatformType(ThirdPlatformType.LINKED_MALL)
                                        .shippingItems(shippingItems)
                                        .giftItemList(giftItems)
                                        .logistics(logistics)
                                        .shipperType(ShipperType.PROVIDER)
                                        .status(DeliverStatus.SHIPPED)
                                        .providerName(tradeVO.getSupplier().getSupplierName())
                                        .build();

                                tradeDeliverVOList.add(tradeDeliverVO);
                            }
                        });
                // 填充并更新 linkedmall订单信息
                if(CollectionUtils.isNotEmpty(tradeDeliverVOList)) {
                    tradeVO.getTradeDelivers().addAll(tradeDeliverVOList);
                    tradeDeliverVOs.addAll(tradeDeliverVOList);
                    ThirdPlatformTrade thirdPlatformTrade = KsBeanUtil.convert(tradeVO, ThirdPlatformTrade.class);
                    // 添加日志
                    tradeEventLog.setEventDetail(String.format("同步linkedmall订单%s发货清单", thirdPlatformTrade.getId()));
                    thirdPlatformTrade.appendTradeEventLog(tradeEventLog);
                    thirdPlatformTradeRepository.save(thirdPlatformTrade);
                }
            }
        });

        // 更新父订单、主订单配送信息
        if(CollectionUtils.isNotEmpty(tradeDeliverVOs)) {
            // 同步provideTrade商品/赠品对应发货数和发货状态,发货清单
            List<TradeDeliverVO> providerDelivers = this.syncTradeDelivers(providerTrade, tradeDeliverVOs,
                    tradeItemList,giftList);

            providerTrade.getTradeDelivers().addAll(providerDelivers);
            ProviderTrade providerTrade1 = KsBeanUtil.convert(providerTrade, ProviderTrade.class);
            // 添加日志
            tradeEventLog.setEventDetail(String.format("同步linkedmall订单%s发货清单", providerTrade1.getId()));
            providerTrade1.appendTradeEventLog(tradeEventLog);
            providerTradeService.updateProviderTrade(providerTrade1);

            // 同步Trade商品对应发货数和发货状态,发货清单
            List<TradeDeliverVO> tradeDelivers = this.syncTradeDelivers(trade, providerDelivers, tradeItemList,giftList);
            trade.getTradeDelivers().addAll(tradeDelivers);

            Trade trade1 = KsBeanUtil.convert(trade, Trade.class);
            // 添加日志
            tradeEventLog.setEventDetail(String.format("同步linkedmall订单%s发货清单", trade1.getId()));
            trade1.appendTradeEventLog(tradeEventLog);
            tradeService.updateTrade(trade1);
        }
    }

    // 同步发货清单
    private List<TradeDeliverVO> syncTradeDelivers(TradeVO trade, List<TradeDeliverVO> tradeDeliverVOs,
                                                   Map<String, TradeItemVO> tradeItemList,
                                                   Map<String, TradeItemVO> giftList) {

        List<TradeDeliverVO> providerDelivers = tradeDeliverVOs.stream().map(tradeDeliverVO -> {
            TradeDeliverVO deliverVO = KsBeanUtil.convert(tradeDeliverVO, TradeDeliverVO.class);
            deliverVO.setTradeId(trade.getId());
            deliverVO.setSunDeliverId(tradeDeliverVO.getDeliverId());
            deliverVO.setDeliverId(generatorService.generate("TD"));
            deliverVO.setShipperType(ShipperType.PROVIDER);
            return deliverVO;
        }).collect(Collectors.toList());

        // 同步商品对应发货数和发货状态
        trade.getTradeItems().stream()
                .filter(tradeItemVO -> tradeItemList.containsKey(tradeItemVO.getSkuId()))
                .forEach(tradeItemVO -> {
                    TradeItemVO vo = tradeItemList.get(tradeItemVO.getSkuId());
                    tradeItemVO.setDeliveredNum(tradeItemVO.getDeliveredNum() + vo.getDeliveredNum());
                    tradeItemVO.setDeliverStatus(DeliverStatus.SHIPPED);
                });
        // 同步赠品对应发货数和发货状态
        trade.getGifts().stream()
                .filter(tradeItemVO -> giftList.containsKey(tradeItemVO.getSkuId()))
                .forEach(tradeItemVO -> {
            TradeItemVO vo = giftList.get(tradeItemVO.getSkuId());
            tradeItemVO.setDeliveredNum(tradeItemVO.getDeliveredNum() + vo.getDeliveredNum());
            tradeItemVO.setDeliverStatus(DeliverStatus.SHIPPED);
        });
        return providerDelivers;
    }

    // linkedmall 物流信息入库
    private void saveLinkedMallLogictics(QueryLogisticsResponse.DataItem dataItem, String tradeId, String lmOrderId, String customerId) {
        LinkedMallTradeLogistics logistics = new LinkedMallTradeLogistics();
        logistics.setTradeId(tradeId);
        logistics.setCustomerId(customerId);
        logistics.setLmOrderId(lmOrderId);

        logistics.setMailNo(dataItem.getMailNo());
        logistics.setDataProvider(dataItem.getDataProvider());
        logistics.setDataProviderTitle(dataItem.getDataProviderTitle());
        logistics.setLogisticsCompanyName(dataItem.getLogisticsCompanyName());
        logistics.setLogisticsCompanyCode(dataItem.getLogisticsCompanyCode());

        dataItem.getLogisticsDetailList().forEach(v -> {
            LinkedMallLogisticsDetail logisticsDetail = LinkedMallLogisticsDetail.builder()
                    .standerdDesc(v.getStanderdDesc())
                    .ocurrTimeStr(v.getOcurrTimeStr())
                    .build();
            logistics.getLogisticsDetailList().add(logisticsDetail);

        });

        dataItem.getGoods().forEach(v -> {
            LinkedMallGoods goods = LinkedMallGoods.builder()
                    .goodName(v.getGoodName())
                    .quantity(v.getQuantity())
                    .itemId(String.valueOf(v.getItemId()))
                    .build();
            logistics.getGoods().add(goods);
        });

        linkedMallTradeLogisticsService.updateLinkedMallLogistics(logistics);
    }

    public void voidTrade(String providerTradeId){
        mongoTemplate.updateMulti(new Query(Criteria.where("parentId").is(providerTradeId)), new Update().set("tradeState.flowState", FlowState.VOID), ThirdPlatformTrade.class);
    }


    /**
     * 合并订单明细数据
     * @param tradeItems 普通商品
     * @param gifts 赠品
     * @return 新合并数据
     */
    private List<TradeItem> zipLinkedMallItem(List<TradeItem> tradeItems, List<TradeItem> gifts) {
        List<TradeItem> items = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tradeItems)) {
            items.addAll(tradeItems.stream()
                    .filter(i -> Objects.equals(ThirdPlatformType.LINKED_MALL, i.getThirdPlatformType()))
                    .map(i -> {
                        TradeItem item = new TradeItem();
                        item.setThirdPlatformSpuId(i.getThirdPlatformSpuId());
                        item.setThirdPlatformSkuId(i.getThirdPlatformSkuId());
                        item.setNum(i.getNum());
                        return item;
                    }).collect(Collectors.toList()));
        }

        if (CollectionUtils.isNotEmpty(gifts)) {
            List<TradeItem> giftItems = gifts.stream()
                    .filter(i -> Objects.equals(ThirdPlatformType.LINKED_MALL, i.getThirdPlatformType()))
                    .map(i -> {
                        TradeItem item = new TradeItem();
                        item.setThirdPlatformSpuId(i.getThirdPlatformSpuId());
                        item.setThirdPlatformSkuId(i.getThirdPlatformSkuId());
                        item.setNum(i.getNum());
                        return item;
                    }).collect(Collectors.toList());

            if(CollectionUtils.isNotEmpty(items)) {
                items = IteratorUtils.zip(items, giftItems,
                        (a, b) -> a.getThirdPlatformSpuId().equals(b.getThirdPlatformSpuId()) && a.getThirdPlatformSkuId().equals(b.getThirdPlatformSkuId()),
                        (a, b) -> {a.setNum(a.getNum() + b.getNum());});
            }else{
                items.addAll(giftItems);
            }
        }
        return items;
    }
}
