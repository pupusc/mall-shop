package com.soybean.mall.order.miniapp.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.soybean.mall.order.api.request.order.CreateWxOrderAndPayRequest;
import com.soybean.mall.order.bean.dto.WxLogisticsInfoDTO;
import com.soybean.mall.order.bean.vo.MiniProgramOrderReportVO;
import com.soybean.mall.order.bean.vo.OrderCommitResultVO;
import com.soybean.mall.order.trade.model.OrderReportDetailDTO;
import com.soybean.mall.wx.mini.common.bean.request.WxSendMessageRequest;
import com.soybean.mall.wx.mini.common.controller.CommonController;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.dto.*;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import com.soybean.mall.wx.mini.order.bean.request.WxDeliverySendRequest;
import com.soybean.mall.wx.mini.order.bean.request.WxOrderPayRequest;
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

    private final String BATCH_UPDATE_DELIVERY_STATUS_TO_WECHAT_LOCKS = "syncDeliveryStatusToWechat";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WxOrderApiController wxOrderApiController;

    @Autowired
    private TradeRepository tradeRepository;

    @Value("${wx.order.delivery.send.message.templateId}")
    private String orderDeliveryMsgTemplateId;

    @Value("${wx.create.order.send.message.link.url}")
    private String createOrderSendMsgLinkUrl;

    @Value("${wx.create.order.send.message.templateId}")
    private String createOrderSendMsgTemplateId;

    @Autowired
    private CommonController wxCommonController;

    @Value("${wx.logistics}")
    private String wxLogisticsStr;

    @Value("${wx.default.image.url}")
    private String defaultImageUrl;

    @Value("${wx.goods.detail.url}")
    private String goodsDetailUrl;
    @Value("${wx.order.detail.url}")
    private String orderDetailUrl;

    @Autowired
    private RedisService redisService;

    private static  final String MINI_PROGRAM_ORDER_REPORT_PRICE = "mini:ord:report:price:";

    private static  final String MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE = "mini:ord:report:hour:price:";

    private static  final String MINI_PROGRAM_ORDER_REPORT_LIST = "mini:ord:list:";
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
//            criterias.add(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()));
//            criterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
//            criterias.add(Criteria.where("tradeState.deliverStatus").ne(DeliverStatus.NOT_YET_SHIPPED.getStatusId()));
//            criterias.add(Criteria.where("channelType").is(ChannelType.MINIAPP));
//            criterias.add(Criteria.where("miniProgram.syncStatus").is(0));
//            criterias.add(Criteria.where("cycleBuyFlag").is(false));
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
                    Criteria.where("channelType").is(ChannelType.MINIAPP),
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
        WxDeliverySendRequest request = new WxDeliverySendRequest();
        request.setOpenid(trade.getBuyer().getOpenId());
        request.setOutOrderId(trade.getId());
        request.setFinishAllDelivery(Objects.equals(trade.getTradeState().getDeliverStatus(),DeliverStatus.SHIPPED)?1:0);
        List<WxDeliverySendRequest.WxDeliveryInfo>  deliveryInfos = new ArrayList<>();
        unSyncDelivery.forEach(delivery->{
            WxDeliverySendRequest.WxDeliveryInfo deliveryInfo = new WxDeliverySendRequest.WxDeliveryInfo();
            deliveryInfo.setDeliveryId(getWxLogisticsCode(delivery.getLogistics().getLogisticStandardCode(),delivery.getLogistics().getLogisticCompanyName()));
            deliveryInfo.setWaybillId(delivery.getLogistics().getLogisticNo());
            List<WxProductDTO> productDTS = new ArrayList<>();
            delivery.getShippingItems().forEach(item->{
                WxProductDTO wxProductDTO = new WxProductDTO();
                wxProductDTO.setOutProductId(item.getSpuId());
                wxProductDTO.setOutSkuId(item.getSkuId());
                wxProductDTO.setPrroductNum(item.getItemNum().intValue());
                productDTS.add(wxProductDTO);
            });
            deliveryInfo.setProductInfoList(productDTS);
            deliveryInfos.add(deliveryInfo);
        });
        request.setDeliveryList(deliveryInfos);
        BaseResponse<WxResponseBase> result = wxOrderApiController.deliverySend(request);
        if(result!=null && result.getContext().isSuccess() && Objects.equals(trade.getTradeState().getDeliverStatus(),DeliverStatus.SHIPPED)){
            //全部发货且已经全部同步
            trade.getMiniProgram().setSyncStatus(1);
        }
        trade.getMiniProgram().setDelivery(trade.getTradeDelivers());
        tradeRepository.save(trade);
        sendWxDeliveryMessage(trade);
    }


    private BaseResponse<WxResponseBase> sendWxDeliveryMessage(Trade trade){
        if(!Objects.equals(trade.getChannelType(),ChannelType.MINIAPP)){
            return null;
        }
        WxSendMessageRequest request =new WxSendMessageRequest();
        request.setOpenId(trade.getBuyer().getOpenId());
        request.setTemplateId(orderDeliveryMsgTemplateId);
        request.setUrl(createOrderSendMsgLinkUrl);
        Map<String, Map<String,String>> map = new HashMap<>();
       map.put("character_string1",new HashMap<String,String>(){{
            put("value", trade.getId());
        }});
        map.put("thing2",new HashMap<String,String>(){{
            put("value", filterChineseAndAlp(trade.getTradeItems().get(0).getSpuName()));
        }});
        map.put("phrase3",new HashMap<String,String>(){{
            put("value", trade.getTradeDelivers().get(0).getLogistics().getLogisticCompanyName());
        }});
        map.put("character_string4",new HashMap<String,String>(){{
            put("value", trade.getTradeDelivers().get(0).getLogistics().getLogisticNo());
        }});
        map.put("thing9",new HashMap<String,String>(){{
            put("value", "");
        }});
        request.setData(map);
        return wxCommonController.sendMessage(request);
    }


    public BaseResponse<WxResponseBase> sendWxCreateOrderMessage(Trade trade){
        if(!Objects.equals(trade.getChannelType(),ChannelType.MINIAPP)){
            return null;
        }
        WxSendMessageRequest request =new WxSendMessageRequest();
        request.setOpenId(trade.getBuyer().getOpenId());
        request.setTemplateId(createOrderSendMsgTemplateId);
        request.setUrl(createOrderSendMsgLinkUrl);
        Map<String,Map<String,String>> map = new HashMap<>();
        String address =StringUtils.isNotEmpty(trade.getConsignee().getDetailAddress()) && trade.getConsignee().getDetailAddress().length()>20?trade.getConsignee().getDetailAddress().substring(0,20):trade.getConsignee().getDetailAddress();
        map.put("character_string1",new HashMap<String,String>(){{
            put("value", trade.getId());
        }});
        map.put("amount2",new HashMap<String,String>(){{
            put("value", String.valueOf(trade.getTradePrice().getTotalPrice()));
        }});
        map.put("thing3",new HashMap<String,String>(){{
            put("value", address);
        }});
        map.put("name4",new HashMap<String,String>(){{
            put("value", filterChineseAndAlp(trade.getTradeItems().get(0).getSpuName()));
        }});
        map.put("phrase5",new HashMap<String,String>(){{
            put("value", "待发货");
        }});
        request.setData(map);
        return wxCommonController.sendMessage(request);
    }

    private String filterChineseAndAlp(String str){
        String goodsName = str.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)]","");
        if(StringUtils.isEmpty(goodsName)){
            goodsName ="购买的商品";
        }
        return goodsName;
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
        if(trade == null){
            throw new SbcRuntimeException("");
        }
        //先创建订单
        BaseResponse<WxCreateOrderResponse> orderResult = wxOrderApiController.addOrder(buildRequest(trade));
        //支付同步
        WxOrderPayRequest wxOrderPayRequest =new WxOrderPayRequest();
        wxOrderPayRequest.setOpenId(trade.getBuyer().getOpenId());
        wxOrderPayRequest.setOutOrderId(trade.getId());
        wxOrderPayRequest.setActionId(1);
        wxOrderPayRequest.setPayTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1));
        wxOrderPayRequest.setTransactionId(trade.getId());
        BaseResponse<WxResponseBase> payResult = wxOrderApiController.orderPay(wxOrderPayRequest);
        this.sendWxCreateOrderMessage(trade);
    }

    private WxCreateOrderRequest buildRequest(Trade trade) {
        WxCreateOrderRequest result = new WxCreateOrderRequest();
        result.setOutOrderId(trade.getId());
        result.setCreateTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1));
        result.setOpenid(trade.getBuyer().getOpenId());
        result.setPath(orderDetailUrl);
        WxOrderDetailDTO detail = new WxOrderDetailDTO();
        List<WxProductInfoDTO> productInfoDTOS = new ArrayList<>();
        trade.getTradeItems().forEach(tradeItem -> {
            productInfoDTOS.add(WxProductInfoDTO.builder()
                    .outProductId(tradeItem.getSpuId())
                    .outSkuId(tradeItem.getSkuId())
                    .productNum(tradeItem.getNum())
                    .salePrice(tradeItem.getOriginalPrice().multiply(new BigDecimal(100)).intValue())
                    .realPrice(tradeItem.getSplitPrice().multiply(new BigDecimal(100)).intValue())
                    .title(tradeItem.getSkuName())
                    .path(goodsDetailUrl+tradeItem.getSpuId())
                    .headImg(StringUtils.isEmpty(tradeItem.getPic())?defaultImageUrl:tradeItem.getPic()).build());
        });
        detail.setProductInfos(productInfoDTOS);

        detail.setPayInfo(WxPayInfoDTO.builder().payMethodType(0)
                .prepayId(trade.getId())
                .prepayTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1)).build());

        WxPriceInfoDTO priceInfo = new WxPriceInfoDTO();
        if(trade.getTradePrice().getTotalPrice()!=null) {
            priceInfo.setOrderPrice(trade.getTradePrice().getTotalPrice().multiply(new BigDecimal(100)).intValue());
        }
        if(trade.getTradePrice().getDeliveryPrice()!=null) {
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

    /**
     * 小程序实时报表
     * @param tid
     */
    public void orderReportCache(String tid){
        Trade trade = tradeRepository.findById(tid).orElse(null);
        if(trade == null){
            throw new SbcRuntimeException("K");
        }
        try {

            String date = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_DATE_1);
            //总金额
            String cachePrice = redisService.getString(MINI_PROGRAM_ORDER_REPORT_PRICE.concat(date));
            BigDecimal lastPrice =new BigDecimal(0);
            if (StringUtils.isNotEmpty(cachePrice)) {
                lastPrice = new BigDecimal(cachePrice);
            }
            BigDecimal totalPrice = trade.getTradePrice().getTotalPrice().add(lastPrice).setScale(2, RoundingMode.HALF_UP);
            redisService.setString(MINI_PROGRAM_ORDER_REPORT_PRICE.concat(date), totalPrice.toString(), 86400);
            log.info("小程序实时报表设置付款金额，trade:{},now price:{},last price:{}",trade,totalPrice,lastPrice);
            //分时金额
            Integer hour = LocalDateTime.now().getHour();
            Map<Integer,BigDecimal> cacheHourPrice = redisService.getObj(MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE.concat(date),Map.class);
            if(cacheHourPrice == null || cacheHourPrice.isEmpty()){
                cacheHourPrice = new HashMap<>();
            }
            BigDecimal lastHourPrice =new BigDecimal(0);
            if (cacheHourPrice.containsKey(hour) && cacheHourPrice.get(hour) !=null) {
                lastHourPrice = cacheHourPrice.get(hour);
            }
            BigDecimal totalHourPrice = trade.getTradePrice().getTotalPrice().add(lastHourPrice).setScale(2, RoundingMode.HALF_UP);
            cacheHourPrice.put(hour,totalHourPrice);
            redisService.setObj(MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE.concat(date), cacheHourPrice, 86400);
            log.info("小程序实时报表设置分时付款金额，trade:{},now price:{},last price:{}",trade,totalPrice,lastPrice);
            //只保存20条数据
            OrderReportDetailDTO orderReportDetailDTO = OrderReportDetailDTO.builder()
                    .createTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1))
                    .goodsName(trade.getTradeItems().get(0).getSpuName())
                    .orderId(trade.getId())
                    .price(trade.getTradePrice().getTotalPrice()).build();
            List<OrderReportDetailDTO> newList = new ArrayList<>(20);
            List<OrderReportDetailDTO> list = redisService.getList(MINI_PROGRAM_ORDER_REPORT_LIST.concat(date),OrderReportDetailDTO.class);
            newList.add(0,orderReportDetailDTO);
            if(CollectionUtils.isNotEmpty(list)){
                newList.addAll(list.stream().limit(list.size()>19 ? 19 :list.size()).collect(Collectors.toList()));
            }
            redisService.setObj(MINI_PROGRAM_ORDER_REPORT_LIST.concat(date),newList,86400);

        }catch(Exception e){
            log.warn("小程序实时数据报表报错，trade:{}",new Gson().toJson(trade),e);
        }

    }

    public MiniProgramOrderReportVO getMiniProgramOrderReportCache(){
        MiniProgramOrderReportVO result = new MiniProgramOrderReportVO();
        String date = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_DATE_1);
        //总金额
        String cachePrice = redisService.getString(MINI_PROGRAM_ORDER_REPORT_PRICE.concat(date));
        if(StringUtils.isNotEmpty(cachePrice)){
            result.setTotalPrice(new BigDecimal(cachePrice));
        }
        //分时金额
        result.setHourPrice(redisService.getObj(MINI_PROGRAM_ORDER_REPORT_HOUR_PRICE.concat(date),Map.class));
        //订单数据
        result.setOrders(redisService.getList(MINI_PROGRAM_ORDER_REPORT_LIST.concat(date), MiniProgramOrderReportVO.OrderReportDetailVO.class));
        return result;
    }
}
