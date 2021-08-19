package com.wanmi.sbc.order.pointstrade.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.common.OperationLogMq;
import com.wanmi.sbc.order.mq.OrderProducerService;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeFSMService;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
import com.wanmi.sbc.order.pointstrade.fsm.params.PointsTradeStateRequest;
import com.wanmi.sbc.order.pointstrade.request.PointsTradeQueryRequest;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import com.wanmi.sbc.order.trade.request.TradeDeliverRequest;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.response.OrderAutoReceiveConfigGetResponse;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName PointsTradeService
 * @Description 积分订单业务逻辑层
 * @Author lvzhenwei
 * @Date 2019/5/10 10:03
 **/
@Service
@Slf4j
public class PointsTradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private PointsTradeService pointsTradeService;

    @Autowired
    private OperationLogMq operationLogMq;

    @Autowired
    private PointsTradeFSMService pointsTradeFSMService;

    /**
     * 注入消费记录生产者service
     */
    @Autowired
    public OrderProducerService orderProducerService;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    /**
     * @return com.wanmi.sbc.order.pointstrade.model.root.PointsTrade
     * @Author lvzhenwei
     * @Description 根基订单id获取积分订单详情
     * @Date 14:44 2019/5/10
     * @Param [id]
     **/
    public Trade getById(String id) {
        return tradeRepository.findById(id).orElse(null);
    }

    /**
     * 根据查询条件分页查询订单信息
     *
     * @param whereCriteria 条件
     * @param request       参数
     * @return
     */
    public Page<Trade> page(Criteria whereCriteria, PointsTradeQueryRequest request) {
        long totalSize = this.countNum(whereCriteria, request);
        if (totalSize < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        return new PageImpl<>(mongoTemplate.find(query.with(request.getPageRequest()), Trade.class), request
                .getPageable(), totalSize);
    }

    /**
     * 发货校验,检查请求发货商品数量是否符合应发货数量
     *
     * @param tid                 订单id
     * @param tradeDeliverRequest 发货请求参数结构
     */
    public void deliveryCheck(String tid, TradeDeliverRequest tradeDeliverRequest) {
        Trade trade = detail(tid);
        Map<String, TradeItem> skusMap = trade.getTradeItems().stream().collect(Collectors.toMap(TradeItem::getSkuId,
                Function.identity()));
        Map<String, TradeItem> giftsMap = trade.getGifts().stream().collect(Collectors.toMap(TradeItem::getSkuId,
                Function.identity()));
        tradeDeliverRequest.getShippingItemList().forEach(i -> {
            TradeItem tradeItem = skusMap.get(i.getSkuId());
            if (tradeItem.getDeliveredNum() + i.getItemNum() > tradeItem.getNum()) {
                throw new SbcRuntimeException("K-050315");
            }
        });
        tradeDeliverRequest.getGiftItemList().forEach(i -> {
            TradeItem tradeItem = giftsMap.get(i.getSkuId());
            if (tradeItem.getDeliveredNum() + i.getItemNum() > tradeItem.getNum()) {
                throw new SbcRuntimeException("K-050315");
            }
        });
    }

    /**
     * 发货
     *
     * @param tid
     * @param tradeDeliver
     * @param operator
     */
    @GlobalTransactional
    @Transactional
    public String deliver(String tid, TradeDeliver tradeDeliver, Operator operator) {
        Trade pointsTrade = pointsTradeService.detail(tid);
        checkLogisticsNo(tradeDeliver.getLogistics().getLogisticNo(), tradeDeliver.getLogistics()
                .getLogisticStandardCode());
        // 生成ID
        tradeDeliver.setDeliverId(generatorService.generate("TD"));
        tradeDeliver.setStatus(DeliverStatus.NOT_YET_SHIPPED);
        tradeDeliver.setTradeId(tid);
        tradeDeliver.setProviderName(pointsTrade.getSupplier().getSupplierName());
        PointsTradeStateRequest stateRequest = PointsTradeStateRequest
                .builder()
                .tid(tid)
                .operator(operator)
                .data(tradeDeliver)
                .event(PointsTradeEvent.DELIVER)
                .build();
        //积分订单发货状态机操作
        pointsTradeFSMService.changeState(stateRequest);
        return tradeDeliver.getDeliverId();
    }

    /**
     * 检验物流单号是否已经存在
     *
     * @param logisticsNo
     * @param logisticStandardCode
     */
    private void checkLogisticsNo(String logisticsNo, String logisticStandardCode) {
        if (tradeRepository
                .findTopByTradeDelivers_Logistics_LogisticNoAndTradeDelivers_Logistics_logisticStandardCode(logisticsNo,
                        logisticStandardCode)
                .isPresent()) {
            throw new SbcRuntimeException("K-050124");
        }

    }

    /**
     * 积分订单确认收货
     *
     * @param tid
     * @param operator
     */
    @GlobalTransactional
    @Transactional
    public void confirmReceive(String tid, Operator operator) {
        Trade trade = detail(tid);
        PointsTradeEvent event;
        if (trade.getTradeState().getPayState() == PayState.PAID) {
            event = PointsTradeEvent.COMPLETE;
        } else {
            event = PointsTradeEvent.CONFIRM;
        }
        // 发送订单完成MQ消息
        //orderProducerService.sendMQForPointsOrderComplete(tid);
        PointsTradeStateRequest stateRequest = PointsTradeStateRequest
                .builder()
                .data(trade)
                .tid(tid)
                .operator(operator)
                .event(event)
                .build();
        //积分订单确认收货状态机流转
        pointsTradeFSMService.changeState(stateRequest);
    }

    /**
     * 统计数量
     *
     * @param whereCriteria
     * @param request
     * @return
     */
    public long countNum(Criteria whereCriteria, PointsTradeQueryRequest request) {
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        long totalSize = mongoTemplate.count(query, Trade.class);
        return totalSize;
    }

    /**
     * @return java.util.List<com.wanmi.sbc.order.pointstrade.model.root.PointsTrade>
     * @Author lvzhenwei
     * @Description 查询积分订单导出数据
     * @Date 15:29 2019/5/10
     * @Param [pointsTradeQueryRequest]
     **/
    public List<Trade> listPointsTradeExport(PointsTradeQueryRequest pointsTradeQueryRequest) {
        long count = this.countNum(pointsTradeQueryRequest.getWhereCriteria(), pointsTradeQueryRequest);
        if (count > 1000) {
            count = 1000;
        }
        pointsTradeQueryRequest.putSort(pointsTradeQueryRequest.getSortColumn(), pointsTradeQueryRequest.getSortRole());
        pointsTradeQueryRequest.setPageNum(0);
        pointsTradeQueryRequest.setPageSize((int) count);

        //设置返回字段
        Map fieldsObject = new HashMap();
        fieldsObject.put("_id", true);
        fieldsObject.put("tradeState.createTime", true);
        fieldsObject.put("buyer.name", true);
        fieldsObject.put("buyer.account", true);
        fieldsObject.put("buyer.levelName", true);
        fieldsObject.put("buyer.phone", true);
        fieldsObject.put("consignee.name", true);
        fieldsObject.put("consignee.phone", true);
        fieldsObject.put("consignee.detailAddress", true);
        fieldsObject.put("payInfo.desc", true);
        fieldsObject.put("deliverWay", true);
        fieldsObject.put("tradePrice.deliveryPrice", true);
        fieldsObject.put("tradePrice.goodsPrice", true);
        fieldsObject.put("tradePrice.special", true);
        fieldsObject.put("tradePrice.privilegePrice", true);
        fieldsObject.put("tradePrice.points", true);
        fieldsObject.put("tradeItems.oid", true);
        fieldsObject.put("tradeItems.skuId", true);
        fieldsObject.put("tradeItems.skuNo", true);
        fieldsObject.put("tradeItems.num", true);
        fieldsObject.put("buyerRemark", true);
        fieldsObject.put("sellerRemark", true);
        fieldsObject.put("tradeState.flowState", true);
        fieldsObject.put("tradeState.payState", true);
        fieldsObject.put("tradeState.deliverStatus", true);
        fieldsObject.put("invoice.type", true);
        fieldsObject.put("invoice.projectName", true);
        fieldsObject.put("invoice.generalInvoice.title", true);
        fieldsObject.put("invoice.specialInvoice.companyName", true);
        fieldsObject.put("supplier.supplierName", true);
        fieldsObject.put("pointsOrderType", true);
//        fieldsObject.put("buyer", false);
        Query query = new BasicQuery(new Document(), new Document(fieldsObject));
        query.addCriteria(pointsTradeQueryRequest.getWhereCriteria());
        System.err.println("mongo：  " + LocalDateTime.now());
        List<Trade> pointsTradeList = mongoTemplate.find(query.with(pointsTradeQueryRequest.getPageRequest()), Trade.class);

        System.err.println("mongo：  " + LocalDateTime.now());
        return pointsTradeList;
    }



    /**
     * 修改卖家备注
     *
     * @param tid
     * @param sellerRemark
     */
    //@TccTransaction
@Transactional
    public void remedySellerRemark(String tid, String sellerRemark, Operator operator) {
        //1、查找订单信息
        Trade trade = detail(tid);
        trade.setSellerRemark(sellerRemark);
        trade.appendTradeEventLog(new TradeEventLog(operator, "修改备注", "修改卖家备注", LocalDateTime.now()));
        //保存
        pointsTradeService.updateTrade(trade);
        this.operationLogMq.convertAndSend(operator, "修改备注", "修改卖家备注");
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param trade
     */
    public void updateTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    /**
     * 查询订单
     *
     * @param tid
     */
    public Trade detail(String tid) {
        Trade trade = tradeRepository.findById(tid).orElse(null);
        return trade;
    }

    /**
     * 积分订单自动收货
     */
    @GlobalTransactional
    @Transactional
    public void pointsOrderAutoReceive() {
        //查询符合订单
        //批量扭转状态
        OrderAutoReceiveConfigGetResponse config = auditQueryProvider.getOrderAutoReceiveConfig().getContext();
        val pageSize = 1000;
        try {
            Integer day = Integer.valueOf(JSON.parseObject(config.getContext()).get("day").toString());
            LocalDateTime endDate = LocalDateTime.now().minusDays(day);
            long total = this.countTradeByDate(endDate, FlowState.DELIVERED);
            log.info("积分订单自动确认收货分页订单数: " + total);
            int pageNum = 0;
            boolean loopFlag = true;
            //超过1000条批量处理
            while (loopFlag && total > 0) {
                List<Trade> tradeList = this.queryTradeByDate(endDate, FlowState.DELIVERED, pageNum, pageSize);
                if (tradeList != null && !tradeList.isEmpty()) {
                    tradeList.forEach(trade -> this.confirmReceive(trade.getId(), Operator.builder().platform(Platform.PLATFORM)
                            .name("system").account("system").platform(Platform.PLATFORM).build()));
                    if (tradeList.size() == pageSize) {
                        pageNum++;
                        continue;
                    }
                }
                loopFlag = false;
            }
            log.info("自动确认收货成功");
        } catch (Exception ex) {
            log.error("orderAutoReceive schedule error");
            throw new SbcRuntimeException("K-050129");
        }
    }

    /**
     * 根据流程状态时间查询总条数
     *
     * @param endDate
     * @param flowState
     * @return
     */
    public long countTradeByDate(LocalDateTime endDate, FlowState flowState) {
        Criteria criteria = new Criteria();

        criteria.andOperator(Criteria.where("tradeState.flowState").is(flowState.toValue())
                , Criteria.where("tradeState.deliverTime").lt(endDate)
                , Criteria.where("orderType").is(OrderType.POINTS_ORDER.getOrderTypeId())
        );
        return mongoTemplate.count(new Query(criteria), Trade.class);
    }

    /**
     * 根据流程状态时间查询积分订单
     *
     * @param endDate   endDate
     * @param flowState flowState
     * @return List<Trade>
     */
    public List<Trade> queryTradeByDate(LocalDateTime endDate, FlowState flowState, int PageNum, int pageSize) {
        Criteria criteria = new Criteria();

        criteria.andOperator(Criteria.where("tradeState.flowState").is(flowState.toValue())
                , Criteria.where("tradeState.deliverTime").lt(endDate)
                , Criteria.where("orderType").is(OrderType.POINTS_ORDER.getOrderTypeId())
        );

        return mongoTemplate.find(
                new Query(criteria).skip(PageNum * pageSize).limit(pageSize)
                , Trade.class);
    }

}
