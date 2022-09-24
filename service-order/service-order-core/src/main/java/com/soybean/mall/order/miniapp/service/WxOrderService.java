package com.soybean.mall.order.miniapp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.soybean.mall.order.bean.vo.MiniProgramOrderReportVO;
import com.soybean.mall.order.config.OrderConfigProperties;
import com.soybean.mall.order.enums.MiniOrderOperateType;
import com.soybean.mall.wx.mini.enums.AfterSalesStateEnum;
import com.soybean.mall.wx.mini.enums.AfterSalesTypeEnum;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.soybean.mall.wx.mini.goods.bean.response.WxGetProductDetailResponse;
import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxVideoOrderDetailResponse;
import com.wanmi.sbc.order.api.enums.MiniProgramSceneType;
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
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnAddress;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Logistics;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import com.wanmi.sbc.order.trade.service.TradeCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private TradeCacheService tradeCacheService;

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

    @Autowired
    private OrderConfigProperties orderConfigProperties;

    @Autowired
    private WxGoodsApiController wxGoodsApiController;

    /**
     * 视频号售后公共部分
     */
    private boolean isVideoAfterSaleContinueValid(ReturnOrder returnOrder) {
        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }

        //表示小程序 视频号
        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP)
                || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
            log.info("WxOrderService  非视频号订单 不继续执行");
            return false;
        }

        if (returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) <= 0) {
            log.info("WxOrderService  视频号订单 价格为0 只走积分退款部分。");
            return false;
        }
        return true;
    }

    /**
     * 小程序订单同步确认收货
     *
     * @param trade
     */
    @Transactional
    public void syncWxOrderReceive(Trade trade) {
        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            return;
        }
        WxDeliveryReceiveRequest request = WxDeliveryReceiveRequest.builder().outOrderId(trade.getId()).openid(trade.getBuyer().getOpenId()).build();
        try {
            WxOrderDetailRequest wxOrderDetailRequest = new WxOrderDetailRequest();
            wxOrderDetailRequest.setOutOrderId(trade.getId());
            wxOrderDetailRequest.setOpenid(trade.getBuyer().getOpenId());
            WxVideoOrderDetailResponse context = wxOrderApiController.getDetail(wxOrderDetailRequest).getContext();
            //后续要抽取状态码
            if (context != null && context.getOrder() != null && context.getOrder().getStatus() == 30) {
                BaseResponse<WxResponseBase> response = wxOrderApiController.receive(request);
                log.info("微信小程序订单确认收货，request:{},response:{}", JSON.toJSONString(request), response != null ? JSON.toJSON(response) : "空");
                if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
                    addMiniOrderOperateResult(JSON.toJSONString(request), response != null ? JSON.toJSONString(response) : "空", MiniOrderOperateType.RECEIVE.getIndex(), trade.getId());
                }
            } else {
                log.error("微信小程序订单确认收货失败，orderId:{} 为:{} 无法进行确认收货", trade.getId(), JSON.toJSONString(context));
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
        Logistics logistics = trade.getTradeDelivers().get(0).getLogistics();
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
            put("value", logistics == null ? "无" : logistics.getLogisticCompanyName());
        }});
        map.put("character_string4", new HashMap<String, String>() {{
            put("value", logistics == null ? "无" : logistics.getLogisticNo());
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
            String goodsName = "";
            String pic = "";
            BigDecimal price = trade.getTradePrice().getGoodsPrice();
            for (TradeItem tradeItem : trade.getTradeItems()) {
                if (StringUtils.isBlank(tradeItem.getPackId())) {
                    goodsName = tradeItem.getSpuName();
                    pic = tradeItem.getPic();
                } else {
                    if (Objects.equals(tradeItem.getSpuId(), tradeItem.getPackId())) {
                        goodsName = tradeItem.getSpuName();
                        pic = tradeItem.getPic();
                    }
                }
            }

            OrderReportDetailDTO orderReportDetailDTO = OrderReportDetailDTO.builder()
                    .createTime(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_1))
                    .goodsName(goodsName)
                    .orderId(trade.getId())
                    .pic(pic)
                    .price(price).build();
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
        Map<Integer,BigDecimal> hourPriceMap = redisService.getObj(MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE.concat(date), Map.class);
        if (hourPriceMap == null) {
            hourPriceMap = new HashMap<>();
        }
//        Map<String, BigDecimal> hourPriceResultMap = new LinkedHashMap<>();
//        for (int i = 0; i< 24 ; i++) {
//            if (i < 7) {
//                String key = "0-6";
//                BigDecimal mergePriceKey = hourPriceResultMap.get(key) == null ? BigDecimal.ZERO : hourPriceResultMap.get(key);
//                BigDecimal currentPrice = hourPriceMap.get(i) == null ? BigDecimal.ZERO : hourPriceMap.get(i);
//                hourPriceResultMap.put(key + "时", mergePriceKey.add(currentPrice));
//            } else {
//                hourPriceResultMap.put(i + "时", hourPriceMap.get(i) == null ? BigDecimal.ZERO : hourPriceMap.get(i));
//            }
//
//        }
        result.setHourPrice(hourPriceMap);
        //订单数据
        result.setOrders(redisService.getList(MINI_PROGRAM_ORDER_REPORT_LIST.concat(date), MiniProgramOrderReportVO.OrderReportDetailVO.class));
        return result;
    }

    public void sendWxCancelOrderMessage(Trade trade, WxVideoOrderDetailResponse context){
        if(!Objects.equals(trade.getChannelType(),ChannelType.MINIAPP)){
            return;
        }

        try {
            //视频号
            if (Objects.equals(trade.getChannelType(),ChannelType.MINIAPP) && Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
                if (StringUtils.isBlank(trade.getBuyer().getOpenId())) {
                    log.error("WxOrderService sendWxCancelOrderMessage orderId:{} openId:{} 自动取消openid为空", trade.getId(), trade.getBuyer().getOpenId());
                    return;
                }

                if (context.getOrder().getStatus() != 10) {
                    log.error("WxOrderService sendWxCancelOrderMessage orderId:{} openId:{} 获取微信订单状态为：{} 不可以自动取消微信订单", trade.getId(), trade.getBuyer().getOpenId(), context.getOrder().getStatus());
                    return;
                }
                WxOrderCancelRequest wxOrderCancelRequest = new WxOrderCancelRequest();
                wxOrderCancelRequest.setOutOrderId(trade.getId());
                wxOrderCancelRequest.setOpenid(trade.getBuyer().getOpenId());
                wxOrderCancelRequest.setOrderId(context.getOrder().getOrderId());
                BaseResponse<WxResponseBase> wxResponseBaseBaseResponse = wxOrderApiController.cancelOrder(wxOrderCancelRequest);
                log.info("WxOrderService sendWxCancelOrderMessage orderId:{} openId:{} 取消结果为：{}", trade.getId(), trade.getBuyer().getOpenId(), JSON.toJSONString(wxResponseBaseBaseResponse));
                this.releaseWechatVideoStock(context);

            } else {
                WxSendMessageRequest request = new WxSendMessageRequest();
                request.setOpenId(trade.getBuyer().getOpenId());
                request.setTemplateId(cancelOrderSendMsgTemplateId);
                request.setUrl(createOrderSendMsgLinkUrl + trade.getId());
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
                log.info("微信小程序取消订单发送消息request:{},response:{}", request, response);
            }
        }catch (Exception e){
            log.error("微信小程序/视频号取消订单发送消息失败,trade:{}",trade,e);
      }
    }

    /**
     * 释放库存，添加trycatch 防止异常影响数据，此处添加try catch
     * @param context
     */
    public void releaseWechatVideoStock(WxVideoOrderDetailResponse context) {
        if (context == null || context.getOrder() == null) {
            return;
        }

        try {
            //1、获取商品的库存
            for (WxVideoOrderDetailResponse.ProductInfos productInfo : context.getOrder().getOrderDetail().getProductInfos()) {
                // 获取总的库存信息
                BaseResponse<WxGetProductDetailResponse.Spu> productDetail =
                        wxGoodsApiController.getProductDetail(productInfo.getOutProductId());
                for (WxGetProductDetailResponse.Sku skuParam : productDetail.getContext().getSkus()) {
                    if (Objects.equals(productInfo.getOutSkuId(), skuParam.getOutSkuId())) {
                        WxUpdateProductWithoutAuditRequest wxUpdateProductWithoutAuditRequest = new WxUpdateProductWithoutAuditRequest();
                        wxUpdateProductWithoutAuditRequest.setOutProductId(productInfo.getOutProductId());

                        List<WxUpdateProductWithoutAuditRequest.Sku> skus = new ArrayList<>();
                        WxUpdateProductWithoutAuditRequest.Sku sku = new WxUpdateProductWithoutAuditRequest.Sku();
                        sku.setOutSkuId(productInfo.getOutSkuId());
                        sku.setStockNum(skuParam.getStockNum() + productInfo.getProductCnt());
                        skus.add(sku);
                        wxUpdateProductWithoutAuditRequest.setSkus(skus);
                        BaseResponse<WxResponseBase> wxResponseBaseBaseResponse = wxGoodsApiController.updateGoodsWithoutAudit(wxUpdateProductWithoutAuditRequest);
                        log.error("微信小程序 视频号取消订单 {} 释放库存返回的结果为 {}", context.getOrder().getOutOrderId(), JSON.toJSONString(wxResponseBaseBaseResponse));
                    }
                }
            }
        } catch (Exception ex) {
            log.error("视频号取消订单 {} 增加库存异常", context.getOrder().getOutOrderId());
        }
    }

    /**
     * 获取微信视频号订单信息
     * @param trade
     */
    public WxVideoOrderDetailResponse getWechatVideoOrder(Trade trade) {
        WxOrderDetailRequest request = new WxOrderDetailRequest();
        request.setOutOrderId(trade.getId());
        request.setOpenid(trade.getBuyer().getOpenId());
        BaseResponse<WxVideoOrderDetailResponse> detail = wxOrderApiController.getDetail(request);
        WxVideoOrderDetailResponse context = detail.getContext();
        log.info("WxOrderService sendWxCancelOrderMessage orderId:{} openId:{} 获取微信订单信息为{}", trade.getId(), trade.getBuyer().getOpenId(), JSON.toJSONString(context));
        return context;
    }

//    public void createWxOrder(String tid) {
//        Trade trade = tradeRepository.findById(tid).orElse(null);
//        if (trade == null) {
//            throw new SbcRuntimeException("K-050100", new Object[]{tid});
//        }
//        if (!Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
//            return;
//        }
//        log.info("微信小程序订单创建start,tid:{}", tid);
//        WxCreateOrderRequest wxCreateOrderRequest = null;
//        try {
//            //先创建订单
//            wxCreateOrderRequest = this.buildRequest(trade);
//            BaseResponse<WxCreateOrderResponse> orderResult = wxOrderApiController.addOrder(wxCreateOrderRequest);
//            log.info("微信小程序0元订单创建，request:{},response:{}", wxCreateOrderRequest, orderResult);
//            if (orderResult == null || orderResult.getContext() == null || !orderResult.getContext().isSuccess()) {
//                addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), (orderResult != null ? JSON.toJSONString(orderResult) : "空"), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
//                return;
//            }
//        } catch (Exception e) {
//            log.error("微信小程序创建订单失败，tid：{}", tid, e);
//            addMiniOrderOperateResult(JSON.toJSONString(wxCreateOrderRequest), e.getMessage(), MiniOrderOperateType.ADD_ORDER.getIndex(), tid);
//            return;
//        }
//    }

    public WxCreateOrderRequest buildRequest(Trade trade) {

//        int outTime = 60; //1小时
//        try {
//            // 查询设置中订单超时时间
//            JSONObject timeoutCancelConfigJsonObj = JSON.parseObject(orderConfigProperties.getTimeOutJson());
//            Object minuteObj = timeoutCancelConfigJsonObj.get("wxOrderTimeOut");
//            if (minuteObj != null) {
//                int tmpOutTime = Integer.parseInt(minuteObj.toString());
//                if (tmpOutTime >= 15 && tmpOutTime <= (24 * 60)) {
//                    outTime = tmpOutTime;
//                }
//            }
//        } catch (Exception ex) {
//            log.error("TradeService timeoutCancelConfig error", ex);
//        }
//        log.info("WxOrderService buildRequest 订单：{} 超时是:{}分钟", trade.getId(), outTime );
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
        result.setExpireTime(trade.getOrderTimeOut().toEpochSecond(ZoneOffset.of("+8")));
        WxOrderDetailDTO detail = new WxOrderDetailDTO();
        List<WxProductInfoDTO> productInfoDTOS = new ArrayList<>();
        for (TradeItem tradeItem : trade.getTradeItems()) {
            int salePrice = tradeItem.getSplitPrice().multiply(new BigDecimal(100)).divide(new BigDecimal(tradeItem.getNum()), 2, BigDecimal.ROUND_DOWN).intValue();
            if (salePrice <= 0) {
                continue;
            }
            productInfoDTOS.add(WxProductInfoDTO.builder()
                    .outProductId(tradeItem.getSpuId())
                    .outSkuId(tradeItem.getSkuId())
                    .productNum(tradeItem.getNum())
                    .salePrice(salePrice)
                    .realPrice(salePrice)
                    .skuRealPrice(tradeItem.getSplitPrice().multiply(new BigDecimal(100)).intValue())
                    .title(tradeItem.getSkuName())
                    .path(goodsDetailUrl + tradeItem.getSpuId())
                    .headImg(StringUtils.isEmpty(tradeItem.getPic()) ? defaultImageUrl : tradeItem.getPic()).build());
        }
//        if (CollectionUtils.isEmpty(productInfoDTOS)) {
//            throw new SbcRuntimeException("K-999999", "有效商品为空");
//        }

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


    public PaymentParamsDTO getPaymentParams(String openId, String tid) {
        if (StringUtils.isBlank(openId) || StringUtils.isBlank(tid)) {
            throw new SbcRuntimeException(null, "K-050009", "参数有误");
        }
        WxOrderDetailRequest request = new WxOrderDetailRequest();
        request.setOpenid(openId);
        request.setOutOrderId(tid);
        BaseResponse<GetPaymentParamsResponse> response = wxOrderApiController.getPaymentParams(request);
        if (response == null) {
            throw new SbcRuntimeException(null, "K-050009", "获取预支付参数失败");
        }
        if (!response.getContext().isSuccess()) {
            throw new SbcRuntimeException(null, response.getContext().getErrcode().toString(), response.getContext().getErrmsg());
        }
        return response.getContext().getPaymentParams();
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
//        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
//        if (trade == null) {
//            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
//        }
//        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
//            return;
//        }
        if (!this.isVideoAfterSaleContinueValid(returnOrder)) {
            return;
        }
        if (StringUtils.isBlank(returnOrder.getAftersaleId())) {
            throw new SbcRuntimeException("K-050421");
        }

        //获取订单信息
        WxDealAftersaleRequest wxDealAftersaleRequest = new WxDealAftersaleRequest();
        wxDealAftersaleRequest.setAftersaleId(Long.valueOf(returnOrder.getAftersaleId()));
        BaseResponse<WxDetailAfterSaleResponse> wxDetailAfterSaleResponseBaseResponse = wxOrderApiController.detailAfterSale(wxDealAftersaleRequest);
        log.info("WxOrderService afterSaleId {} 运营退货退款 结果信息为：{}", returnOrder.getAftersaleId(), JSON.toJSONString(wxDetailAfterSaleResponseBaseResponse));
        if (!wxDetailAfterSaleResponseBaseResponse.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050427");
        }

        if (Objects.equals(wxDetailAfterSaleResponseBaseResponse.getContext().getAfterSalesOrder().getStatus(), AfterSalesStateEnum.AFTER_SALES_STATE_ELEVEN.getCode())) {
            throw new SbcRuntimeException("K-050433");
        }

        //退款成功
        if (Objects.equals(wxDetailAfterSaleResponseBaseResponse.getContext().getAfterSalesOrder().getStatus(), AfterSalesStateEnum.AFTER_SALES_STATE_THIRTEEN.getCode())) {
            //表示已经退款，直接修改订单状态即可
            throw new SbcRuntimeException("K-100104");
        }

//        //如果订单为退货退款，同时状态为2的时候，则修改微信订单为退款
//        if (Objects.equals(returnOrder.getReturnType(), ReturnType.RETURN)) {
//
//            WxDetailAfterSaleResponse context = wxDetailAfterSaleResponseBaseResponse.getContext();
//            WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder = context.getAfterSalesOrder();
//
//            //售后订单为退款,
//            if (Objects.equals(afterSalesOrder.getStatus(), AfterSalesStateEnum.AFTER_SALES_STATE_TWO.getCode())) {
//                //2表示退款，当前为退货退款状态，则修改订单为退款状态；
//                WxAfterSaleUpdateRequest request = new WxAfterSaleUpdateRequest();
//                request.setAftersaleId(afterSalesOrder.getAftersaleId());
//                request.setOrderamt(afterSalesOrder.getOrderamt());
//                request.setOpenid(afterSalesOrder.getOpenid());
//                request.setType(AfterSalesTypeEnum.REFUND.getCode());
//                request.setRefundReason(afterSalesOrder.getRefundReason());
//                request.setRefundReasonType(afterSalesOrder.getRefundReasonType());
//                BaseResponse<WxResponseBase> wxResponseBaseBaseResponse = wxOrderApiController.updateAfterSaleOrder(request);
//                log.info("WxOrderService afterSaleId {} 运营退货退款 结果信息为：{}", returnOrder.getAftersaleId(), JSON.toJSONString(wxResponseBaseBaseResponse));
//                if (!wxResponseBaseBaseResponse.getContext().isSuccess()) {
//                    throw new SbcRuntimeException("K-050428");
//                }
//            }
//
//        }


        WxDealAftersaleRequest request = new WxDealAftersaleRequest();
//        request.setOutAftersaleId(returnOrder.getId());
        request.setAftersaleId(Long.parseLong(returnOrder.getAftersaleId()));
        BaseResponse<WxResponseBase> response = wxOrderApiController.acceptRefundAfterSale(request);
        log.info("WxOrderService afterSaleId {} 运营退款 结果信息为：{}", returnOrder.getAftersaleId(), JSON.toJSONString(response));
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050425");
        }

    }

    /**
     * 取消退款
     */
    public void cancelAfterSale(ReturnOrder returnOrder) {
//        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
//        if (trade == null) {
//            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
//        }
//        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
//            return;
//        }
        if (!this.isVideoAfterSaleContinueValid(returnOrder)) {
            return;
        }
        if (StringUtils.isBlank(returnOrder.getAftersaleId())) {
            throw new SbcRuntimeException("K-050421");
        }
        WxDealAftersaleNeedOpenidRequest request = new WxDealAftersaleNeedOpenidRequest();
//        request.setOutAftersaleId(returnOrder.getId());
        request.setAftersaleId(Long.parseLong(returnOrder.getAftersaleId()));
        request.setOpenid(returnOrder.getBuyer().getOpenId());
        BaseResponse<WxResponseBase> response = wxOrderApiController.cancelAfterSale(request);
        log.info("微信小程序取消售后request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050423");
        }

    }


    /**
     * 售后-同意退货
     */
    public void acceptReturnAfterSale(ReturnOrder returnOrder) {
//        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
//        if (trade == null) {
//            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
//        }
//        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0 || Objects.equals(returnOrder.getReturnType(),ReturnType.REFUND)) {
//            return;
//        }
        if (!this.isVideoAfterSaleContinueValid(returnOrder)) {
            return;
        }
        ReturnAddress returnAddress = returnOrder.getReturnAddress();
        if (returnAddress == null) {
            throw new SbcRuntimeException("K-050417");
        }
        if (StringUtils.isBlank(returnOrder.getAftersaleId())) {
            throw new SbcRuntimeException("K-050421");
        }
        //获取公司收货地址
        WxAcceptReturnAftersaleRequest request = new WxAcceptReturnAftersaleRequest();
//        request.setOutAftersaleId(returnOrder.getId());
        request.setAftersaleId(Long.parseLong(returnOrder.getAftersaleId()));
        WxAcceptReturnAftersaleRequest.AddressInfo addressInfo = new WxAcceptReturnAftersaleRequest.AddressInfo();
        addressInfo.setDetailedAddress(returnAddress.getDetailAddress());
        addressInfo.setCity(returnAddress.getCityName());
        addressInfo.setCountry("中国");
        addressInfo.setProvince(returnAddress.getProvinceName());
        addressInfo.setTelNumber(returnAddress.getPhone());
        addressInfo.setReceiverName(returnAddress.getName());
        addressInfo.setTown(returnAddress.getAreaName());
        request.setAddressInfo(addressInfo);
        BaseResponse<WxResponseBase> response = wxOrderApiController.acceptReturnAfterSale(request); // 退货
        log.info("微信小程序同意退货request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050422");
        }

    }

    /**
     * 售后-拒绝退货
     */
    public void rejectAfterSale(ReturnOrder returnOrder) {

        if (!this.isVideoAfterSaleContinueValid(returnOrder)) {
            return;
        }

        if (StringUtils.isBlank(returnOrder.getAftersaleId())) {
            throw new SbcRuntimeException("K-050421");
        }
        WxDealAftersaleRequest request = new WxDealAftersaleRequest();
        request.setAftersaleId(Long.parseLong(returnOrder.getAftersaleId()));
        BaseResponse<WxResponseBase> response = wxOrderApiController.rejectAfterSale(request);
        log.info("微信小程序拒绝售后request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050424");
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
     * 查询之前售后单并取消
     * @param tid
     */
//    public void cancelAfterSaleByOrderId(String tid,ReturnOrder returnOrder){
//        Trade trade = tradeRepository.findById(tid).orElse(null);
//        if (trade == null ) {
//            throw new SbcRuntimeException("K-050100", new Object[]{tid});
//        }
//        if (!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) || !Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())|| returnOrder.getReturnPrice().getApplyPrice().compareTo(new BigDecimal(0)) == 0) {
//            return;
//        }
//        WxAfterSaleListRequest wxAfterSaleListRequest = new WxAfterSaleListRequest();
//        wxAfterSaleListRequest.setLimit(100);
//        wxAfterSaleListRequest.setOffset(0);
//        wxAfterSaleListRequest.setOutOrderId(tid);
//        wxAfterSaleListRequest.setOpenid(trade.getBuyer().getOpenId());
//        BaseResponse<WxListAfterSaleResponse> list = wxOrderApiController.listAfterSale(wxAfterSaleListRequest);
//        log.info("微信视频号获取售后列表，requets:{},response:{}",wxAfterSaleListRequest,list);
//        if(list == null || list.getContext() == null || CollectionUtils.isEmpty(list.getContext().getAfterSalesOrders())){
//            return;
//        }
//        list.getContext().getAfterSalesOrders().forEach(p->{
//            WxDealAftersaleRequest wxDealAftersaleRequest = new WxDealAftersaleRequest();
//            wxDealAftersaleRequest.setAftersaleId(Long.valueOf(p));
//            BaseResponse<WxDetailAfterSaleResponse> wxDetailAfterSaleResponse = wxOrderApiController.detailAfterSale(wxDealAftersaleRequest);
//            log.info("微信视频号获取售后详情，request:{},response:{}",wxDealAftersaleRequest,wxDetailAfterSaleResponse);
//            if(wxDetailAfterSaleResponse!=null && wxDetailAfterSaleResponse.getContext()!=null && wxDetailAfterSaleResponse.getContext().getAfterSalesOrder()!=null &&
//                    Arrays.asList(2,23).contains(wxDetailAfterSaleResponse.getContext().getAfterSalesOrder().getStatus())){
//                //取消
//                WxDealAftersaleNeedOpenidRequest wxDealAftersaleNeedOpenidRequest = new WxDealAftersaleNeedOpenidRequest();
//                wxDealAftersaleNeedOpenidRequest.setOpenid(trade.getBuyer().getOpenId());
//                wxDealAftersaleNeedOpenidRequest.setOutAftersaleId(wxDetailAfterSaleResponse.getContext().getAfterSalesOrder().getOutAftersaleId());
//                BaseResponse<WxResponseBase> cancelResponse = wxOrderApiController.cancelAfterSale(wxDealAftersaleNeedOpenidRequest);
//                log.info("微信视频号取消售后，request:{},response:{}",wxDealAftersaleNeedOpenidRequest,cancelResponse);
//                if(cancelResponse == null || cancelResponse.getContext() ==null || !cancelResponse.getContext().isSuccess()){
//                    throw  new SbcRuntimeException("K-050418");
//                }
//            }
//        });
//    }



    /*********************** 视频号售后 *************************************/

    /**
     * 创建售后单-视频号
     *
     * @param returnOrder
     */
    public String addEcAfterSale(ReturnOrder returnOrder, Trade trade) {

        String aftersaleId = "";
        if (!this.isVideoAfterSaleContinueValid(returnOrder)) {
            return aftersaleId;
        }

        if (returnOrder.getReturnItems().size() > 1) {
            throw new SbcRuntimeException("K-050429");
        }

        //查询订单的商品信息
        WxOrderDetailRequest wxOrderDetailRequest = new WxOrderDetailRequest();
        wxOrderDetailRequest.setOutOrderId(returnOrder.getTid());
        wxOrderDetailRequest.setOpenid(trade.getBuyer().getOpenId());
        BaseResponse<WxVideoOrderDetailResponse> orderDetailResponse = wxOrderApiController.getDetail(wxOrderDetailRequest);
        WxVideoOrderDetailResponse context = orderDetailResponse.getContext();
        if (!context.isSuccess()) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }

        //获取发货信息
        Map<String, ReturnType> skuId2ReturnTypeMap = new HashMap<>();
        Map<String, Boolean> skuId2CanAfterSaleMap = new HashMap<>();
        WxVideoOrderDetailResponse.DeliveryDetail deliveryDetail = context.getOrder().getDeliveryDetail();
        if (deliveryDetail != null) {
            for (WxVideoOrderDetailResponse.ProductInfos productInfo : context.getOrder().getOrderDetail().getProductInfos()) {
                skuId2CanAfterSaleMap.put(productInfo.getOutSkuId(), productInfo.getCanAfterSale());
            }
            if (!CollectionUtils.isEmpty(deliveryDetail.getDeliveryInfos())) {
                for (WxVideoOrderDetailResponse.DeliveryInfo deliveryInfo : deliveryDetail.getDeliveryInfos()) {
                    for (WxVideoOrderDetailResponse.DeliveryProduct deliveryProduct : deliveryInfo.getDeliveryProducts()) {
                        skuId2ReturnTypeMap.put(deliveryProduct.getOutSkuId(), ReturnType.RETURN);
                    }
                }
            }
        }


        //判断当前售后类型是否正确
        ReturnItem item = returnOrder.getReturnItems().get(0);
        ReturnType returnType = skuId2ReturnTypeMap.get(item.getSkuId());
        //表示退款
        if (returnType == null && !Objects.equals(returnOrder.getReturnType(), ReturnType.REFUND)) {
            throw new SbcRuntimeException("K-050430");
        }
        if (returnType != null && !Objects.equals(returnOrder.getReturnType(), ReturnType.RETURN)) {
            throw new SbcRuntimeException("K-050431");
        }

        if (skuId2CanAfterSaleMap.get(item.getSkuId()) != null && !skuId2CanAfterSaleMap.get(item.getSkuId())) {
            throw new SbcRuntimeException("K-050432", new Object[]{item.getSkuName()});
        }

        WxCreateNewAfterSaleRequest request = new WxCreateNewAfterSaleRequest();
        request.setOutOrderId(returnOrder.getTid());
        request.setOutAftersaleId(returnOrder.getId());
        request.setOpenid(returnOrder.getBuyer().getOpenId());
        request.setType(Objects.equals(ReturnType.RETURN, returnOrder.getReturnType()) ? 2 : 1);
        request.setRefundReason(returnOrder.getDescription());
        request.setRefundReasonType(getReasonType(returnOrder.getReturnReason()));

        request.setOrderamt(returnOrder.getReturnPrice().getApplyPrice().multiply(new BigDecimal(100)).longValue());
        WxCreateNewAfterSaleRequest.ProductInfo productInfo = new WxCreateNewAfterSaleRequest.ProductInfo();
        productInfo.setOutProductId(item.getSpuId());
        productInfo.setOutSkuId(item.getSkuId());
        productInfo.setProductCnt(item.getNum());
        request.setProductInfo(productInfo);
        BaseResponse<WxCreateNewAfterSaleResponse> response = wxOrderApiController.createNewAfterSale(request);
        log.info("WxOrderService addEcAfterSale 微信小程序创建售后request:{},response:{}", request, response);
        if(response == null || response.getContext() ==null){
            throw new SbcRuntimeException("K-050415");
        } else if (!response.getContext().isSuccess()) {
            throw new SbcRuntimeException(null, response.getContext().getErrcode().toString(), response.getContext().getErrmsg());
        } else {
            aftersaleId = response.getContext().getAftersaleId().toString();
        }
        return aftersaleId;
    }


    /**
     * 视频号售后-上传物流信息
     */
    public void uploadReturnInfo(ReturnOrder returnOrder) {
        if (!this.isVideoAfterSaleContinueValid(returnOrder)) {
            return;
        }
        if (StringUtils.isBlank(returnOrder.getAftersaleId())) {
            throw new SbcRuntimeException("K-050421");
        }
        WxDealAftersaleRequest wxDealAftersaleRequest = new WxDealAftersaleRequest();
        wxDealAftersaleRequest.setAftersaleId(Long.valueOf(returnOrder.getAftersaleId()));
        BaseResponse<WxDetailAfterSaleResponse> wxDetailAfterSaleResponseBaseResponse = wxOrderApiController.detailAfterSale(wxDealAftersaleRequest);
        WxDetailAfterSaleResponse context = wxDetailAfterSaleResponseBaseResponse.getContext();
        if (context != null) {
            WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder = context.getAfterSalesOrder();
            //表示退货退款
            if (!Objects.equals(afterSalesOrder.getType(), 2)) {
                log.info("WxOrderService uploadReturnInfo afterSalesOrder.getType() is not 2(退货退款) return  result:{}", JSON.toJSONString(afterSalesOrder));
                return;
            }
        }

        WxUploadReturnInfoRequest request = new WxUploadReturnInfoRequest();
        request.setAftersaleId(Long.parseLong(returnOrder.getAftersaleId()));
        request.setOpenid(returnOrder.getBuyer().getOpenId());
        request.setWayBillId(returnOrder.getReturnLogistics().getNo());
        request.setDeliveryName(returnOrder.getReturnLogistics().getCompany());
        request.setDeliveryId(returnOrder.getReturnLogistics().getCode());
        BaseResponse<WxResponseBase> response = wxOrderApiController.uploadReturnInfo(request);
        log.info("微信小程序上传物流信息request:{},response:{}", request, response);
        if (response == null || response.getContext() == null || !response.getContext().isSuccess()) {
            throw new SbcRuntimeException("K-050426");
        }

    }
}
