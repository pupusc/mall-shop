package com.soybean.mall.order.miniapp.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.soybean.mall.order.api.request.order.CreateWxOrderAndPayRequest;
import com.soybean.mall.order.bean.dto.WxLogisticsInfoDTO;
import com.soybean.mall.order.bean.vo.MiniProgramOrderReportVO;
import com.soybean.mall.order.bean.vo.OrderCommitResultVO;
import com.soybean.mall.order.enums.MiniOrderOperateType;
import com.soybean.mall.order.miniapp.model.root.MiniOrderOperateResult;
import com.soybean.mall.order.miniapp.repository.MiniOrderOperateResultRepository;
import com.soybean.mall.order.trade.model.OrderReportDetailDTO;
import com.soybean.mall.wx.mini.common.bean.request.WxSendMessageRequest;
import com.soybean.mall.wx.mini.common.controller.CommonController;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.dto.*;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import com.soybean.mall.wx.mini.order.bean.request.WxDeliveryReceiveRequest;
import com.soybean.mall.wx.mini.order.bean.request.WxDeliverySendRequest;
import com.soybean.mall.wx.mini.order.bean.request.WxOrderPayRequest;
import com.soybean.mall.wx.mini.order.bean.response.GetPaymentParamsResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.value.Logistics;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class TradeOrderService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WxOrderApiController wxOrderApiController;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private WxOrderService wxOrderService;



    @Value("${wx.logistics}")
    private String wxLogisticsStr;

    private final String BATCH_UPDATE_DELIVERY_STATUS_TO_WECHAT_LOCKS = "syncDeliveryStatusToWechat";


    /**
     * 批量同步发货状态到微信-查询本地
     *
     * @param pageSize
     */
    public void batchSyncDeliveryStatusToWechat(int pageSize, String ptid) {
        RLock lock = redissonClient.getLock(BATCH_UPDATE_DELIVERY_STATUS_TO_WECHAT_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return;
        }
        lock.lock();
        try {
            /**
             * 查询部分发货或全部发货且未更新全部状态的小程序订单
             */
            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()));
            criterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
            criterias.add(Criteria.where("tradeState.deliverStatus").ne(DeliverStatus.NOT_YET_SHIPPED.getStatusId()));
            criterias.add(Criteria.where("channelType").is(ChannelType.MINIAPP.toString()));
            criterias.add(Criteria.where("miniProgram.syncStatus").is(0));
            criterias.add(Criteria.where("cycleBuyFlag").is(false));
            //单个订单发货状态同步
            if (StringUtils.isNoneBlank(ptid)) {
                criterias.add(Criteria.where("id").is(ptid));
            }
            if (pageSize <= 0) {
                pageSize = 200;
            }
            Criteria newCriteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
            Query query = new Query(newCriteria).limit(pageSize);
            query.with(Sort.by(Sort.Direction.ASC, "tradeState.payTime"));
            List<Trade> trades = mongoTemplate.find(query, Trade.class);

            /**
             * 周期购订单发货单状态更新
             */
            Criteria cycleBuycriteria = new Criteria();
            cycleBuycriteria.andOperator(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()),
                    Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()),
                    Criteria.where("tradeState.deliverStatus").ne(DeliverStatus.NOT_YET_SHIPPED.getStatusId()),
                    Criteria.where("channelType").is(ChannelType.MINIAPP.toString()),
                    Criteria.where("miniProgram.syncStatus").is(0),
                    Criteria.where("cycleBuyFlag").is(true));

            Query cycleQuery = new Query(cycleBuycriteria).limit(pageSize);
            query.with(Sort.by(Sort.Direction.ASC, "tradeState.payTime"));
            List<Trade> cycleBuyTradeList = mongoTemplate.find(cycleQuery, Trade.class);

            List<Trade> totalTradeList =
                    Stream.of(trades, cycleBuyTradeList).flatMap(Collection::stream).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(totalTradeList)) {
                log.info("#批量同步发货状态到微信的订单:{}", totalTradeList);
                totalTradeList.stream().forEach(trade -> {
                    syncDeliveryStatusToWechat(trade);
                    log.info("#批量同步发货状态到微信的订单:{}", trade);
                });
            }
        } catch (Exception e) {
            log.error("#批量同步发货状态异常:{}", e);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    private void syncDeliveryStatusToWechat(Trade trade){
        //判断是否需要同步
        List<String> deliveryIds = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(trade.getMiniProgram().getDelivery())) {
            deliveryIds = trade.getMiniProgram().getDelivery().stream().map(TradeDeliver::getDeliverId).collect(Collectors.toList());
        }
        List<String> delvieryIdsNew = deliveryIds;
        List<TradeDeliver> unSyncDelivery = trade.getTradeDelivers().stream().filter(tradeDeliver -> !ObjectUtils.isEmpty(tradeDeliver)
                        && !delvieryIdsNew.contains(tradeDeliver.getDeliverId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(unSyncDelivery)) {
              log.info("没有需要同步的物流信息，trade：{}",trade);
              return;
        }
        try {
            WxDeliverySendRequest request = new WxDeliverySendRequest();
            request.setOpenid(trade.getBuyer().getOpenId());
            request.setOutOrderId(trade.getId());
            request.setFinishAllDelivery(Objects.equals(trade.getTradeState().getDeliverStatus(), DeliverStatus.SHIPPED) ? 1 : 0);

            List<WxDeliverySendRequest.WxDeliveryInfo> deliveryInfos = new ArrayList<>();

            for (TradeDeliver delivery : unSyncDelivery) {
                if (delivery.getLogistics() == null) {
                    continue;
                }
                WxDeliverySendRequest.WxDeliveryInfo deliveryInfo = new WxDeliverySendRequest.WxDeliveryInfo();

                deliveryInfo.setDeliveryId(getWxLogisticsCode(delivery.getLogistics().getLogisticStandardCode(), delivery.getLogistics().getLogisticCompanyName()));
                deliveryInfo.setWaybillId(delivery.getLogistics().getLogisticNo());
                List<WxProductDTO> productDTS = new ArrayList<>();
                for (ShippingItem shippingItem : delivery.getShippingItems()) {
                    WxProductDTO wxProductDTO = new WxProductDTO();
                    wxProductDTO.setOutProductId(trade.getTradeItems().stream().filter(p -> p.getSkuId().equals(shippingItem.getSkuId())).findFirst().get().getSpuId());
                    wxProductDTO.setOutSkuId(shippingItem.getSkuId());
                    wxProductDTO.setPrroductNum(shippingItem.getItemNum().intValue());
                    productDTS.add(wxProductDTO);
                }
                deliveryInfo.setProductInfoList(productDTS);
                deliveryInfos.add(deliveryInfo);
            }
            request.setDeliveryList(deliveryInfos);
            BaseResponse<WxResponseBase> result = wxOrderApiController.deliverySend(request);
            if (result != null && result.getContext().isSuccess()) {
                //全部发货且已经全部同步
                if (Objects.equals(trade.getTradeState().getDeliverStatus(), DeliverStatus.SHIPPED)) {
                    trade.getMiniProgram().setSyncStatus(1);
                }
                trade.getMiniProgram().setDelivery(trade.getTradeDelivers());
                tradeRepository.save(trade);
                wxOrderService.sendWxDeliveryMessage(trade);
            }
        }catch (Exception e){
            log.warn("微信小程序同步发货状态失败，trade:{}",trade,e);
        }

    }

    private String getWxLogisticsCode(String code,String name){
        if(StringUtils.isEmpty(wxLogisticsStr)){
            return "OTHERS";
        }
        List<WxLogisticsInfoDTO> wxLogisticsMap = JSON.parseArray(wxLogisticsStr,WxLogisticsInfoDTO.class);
        Optional<WxLogisticsInfoDTO> optional = null;
        if(StringUtils.isNotEmpty(code)){
            optional = wxLogisticsMap.stream().filter(p->p.getErpLogisticCode().equals(code)).findFirst();
            if(optional.isPresent()){
                return optional.get().getLogisticCode();
            }
        }
        optional = wxLogisticsMap.stream().filter(p->p.getLogisticName().equals(name)).findFirst();
        if(optional.isPresent()){
            return optional.get().getLogisticCode();
        }
        return "OTHERS";
    }

    public void createWxOrderAndPay(String tid){
        Trade trade = tradeRepository.findById(tid).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{tid});
        }
        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            return;
        }
        log.info("微信小程序0元订单创建并支付start,tid:{}",tid);
        WxCreateOrderRequest wxCreateOrderRequest = null;
        try {
            //先创建订单
            wxCreateOrderRequest = wxOrderService.buildRequest(trade);
            BaseResponse<WxCreateOrderResponse> orderResult = wxOrderApiController.addOrder(wxCreateOrderRequest);
            log.info("微信小程序0元订单创建，requet:{},response:{}", wxCreateOrderRequest, orderResult);
            if (orderResult == null || orderResult.getContext() == null || !orderResult.getContext().isSuccess()) {
                wxOrderService.addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), (orderResult != null ? JSON.toJSONString(orderResult) : "空"), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
                return;
            }
        } catch (Exception e) {
            log.error("微信小程序创建订单失败，tid：{}", tid, e);
            wxOrderService.addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), e.getMessage(), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
            return;
        }
        log.info("微信小程序0元订单创建成功，同步支付结果start,tid:{}",tid);
        wxOrderService.syncWxOrderPay(trade,trade.getId());

    }

    public PaymentParamsDTO createWxOrderAndGetPaymentsParams(String tid){
        Trade trade = tradeRepository.findById(tid).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{tid});
        }
        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            return null;
        }
        log.info("微信小程序订单创建并获取支付参数start,tid:{}",tid);
        WxCreateOrderRequest wxCreateOrderRequest = null;
        try {
            //先创建订单
            wxCreateOrderRequest = wxOrderService.buildRequest(trade);
            BaseResponse<WxCreateOrderResponse> orderResult = wxOrderApiController.addOrder(wxCreateOrderRequest);
            log.info("微信小程序订单创建，request:{},response:{}", wxCreateOrderRequest, orderResult);
            if (orderResult == null || orderResult.getContext() == null || !orderResult.getContext().isSuccess()) {
                wxOrderService.addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), (orderResult != null ? JSON.toJSONString(orderResult) : "空"), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
                return null;
            }
        } catch (Exception e) {
            log.error("微信小程序创建订单失败，tid：{}", tid, e);
            wxOrderService.addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), e.getMessage(), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
            return null;
        }
        log.info("微信小程序订单创建成功，获取支付参数start,tid:{}",tid);
        return wxOrderService.getPaymentParams(trade);

    }











}
