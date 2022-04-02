package com.soybean.mall.order.miniapp.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.soybean.mall.order.bean.vo.MiniProgramOrderReportVO;
import com.soybean.mall.order.enums.MiniOrderOperateType;
import com.soybean.mall.order.enums.MiniProgramSceneType;
import com.soybean.mall.order.miniapp.model.root.MiniOrderOperateResult;
import com.soybean.mall.order.miniapp.repository.MiniOrderOperateResultRepository;
import com.soybean.mall.order.trade.model.OrderReportDetailDTO;
import com.soybean.mall.wx.mini.common.bean.request.WxSendMessageRequest;
import com.soybean.mall.wx.mini.common.controller.CommonController;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.dto.*;
import com.soybean.mall.wx.mini.order.bean.enums.WxAfterSaleReasonType;
import com.soybean.mall.wx.mini.order.bean.request.*;
import com.soybean.mall.wx.mini.order.bean.response.GetPaymentParamsResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateNewAfterSaleResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnAddress;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WxOrderService {

    @Autowired
    private CommonController wxCommonController;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiniOrderOperateResultRepository miniOrderOperateResultRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private WxOrderApiController wxOrderApiController;

    @Value("${wx.order.delivery.send.message.templateId}")
    private String orderDeliveryMsgTemplateId;

    @Value("${wx.create.order.send.message.link.url}")
    private String createOrderSendMsgLinkUrl;

    @Value("${wx.create.order.send.message.templateId}")
    private String createOrderSendMsgTemplateId;

    @Value("${wx.cancel.order.send.message.templateId}")
    private String cancelOrderSendMsgTemplateId;

    @Value("${wx.aftersale.order.send.message.templateId}")
    private String afterSaleSendMsgTemplateId;

    private static  final String MINI_PROGRAM_ORDER_REPORT_PRICE = "mini:ord:report:price:";

    private static final String MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE = "mini:ord:report:hour:price:";

    private static final String MINI_PROGRAM_ORDER_REPORT_LIST = "mini:ord:list:";

    @Value("${wx.default.image.url}")
    private String defaultImageUrl;

    @Value("${wx.goods.detail.url}")
    private String goodsDetailUrl;

    @Value("${wx.order.list.url}")
    private String orderListUrl;

    /**
     * 小程序订单同步确认收货
     *
     * @param trade
     */
    public void syncWxOrderReceive(Trade trade) {
        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            return;
        }
        WxDeliveryReceiveRequest request = WxDeliveryReceiveRequest.builder().outOrderId(trade.getId()).openid(trade.getBuyer().getOpenId()).build();
        try {
            BaseResponse<WxResponseBase> response = wxOrderApiController.receive(request);
            log.info("微信小程序订单确认收货，request:{},response:{}", JSON.toJSONString(request), response != null ? JSON.toJSON(response) : "空");
            if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
                addMiniOrderOperateResult(JSON.toJSONString(request), response != null ? JSON.toJSONString(response) : "空", MiniOrderOperateType.RECEIVE.getIndex(), trade.getId());
            }
        } catch (Exception e) {
            log.error("微信小程序订单确认收货失败，orderId:{}", trade.getId(), e);
            addMiniOrderOperateResult(JSON.toJSONString(request), e.getMessage(), MiniOrderOperateType.RECEIVE.getIndex(), trade.getId());
        }
    }


    public void addMiniOrderOperateResult(String request, String resultStr, Integer type, String orderId) {
        try {
            MiniOrderOperateResult result = MiniOrderOperateResult.builder()
                    .operateType(type)
                    .requestContext(request)
                    .resultContext(resultStr)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .orderId(orderId)
                    .status(1)
                    .build();
            miniOrderOperateResultRepository.save(result);
        } catch (Exception e) {
            log.error("微信小程序失败记录添加失败,request:{},result:{},type:{},orderId:{}", request, resultStr, type, orderId, e);
        }
    }

    /**
     * 同步订单支付状态
     *
     * @param trade
     */
    public Boolean syncWxOrderPay(Trade trade, String transactionId) {
        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            return true;
        }
        log.info("微信小程序同步支付结果start，transactionId:{},trade:{}", transactionId, trade);
        WxOrderPayRequest wxOrderPayRequest = new WxOrderPayRequest();
        wxOrderPayRequest.setOpenId(trade.getBuyer().getOpenId());
        wxOrderPayRequest.setOutOrderId(trade.getId());
        wxOrderPayRequest.setActionId(1);
        LocalDateTime payTime = trade.getTradeState().getPayTime() != null ? trade.getTradeState().getPayTime() : LocalDateTime.now();
        wxOrderPayRequest.setPayTime(DateUtil.format(payTime, DateUtil.FMT_TIME_1));
        wxOrderPayRequest.setTransactionId(transactionId);
        try {
            BaseResponse<WxResponseBase> response = wxOrderApiController.orderPay(wxOrderPayRequest);
            log.info("微信小程序订单同步支付状态,request:{},response:{}", JSON.toJSONString(wxOrderPayRequest), JSON.toJSON(response));
            //如果失败则记录原因
            if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
                this.addMiniOrderOperateResult(JSON.toJSONString(wxOrderPayRequest), response != null ? JSON.toJSONString(response) : "空", MiniOrderOperateType.SYNC_PAY_RESULT.getIndex(), trade.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("微信小程序订单同步支付状态失败,transactionId:{},trade:{}", transactionId, trade, e);
            this.addMiniOrderOperateResult(JSON.toJSONString(wxOrderPayRequest), e.getMessage(), MiniOrderOperateType.SYNC_PAY_RESULT.getIndex(), trade.getId());
            return false;
        }
        this.sendWxCreateOrderMessage(trade);
        this.orderReportCache(trade.getId());
        return true;
    }

    public BaseResponse<WxResponseBase> sendWxDeliveryMessage(Trade trade) {
        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            return null;
        }
        WxSendMessageRequest request = new WxSendMessageRequest();
        request.setOpenId(trade.getBuyer().getOpenId());
        request.setTemplateId(orderDeliveryMsgTemplateId);
        request.setUrl(createOrderSendMsgLinkUrl + trade.getId());
        Map<String, Map<String, String>> map = new HashMap<>();
        map.put("character_string1", new HashMap<String, String>() {{
            put("value", trade.getId());
        }});
        map.put("thing2", new HashMap<String, String>() {{
            put("value", filterChineseAndAlp(trade.getTradeItems().get(0).getSpuName()));
        }});
        map.put("phrase3", new HashMap<String, String>() {{
            put("value", trade.getTradeDelivers().get(0).getLogistics().getLogisticCompanyName());
        }});
        map.put("character_string4", new HashMap<String, String>() {{
            put("value", trade.getTradeDelivers().get(0).getLogistics().getLogisticNo());
        }});
        map.put("thing9", new HashMap<String, String>() {{
            put("value", StringUtils.isNotEmpty(trade.getBuyerRemark()) ? trade.getBuyerRemark() : "无");
        }});
        request.setData(map);
        return wxCommonController.sendMessage(request);
    }


    public void sendWxCreateOrderMessage(Trade trade) {
        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            return;
        }
        try {
            WxSendMessageRequest request = new WxSendMessageRequest();
            request.setOpenId(trade.getBuyer().getOpenId());
            request.setTemplateId(createOrderSendMsgTemplateId);
            request.setUrl(createOrderSendMsgLinkUrl + trade.getId());
            Map<String, Map<String, String>> map = new HashMap<>();
            String address = StringUtils.isNotEmpty(trade.getConsignee().getDetailAddress()) && trade.getConsignee().getDetailAddress().length() > 20 ? trade.getConsignee().getDetailAddress().substring(0, 20) : trade.getConsignee().getDetailAddress();
            map.put("character_string1", new HashMap<String, String>() {{
                put("value", trade.getId());
            }});
            map.put("amount2", new HashMap<String, String>() {{
                put("value", String.valueOf(trade.getTradePrice().getTotalPrice()));
            }});
            map.put("thing3", new HashMap<String, String>() {{
                put("value", address);
            }});
            map.put("name4", new HashMap<String, String>() {{
                put("value", filterChineseAndAlp(trade.getTradeItems().get(0).getSpuName()));
            }});
            map.put("phrase5", new HashMap<String, String>() {{
                put("value", "待发货");
            }});
            request.setData(map);
            BaseResponse<WxResponseBase> response = wxCommonController.sendMessage(request);
            log.info("微信小程序创建订单发送消息request:{},response:{}", request, response);
        } catch (Exception e) {
            log.error("微信小程序创建订单发送消息失败,trade:{}", trade, e);
        }
    }

    private String filterChineseAndAlp(String str) {
        String goodsName = str.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)]", "");
        if (StringUtils.isEmpty(goodsName)) {
            goodsName = "购买的商品";
        }
        return goodsName;
    }

    /**
     * 小程序实时报表
     *
     * @param tid
     */
    public void orderReportCache(String tid) {
        Trade trade = tradeRepository.findById(tid).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{tid});
        }
        if (!trade.getChannelType().equals(ChannelType.MINIAPP)) {
            return;
        }
        try {

            String date = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_DATE_1);
            //总金额
            String cachePrice = redisService.getString(MINI_PROGRAM_ORDER_REPORT_PRICE.concat(date));
            BigDecimal lastPrice = new BigDecimal(0);
            if (StringUtils.isNotEmpty(cachePrice)) {
                lastPrice = new BigDecimal(cachePrice);
            }
            BigDecimal totalPrice = trade.getTradePrice().getGoodsPrice().add(lastPrice).setScale(2, RoundingMode.HALF_UP);
            redisService.setString(MINI_PROGRAM_ORDER_REPORT_PRICE.concat(date), totalPrice.toString(), 86400);
            log.info("小程序实时报表设置付款金额，trade:{},now price:{},last price:{}", trade, totalPrice, lastPrice);
            //分时金额
            Integer hour = LocalDateTime.now().getHour();
            Map<Integer, BigDecimal> cacheHourPrice = redisService.getObj(MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE.concat(date), Map.class);
            if (cacheHourPrice == null || cacheHourPrice.isEmpty()) {
                cacheHourPrice = new HashMap<>();
            }
            BigDecimal lastHourPrice = new BigDecimal(0);
            if (cacheHourPrice.containsKey(hour) && cacheHourPrice.get(hour) != null) {
                lastHourPrice = cacheHourPrice.get(hour);
            }
            BigDecimal totalHourPrice = trade.getTradePrice().getGoodsPrice().add(lastHourPrice).setScale(2, RoundingMode.HALF_UP);
            cacheHourPrice.put(hour, totalHourPrice);
            redisService.setObj(MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE.concat(date), cacheHourPrice, 86400);
            log.info("小程序实时报表设置分时付款金额，trade:{},now price:{},last price:{}", trade, totalPrice, lastPrice);
            //只保存20条数据
            OrderReportDetailDTO orderReportDetailDTO = OrderReportDetailDTO.builder()
                    .createTime(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_1))
                    .goodsName(trade.getTradeItems().get(0).getSpuName())
                    .orderId(trade.getId())
                    .pic(trade.getTradeItems().get(0).getPic())
                    .price(trade.getTradePrice().getGoodsPrice()).build();
            List<OrderReportDetailDTO> newList = new ArrayList<>(20);
            List<OrderReportDetailDTO> list = redisService.getList(MINI_PROGRAM_ORDER_REPORT_LIST.concat(date), OrderReportDetailDTO.class);
            newList.add(0, orderReportDetailDTO);
            if (CollectionUtils.isNotEmpty(list)) {
                newList.addAll(list.stream().limit(list.size() > 19 ? 19 : list.size()).collect(Collectors.toList()));
            }
            redisService.setObj(MINI_PROGRAM_ORDER_REPORT_LIST.concat(date), newList, 86400);

        } catch (Exception e) {
            log.warn("小程序实时数据报表报错，trade:{}", new Gson().toJson(trade), e);
        }

    }

    public MiniProgramOrderReportVO getMiniProgramOrderReportCache() {
        MiniProgramOrderReportVO result = new MiniProgramOrderReportVO();
        String date = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_DATE_1);
        //总金额
        String cachePrice = redisService.getString(MINI_PROGRAM_ORDER_REPORT_PRICE.concat(date));
        if (StringUtils.isNotEmpty(cachePrice)) {
            result.setTotalPrice(new BigDecimal(cachePrice));
        }
        //分时金额
        result.setHourPrice(redisService.getObj(MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE.concat(date), Map.class));
        //订单数据
        result.setOrders(redisService.getList(MINI_PROGRAM_ORDER_REPORT_LIST.concat(date), MiniProgramOrderReportVO.OrderReportDetailVO.class));
        return result;
    }

    public void sendWxCancelOrderMessage(Trade trade){
        if(!Objects.equals(trade.getChannelType(),ChannelType.MINIAPP)){
            return;
        }
        try {
            WxSendMessageRequest request = new WxSendMessageRequest();
            request.setOpenId(trade.getBuyer().getOpenId());
            request.setTemplateId(cancelOrderSendMsgTemplateId);
            request.setUrl(createOrderSendMsgLinkUrl+trade.getId());
            Map<String, Map<String, String>> map = new HashMap<>();
           map.put("character_string2", new HashMap<String, String>() {{
                put("value", trade.getId());
            }});
            map.put("thing1", new HashMap<String, String>() {{
                put("value", "超时未付款");
            }});
            map.put("time3", new HashMap<String, String>() {{
                put("value", DateUtil.format(LocalDateTime.now(), DateUtil.FMT_DATE_1));
            }});
            map.put("thing4", new HashMap<String, String>() {{
                put("value", "普通订单");
            }});
            request.setData(map);
            BaseResponse<WxResponseBase> response = wxCommonController.sendMessage(request);
            log.info("微信小程序取消订单发送消息request:{},response:{}",request,response);
        }catch (Exception e){
            log.error("微信小程序取消订单发送消息失败,trade:{}",trade,e);
      }
    }


    public void createWxOrder(String tid) {
        Trade trade = tradeRepository.findById(tid).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{tid});
        }
        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            return;
        }
        log.info("微信小程序订单创建start,tid:{}", tid);
        WxCreateOrderRequest wxCreateOrderRequest = null;
        try {
            //先创建订单
            wxCreateOrderRequest = this.buildRequest(trade);
            BaseResponse<WxCreateOrderResponse> orderResult = wxOrderApiController.addOrder(wxCreateOrderRequest);
            log.info("微信小程序0元订单创建，request:{},response:{}", wxCreateOrderRequest, orderResult);
            if (orderResult == null || orderResult.getContext() == null || !orderResult.getContext().isSuccess()) {
                addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), (orderResult != null ? JSON.toJSONString(orderResult) : "空"), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
                return;
            }
        } catch (Exception e) {
            log.error("微信小程序创建订单失败，tid：{}", tid, e);
            addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), e.getMessage(), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
            return;
        }
    }

    public WxCreateOrderRequest buildRequest(Trade trade) {
        WxCreateOrderRequest result = new WxCreateOrderRequest();
        result.setOutOrderId(trade.getId());
        result.setCreateTime(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_1));
        result.setOpenid(trade.getBuyer().getOpenId());
        result.setPath(orderListUrl);
        result.setFundType(0);
        result.setAftersaleDuration(7);
        if (Objects.equals(trade.getChannelType(), ChannelType.MINIAPP) && Objects.equals(trade.getMiniProgramScene(), 2)) {
            result.setFundType(1);
        }
        result.setExpireTime(LocalDateTime.now().plusHours(1).toEpochSecond(ZoneOffset.of("+8")));
        WxOrderDetailDTO detail = new WxOrderDetailDTO();
        List<WxProductInfoDTO> productInfoDTOS = new ArrayList<>();
        trade.getTradeItems().forEach(tradeItem -> {
            productInfoDTOS.add(WxProductInfoDTO.builder()
                    .outProductId(tradeItem.getSpuId())
                    .outSkuId(tradeItem.getSkuId())
                    .productNum(tradeItem.getNum())
                    .salePrice(tradeItem.getSplitPrice().multiply(new BigDecimal(100)).divide(new BigDecimal(tradeItem.getNum()),2,BigDecimal.ROUND_DOWN).intValue())
                    .realPrice(tradeItem.getSplitPrice().multiply(new BigDecimal(100)).divide(new BigDecimal(tradeItem.getNum()),2,BigDecimal.ROUND_DOWN).intValue())
                    .skuRealPrice(tradeItem.getSplitPrice().multiply(new BigDecimal(100)).intValue())
                    .title(tradeItem.getSkuName())
                    .path(goodsDetailUrl + tradeItem.getSpuId())
                    .headImg(StringUtils.isEmpty(tradeItem.getPic()) ? defaultImageUrl : tradeItem.getPic()).build());
        });
        detail.setProductInfos(productInfoDTOS);

        detail.setPayInfo(WxPayInfoDTO.builder().payMethodType(0)
                .prepayId(trade.getId())
                .prepayTime(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_1)).build());

        WxPriceInfoDTO priceInfo = new WxPriceInfoDTO();
        if (trade.getTradePrice().getTotalPrice() != null) {
            priceInfo.setOrderPrice(trade.getTradePrice().getTotalPrice().multiply(new BigDecimal(100)).intValue());
        }
        if (trade.getTradePrice().getDeliveryPrice() != null) {
            priceInfo.setFreight(trade.getTradePrice().getDeliveryPrice().multiply(new BigDecimal(100)).intValue());
        }
        detail.setPriceInfo(priceInfo);

        WxAddressInfoDTO addressInfo = new WxAddressInfoDTO();
        addressInfo.setCity(trade.getConsignee().getCityName());
        addressInfo.setReceiverName(trade.getConsignee().getName());
        addressInfo.setDetailedAddress(trade.getConsignee().getDetailAddress());
        addressInfo.setProvince(trade.getConsignee().getProvinceName());
        addressInfo.setTown(trade.getConsignee().getAreaName());
        addressInfo.setTelNumber(trade.getConsignee().getPhone());
        result.setAddressInfo(addressInfo);
        result.setOrderDetail(detail);
        return result;
    }


    public PaymentParamsDTO getPaymentParams(Trade trade) {
        WxOrderDetailRequest request = new WxOrderDetailRequest();
        request.setOpenid(trade.getBuyer().getOpenId());
        request.setOutOrderId(trade.getId());
        BaseResponse<GetPaymentParamsResponse> response = wxOrderApiController.getPaymentParams(request);
        if (response == null || response.getContext().getPaymentParams() == null) {
            return null;
        }
        return response.getContext().getPaymentParams();
    }


    /**
     * 创建售后单-视频号
     *
     * @param returnOrder
     */
    public void addEcAfterSale(ReturnOrder returnOrder) {
        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }
        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex()) || returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
            return;
        }
        WxCreateNewAfterSaleRequest request = new WxCreateNewAfterSaleRequest();
        request.setOutOrderId(returnOrder.getTid());
        request.setOutAftersaleId(returnOrder.getId());
        request.setOpenid(returnOrder.getBuyer().getOpenId());
        request.setType(Objects.equals(ReturnType.RETURN, returnOrder.getReturnType()) ? 2 : 1);
        request.setRefundReason(returnOrder.getDescription());
        request.setRefundReasonType(getReasonType(returnOrder.getReturnReason()));
        returnOrder.getReturnItems().forEach(item -> {
            request.setOrderamt(returnOrder.getReturnPrice().getApplyPrice().multiply(new BigDecimal(100)).longValue());
            WxCreateNewAfterSaleRequest.ProductInfo productInfo = new WxCreateNewAfterSaleRequest.ProductInfo();
            productInfo.setOutProductId(item.getSpuId());
            productInfo.setOutSkuId(item.getSkuId());
            productInfo.setProductCnt(item.getNum());
            request.setProductInfo(productInfo);
            BaseResponse<WxCreateNewAfterSaleResponse> response = wxOrderApiController.createNewAfterSale(request);
            log.info("微信小程序创建售后request:{},response:{}", request, response);
            if(response == null || response.getContext() ==null || !response.getContext().isSuccess()){
                throw new SbcRuntimeException("K-050415");
            }

        });
    }

    private Integer getReasonType(ReturnReason returnReason){
        if(Objects.equals(returnReason,ReturnReason.ERRORGOODS)){
            return WxAfterSaleReasonType.INCORRECT_SELECTION.getId();
        }
        if(Objects.equals(returnReason,ReturnReason.WRONGGOODS)){
            return WxAfterSaleReasonType.NO_LONGER_WANT.getId();
        }
        return WxAfterSaleReasonType.OTHERS.getId();
    }


    /**
     * 同意退款
     */
    public void acceptRefundAfterSale(ReturnOrder returnOrder) {
        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }
        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
            return;
        }
        WxDealAftersaleRequest request = new WxDealAftersaleRequest();
        request.setOutAftersaleId(returnOrder.getId());
        BaseResponse<WxResponseBase> response = wxOrderApiController.acceptRefundAfterSale(request);
        log.info("微信小程序同意退款request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050415");
        }

    }

    /**
     * 同意退款
     */
    public void cancelAfterSale(ReturnOrder returnOrder) {
        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }
        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
            return;
        }
        WxDealAftersaleNeedOpenidRequest request = new WxDealAftersaleNeedOpenidRequest();
        request.setOutAftersaleId(returnOrder.getId());
        request.setOpenid(returnOrder.getBuyer().getOpenId());
        BaseResponse<WxResponseBase> response = wxOrderApiController.cancelAfterSale(request);
        log.info("微信小程序取消售后request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050415");
        }

    }


    /**
     * 售后-同意退货
     */
    public void acceptReturnAfterSale(ReturnOrder returnOrder) {
        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }
        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
            return;
        }
        ReturnAddress returnAddress = returnOrder.getReturnAddress();
        if (returnAddress == null) {
            throw new SbcRuntimeException("K-050417");
        }
        //获取公司收货地址
        WxAcceptReturnAftersaleRequest request = new WxAcceptReturnAftersaleRequest();
        request.setOutAftersaleId(returnOrder.getId());
        WxAcceptReturnAftersaleRequest.AddressInfo addressInfo = new WxAcceptReturnAftersaleRequest.AddressInfo();
        addressInfo.setDetailedAddress(returnAddress.getDetailAddress());
        addressInfo.setCity(returnAddress.getCityName());
        addressInfo.setCountry("中国");
        addressInfo.setProvince(returnAddress.getProvinceName());
        addressInfo.setTelNumber(returnAddress.getPhone());
        addressInfo.setReceiverName(returnAddress.getName());
        addressInfo.setTown(returnAddress.getAreaName());
        request.setAddressInfo(addressInfo);
        BaseResponse<WxResponseBase> response = wxOrderApiController.acceptReturnAfterSale(request);
        log.info("微信小程序同意退货request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050415");
        }

    }

    /**
     * 售后-同意退货
     */
    public void rejectAfterSale(ReturnOrder returnOrder) {
        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }
        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
            return;
        }
        WxDealAftersaleRequest request = new WxDealAftersaleRequest();
        request.setOutAftersaleId(returnOrder.getId());
        BaseResponse<WxResponseBase> response = wxOrderApiController.rejectAfterSale(request);
        log.info("微信小程序拒绝售后request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050415");
        }

    }

    /**
     * 售后状态通知
     * @param returnOrder
     */
    public void sendWxAfterSaleMessage(ReturnOrder returnOrder){
        if(!Objects.equals(returnOrder.getChannelType(),ChannelType.MINIAPP)){
            return;
        }
        try {
            WxSendMessageRequest request = new WxSendMessageRequest();
            request.setOpenId(returnOrder.getBuyer().getOpenId());
            request.setTemplateId(afterSaleSendMsgTemplateId);
            request.setUrl(createOrderSendMsgLinkUrl+returnOrder.getTid());
            Map<String, Map<String, String>> map = new HashMap<>();
            map.put("character_string1", new HashMap<String, String>() {{
                put("value", returnOrder.getTid());
            }});
            map.put("thing6", new HashMap<String, String>() {{
                put("value", filterChineseAndAlp(returnOrder.getReturnItems().get(0).getSpuName()));
            }});
            map.put("amount2", new HashMap<String, String>() {{
                put("value", String.valueOf(returnOrder.getReturnPrice().getTotalPrice()));
            }});
            map.put("phrase4", new HashMap<String, String>() {{
                put("value", returnOrder.getReturnFlowState().getDesc());
            }});
            request.setData(map);
            BaseResponse<WxResponseBase> response = wxCommonController.sendMessage(request);
            log.info("微信小程序售后通知发送消息request:{},response:{}",request,response);
        }catch (Exception e){
            log.error("微信小程序售后通知发送消息失败,trade:{}",returnOrder,e);
        }
    }

    /**
     * 售后-上传物流信息
     */
    public void uploadReturnInfo(ReturnOrder returnOrder) {
        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
        if (trade == null || returnOrder == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }
        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
            return;
        }
        WxUploadReturnInfoRequest request = new WxUploadReturnInfoRequest();
        request.setOutAftersaleId(returnOrder.getId());
        request.setOpenId(returnOrder.getBuyer().getOpenId());
        request.setWayBillId(returnOrder.getReturnLogistics().getNo());
        request.setDeliveryName(returnOrder.getReturnLogistics().getCompany());
        request.setDeliveryId(returnOrder.getReturnLogistics().getCode());
        BaseResponse<WxResponseBase> response = wxOrderApiController.uploadReturnInfo(request);
        log.info("微信小程序上传物流信息request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050415");
        }

    }
}
