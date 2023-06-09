package com.wanmi.sbc.order.open;

import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Lists;
import com.wanmi.sbc.account.bean.enums.PayWay;
//import com.wanmi.sbc.common.base.BaseResponse;
//import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
//import com.wanmi.sbc.order.bean.enums.DeliverStatus;
//import com.wanmi.sbc.order.bean.enums.FlowState;
//import com.wanmi.sbc.order.bean.enums.ShipperType;
//import com.wanmi.sbc.order.open.model.FddsBaseResult;
//import com.wanmi.sbc.order.open.model.FddsOrderCreateParam;
//import com.wanmi.sbc.order.open.model.FddsOrderCreateResultData;
import com.wanmi.sbc.order.open.model.FddsOrderQueryResultData;
//import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
//import com.wanmi.sbc.order.trade.model.root.Trade;
//import com.wanmi.sbc.order.trade.repository.ProviderTradeRepository;
//import com.wanmi.sbc.order.trade.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-04-02 00:40:00
 */
@RefreshScope
@Slf4j
@Service
public class FddsProviderService {
    /**
     * 开放平台商品下单地址
     */
    private static String orderCreateUrl = "/user/order/createByThirdProductNo";
    /**
     * 开放平台订单查询地址
     */
    private static String orderQueryUrl = "/vip/order/detail";

//    @Autowired
//    private FddsOpenPlatformService fddsOpenPlatformService;
//    @Autowired
//    private TradeRepository tradeRepository;
//    @Autowired
//    private ProviderTradeRepository providerTradeRepository;
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Value("${notice.send.message.url}")
//    private String noticeSendMsgUrl;
//
//    @Value("${notice.send.message.token}")
//    private String noticeSendMsgToken;
//
//    @Value("${notice.send.message.tenantId}")
//    private String noticeSendMsgTenantId;
//
//    @Value("${notice.send.message.noticeId}")
//    private Integer noticeSendMsgNoticeId;
//
//    @Value("${wx.applet.general.promoter}")
//    private String wxAppletGeneralPromoter;
//
//    @Value("${wx.applet.video.promoter}")
//    private String wxAppletVideoPromoter;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 1.开放平台下单
     * 2.下单成功：更新发货状态
     * 3.下单失败：通知运营退款
     * 4.下单超时：补偿机制
     *      补偿机制->成功->更新发货状态
     *      补偿机制->失败->通知运营退款
     */
//    public BaseResponse createFddsTrade(ProviderTrade providerTrade) {
//        log.info("樊登读书->开放平台订单创建, provideTradeId={}", providerTrade.getId());
//
//        //处理重复提交
//        RLock lock = redissonClient.getLock("LOCK:CREATE-FDDS-ORDER:" + providerTrade.getId());
//        if (lock.isLocked()) {
//            log.warn("创建订单任务正在执行中，本次提交不做处理，tradeId = {}, providerTradeId = {}, deliveryOrderId = {}",
//                    providerTrade.getParentId(), providerTrade.getId(), providerTrade.getDeliveryOrderId());
//            return BaseResponse.FAILED();
//        }
//
//        try {
//            lock.lock(3, TimeUnit.SECONDS);
//            //验证重复下单
//            if (DeliverStatus.SHIPPED.equals(providerTrade.getTradeState().getDeliverStatus()) && StringUtils.isNotBlank(providerTrade.getDeliveryOrderId())) {
//                log.warn("该订单已经完成发货，本次提交不做处理，tradeId = {}, providerTradeId = {}, deliveryOrderId = {}",
//                        providerTrade.getParentId(), providerTrade.getId(), providerTrade.getDeliveryOrderId());
//                return BaseResponse.FAILED();
//            }
//
//            Trade trade = tradeRepository.findById(providerTrade.getParentId()).get();
//            if (Objects.isNull(trade)) {
//                log.info("根据订单id查询主单结果不存在, tradeId = {}", providerTrade.getParentId());
//                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
//            }
//
//            //供应商下单
//            FddsBaseResult createResult = createOutOrder(providerTrade, trade);
//            if (!createResult.isSuccess()) {
//                return createOutOrderFail(providerTrade, createResult);
//            }
//
//            FddsOrderCreateResultData resultData = JSON.parseObject(JSON.toJSONString(createResult.getData()), FddsOrderCreateResultData.class);
//            return createOutOrderSuccess(providerTrade, resultData.getOrderNumber());
//        } finally {
//            lock.unlock();
//        }
//    }

    /**
     * 创建樊登读书开放平台订单
     */
//    private FddsBaseResult createOutOrder(ProviderTrade providerTrade, Trade trade) {
//        TradeItem tradeItem = getUniqueItem(providerTrade);
//
//        FddsOrderCreateParam createParam = new FddsOrderCreateParam();
//        createParam.setTradeNo(providerTrade.getId());
//        createParam.setMobile(providerTrade.getDirectChargeMobile());
//        createParam.setExternalProductNo(tradeItem.getErpSkuNo());
//        createParam.setPayType(convertPayType(providerTrade.getPayWay()));
//        createParam.setPayTime(providerTrade.getTradeState().getPayTime());
//        createParam.setPlDiscontPrice(tradeItem.getPrice().multiply(new BigDecimal(100)));
//
//        if (ChannelType.MINIAPP.equals(trade.getChannelType())) {
//            //小程序非视频号
//            if (Integer.valueOf(1).equals(trade.getMiniProgramScene())) {
//                createParam.setPromoterType("4");
//                createParam.setPromoterNo(wxAppletGeneralPromoter);
//            } else if (Integer.valueOf(2).equals(trade.getMiniProgramScene())) { //小程序视频号
//                createParam.setPromoterType("4");
//                createParam.setPromoterNo(wxAppletVideoPromoter);
//            } else {
//                log.warn("小程序来源订单，没有匹配的推广人，miniProgramScene = {}", trade.getMiniProgramScene());
//            }
//        }
//
//        FddsBaseResult<FddsOrderCreateResultData> createResult;
//        try {
//            checkParams(createParam);
//            createResult = fddsOpenPlatformService.doRequest(orderCreateUrl, JSON.toJSONString(createParam), FddsBaseResult.class);
//        } catch (Exception e) {
//            Long orderNumber = queryUnknowOrder(providerTrade);
//            if (Objects.nonNull(orderNumber)) {
//                log.info("樊登读书下单失败，二次查询找到对应的订单, tradeNo = {}, orderNumber = {}", providerTrade.getId(), orderNumber);
//                return FddsBaseResult.success(new FddsOrderCreateResultData(providerTrade.getId(), orderNumber));
//            }
//
//            throw new SbcRuntimeException(e);
//        }
//
//        return createResult;
//    }Î

//    private void checkParams(FddsOrderCreateParam createParam) {
//        String errorMsg = createParam.checkParams();
//        if (Objects.nonNull(errorMsg)) {
//            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, errorMsg);
//        }
//    }

    /**
     * 下单成功
     * 1.更新订单号
     * 2.更新发货信息
     */
//    private BaseResponse createOutOrderSuccess(ProviderTrade providerTrade, Long orderNumber) {
//        //查询主单
//        Trade trade = tradeRepository.findById(providerTrade.getParentId()).orElse(null);
//        if (Objects.isNull(trade)) {
//            log.error("根据主单id查询主单结果不存在，id = {}", providerTrade.getParentId());
//            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
//        }
//
//        //根据主单号查询所有发货单并以此判断主单是部分发货还是全部发货
//        List<ProviderTrade> providerTrades = providerTradeRepository.findListByParentId(providerTrade.getParentId());
//        if (CollectionUtils.isEmpty(providerTrades)) {
//            log.error("根据主单id查询子单结果不存在，parentId = {}", providerTrade.getParentId());
//            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
//        }
//
//        log.info("FddsProviderSerivce.createOutOrderSuccess tradeId:{} update providerTrade:{}", trade.getId(), JSON.toJSONString(providerTrade));
//
//        //当前时间
//        LocalDateTime nowTime = LocalDateTime.now();
//
//        //生成发货单信息tradeDelivers（子单暂无、主单暂无）
//        List<TradeDeliver> tradeDeliverVOList = new ArrayList<>();
//        TradeDeliver tradeDeliverVO = new TradeDeliver();
//        tradeDeliverVO.setTradeId(providerTrade.getId());
//        tradeDeliverVO.setDeliverId(orderNumber.toString());
//        tradeDeliverVO.setDeliverTime(nowTime);
//        tradeDeliverVO.setShippingItems(Lists.newArrayList()); //发货清单设空
//        tradeDeliverVO.setProviderName(providerTrade.getSupplier().getSupplierName());
//        tradeDeliverVO.setShipperType(ShipperType.PROVIDER);
//        tradeDeliverVO.setStatus(DeliverStatus.SHIPPED);
//        tradeDeliverVOList.add(tradeDeliverVO);
//
//        //更新订单号
//        providerTrade.setDeliveryOrderId(orderNumber.toString());
//
//        //更新发货状态：
//        //子单全部发货
//        //1.子单商品发货信息 tradeItems.deliveredNum、tradeItems.deliverStatus
//        //2.子单订单发货信息 tradeState.deliverStatus、tradeState.deliverTime、tradeState.flowState
//        providerTrade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
//        providerTrade.getTradeState().setFlowState(FlowState.COMPLETED);
//        providerTrade.getTradeState().setDeliverTime(nowTime);
//        providerTrade.getTradeDelivers().addAll(tradeDeliverVOList);
//
//        //此处重新赋值商品信息
//        for (ProviderTrade p : providerTrades) {
//            log.info("FddsProviderSerivce.createOutOrderSuccess tradeId:{}, p:{}", trade.getId(), JSON.toJSONString(p));
//            if (Objects.equals(p.getId(), providerTrade.getId())) {
//                providerTrade.setTradeItems(p.getTradeItems());
//            }
//        }
//
//        for (TradeItem tradeItem : providerTrade.getTradeItems()) {
//            tradeItem.setDeliveredNum(tradeItem.getNum());
//            tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
//        }
//
//        //主单发货状态，默认全部发货
//        //3.主单商品发货信息 tradeItems.deliveredNum、tradeItems.deliverStatus
//        //4.主单订单发货信息 tradeState.deliverStatus、tradeState.deliverTime、tradeState.flowState
//        trade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
//        trade.getTradeState().setFlowState(FlowState.COMPLETED);
//        trade.getTradeState().setDeliverTime(nowTime);
//        trade.getTradeDelivers().addAll(tradeDeliverVOList);
//        //此发货单全部发货，判断其他发货单存在部分发货，则主订单是部分发货
//        for (ProviderTrade providerTradeParam : providerTrades) {
//            if (Objects.equals(providerTradeParam.getId(), providerTrade.getId())) {
//                continue;
//            }
//            if (!Objects.equals(providerTradeParam.getTradeState().getDeliverStatus(), DeliverStatus.SHIPPED)) {
//                trade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
//                trade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
//                break;
//            }
//        }
////        if (providerTrades.stream().anyMatch(p -> !providerTrade.getId().equals(p.getId())
////                && !DeliverStatus.SHIPPED.equals(p.getTradeState().getDeliverStatus())
////                && !FlowState.VOID.equals(p.getTradeState().getFlowState()))) {
////            log.info("FddsProviderSerivce.createOutOrderSuccess tradeId:{} update part_shipped", trade.getId());
////
////        }
//
//        //更新子单信息
//        providerTradeRepository.save(providerTrade);
//
//        List<String> itemOids = providerTrade.getTradeItems().stream().map(TradeItem::getOid).collect(Collectors.toList());
//        List<TradeItem> tradeItems = trade.getTradeItems().stream().filter(item -> !itemOids.contains(item.getOid())).collect(Collectors.toList());
//        tradeItems.addAll(providerTrade.getTradeItems());
//
//        log.info("FddsProviderSerivce.createOutOrderSuccess tradeId:{} update tradeItems:{}", trade.getId(), JSON.toJSONString(tradeItems));
//        trade.setTradeItems(tradeItems);
//
//        //如果全部发货，则更新发货数量
//        if (DeliverStatus.SHIPPED.equals(trade.getTradeState().getDeliverStatus())
//                && FlowState.DELIVERED.equals(trade.getTradeState().getFlowState())) {
//            for (TradeItem tradeItem : trade.getTradeItems()) {
//                tradeItem.setDeliveredNumHis(tradeItem.getDeliveredNum());
//                if (tradeItem.getNum() > tradeItem.getDeliveredNum()) {
//                    tradeItem.setDeliveredNum(tradeItem.getNum());
//                }
//            }
//            for (TradeItem giftsTradeItem : trade.getGifts()) {
//                giftsTradeItem.setDeliveredNumHis(giftsTradeItem.getDeliveredNum());
//                if (giftsTradeItem.getNum() > giftsTradeItem.getDeliveredNum()) {
//                    giftsTradeItem.setDeliveredNum(giftsTradeItem.getNum());
//                }
//            }
//        }
//        //更新主单信息
//        tradeRepository.save(trade);
//        return BaseResponse.success("开放平台下单成功");
//    }

    /**
     * 下单失败：
     * 1.更新订单备注
     * 2.消息通知退款
     *
     * 返回状态码：
     * 2001	会期商品不存在
     * 2006	已领取不可重复领取
     * 2007	商品已下架
     * 2008	书籍包已下线或未发布
     * 2009	书籍包重复购买
     * 2010	结算方式未配置
     * 2007	商品已下架
     */
//    private BaseResponse createOutOrderFail(ProviderTrade providerTrade, FddsBaseResult result) {
//        log.warn("开放平台商品下单失败, 响应编码 = {}, 响应信息 = {}", result.getStatus(), result.getMsg());
//
//        String msg = "充值失败：" + result.getMsg();
//        //更新子单备注
//        ProviderTrade result4Child = mongoTemplate.findAndModify(Query.query(Criteria.where("_id").is(providerTrade.getId())), Update.update("sellerRemark", msg), ProviderTrade.class);
//        if (Objects.isNull(result4Child)) {
//            throw new SbcRuntimeException(CommonErrorCode.FAILED, "订单子单备注信息更新失败");
//        }
//        //更新主单备注
//        Trade result4Parent = mongoTemplate.findAndModify(Query.query(Criteria.where("_id").is(providerTrade.getParentId())), Update.update("sellerRemark", msg), Trade.class);
//        if (Objects.isNull(result4Parent)) {
//            throw new SbcRuntimeException(CommonErrorCode.FAILED, "订单主单备注信息更新失败");
//        }
//        //消息通知客服
//        noticeWaiter(providerTrade);
//        return BaseResponse.FAILED();
//    }

//    private Long queryUnknowOrder(ProviderTrade providerTrade) {
//        Map<String, String> param = new HashMap<>();
//        param.put("tradeNo", providerTrade.getId());
//        FddsBaseResult queryResult = fddsOpenPlatformService.doRequest(orderQueryUrl, JSON.toJSONString(param), FddsBaseResult.class);
//
//        if (queryResult.isSuccess()) {
//            FddsOrderQueryResultData resultData = JSON.parseObject(JSON.toJSONString(queryResult.getData()), FddsOrderQueryResultData.class);
//            return resultData.getOrderNumber();
//        }
//        if ("0006".equals(queryResult.getStatus())) {
//            log.info("开放平台查询订单信息不存在, 状态码={}", queryResult.getStatus());
//            return null;
//        }
//        log.warn("开放平台查询订单信息失败，返回值={}", JSON.toJSONString(queryResult));
//        return null;
//    }

//    /**
//     * 合作方支付方式：99-其他（无支付渠道费），1-IAP为苹果支付（支付渠道费30%），2-支付宝 3-微信（支付渠道费0.6%）
//     */
//    private String convertPayType(PayWay payWay) {
//        if (PayWay.ALIPAY.equals(payWay)) {
//            return "2";
//        }
//        if (PayWay.WECHAT.equals(payWay)) {
//            return "3";
//        }
//        return "99";
//    }

    //子单号+sku编号+商品名称，充值失败；
//    private String NOTICE_SEND_MESSAGE = "{0}+{1}+{2}，充值失败；";
//    public void noticeWaiter(ProviderTrade providerTrade) {
//        //String orderNo = providerTrade.getId().replaceAll("[^0-9]", "").trim();
//        if (hasNoticed(providerTrade.getId())) {
//            log.info("下单失败，飞书通知已存在，本次通知取消，providerId = {}", providerTrade.getId());
//            return;
//        }
//
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        String noticeMsg = null;
//        try {
//            HttpPost post = new HttpPost(noticeSendMsgUrl);
//            post.addHeader("Content-type", "application/json; charset=utf-8");
//            post.setHeader("Accept", "application/json");
//            post.setHeader("token",noticeSendMsgToken);
//            post.setHeader("tenantId",noticeSendMsgTenantId);
//            Map<String,Object> content = new HashMap<>();
//
//            noticeMsg = MessageFormat.format(NOTICE_SEND_MESSAGE, providerTrade.getId(), getUniqueItem(providerTrade).getErpSkuNo(), getUniqueItem(providerTrade).getSkuName());
//            content.put("content", noticeMsg);
//            Map<String,Object> map = new HashMap<>();
//            map.put("replaceParams",content);
//            map.put("noticeId",noticeSendMsgNoticeId);
//            StringEntity entity = new StringEntity(JSON.toJSONString(map),"UTF-8");
//            post.setEntity(entity);
//            HttpResponse res = httpClient.execute(post);
//            log.info("send message request:{},response:{}",post, res);
//            setNoticed(providerTrade.getId());
//        } catch (Exception e) {
//            log.info("消息内容：{}", noticeMsg);
//            log.error("开放平台充值失败，调用消息通知售后发生异常", e);
//        } finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                log.error("httpClient关闭错误", e);
//            }
//        }
//    }
//
//    private boolean hasNoticed(String provideId) {
//        return redissonClient.getMap("NOTICE:CREATE-FDDS-ORDER:" + provideId.substring(0, 7)).containsKey(provideId);
//    }
//    private void setNoticed(String provideId) {
//        RMap<Object, Object> map = redissonClient.getMap("NOTICE:CREATE-FDDS-ORDER:" + provideId.substring(0, 7));
//        boolean isNew = !map.isExists();
//        map.addAndGet(provideId, 1);
//        if (isNew) {
//            map.expire(180, TimeUnit.DAYS);
//        }
//    }
//
//    private TradeItem getUniqueItem(ProviderTrade providerTrade) {
//        if (CollectionUtils.isEmpty(providerTrade.getTradeItems())) {
//            throw new SbcRuntimeException(CommonErrorCode.FAILED, "樊登读书直冲商品为空");
//        }
//        if (providerTrade.getTradeItems().size() > 1) {
//            throw new SbcRuntimeException(CommonErrorCode.FAILED, "樊登读书直冲商品仅支持单一充值");
//        }
//        return providerTrade.getTradeItems().get(0);
//    }

}
