package com.soybean.mall.order.miniapp.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.UpdateResult;
import com.soybean.mall.order.bean.dto.WxLogisticsInfoDTO;
import com.soybean.mall.order.dszt.TransferService;
import com.soybean.mall.order.enums.MiniOrderOperateType;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.dto.PaymentParamsDTO;
import com.soybean.mall.wx.mini.order.bean.dto.WxProductDTO;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import com.soybean.mall.wx.mini.order.bean.request.WxDeliverySendRequest;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.order.api.request.trade.SyncOrderDataRequest;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
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

	@Autowired
	private TransferService transferService;

	@Autowired
	private ShopCenterOrderProvider shopCenterOrderProvider;

	@Autowired
	private RedisTemplate redisTemplate;

	@Value("${wx.logistics}")
	private String wxLogisticsStr;

	private final String BATCH_UPDATE_DELIVERY_STATUS_TO_WECHAT_LOCKS = "syncDeliveryStatusToWechat";

	private final String SYNC_ORDER_DATA_REDIS_KEY = "trade_query_id";

	/**
	 * 批量同步发货状态到微信-查询本地
	 *
	 * @param pageSize
	 */
	public void batchSyncDeliveryStatusToWechat(int pageSize, String ptid, String startTime) {
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
			criterias.add(Criteria.where("miniProgramScene").is(2));
			criterias.add(Criteria.where("miniProgram.syncStatus").is(0));
			criterias.add(Criteria.where("cycleBuyFlag").is(false));
			// 单个订单发货状态同步
			if (StringUtils.isNoneBlank(ptid)) {
				criterias.add(Criteria.where("id").is(ptid));
			}
			if (StringUtils.isNotBlank(startTime)) {

				criterias.add(Criteria.where("tradeState.payTime")
						.gte(LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1))));
			}
			if (pageSize <= 0) {
				pageSize = 200;
			}
			Criteria newCriteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
			Query query = new Query(newCriteria).limit(pageSize);
			query.with(Sort.by(Sort.Direction.ASC, "tradeState.payTime"));
			log.info("TradeOrderService batchSyncDeliveryStatusToWechat param:{}", query);

			List<Trade> trades = mongoTemplate.find(query, Trade.class);

			/**
			 * 周期购订单发货单状态更新
			 */
			Criteria cycleBuycriteria = new Criteria();
			cycleBuycriteria.andOperator(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()),
					Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()),
					Criteria.where("tradeState.deliverStatus").ne(DeliverStatus.NOT_YET_SHIPPED.getStatusId()),
					Criteria.where("channelType").is(ChannelType.MINIAPP.toString()),
					Criteria.where("miniProgram.syncStatus").is(0), Criteria.where("cycleBuyFlag").is(true));

			Query cycleQuery = new Query(cycleBuycriteria).limit(pageSize);
			query.with(Sort.by(Sort.Direction.ASC, "tradeState.payTime"));
			List<Trade> cycleBuyTradeList = mongoTemplate.find(cycleQuery, Trade.class);

			List<Trade> totalTradeList = Stream.of(trades, cycleBuyTradeList).flatMap(Collection::stream)
					.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(totalTradeList)) {
				log.info("#批量同步发货状态到微信的订单数量为:{}", totalTradeList.size());
				totalTradeList.stream().forEach(trade -> {
					syncDeliveryStatusToWechat(trade);
					log.info("#批量同步发货状态到微信的订单:{}", trade);
				});
			}
		} catch (Exception e) {
			log.error("#批量同步发货状态异常:{}", e);
		} finally {
			// 释放锁
			lock.unlock();
		}
	}

	private void syncDeliveryStatusToWechat(Trade trade) {
		// 判断是否需要同步
		List<String> deliveryIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(trade.getMiniProgram().getDelivery())) {
			deliveryIds = trade.getMiniProgram().getDelivery().stream().map(TradeDeliver::getDeliverId)
					.collect(Collectors.toList());
		}
		List<String> delvieryIdsNew = deliveryIds;
		List<TradeDeliver> unSyncDelivery = trade.getTradeDelivers().stream()
				.filter(tradeDeliver -> !ObjectUtils.isEmpty(tradeDeliver)
						&& !delvieryIdsNew.contains(tradeDeliver.getDeliverId()))
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(unSyncDelivery)) {
			log.info("没有需要同步的物流信息，trade：{}", trade);
			return;
		}
		try {
			WxDeliverySendRequest request = new WxDeliverySendRequest();
			request.setOpenid(trade.getBuyer().getOpenId());
			request.setOutOrderId(trade.getId());
			request.setFinishAllDelivery(
					Objects.equals(trade.getTradeState().getDeliverStatus(), DeliverStatus.SHIPPED) ? 1 : 0);

			List<WxDeliverySendRequest.WxDeliveryInfo> deliveryInfos = new ArrayList<>();

			LocalDateTime finishDeliverTime = null;
			for (TradeDeliver delivery : unSyncDelivery) {
				if (delivery.getLogistics() == null) {
					continue;
				}
				WxDeliverySendRequest.WxDeliveryInfo deliveryInfo = new WxDeliverySendRequest.WxDeliveryInfo();
				if (finishDeliverTime == null) {
					finishDeliverTime = delivery.getDeliverTime();
				} else {
					if (delivery.getDeliverTime() != null && delivery.getDeliverTime().isAfter(finishDeliverTime)) {
						finishDeliverTime = delivery.getDeliverTime();
					}
				}

				deliveryInfo.setDeliveryId(getWxLogisticsCode(delivery.getLogistics().getLogisticStandardCode(),
						delivery.getLogistics().getLogisticCompanyName()));
				deliveryInfo.setWaybillId(delivery.getLogistics().getLogisticNo());
				List<WxProductDTO> productDTS = new ArrayList<>();
				for (ShippingItem shippingItem : delivery.getShippingItems()) {
					WxProductDTO wxProductDTO = new WxProductDTO();
					wxProductDTO.setOutProductId(trade.getTradeItems().stream()
							.filter(p -> p.getSkuId().equals(shippingItem.getSkuId())).findFirst().get().getSpuId());
					wxProductDTO.setOutSkuId(shippingItem.getSkuId());
					wxProductDTO.setPrroductNum(shippingItem.getItemNum().intValue());
					productDTS.add(wxProductDTO);
				}
				deliveryInfo.setProductInfoList(productDTS);
				deliveryInfos.add(deliveryInfo);
			}
			request.setDeliveryList(deliveryInfos);

			// 表示完成发货,此处以当前时间确定
			if (Objects.equals(request.getFinishAllDelivery(), 1)) {
				request.setShipDoneTime(
						finishDeliverTime == null ? DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_1)
								: DateUtil.format(finishDeliverTime, DateUtil.FMT_TIME_1));
			}
			BaseResponse<WxResponseBase> result = wxOrderApiController.deliverySend(request);
			if (result != null && result.getContext().isSuccess()) {
				// 全部发货且已经全部同步
				if (Objects.equals(trade.getTradeState().getDeliverStatus(), DeliverStatus.SHIPPED)) {
					trade.getMiniProgram().setSyncStatus(1);
				}
				trade.getMiniProgram().setDelivery(trade.getTradeDelivers());
				tradeRepository.save(trade);
				wxOrderService.sendWxDeliveryMessage(trade);
			}
		} catch (Exception e) {
			log.warn("微信小程序同步发货状态失败，trade:{}", trade, e);
		}

	}

	private String getWxLogisticsCode(String code, String name) {
		if (StringUtils.isEmpty(wxLogisticsStr)) {
			return "OTHERS";
		}
		List<WxLogisticsInfoDTO> wxLogisticsMap = JSON.parseArray(wxLogisticsStr, WxLogisticsInfoDTO.class);
		Optional<WxLogisticsInfoDTO> optional = null;
		if (StringUtils.isNotEmpty(code)) {
			optional = wxLogisticsMap.stream().filter(p -> p.getErpLogisticCode().equals(code)).findFirst();
			if (optional.isPresent()) {
				return optional.get().getLogisticCode();
			}
		}
		optional = wxLogisticsMap.stream().filter(p -> p.getLogisticName().equals(name)).findFirst();
		if (optional.isPresent()) {
			return optional.get().getLogisticCode();
		}
		return "OTHERS";
	}

	public void createWxOrderAndPay(String tid) {
		Trade trade = tradeRepository.findById(tid).orElse(null);
		if (trade == null) {
			throw new SbcRuntimeException("K-050100", new Object[] { tid });
		}
		if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
			return;
		}
		log.info("微信小程序0元订单创建并支付start,tid:{}", tid);
		WxCreateOrderRequest wxCreateOrderRequest = null;
		try {
			// 先创建订单
			wxCreateOrderRequest = wxOrderService.buildRequest(trade);
			BaseResponse<WxCreateOrderResponse> orderResult = wxOrderApiController.addOrder(wxCreateOrderRequest);
			log.info("微信小程序0元订单创建，requet:{},response:{}", wxCreateOrderRequest, orderResult);
			if (orderResult == null || orderResult.getContext() == null || !orderResult.getContext().isSuccess()) {
				wxOrderService.addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest),
						(orderResult != null ? JSON.toJSONString(orderResult) : "空"),
						MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
				return;
			}
		} catch (Exception e) {
			log.error("微信小程序创建订单失败，tid：{}", tid, e);
			wxOrderService.addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), e.getMessage(),
					MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
			return;
		}
		log.info("微信小程序0元订单创建成功，同步支付结果start,tid:{}", tid);
		wxOrderService.syncWxOrderPay(trade, trade.getId());

	}

	public PaymentParamsDTO createWxOrderAndGetPaymentsParams(String tid) {
		Trade trade = tradeRepository.findById(tid).orElse(null);
		if (trade == null) {
			throw new SbcRuntimeException("K-050100", new Object[] { tid });
		}
		if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
			throw new SbcRuntimeException("K-050144", new Object[] { tid });
		}
		log.info("微信小程序订单创建并获取支付参数start,tid:{}", tid);
		WxCreateOrderRequest wxCreateOrderRequest = null;
//        try {
		wxCreateOrderRequest = wxOrderService.buildRequest(trade);
		if (CollectionUtils.isEmpty(wxCreateOrderRequest.getOrderDetail().getProductInfos())) {
			throw new SbcRuntimeException("999999", "下单商品没有金额信息，不可以下单");
		}
		BaseResponse<WxCreateOrderResponse> orderResult = wxOrderApiController.addOrder(wxCreateOrderRequest);
		log.info("微信小程序订单创建，request:{},response:{}", wxCreateOrderRequest, orderResult);
		if (orderResult == null || orderResult.getContext() == null || !orderResult.getContext().isSuccess()) {
//            wxOrderService.addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), (orderResult != null ? JSON.toJSONString(orderResult) : "空"), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
			throw new SbcRuntimeException("K-99999", orderResult == null ? "下单失败"
					: StringUtils.isBlank(orderResult.getMessage()) ? "下单失败，网络异常" : orderResult.getMessage());
		}
//        } catch (Exception e) {
//            log.error("微信小程序创建订单失败，tid：{}", tid, e);
//            wxOrderService.addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), e.getMessage(), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
//            return null;
//        }
		log.info("微信小程序订单创建成功，获取支付参数start,tid:{}", tid);
		return wxOrderService.getPaymentParams(trade.getBuyer().getOpenId(), trade.getId());

	}

	private AtomicBoolean start = new AtomicBoolean(false);
	/**
	 * @return
	 */
	public BaseResponse syncOrderDataAll(SyncOrderDataRequest syncOrderDataRequest) {
		if ("stop".equals(syncOrderDataRequest.getCommand())) {
			start.set(false);
			return BaseResponse.success(true);
		}
		if (start.get()) {
			return BaseResponse.error("正在进行中，请重置处理状态!");
		}

		log.info("====>>电商中台订单同步开始<<====");
		//指定id
		if (StringUtil.isNotBlank(syncOrderDataRequest.getId())) {
			Query query = new Query(Criteria.where("_id").is(syncOrderDataRequest.getId()));
			export(query, false);
			return BaseResponse.success(true);
		}

		String queryId = (String) redisTemplate.opsForValue().get(SYNC_ORDER_DATA_REDIS_KEY);
		if (StringUtil.isBlank(queryId)) {
			queryId = "0";
		}

		Query query = new Query(Criteria.where("_id").gt(queryId).and("tradeState.payState").is("PAID").and("yzTid").exists(false));
		Long count = mongoTemplate.count(query, Trade.class);
		if (count == 0) {
			return BaseResponse.success(true);
		}

		Integer foreachTimes = (int) (count / 1000) + 1;
		for (int i = 0; i < foreachTimes; i++) {
			Integer offset = (i - 1) * 1000;
			query = query.skip(offset).limit(1000).with(Sort.by(Sort.Direction.ASC, "_id"));
			export(query, true);
		}
		log.info("====>>电商中台订单同步结束<<====");
		return BaseResponse.success(true);
	}

	private void export(Query query, boolean setRedis) {
		List<Trade> tradeList = mongoTemplate.find((query), Trade.class);
		for (Trade trade : tradeList) {
			if (!start.get()) {
				return;
			}
			try {
				log.warn("==>>电商中台订单同步开始：tradeId={}", trade.getId());
				CreateOrderReq createOrderReq = transferService.trade2CreateOrderReq(trade);
				createOrderReq.setPlatformCode("WAN_MI");
				createOrderReq.setPlatformOrderId(trade.getId());
				updateVersion(trade.getId(), 0);
				shopCenterOrderProvider.createOrder(createOrderReq);
				updateVersion(trade.getId(), 1);
				log.warn("==>>电商中台订单同步成功：tradeId={}", trade.getId());
			} catch (Exception e) {
				log.error("e:{}", e);
				log.warn("==>>电商中台订单同步错误：tradeId={}", trade.getId());
			}
			if (setRedis) {
				redisTemplate.opsForValue().set(SYNC_ORDER_DATA_REDIS_KEY, trade.getId());
			}
		}
	}

	private void updateVersion(String id, int version) {
		//更新本地版本
		UpdateResult updateResult = mongoTemplate.updateFirst(
				new Query(Criteria.where("_id").is(id)),
				new Update().set("sVersion", version),
				Trade.class);

		if (updateResult.getModifiedCount() != 1) {
			log.warn("更新订单的版本信息影响数量错误，主键:{}, 版本:{}, 更新数量:{}", id, version, updateResult.getModifiedCount());
			throw new RuntimeException("更新订单的版本信息错误");
		}
	}
}
