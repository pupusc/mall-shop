package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.enums.SaleAfterCompensationTypeEnum;
import com.wanmi.sbc.erp.api.enums.SaleAfterCreateEnum;
import com.wanmi.sbc.erp.api.enums.SaleAfterRefundTypeEnum;
import com.wanmi.sbc.erp.api.enums.UnifiedOrderDeliveryStatusEnum;
import com.wanmi.sbc.erp.api.enums.UnifiedOrderItemStatusEnum;
import com.wanmi.sbc.erp.api.enums.UnifiedOrderObjectTypeEnum;
import com.wanmi.sbc.erp.api.enums.UnifiedSaleAfterStatusEnum;
import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterProductProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterSaleAfterProvider;
import com.wanmi.sbc.erp.api.req.QuerySaleAfterOrderReq;
import com.wanmi.sbc.erp.api.req.SaleAfterBookSubscribeReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCancelReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCompensateReq;
import com.wanmi.sbc.erp.api.req.SaleAfterConfirmDeliverReq;
import com.wanmi.sbc.erp.api.req.SaleAfterConfirmPaymentReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateReq;
import com.wanmi.sbc.erp.api.req.SaleAfterExamineReq;
import com.wanmi.sbc.erp.api.req.SaleAfterExchangeGoodsReq;
import com.wanmi.sbc.erp.api.req.SaleAfterFillDeliverInfoReq;
import com.wanmi.sbc.erp.api.req.SaleAfterGoodsReq;
import com.wanmi.sbc.erp.api.req.SaleAfterItemReq;
import com.wanmi.sbc.erp.api.req.SaleAfterOnlyRefundMasterReq;
import com.wanmi.sbc.erp.api.req.SaleAfterOnlyRefundReq;
import com.wanmi.sbc.erp.api.req.SaleAfterReq;
import com.wanmi.sbc.erp.api.req.SaleAfterReturnAllReq;
import com.wanmi.sbc.erp.api.req.bo.SaleAfterBO;
import com.wanmi.sbc.erp.api.req.bo.SaleAfterItemBO;
import com.wanmi.sbc.erp.api.req.bo.SaleAfterOrdItemBO;
import com.wanmi.sbc.erp.api.req.bo.SaleAfterOrderBO;
import com.wanmi.sbc.erp.api.req.bo.SaleAfterPostFeeBO;
import com.wanmi.sbc.erp.api.req.bo.SaleAfterRefundDetailBO;
import com.wanmi.sbc.erp.api.resp.GoodsPackRsp;
import com.wanmi.sbc.erp.api.resp.OrdOrderResp;
import com.wanmi.sbc.erp.api.resp.PackSkuListRsp;
import com.wanmi.sbc.erp.api.resp.PaymentResp;
import com.wanmi.sbc.erp.api.resp.SaleAfterItemResp;
import com.wanmi.sbc.erp.api.resp.SaleAfterOrderResp;
import com.wanmi.sbc.erp.api.resp.SaleAfterResp;
import com.wanmi.sbc.erp.configuration.shopcenter.ShopCenterRouterConfig;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ShopCenterSaleAfterController implements ShopCenterSaleAfterProvider {
	@Autowired
	private ShopCenterRouterConfig routerConfig;
	@Autowired
	private ShopCenterOrderProvider shopCenterOrderProvider;
	@Autowired
	private ShopCenterProductProvider shopCenterProductProvider;


	@Override
	public BaseResponse<Long> createSaleAfter(SaleAfterCreateReq request) {
		// 查询订单信息
		OrdOrderResp ordOrderBO = shopCenterOrderProvider.queryMasterOrderByTid(request.getOrderTid()).getContext();
		if (ordOrderBO == null) {
			return BaseResponse.FAILED();
		}

		// 数据校验
		String msg = validateCreateSaleAfter(request, ordOrderBO);
		if (StringUtil.isNotBlank(msg)) {
			log.warn("ShopCenterSaleAfterController.createSaleAfter，校验异常={}", msg);
			return BaseResponse.FAILED();
		}

		// 创建售后单
		SaleAfterBO saleAfterBO = buildSaleAfterBO(request);

		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.createSaleAfter");

			JSONObject param = (JSONObject) JSON.toJSON(saleAfterBO);
			param.put("orderNumber", ordOrderBO.getOrderNumber());

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Long data = JSON.parseObject(json.getString("data"), Long.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.saleAfterList.异常", e);
		}
		return BaseResponse.FAILED();
	}

	private String validateCreateSaleAfter(SaleAfterCreateReq reqVO, OrdOrderResp ordOrderBO) {
		StringBuilder msg = new StringBuilder();

		if (CollectionUtils.isNotEmpty(reqVO.getSaleAfterItemVOList())) {
			for (SaleAfterItemReq saleAfterItemVO : reqVO.getSaleAfterItemVOList()) {
				SaleAfterReturnAllReq saleAfterReturnAllVO = saleAfterItemVO.getSaleAfterReturnAllVO();
				SaleAfterOnlyRefundReq saleAfterOnlyRefundVO = saleAfterItemVO.getSaleAfterOnlyRefundVO();
				SaleAfterExchangeGoodsReq saleAfterExchangeGoodsVO = saleAfterItemVO.getSaleAfterExchangeGoodsVO();

				if (saleAfterReturnAllVO != null) {
					// 退货退款校验
					Integer returnableNum = saleAfterItemVO.getReturnableNum();
					Integer refundNumber = saleAfterReturnAllVO.getRefundNumber();
					if (refundNumber <= 0 || refundNumber > returnableNum) {
						msg.append("子订单").append(saleAfterItemVO.getItemOrderTid()).append("退货数量必须大于0且小于可退数量");
					}

					BigDecimal refundAmountB = new BigDecimal(saleAfterReturnAllVO.getRefundAmount());
					BigDecimal refundableAmountB = new BigDecimal(saleAfterItemVO.getOughtFee())
							.divide(new BigDecimal(saleAfterItemVO.getNum()), 0, RoundingMode.HALF_UP)
							.multiply(new BigDecimal(saleAfterReturnAllVO.getRefundNumber()));
					if (refundAmountB.compareTo(new BigDecimal(0)) <= 0
							|| refundAmountB.compareTo(refundableAmountB) > 0) {
						msg.append("子订单").append(saleAfterItemVO.getItemOrderTid()).append("退货金额必须大于0且小于可退金额");
					}
					return msg.toString();
				}

				if (saleAfterOnlyRefundVO != null) {
					// 仅退款校验
					if (saleAfterOnlyRefundVO.getRefundAmount() > saleAfterItemVO.getRefundableAmount()) {
						msg.append("子订单").append(saleAfterItemVO.getItemOrderTid()).append("退款金额必须小于可退金额");
					}
					return msg.toString();
				}

				if (saleAfterExchangeGoodsVO != null) {
					// 换货校验
					SaleAfterGoodsReq saleAfterGoodsVO = saleAfterExchangeGoodsVO.getGoodsVOList().get(0);
					if (saleAfterGoodsVO.getCount() <= 0
							|| saleAfterGoodsVO.getCount() > saleAfterItemVO.getReturnableNum()) {
						msg.append("子订单").append(saleAfterItemVO.getItemOrderTid()).append("购买件数必须大于0且小于可退数量");
					}
					return msg.toString();
				}
			}
		}

		// 图书订阅校验
		SaleAfterBookSubscribeReq saleAfterBookSubscribeVO = reqVO.getSaleAfterBookSubscribeVO();
		if (saleAfterBookSubscribeVO != null) {
			if (saleAfterBookSubscribeVO.getRefundNumber() <= 0
					|| saleAfterBookSubscribeVO.getRefundNumber() > saleAfterBookSubscribeVO.getReturnableNum()) {
				msg.append("子订单").append(saleAfterBookSubscribeVO.getItemOrderTid()).append("退订期数必须大于0且小于等于可退期数");
				return msg.toString();
			}
		}

		List<SaleAfterOnlyRefundMasterReq> saleAfterOnlyRefundMasterVOList = reqVO.getSaleAfterOnlyRefundMasterVOList();
		// 主订单-仅退款校验
		if (CollectionUtils.isNotEmpty(saleAfterOnlyRefundMasterVOList)) {
			for (SaleAfterOnlyRefundMasterReq vo : saleAfterOnlyRefundMasterVOList) {
				if (vo.getRefundAmount() <= 0) {
					msg.append("退款金额必须大于0");
					return msg.toString();
				}

				if (vo.getRefundReason() == 1) { // 退运费校验
					if (ordOrderBO.getPostFee() == 0) {
						msg.append("该订单不允许退运费");
						return msg.toString();
					}

					if (vo.getRefundAmount() >= ordOrderBO.getPostFee()) {
						msg.append("退运费金额必须小于实际邮费");
						return msg.toString();
					}
				}
			}
		}
		return msg.toString();
	}

	private SaleAfterBO buildSaleAfterBO(SaleAfterCreateReq request) {
		SaleAfterBO saleAfterBO = new SaleAfterBO();

		// 查询支付相关信息
		List<PaymentResp> paymentResponseBOList = shopCenterOrderProvider.getPaymentByOrderId(request.getOrderTid()).getContext();

		List<Integer> refundTypeList = new ArrayList<>();
		List<SaleAfterItemBO> saleAfterItemBOList = new ArrayList<>();

		// 图书订阅VO
		SaleAfterBookSubscribeReq saleAfterBookSubscribeVO = request.getSaleAfterBookSubscribeVO();

		// 图书订阅退货退款
		if (saleAfterBookSubscribeVO != null) {
			refundTypeList.add(SaleAfterRefundTypeEnum.RETURNS_AND_REFUNDS.getCode());
			return buildSaleAfterBookSubscribe(request, refundTypeList, saleAfterBO, saleAfterBookSubscribeVO, paymentResponseBOList, saleAfterItemBOList);
		}

		// 子订单售后
		if (CollectionUtils.isNotEmpty(request.getSaleAfterItemVOList())) {
			buildSaleAfterItem(request, refundTypeList, paymentResponseBOList, saleAfterItemBOList);
		}

		// 主订单补偿
		if (request.getSaleAfterCompensateVO() != null) {
			buildMasterCompensate(request, refundTypeList, saleAfterItemBOList);
		}

		// 主订单仅退款
		if (CollectionUtils.isNotEmpty(request.getSaleAfterOnlyRefundMasterVOList())) {
			refundTypeList.add(SaleAfterRefundTypeEnum.MASTER_ONLY_REFUND.getCode());

			SaleAfterPostFeeBO saleAfterPostFee = new SaleAfterPostFeeBO();
			List<SaleAfterRefundDetailBO> saleAfterPostFeeDetailList = new ArrayList<>();

			List<SaleAfterOnlyRefundMasterReq> saleAfterOnlyRefundMasterVOList = request.getSaleAfterOnlyRefundMasterVOList();
			Map<Integer, List<SaleAfterOnlyRefundMasterReq>> masterVOMap = saleAfterOnlyRefundMasterVOList.stream()
					.collect(Collectors.groupingBy(SaleAfterOnlyRefundMasterReq::getRefundReason));

			for (Map.Entry<Integer, List<SaleAfterOnlyRefundMasterReq>> entry : masterVOMap.entrySet()) {
				List<SaleAfterRefundDetailBO> saleAfterRefundDetailBOList = new ArrayList<>();

				Integer refundReasonInt = entry.getKey();
				List<SaleAfterOnlyRefundMasterReq> voList = entry.getValue();
				if (refundReasonInt == 1) { // 退运费处理
					for (SaleAfterOnlyRefundMasterReq vo : voList) {
						SaleAfterRefundDetailBO detailBO = new SaleAfterRefundDetailBO();
						detailBO.setPayType(vo.getRefundAmountType());
						detailBO.setAmount(vo.getRefundAmount());
						detailBO.setRefundReason("退运费");
						saleAfterPostFeeDetailList.add(detailBO);
					}
					saleAfterPostFee.setSaleAfterRefundDetailBOList(saleAfterPostFeeDetailList);
					saleAfterBO.setSaleAfterPostFee(saleAfterPostFee);
				} else { // 补偿处理
					for (SaleAfterOnlyRefundMasterReq vo : voList) {
						SaleAfterRefundDetailBO detailBO = new SaleAfterRefundDetailBO();
						detailBO.setPayType(vo.getRefundAmountType());
						detailBO.setAmount(vo.getRefundAmount());
						detailBO.setRefundReason("补偿");
						saleAfterRefundDetailBOList.add(detailBO);
					}

					Integer refundFee = saleAfterRefundDetailBOList.stream()
							.mapToInt(SaleAfterRefundDetailBO::getAmount).sum();
					SaleAfterItemBO saleAfterItemBO = new SaleAfterItemBO();
					saleAfterItemBO.setRefundType(SaleAfterRefundTypeEnum.MASTER_ONLY_REFUND.getCode());
					saleAfterItemBO.setObjectId(request.getOrderTid());
					saleAfterItemBO.setObjectType(UnifiedOrderObjectTypeEnum.ORDER.getObjectType());
					saleAfterItemBO.setRefundFee(refundFee);
					saleAfterItemBO.setSaleAfterRefundDetailBOList(saleAfterRefundDetailBOList);
					saleAfterItemBOList.add(saleAfterItemBO);
				}
			}
		}

		SaleAfterOrderBO saleAfterOrderBO = new SaleAfterOrderBO();
		saleAfterOrderBO.setPlatformRefundId(generatePlatformRefundId(request.getOrderTid()));
		saleAfterOrderBO.setApplyTime(new Date());
		saleAfterOrderBO.setMemo(request.getReason());
		saleAfterOrderBO.setOrderId(request.getOrderTid());

		saleAfterBO.setSaleAfterCreateEnum(SaleAfterCreateEnum.BACK_END);
		saleAfterBO.setRefundTypeList(refundTypeList);
		saleAfterBO.setSaleAfterOrderBO(saleAfterOrderBO);
		saleAfterBO.setSaleAfterItemBOList(saleAfterItemBOList);

		return saleAfterBO;
	}

	private SaleAfterBO buildSaleAfterBookSubscribe(SaleAfterCreateReq reqVO, List<Integer> refundTypeList,
	                                                SaleAfterBO saleAfterBO, SaleAfterBookSubscribeReq saleAfterBookSubscribeVO,
	                                                List<PaymentResp> paymentResponseBOList, List<SaleAfterItemBO> saleAfterItemBOList) {
		SaleAfterItemBO saleAfterItemBO = new SaleAfterItemBO();
		SaleAfterOrderBO saleAfterOrderBO = new SaleAfterOrderBO();
		SaleAfterRefundDetailBO detailBO = new SaleAfterRefundDetailBO();
		List<SaleAfterRefundDetailBO> saleAfterRefundDetailBOList = new ArrayList<>();

		saleAfterItemBO.setRefundType(SaleAfterRefundTypeEnum.RETURNS_AND_REFUNDS.getCode());
		saleAfterItemBO.setRefundFee(saleAfterBookSubscribeVO.getRefundAmount());
		saleAfterItemBO.setRefundNum(saleAfterBookSubscribeVO.getRefundNumber());
		saleAfterItemBO.setObjectId(saleAfterBookSubscribeVO.getItemOrderTid());
		saleAfterItemBO.setObjectType(UnifiedOrderObjectTypeEnum.ORDER_ITEM.getObjectType());
		buildRefundDetail(reqVO, saleAfterBookSubscribeVO.getRefundAmount(), saleAfterRefundDetailBOList,
				paymentResponseBOList);
		saleAfterRefundDetailBOList.add(detailBO);
		saleAfterItemBO.setSaleAfterRefundDetailBOList(saleAfterRefundDetailBOList);
		saleAfterItemBOList.add(saleAfterItemBO);

		saleAfterOrderBO.setPlatformRefundId(generatePlatformRefundId(reqVO.getOrderTid()));
		saleAfterOrderBO.setApplyTime(new Date());
		saleAfterOrderBO.setRefundFee(saleAfterBookSubscribeVO.getRefundAmount());
		saleAfterOrderBO.setRefundPostFee(0);
		saleAfterOrderBO.setMemo(reqVO.getReason());
		saleAfterOrderBO.setOrderId(reqVO.getOrderTid());

		saleAfterBO.setRefundTypeList(refundTypeList);
		saleAfterBO.setSaleAfterOrderBO(saleAfterOrderBO);
		saleAfterBO.setSaleAfterItemBOList(saleAfterItemBOList);

		return saleAfterBO;
	}

	private void buildSaleAfterItem(SaleAfterCreateReq reqVO, List<Integer> refundTypeList,
	                                List<PaymentResp> paymentResponseBOList, List<SaleAfterItemBO> saleAfterItemBOList) {
		List<SaleAfterItemReq> saleAfterItemVOList = reqVO.getSaleAfterItemVOList();
		for (SaleAfterItemReq saleAfterItemVO : saleAfterItemVOList) {
			List<SaleAfterRefundDetailBO> saleAfterRefundDetailBOList = new ArrayList<>();
			SaleAfterItemBO saleAfterItemBO = new SaleAfterItemBO();

			SaleAfterReturnAllReq saleAfterReturnAllVO = saleAfterItemVO.getSaleAfterReturnAllVO();
			SaleAfterOnlyRefundReq saleAfterOnlyRefundVO = saleAfterItemVO.getSaleAfterOnlyRefundVO();
			SaleAfterExchangeGoodsReq saleAfterExchangeGoodsVO = saleAfterItemVO.getSaleAfterExchangeGoodsVO();

			// 子订单退货退款
			if (saleAfterReturnAllVO != null) {
				refundTypeList.add(SaleAfterRefundTypeEnum.RETURNS_AND_REFUNDS.getCode());

				saleAfterItemBO.setRefundType(SaleAfterRefundTypeEnum.RETURNS_AND_REFUNDS.getCode());
				saleAfterItemBO.setRefundFee(saleAfterReturnAllVO.getRefundAmount());
				saleAfterItemBO.setExpressCode(saleAfterReturnAllVO.getExpressCode());
				saleAfterItemBO.setExpressNo(saleAfterReturnAllVO.getExpressNo());
				saleAfterItemBO.setRefundNum(saleAfterReturnAllVO.getRefundNumber());
				saleAfterItemBO.setObjectId(saleAfterItemVO.getItemOrderTid());
				saleAfterItemBO.setObjectType(UnifiedOrderObjectTypeEnum.ORDER_ITEM.getObjectType());
				buildRefundDetail(reqVO, saleAfterReturnAllVO.getRefundAmount(), saleAfterRefundDetailBOList,
						paymentResponseBOList);
				saleAfterItemBO.setSaleAfterRefundDetailBOList(saleAfterRefundDetailBOList);
				saleAfterItemBOList.add(saleAfterItemBO);

				continue;
			}

			// 子订单仅退款
			if (saleAfterOnlyRefundVO != null) {
				refundTypeList.add(SaleAfterRefundTypeEnum.ONLY_REFUNDS.getCode());

				saleAfterItemBO.setRefundType(SaleAfterRefundTypeEnum.ONLY_REFUNDS.getCode());
				saleAfterItemBO.setRefundFee(saleAfterOnlyRefundVO.getRefundAmount());
				saleAfterItemBO.setObjectId(saleAfterItemVO.getItemOrderTid());
				saleAfterItemBO.setObjectType(UnifiedOrderObjectTypeEnum.ORDER_ITEM.getObjectType());
				buildRefundDetail(reqVO, saleAfterOnlyRefundVO.getRefundAmount(), saleAfterRefundDetailBOList,
						paymentResponseBOList);
				saleAfterItemBO.setSaleAfterRefundDetailBOList(saleAfterRefundDetailBOList);
				saleAfterItemBOList.add(saleAfterItemBO);

				continue;
			}

			// 子订单换货
			if (saleAfterExchangeGoodsVO != null) {
				refundTypeList.add(SaleAfterRefundTypeEnum.EXCHANGE_GOODS.getCode());
				List<SaleAfterGoodsReq> saleAfterGoodsVOList = saleAfterExchangeGoodsVO.getGoodsVOList();
				for (SaleAfterGoodsReq vo : saleAfterGoodsVOList) {
					List<SaleAfterOrdItemBO> saleAfterOrdItemBOList = new ArrayList<>();

					saleAfterItemBO.setRefundType(SaleAfterRefundTypeEnum.EXCHANGE_GOODS.getCode());
					saleAfterItemBO.setExpressCode(saleAfterExchangeGoodsVO.getExpressCode());
					saleAfterItemBO.setExpressNo(saleAfterExchangeGoodsVO.getExpressNo());
					saleAfterItemBO.setObjectId(reqVO.getOrderTid());
					saleAfterItemBO.setObjectType(UnifiedOrderObjectTypeEnum.ORDER_ITEM.getObjectType());
					// 处理子订单信息
					List<String> goodsCodeList = new ArrayList<>();
					goodsCodeList.add(vo.getMetaGoodsId());
					List<GoodsPackRsp> goodsPackBOList = shopCenterProductProvider.searchPackByGoodsCodes(goodsCodeList).getContext();
					for (GoodsPackRsp goodsPackBO : goodsPackBOList) {
						saleAfterItemBO.setIsPack(goodsPackBO.getGoodsLists().size() > 1);
						for (PackSkuListRsp packSkuListBO : goodsPackBO.getGoodsLists()) {
							SaleAfterOrdItemBO saleAfterOrdItemBO = new SaleAfterOrdItemBO();
							saleAfterOrdItemBO.setMetaGoodsType(packSkuListBO.getMetaGoodsType());
							saleAfterOrdItemBO.setMetaGoodsId(packSkuListBO.getGoodsId());
							saleAfterOrdItemBO.setMetaGoodsName(packSkuListBO.getGoodsName());
							saleAfterOrdItemBO.setMetaSkuId(packSkuListBO.getSkuId());
							saleAfterOrdItemBO.setMetaSkuName(packSkuListBO.getSkuName());
							saleAfterOrdItemBO.setCostPrice(packSkuListBO.getCostPrice());
							saleAfterOrdItemBO.setGoodsId(packSkuListBO.getGoodsId().intValue());
							saleAfterOrdItemBO.setSaleCode(packSkuListBO.getGoodsCode());
							saleAfterOrdItemBO.setPrice(packSkuListBO.getCostPrice());
							saleAfterOrdItemBO.setNum(vo.getCount());
							saleAfterOrdItemBO.setStatus(UnifiedOrderItemStatusEnum.WAIT_FOR_DELIVERY.getCode());
							saleAfterOrdItemBO.setCostPostPrice(0);
							saleAfterOrdItemBO.setDiscountFee(0);
							saleAfterOrdItemBO.setOughtFee(0);
							saleAfterOrdItemBO.setDeliveryStatus(UnifiedOrderDeliveryStatusEnum.UNSIGN.getStatus());
							saleAfterOrdItemBO.setGiftFlag(0);
							saleAfterOrdItemBO.setGiftObjectId(packSkuListBO.getGoodsId());
							saleAfterOrdItemBO.setOrderId(reqVO.getOrderTid());
							saleAfterOrdItemBO.setOrderItemType(2);
							saleAfterOrdItemBO.setMetaGoodsSubType(packSkuListBO.getMetaSubGoodsType());
							saleAfterOrdItemBO.setMarketPrice(packSkuListBO.getMarketPrice());
							saleAfterOrdItemBO.setRefundFee(0);
							saleAfterOrdItemBOList.add(saleAfterOrdItemBO);
						}
					}
					saleAfterItemBO.setSaleAfterOrdItemBOList(saleAfterOrdItemBOList);
					saleAfterItemBOList.add(saleAfterItemBO);
				}
			}
		}
	}

	private void buildRefundDetail(SaleAfterCreateReq reqVO, Integer refundAmount,
	                               List<SaleAfterRefundDetailBO> saleAfterRefundDetailBOList, List<PaymentResp> paymentResponseBOList) {
		Integer refundAmountIn = refundAmount;

		Map<String, PaymentResp> paymentResponseBOMap = paymentResponseBOList.stream()
				.collect(Collectors.toMap(PaymentResp::getPayType, Function.identity()));

		// 现金
		PaymentResp payTypeOneBO = paymentResponseBOMap.get("1");
		if (payTypeOneBO != null) {
			SaleAfterRefundDetailBO detailBO = new SaleAfterRefundDetailBO();
			detailBO.setRefundReason(reqVO.getReason());
			detailBO.setPayType(1);
			if (refundAmountIn > payTypeOneBO.getAmount()) {
				detailBO.setAmount(payTypeOneBO.getAmount());
				saleAfterRefundDetailBOList.add(detailBO);
				refundAmountIn -= payTypeOneBO.getAmount();
			} else {
				detailBO.setAmount(refundAmountIn);
				saleAfterRefundDetailBOList.add(detailBO);
				return;
			}
		}

		// 知豆
		PaymentResp payTypeTwoBO = paymentResponseBOMap.get("2");
		if (payTypeTwoBO != null) {
			SaleAfterRefundDetailBO detailBO = new SaleAfterRefundDetailBO();
			detailBO.setRefundReason(reqVO.getReason());
			detailBO.setPayType(2);
			if (refundAmountIn > payTypeTwoBO.getAmount()) {
				detailBO.setAmount(payTypeTwoBO.getAmount());
				saleAfterRefundDetailBOList.add(detailBO);
				refundAmountIn -= payTypeTwoBO.getAmount();
			} else {
				detailBO.setAmount(refundAmountIn);
				saleAfterRefundDetailBOList.add(detailBO);
				return;
			}
		}

		// 智慧币
		PaymentResp payTypeFourBO = paymentResponseBOMap.get("4");
		if (payTypeFourBO != null) {
			SaleAfterRefundDetailBO detailBO = new SaleAfterRefundDetailBO();
			detailBO.setRefundReason(reqVO.getReason());
			detailBO.setPayType(4);
			if (refundAmountIn > payTypeFourBO.getAmount()) {
				detailBO.setAmount(payTypeFourBO.getAmount());
				saleAfterRefundDetailBOList.add(detailBO);
				refundAmountIn -= payTypeFourBO.getAmount();
			} else {
				detailBO.setAmount(refundAmountIn);
				saleAfterRefundDetailBOList.add(detailBO);
				return;
			}
		}

		// 积分
		PaymentResp payTypeThreeBO = paymentResponseBOMap.get("3");
		if (payTypeThreeBO != null) {
			SaleAfterRefundDetailBO detailBO = new SaleAfterRefundDetailBO();
			detailBO.setRefundReason(reqVO.getReason());
			detailBO.setPayType(3);
			if (refundAmountIn > payTypeThreeBO.getAmount()) {
				detailBO.setAmount(payTypeThreeBO.getAmount());
				saleAfterRefundDetailBOList.add(detailBO);
				refundAmountIn -= payTypeThreeBO.getAmount();
			} else {
				detailBO.setAmount(refundAmountIn);
				saleAfterRefundDetailBOList.add(detailBO);
			}
		}
	}

	private void buildMasterCompensate(SaleAfterCreateReq reqVO, List<Integer> refundTypeList,
	                                   List<SaleAfterItemBO> saleAfterItemBOList) {
		SaleAfterCompensateReq saleAfterCompensateVO = reqVO.getSaleAfterCompensateVO();
		List<SaleAfterGoodsReq> saleAfterGoodsVOList = saleAfterCompensateVO.getGoodsVOList();
		for (SaleAfterGoodsReq vo : saleAfterGoodsVOList) {
			SaleAfterItemBO saleAfterItemBO = new SaleAfterItemBO();
			saleAfterItemBO.setObjectId(reqVO.getOrderTid());
			saleAfterItemBO.setObjectType(UnifiedOrderObjectTypeEnum.ORDER.getObjectType());

			if (SaleAfterCompensationTypeEnum.COMPENSATION_GOODS.getCode().equals(vo.getCompensationType())) {
				refundTypeList.add(SaleAfterRefundTypeEnum.COMPENSATION.getCode());
				saleAfterItemBO.setRefundType(SaleAfterRefundTypeEnum.COMPENSATION.getCode());
			} else {
				refundTypeList.add(SaleAfterRefundTypeEnum.COMPENSATION.getCode());
				saleAfterItemBO.setRefundType(SaleAfterRefundTypeEnum.COMPENSATION.getCode());
			}

			List<SaleAfterOrdItemBO> saleAfterOrdItemBOList = new ArrayList<>();
			// 处理子订单信息
			List<String> goodsCodeList = new ArrayList<>();
			goodsCodeList.add(vo.getMetaGoodsId());
			List<GoodsPackRsp> goodsPackBOList = shopCenterProductProvider.searchPackByGoodsCodes(goodsCodeList).getContext();
			for (GoodsPackRsp goodsPackBO : goodsPackBOList) {
				saleAfterItemBO.setIsPack(goodsPackBO.getGoodsLists().size() > 1);
				for (PackSkuListRsp packSkuListBO : goodsPackBO.getGoodsLists()) {
					SaleAfterOrdItemBO saleAfterOrdItemBO = new SaleAfterOrdItemBO();
					saleAfterOrdItemBO.setMetaGoodsType(packSkuListBO.getMetaGoodsType());
					saleAfterOrdItemBO.setMetaGoodsId(packSkuListBO.getGoodsId());
					saleAfterOrdItemBO.setMetaGoodsName(packSkuListBO.getGoodsName());
					saleAfterOrdItemBO.setMetaSkuId(packSkuListBO.getSkuId());
					saleAfterOrdItemBO.setMetaSkuName(packSkuListBO.getSkuName());
					saleAfterOrdItemBO.setCostPrice(packSkuListBO.getCostPrice());
					saleAfterOrdItemBO.setGoodsId(packSkuListBO.getGoodsId().intValue());
					saleAfterOrdItemBO.setSaleCode(packSkuListBO.getGoodsCode());
					saleAfterOrdItemBO.setPrice(packSkuListBO.getCostPrice());
					saleAfterOrdItemBO.setNum(vo.getCount());
					saleAfterOrdItemBO.setStatus(UnifiedOrderItemStatusEnum.WAIT_FOR_DELIVERY.getCode());
					saleAfterOrdItemBO.setCostPostPrice(0);
					saleAfterOrdItemBO.setDiscountFee(0);
					saleAfterOrdItemBO.setOughtFee(0);
					saleAfterOrdItemBO.setDeliveryStatus(UnifiedOrderDeliveryStatusEnum.UNSIGN.getStatus());
					if (SaleAfterCompensationTypeEnum.COMPENSATION_GOODS.getCode().equals(vo.getCompensationType())) {
						saleAfterOrdItemBO.setGiftFlag(3); // 补偿
					} else {
						saleAfterOrdItemBO.setGiftFlag(1); // 赠送
					}
					saleAfterOrdItemBO.setGiftObjectId(packSkuListBO.getGoodsId());
					saleAfterOrdItemBO.setOrderId(reqVO.getOrderTid());
					saleAfterOrdItemBO.setOrderItemType(2); // 补偿单
					saleAfterOrdItemBO.setMetaGoodsSubType(packSkuListBO.getMetaSubGoodsType());
					saleAfterOrdItemBO.setMarketPrice(packSkuListBO.getMarketPrice());
					saleAfterOrdItemBO.setRefundFee(0);
					saleAfterOrdItemBOList.add(saleAfterOrdItemBO);
				}
			}
			saleAfterItemBO.setSaleAfterOrdItemBOList(saleAfterOrdItemBOList);
			saleAfterItemBOList.add(saleAfterItemBO);
		}
	}

	private String generatePlatformRefundId(Long orderTid) {
		StringBuilder platformRefundId = new StringBuilder();
		String time = new SimpleDateFormat("ddHHmmss").format(new Date());
		platformRefundId.append("R").append(orderTid).append(time);
		return platformRefundId.toString();
	}


	@Override
	public BaseResponse<List<SaleAfterResp>> saleAfterList(SaleAfterReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.saleAfterList");

			JSONObject param = (JSONObject) JSON.toJSON(request);
			param.putAll(param.getJSONObject("page"));

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			List<SaleAfterResp> data = JSON.parseArray(json.getString("data"), SaleAfterResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.saleAfterList.异常", e);
		}
		return BaseResponse.success(Collections.emptyList());
	}

	@Override
	public BaseResponse<List<SaleAfterOrderResp>> querySaleAfterOrder(QuerySaleAfterOrderReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.querySaleAfterOrder");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			List<SaleAfterOrderResp> data = JSON.parseArray(json.getString("data"), SaleAfterOrderResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.querySaleAfterOrder.异常", e);
		}
		return BaseResponse.success(Collections.emptyList());
	}

	@Override
	public BaseResponse<Boolean> examineSaleAfter(SaleAfterExamineReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.examineSaleAfter");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.examineSaleAfter.异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<Boolean> cancelSaleAfter(SaleAfterCancelReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.cancelSaleAfter");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.cancelSaleAfter.异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<Boolean> fillDeliverInfo(SaleAfterFillDeliverInfoReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.fillDeliverInfo");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.fillDeliverInfo.异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<Boolean> confirmDeliver(SaleAfterConfirmDeliverReq request) {
		try {
			QuerySaleAfterOrderReq querySaleAfterOrderBO = new QuerySaleAfterOrderReq();
			querySaleAfterOrderBO.setSaId(request.getSaOrderTid());
			SaleAfterOrderResp saleAfterOrderResp = querySaleAfterOrder(querySaleAfterOrderBO).getContext().get(0);
			List<SaleAfterItemResp> items = saleAfterOrderResp.getItems();
			List<Integer> refundTypeList = items.stream().map(SaleAfterItemResp::getRefundType).collect(Collectors.toList());


			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.confirmDeliver");

			JSONObject param = (JSONObject) JSON.toJSON(request);
			// 如果退款类型为子订单换货且不包含主订单仅退款，则直接改为售后完成。其他则为确认打款
			if (refundTypeList.contains(SaleAfterRefundTypeEnum.EXCHANGE_GOODS.getCode())
					&& !refundTypeList.contains(SaleAfterRefundTypeEnum.MASTER_ONLY_REFUND.getCode())) {
				param.put("status", UnifiedSaleAfterStatusEnum.SALE_AFTER_DONE.getCode());
			} else {
				param.put("status", UnifiedSaleAfterStatusEnum.REFUND_MONEY.getCode());
			}

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.confirmDeliver.异常", e);
		}
		return BaseResponse.FAILED();

	}

	@Override
	public BaseResponse<Boolean> confirmPayment(SaleAfterConfirmPaymentReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.confirmPayment");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.confirmPayment.异常", e);
		}
		return BaseResponse.FAILED();
	}
}
