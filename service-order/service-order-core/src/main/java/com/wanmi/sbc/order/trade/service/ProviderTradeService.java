package com.wanmi.sbc.order.trade.service;

import com.alibaba.fastjson.JSON;
import com.sbc.wanmi.erp.bean.enums.ERPTradePushStatus;
import com.sbc.wanmi.erp.bean.vo.DeliveryInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.response.store.ListNoDeleteStoreByIdsResponse;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.HistoryDeliveryInfoRequest;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.order.api.request.trade.ChangeTradeProviderRequest;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import com.wanmi.sbc.order.api.request.trade.TradeUpdateRequest;
import com.wanmi.sbc.order.bean.enums.*;
import com.wanmi.sbc.order.bean.vo.PurchaseMarketingCalcVO;
import com.wanmi.sbc.order.common.OperationLogMq;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPoints;
import com.wanmi.sbc.order.returnorder.repository.ReturnOrderRepository;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.entity.value.TradeCycleBuyInfo;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.ProviderTradeRepository;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import com.wanmi.sbc.order.trade.request.ProviderTradeQueryRequest;
import com.wanmi.sbc.order.trade.request.TradeDeliverRequest;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wanmi.sbc.order.bean.enums.DeliverStatus.SHIPPED;

/**
 * @Description: 供应商订单处理服务层
 * @Autho qiaokang
 * @Date：2020-02-11 22:56
 */
@Service
@Slf4j
public class ProviderTradeService {

    @Autowired
    private ProviderTradeRepository providerTradeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private ReturnOrderRepository returnOrderRepository;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private OperationLogMq operationLogMq;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradePushERPService tradePushERPService;

    @Autowired
    private GuanyierpProvider guanyierpProvider;


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    /**
     * 更新券码信息lock
     */
    private final String BATCH_UPDATE_DELIVERY_STATUS_LOCKS = "autoSyncDeliveryStatus";



    /**
     * 重置扫描次数
     */
    private final String BATCH_UPDATE_ERP_SCAN_COUNT = "autoResetErpScanCount";

    /**
     * 查询未发货订单
     */
    private final String FIND_NOT_YET_SHIPPED_TRADE = "autoFindNotYetShippedTrade";

    /**
     * 查询历史未发货订单(7天以前)
     */
    private final String HISTORY_NOT_YET_SHIPPED_ORDER = "autoHistoryNotYetShippedOrder";

    private final String BATCH_PUSH_ORDER_LOCKS = "autoBatchPushOrder";

    private final String BATCH_PUSH_NORMAL_ORDER_LOCKS = "autoBatchNormalPushOrder";

    /**
     * 重置推送次数
     */
    private final String BATCH_UPDATE_ERP_PUSH_COUNT = "autoResetErpPushCount";

    private final String ORDER_DELIVER_SYNC_SCAN_COUNT = "ORDER_DELIVER_SYNC_SCAN_COUNT";

    @Value("${default.providerId}")
    private Long defaultProviderId;

    @Value("${fdds.provider.id}")
    private Long fddsProviderId;


    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Value("${default.companyId:1182}")
    private Long defaultCompanyId;

    @Autowired
    private RedisService redisService;

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param trade
     */
    public void addProviderTrade(ProviderTrade trade) {
        providerTradeRepository.save(trade);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param trade
     */
    public void updateProviderTrade(ProviderTrade trade) {
        providerTradeRepository.save(trade);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param tradeList
     */
    public void updateProviderTradeList(List<ProviderTrade> tradeList) {
        providerTradeRepository.saveAll(tradeList);
    }

    /**
     * 删除文档
     *
     * @param tid
     */
    public void deleteProviderTrade(String tid) {
        providerTradeRepository.deleteById(tid);
    }

    /**
     * 根据父订单号查询供货商订单
     *
     * @param parentTid
     */
    public List<ProviderTrade> findListByParentId(String parentTid) {
        return providerTradeRepository.findListByParentId(parentTid);
    }

    /**
     * 根据父订单列表查询供货商订单
     */
    public List<ProviderTrade> findListByParentIdList(List<String> parentTidList) {
        return providerTradeRepository.findByParentIdIn(parentTidList);
    }

    /**
     *
     */
    public ProviderTrade findbyId(String id) {
        return providerTradeRepository.findFirstById(id);
    }

//    /**
//     * 统计数量
//     *
//     * @param whereCriteria
//     * @param request
//     * @return
//     */
//    public long countNum(Criteria whereCriteria, TradeQueryRequest request) {
//        request.putSort(request.getSortColumn(), request.getSortRole());
//        Query query = new Query(whereCriteria);
//        long totalSize = mongoTemplate.count(query, ProviderTrade.class);
//        return totalSize;
//    }

    /**
     * 查询订单
     *
     * @param tid
     */
    public ProviderTrade providerDetail(String tid) {
        return providerTradeRepository.findById(tid).orElse(null);
    }

    /**
     * 更新订单
     *
     * @param tradeUpdateRequest
     */
    @GlobalTransactional
    @Transactional
    public void updateProviderTrade(TradeUpdateRequest tradeUpdateRequest) {
        this.updateProviderTrade(KsBeanUtil.convert(tradeUpdateRequest.getTrade(), ProviderTrade.class));
    }

    /**
     * 订单分页
     *
     * @param whereCriteria 条件
     * @param request       参数
     * @return
     */
    public Page<ProviderTrade> providerPage(Criteria whereCriteria, ProviderTradeQueryRequest request) {
        long totalSize = this.countNum(whereCriteria, request);
        if (totalSize < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        return new PageImpl<>(mongoTemplate.find(query.with(request.getPageRequest()), ProviderTrade.class), request
                .getPageable(), totalSize);
    }

    /**
     * 统计数量
     *
     * @param whereCriteria
     * @param request
     * @return
     */
    public long countNum(Criteria whereCriteria, ProviderTradeQueryRequest request) {
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        long totalSize = mongoTemplate.count(query, ProviderTrade.class);
        return totalSize;
    }

    /**
     * 取消供应商订单
     *
     * @param parentId 父订单id
     * @param operator 操作人
     * @param isAuto   是否定时取消
     */
    @Transactional
    @GlobalTransactional
    public void providerCancel(String parentId, Operator operator, boolean isAuto) {
        List<ProviderTrade> providerTradeList = this.findListByParentId(parentId);

        if (CollectionUtils.isNotEmpty(providerTradeList)) {
            String msg = "用户取消订单";
            if (isAuto) {
                msg = "订单超时未支付，系统自动取消";
            }
            final String data = msg;

            providerTradeList.forEach(providerTrade -> {
                // 更新供应商订单状态为已作废
                providerTrade.getTradeState().setFlowState(FlowState.VOID);
                providerTrade.getTradeState().setEndTime(LocalDateTime.now());
                providerTrade.appendTradeEventLog(new TradeEventLog(operator, "取消订单", data, LocalDateTime.now()));
                this.updateProviderTrade(providerTrade);
            });
        }
    }

    /**
     * 审核供应商订单
     *
     * @param parentId
     * @param reason
     * @param auditState
     */
    public void providerAudit(String parentId, String reason, AuditState auditState) {
        List<ProviderTrade> providerTradeList = this.findListByParentId(parentId);

        if (CollectionUtils.isNotEmpty(providerTradeList)) {
            providerTradeList.forEach(providerTrade -> {
                // 更新供应商订单状态为已作废
                providerTrade.getTradeState().setAuditState(auditState);
                if (AuditState.REJECTED == auditState) {
                    providerTrade.getTradeState().setObsoleteReason(reason);
                } else {
                    providerTrade.getTradeState().setFlowState(FlowState.AUDIT);
                }
                this.updateProviderTrade(providerTrade);
            });
        }

    }

    /**
     * 发货校验,检查请求发货商品数量是否符合应发货数量
     *
     * @param tid                 订单id
     * @param tradeDeliverRequest 发货请求参数结构
     */
    public void deliveryCheck(String tid, TradeDeliverRequest tradeDeliverRequest) {
        ProviderTrade providerTrade = providerDetail(tid);

        //周期购订单 重新设置商品数量 购买的数量*期数
        if (providerTrade.getCycleBuyFlag()) {
            providerTrade.getTradeItems().forEach(tradeItem -> {
                tradeItem.setNum(tradeItem.getCycleNum() * tradeItem.getNum());
            });
        }

        Map<String, TradeItem> skusMap =
                providerTrade.getTradeItems().stream().collect(Collectors.toMap(TradeItem::getSkuId,
                        Function.identity()));
        Map<String, TradeItem> giftsMap =
                providerTrade.getGifts().stream().collect(Collectors.toMap(TradeItem::getSkuId,
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
     * @return
     */
    public String deliver(String tid, TradeDeliver tradeDeliver, Operator operator) {
        ProviderTrade providerTrade = providerDetail(tid);
        //是否开启订单审核
        if (auditQueryProvider.isSupplierOrderAudit().getContext().isAudit() && providerTrade.getTradeState().getAuditState()
                != AuditState.CHECKED) {
            //只有已审核订单才能发货
            throw new SbcRuntimeException("K-050317");
        }
        // 先款后货并且未支付的情况下禁止发货
        if (providerTrade.getPaymentOrder() == PaymentOrder.PAY_FIRST && providerTrade.getTradeState().getPayState() == PayState.NOT_PAID && providerTrade.getPayInfo().getPayTypeId().equals(0)) {
            throw new SbcRuntimeException("K-050318");
        }
        if (verifyAfterProcessing(providerTrade.getParentId())) {
            throw new SbcRuntimeException("K-050114", new Object[]{providerTrade.getParentId()});
        }

        checkLogisticsNo(tradeDeliver.getLogistics().getLogisticNo(), tradeDeliver.getLogistics()
                .getLogisticStandardCode());

        // 生成ID
        tradeDeliver.setDeliverId(generatorService.generate("TD"));
        tradeDeliver.setStatus(SHIPPED);
        tradeDeliver.setProviderName(providerTrade.getSupplier().getSupplierName());
        tradeDeliver.setTradeId(tid);

        //周期购订单 重新设置商品购买数量 购买的数量*期数
        if (providerTrade.getCycleBuyFlag()) {
            providerTrade.getTradeItems().forEach(tradeItem -> {
                tradeItem.setNum(tradeItem.getCycleNum() * tradeItem.getNum());
            });
        }


        List<TradeDeliver> tradeDelivers = providerTrade.getTradeDelivers();
        tradeDelivers.add(0, tradeDeliver);
        providerTrade.setTradeDelivers(tradeDelivers);

        providerTrade.getTradeItems().forEach(tradeItem -> {
            // 当前商品本次发货信息
            List<ShippingItem> shippingItems = tradeDeliver.getShippingItems().stream()
                    .filter(shippingItem -> tradeItem.getSkuId().equals(shippingItem.getSkuId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(shippingItems)) {
                // 当前商品发货数量加上本次发货数量
                tradeItem.setDeliveredNum(shippingItems.get(0).getItemNum() + tradeItem.getDeliveredNum());
                // 判断当前商品是否已全部发货
                if (tradeItem.getNum().equals(tradeItem.getDeliveredNum())) {
                    tradeItem.setDeliverStatus(SHIPPED);
                } else {
                    tradeItem.setDeliverStatus(DeliverStatus.PART_SHIPPED);
                }
            }
        });

        //赠品
        providerTrade.getGifts().forEach(gift -> {
            // 当前赠品本次发货信息
            List<ShippingItem> shippingItems = tradeDeliver.getGiftItemList().stream()
                    .filter(shippingItem -> gift.getSkuId().equals(shippingItem.getSkuId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(shippingItems)) {
                // 当前赠品发货数量加上本次发货数量
                gift.setDeliveredNum(shippingItems.get(0).getItemNum() + gift.getDeliveredNum());
                // 判断赠品商品是否已全部发货
                if (gift.getNum().equals(gift.getDeliveredNum())) {
                    gift.setDeliverStatus(SHIPPED);
                } else {
                    gift.setDeliverStatus(DeliverStatus.PART_SHIPPED);
                }
            }
        });

        // 判断本次发货后，是否还有部分发货或未发货的商品，来设置订单发货状态
        Long partShippedNum = providerTrade.getTradeItems().stream()
                .filter(tradeItem -> (tradeItem.getDeliverStatus().equals(DeliverStatus.PART_SHIPPED) ||
                        tradeItem.getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED))).count();
        //赠品
        Long giftsNum = providerTrade.getGifts().stream().filter(gift -> DeliverStatus.NOT_YET_SHIPPED.equals(gift.getDeliverStatus()) ||
                DeliverStatus.PART_SHIPPED.equals(gift.getDeliverStatus())).count();
        partShippedNum += giftsNum;

        //添加操作日志
        String detail = String.format("订单[%s]已%s,操作人：%s", providerTrade.getId(), "发货", operator.getName());
        if (partShippedNum.intValue() != 0) {
            providerTrade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
            providerTrade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
            detail = String.format("订单[%s]已%s,操作人：%s", providerTrade.getId(), "部分发货", operator.getName());
        } else {
            providerTrade.getTradeState().setFlowState(FlowState.DELIVERED);
            providerTrade.getTradeState().setDeliverStatus(SHIPPED);
        }

        //周期购订单 还原设置商品数量 购买的数量/期数
        if (providerTrade.getCycleBuyFlag()) {
            providerTrade.getTradeItems().forEach(tradeItem -> {
                tradeItem.setNum(tradeItem.getNum() / tradeItem.getCycleNum());
            });

            TradeCycleBuyInfo tradeCycleBuyInfo = providerTrade.getTradeCycleBuyInfo();
            List<DeliverCalendar> deliverCalendars = tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendar -> deliverCalendar.getCycleDeliverStatus() == CycleDeliverStatus.NOT_SHIPPED).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deliverCalendars)) {
                //更新发货日历状态
                deliverCalendars.get(0).setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
            }
            //设置第几期
            int count = providerTrade.getTradeDelivers().size();
            tradeDeliver.setCycleNum(count);
        }

        providerTrade.appendTradeEventLog(TradeEventLog
                .builder()
                .operator(operator)
                .eventType(partShippedNum.intValue() != 0 ? FlowState.DELIVERED_PART.getDescription() : TradeEvent.DELIVER.getDescription())
                .eventTime(LocalDateTime.now())
                .eventDetail(detail)
                .build());

        // 更新发货信息
        this.updateProviderTrade(providerTrade);

        tradeDeliver.setSunDeliverId(tradeDeliver.getDeliverId());
        tradeDeliver.setShipperType((operator.getPlatform() == Platform.SUPPLIER || operator.getPlatform() == Platform.WX_VIDEO) ? ShipperType.PROVIDER : ShipperType.PROVIDER);
        tradeService.deliver(providerTrade.getParentId(), tradeDeliver, operator, BoolFlag.NO);

        return tradeDeliver.getDeliverId();
    }

    /**
     * 子单批量发货处理
     */
    public String dealBatchDeliver(ProviderTrade providerTrade, TradeDeliver tradeDeliver, Operator operator) {

//        if (verifyAfterProcessing(providerTrade.getParentId())) {
//            throw new SbcRuntimeException("K-050114", new Object[]{providerTrade.getId()});
//        }

        //子单发货清单tradeDeliver部分信息重置
        List<TradeItem> tradeItems = providerTrade.getTradeItems();
        List<ShippingItem> shippingItems = tradeItems.stream().map(item -> {
            //默认全部发货
            item.setDeliveredNum(item.getNum());
            item.setDeliverStatus(SHIPPED);
            ShippingItem shippingItem = KsBeanUtil.copyPropertiesThird(item, ShippingItem.class);
            shippingItem.setItemName(item.getSkuName());
            shippingItem.setItemNum(item.getNum());
            return shippingItem;
        }).collect(Collectors.toList());
        List<ShippingItem> giftItems = providerTrade.getGifts().stream().map(item -> {
            item.setDeliveredNum(item.getNum());
            item.setDeliverStatus(SHIPPED);
            ShippingItem shippingItem = KsBeanUtil.copyPropertiesThird(item, ShippingItem.class);
            shippingItem.setItemName(item.getSkuName());
            shippingItem.setItemNum(item.getNum());
            return shippingItem;
        }).collect(Collectors.toList());
        tradeDeliver.setShippingItems(shippingItems);
        tradeDeliver.setGiftItemList(giftItems);
        tradeDeliver.setTradeId(providerTrade.getId());
        providerTrade.addTradeDeliver(tradeDeliver);

        providerTrade.getTradeItems().forEach(tradeItem -> {
            tradeItem.setDeliveredNum(tradeItem.getNum());
            tradeItem.setDeliverStatus(SHIPPED);
        });
        //赠品
        providerTrade.getGifts().forEach(gift -> {
            gift.setDeliveredNum(gift.getNum());
            gift.setDeliverStatus(SHIPPED);
        });

        providerTrade.getTradeState().setFlowState(FlowState.DELIVERED);
        providerTrade.getTradeState().setDeliverStatus(SHIPPED);
        //添加操作日志
        String detail = String.format("订单[%s]已%s,操作人：%s", providerTrade.getId(), "全部发货", operator.getName());
        providerTrade.appendTradeEventLog(TradeEventLog
                .builder()
                .operator(operator)
                .eventType(FlowState.DELIVERED.getDescription())
                .eventTime(LocalDateTime.now())
                .eventDetail(detail)
                .build());

        // 更新发货信息
        this.updateProviderTrade(providerTrade);
        return tradeDeliver.getDeliverId();
    }

    /**
     * 验证订单是否存在售后申请
     *
     * @param tid
     * @return true|false:存在售后，阻塞订单进程|不存在售后，订单进程正常
     */
    public boolean verifyAfterProcessing(String tid) {
        List<ReturnOrder> returnOrders = returnOrderRepository.findByTid(tid);
        if (!CollectionUtils.isEmpty(returnOrders)) {
            // 查询是否存在正在进行中的退单(不是作废,不是拒绝退款,不是已结束)
            Optional<ReturnOrder> optional = returnOrders.stream().filter(item -> item.getReturnFlowState() !=
                    ReturnFlowState.VOID
                    && item.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                    && item.getReturnFlowState() != ReturnFlowState.COMPLETED).findFirst();
            if (optional.isPresent()) {
                return true;
            }

        }
        return false;
    }

    /**
     * 物流单号重复校验
     *
     * @param logisticsNo
     * @param logisticStandardCode
     */
    private void checkLogisticsNo(String logisticsNo, String logisticStandardCode) {
        if (providerTradeRepository
                .findTopByTradeDelivers_Logistics_LogisticNoAndTradeDelivers_Logistics_logisticStandardCode(logisticsNo,
                        logisticStandardCode)
                .isPresent()) {
            throw new SbcRuntimeException("K-050124");
        }
    }

    /**
     * 查询导出数据
     *
     * @param queryRequest
     */
    public List<ProviderTrade> listProviderTradeExport(ProviderTradeQueryRequest queryRequest) {
        long count = this.countNum(queryRequest.getWhereCriteria(), queryRequest);
        if (count > 1000) {
            count = 1000;
        }
        queryRequest.putSort(queryRequest.getSortColumn(), queryRequest.getSortRole());
        queryRequest.setPageNum(0);
        queryRequest.setPageSize((int) count);

        //设置返回字段
        Map fieldsObject = new HashMap();
        fieldsObject.put("_id", true);
        fieldsObject.put("parentId", true);
        fieldsObject.put("tradeState.createTime", true);
        fieldsObject.put("supplierName", true);
        fieldsObject.put("supplierCode", true);
        fieldsObject.put("consignee.name", true);
        fieldsObject.put("consignee.phone", true);
        fieldsObject.put("consignee.detailAddress", true);
        fieldsObject.put("deliverWay", true);
        fieldsObject.put("tradePrice.goodsPrice", true);


//        fieldsObject.put("tradePrice.special", true);
//        fieldsObject.put("tradePrice.privilegePrice", true);
        fieldsObject.put("tradePrice.totalPrice", true);
//        fieldsObject.put("tradeItems.oid", true);
        fieldsObject.put("tradeItems.skuId", true);
        fieldsObject.put("tradeItems.skuNo", true);
        fieldsObject.put("tradeItems.specDetails", true);
        fieldsObject.put("tradeItems.skuName", true);
        fieldsObject.put("tradeItems.num", true);
        fieldsObject.put("tradeItems.cateId", true);
        fieldsObject.put("tradeItems.supplyPrice", true);
        fieldsObject.put("tradeItems.totalSupplyPrice", true);

        fieldsObject.put("buyerRemark", true);
        fieldsObject.put("sellerRemark", true);
        fieldsObject.put("tradeState.flowState", true);
        fieldsObject.put("tradeState.payState", true);
        fieldsObject.put("tradeState.deliverStatus", true);
//        fieldsObject.put("invoice.type", true);
//        fieldsObject.put("invoice.projectName", true);
//        fieldsObject.put("invoice.generalInvoice.title", true);
//        fieldsObject.put("invoice.specialInvoice.companyName", true);
//        fieldsObject.put("supplier.supplierName", true);
        Query query = new BasicQuery(new Document(), new Document(fieldsObject));
        query.addCriteria(queryRequest.getWhereCriteria());
        System.err.println("mongo：  " + LocalDateTime.now());
        List<ProviderTrade> tradeList = mongoTemplate.find(query.with(queryRequest.getPageRequest()), ProviderTrade.class);

        System.err.println("mongo：  " + LocalDateTime.now());
        return tradeList;
    }


    /**
     * 查询订单集合
     *
     * @param tids
     */
    public List<ProviderTrade> details(List<String> tids) {
        return org.apache.commons.collections4.IteratorUtils.toList(providerTradeRepository.findAllById(tids).iterator());
    }

    /**
     * 查询全部订单
     *
     * @param request
     * @return
     */
    public List<ProviderTrade> queryAll(ProviderTradeQueryRequest request) {
        return mongoTemplate.find(new Query(request.getWhereCriteria()), ProviderTrade.class);
    }


    /**
     * 修改备注
     *
     * @param tid
     * @param buyerRemark
     */
    @Transactional
    public void remedyBuyerRemark(String tid, String buyerRemark, Operator operator) {
        //1、查找订单信息
        ProviderTrade providerTrade = providerDetail(tid);
        providerTrade.setBuyerRemark(buyerRemark);
        Trade trade = tradeService.detail(providerTrade.getParentId());
        providerTrade.appendTradeEventLog(new TradeEventLog(operator, "修改备注", "修改供应商订单备注", LocalDateTime.now()));
        trade.appendTradeEventLog(new TradeEventLog(operator, "修改备注", "修改供应商订单备注", LocalDateTime.now()));
        //保存
        providerTradeRepository.save(providerTrade);
        tradeService.updateTrade(trade);
        this.operationLogMq.convertAndSend(operator, "修改供应商订单备注", "修改供应商订单备注");
    }

    public String providerByidAndPid(String tid, String providerId) {
        String providerTradeId = StringUtils.EMPTY;
        List<ProviderTrade> providerTrades = this.findListByParentId(tid);
        for (ProviderTrade providerTrade : providerTrades) {
            if (Long.parseLong(providerId) == providerTrade.getSupplier().getStoreId()) {
                providerTradeId = providerTrade.getId();
            }
        }
        return providerTradeId;
    }

    /**
     * 根据主订单id，供应商(或商家)id获取子订单
     *
     * @param tid
     * @param providerId
     * @return
     */
    public ProviderTrade getProviderTradeByIdAndPid(String tid, Long providerId) {
        List<ProviderTrade> providerTrades = this.findListByParentId(tid);
        Optional<ProviderTrade> optional = providerTrades.stream()
                .filter(trade -> providerId.longValue() == trade.getSupplier().getStoreId().longValue())
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 发货记录作废
     *
     * @param tid
     * @param deliverId
     * @param operator
     */
    @Transactional
    public void deliverRecordObsolete(String tid, String deliverId, Operator operator) {

        ProviderTrade providerTrade = providerTradeRepository.findFirstById(tid);
        List<TradeDeliver> tradeDelivers = providerTrade.getTradeDelivers();
        //查询发货记录
        Optional<TradeDeliver> tradeDeliverOptional = tradeDelivers
                .stream()
                .filter(tradeDeliver -> StringUtils.equals(deliverId, tradeDeliver.getDeliverId()))
                .findFirst();

        if (tradeDeliverOptional.isPresent()) {
            StringBuilder stringBuilder = new StringBuilder(200);

            TradeDeliver tradeDeliver = tradeDeliverOptional.get();

            //处理商品
            handleShippingItems(providerTrade, tradeDeliver.getShippingItems(), stringBuilder, false);

            //订单状态更新
            TradeState tradeState = providerTrade.getTradeState();
            if (isAllNotShipped(providerTrade)) {
                tradeState.setFlowState(FlowState.AUDIT);
                tradeState.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
            } else {
                tradeState.setFlowState(FlowState.DELIVERED_PART);
                tradeState.setDeliverStatus(DeliverStatus.PART_SHIPPED);
            }

            //添加操作日志
            stringBuilder.trimToSize();
            providerTrade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(TradeEvent.OBSOLETE_DELIVER.getDescription())
                    .eventDetail(stringBuilder.toString())
                    .eventTime(LocalDateTime.now())
                    .build());

            //删除发货单
            tradeDelivers.remove(tradeDeliver);

            //保存
            providerTradeRepository.save(providerTrade);
            operationLogMq.convertAndSend(operator, TradeEvent.OBSOLETE_DELIVER.getDescription(), stringBuilder.toString());

        }
    }

    @Transactional
    public void handleShippingItems(ProviderTrade trade, List<ShippingItem> shippingItems, StringBuilder stringBuilder, boolean isGift) {
        ConcurrentHashMap<String, TradeItem> skuItemMap;
        if (isGift) {
            skuItemMap = trade.giftSkuItemMap();
        } else {
            skuItemMap = trade.skuItemMap();
        }

        //订单商品更新
        shippingItems.forEach(shippingItem -> {
            TradeItem tradeItem = skuItemMap.get(shippingItem.getSkuId());

            Long shippedNum = tradeItem.getDeliveredNum();
            shippedNum -= shippingItem.getItemNum();
            tradeItem.setDeliveredNum(shippedNum);

            if (shippedNum.equals(0L)) {
                tradeItem.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
            } else if (shippedNum < tradeItem.getNum()) {
                tradeItem.setDeliverStatus(DeliverStatus.PART_SHIPPED);
            }

            stringBuilder.append(String.format("订单[%s],商品[%s], 作废发货[%s], 目前状态:[%s]\r\n",
                    trade.getId(),
                    (isGift ? "【赠品】" : "") + tradeItem.getSkuName(),
                    shippingItem.getItemNum().toString(),
                    tradeItem.getDeliverStatus().getDescription())
            );

        });
    }

    /**
     * 是否全部未发货
     *
     * @param trade
     * @return
     */
    @Transactional
    public boolean isAllNotShipped(ProviderTrade trade) {
        List<TradeItem> allItems = new ArrayList<>();
        allItems.addAll(trade.getTradeItems());
        allItems.addAll(trade.getGifts());
        List<TradeItem> collect = allItems.stream()
                .filter(tradeItem -> !tradeItem.getDeliveredNum().equals(0L))
                .collect(Collectors.toList());
        return collect.isEmpty();
    }


    public List<ProviderTrade> findTradeListForSettlement(Long storeId, Date startTime, Date endTime, PageRequest pageRequest) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("supplier.storeId").is(storeId)
                , new Criteria().orOperator(
                        Criteria.where("tradeState.flowState").is(FlowState.COMPLETED),
                        Criteria.where("tradeState.flowState").is(FlowState.VOID),
                        Criteria.where("refundFlag").is(true))
                , Criteria.where("tradeState.deliverStatus").in(Arrays.asList(SHIPPED,
                        DeliverStatus.PART_SHIPPED))
                , Criteria.where("returnOrderNum").is(0)
                , Criteria.where("tradeState.finalTime").lt(endTime).gte(startTime)
        );

        return mongoTemplate.find(
                new Query(criteria).skip(pageRequest.getPageNumber() * pageRequest.getPageSize()).limit(pageRequest
                        .getPageSize())
                , ProviderTrade.class);
    }

    /**
     * 更新订单的待确认标识
     *
     * @param providerTradeId 供应商订单号
     * @param payState        付款标识
     */
    @Transactional
    public void updateThirdPlatformPayState(String providerTradeId, PayState payState) {
        mongoTemplate.updateMulti(new Query(Criteria.where("id").is(providerTradeId)), new Update().set("tradeState.payState", payState), ProviderTrade.class);
    }

    /**
     * 更新订单的错误标识
     *
     * @param providerTradeId 供应商订单号
     * @param errorFlag       错误标识
     */
    @Transactional
    public void updateThirdPlatformPayFlag(String providerTradeId, Boolean errorFlag) {
        mongoTemplate.updateMulti(new Query(Criteria.where("id").is(providerTradeId)), new Update().set("thirdPlatformPayErrorFlag", errorFlag), ProviderTrade.class);
    }

    /**
     * 更新正在进行的供应商订单数量
     *
     * @param returnOrderId 退单id
     * @param addFlag       退单数加减状态
     */
    @Transactional
    public void updateReturnOrderNumByRid(String returnOrderId, boolean addFlag) {
        ReturnOrder order = returnOrderRepository.findById(returnOrderId).orElse(null);
        if (Objects.isNull(order)) {
            log.error("退单ID:{},查询不到退单信息", returnOrderId);
            return;
        }

        if (StringUtils.isNotBlank(order.getPtid())) {
            ProviderTrade trade = this.findbyId(order.getPtid());
            if (Objects.nonNull(trade)) {
                // 1.根据addFlag加减正在进行的退单
                Integer num = trade.getReturnOrderNum() == null ? 0 : trade.getReturnOrderNum();
                mongoTemplate.updateFirst(new Query(Criteria.where("id").is(trade.getId())), new Update()
                        .set("returnOrderNum", addFlag ? ++num : --num), ProviderTrade.class);
            }
        }
    }

    /**
     * 根据条件查询父订单id
     *
     * @param queryRequest
     * @return
     */
    public List<String> findParentIdByCondition(ProviderTradeQueryRequest queryRequest) {
        //设置返回字段
        Map fieldsObject = new HashMap();
        fieldsObject.put("parentId", true);
        Query query = new BasicQuery(new Document(), new Document(fieldsObject));
        query.addCriteria(queryRequest.getWhereCriteria());
        List<ProviderTrade> providerTrades = mongoTemplate.find(query, ProviderTrade.class);
        List<String> parentIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(providerTrades)) {
            List<String> ids = providerTrades.stream().map(ProviderTrade::getParentId).collect(Collectors.toList());
            parentIds.addAll(ids);
        }
        return parentIds;
    }

    /**
     * 0元订单推送ERP系统
     *
     * @param tradeNo
     */
    public void defalutPayOrderAsycToERP(String tradeNo) {
        //根据父订单号,查询子订单集合
        List<ProviderTrade> providerTradeList = this.findListByParentId(tradeNo);
        Trade trade = tradeRepository.findById(tradeNo).get();
        if (CollectionUtils.isNotEmpty(providerTradeList)) {
            providerTradeList.stream().forEach(providerTrade -> {
                providerTrade.setPayWay(trade.getPayWay());
                if (!providerTrade.getGrouponFlag() ||
                        GrouponOrderStatus.COMPLETE.equals(trade.getTradeGroupon().getGrouponOrderStatus())) {
                    this.singlePushOrder(providerTrade);
                }
            });
        }
    }

    /**
     * 普通支付订单推送到ERP系统
     *
     * @param providerTrade
     * @return
     */
    public boolean singlePushOrder(ProviderTrade providerTrade) {
        //获取主订单对于的供应商订单
        if (!ObjectUtils.isEmpty(providerTrade)) {
            if (ERPTradePushStatus.PUSHED_SUCCESS.getStateId().equals(providerTrade.getTradeState().getErpTradeState())) {
                log.info("订单{}重复推送", providerTrade.getId());
                return false;
            }
            //推送订单
            BaseResponse baseResponse = tradePushERPService.pushOrderToERP(providerTrade);
            if (baseResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 推送的周期购订单到ERP系统
     */
    public void batchPushCycleOrder(int pageSize) {
        RLock lock = redissonClient.getLock(BATCH_PUSH_ORDER_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return;
        }
        lock.lock();
        try {

            if (pageSize <= 0) {
                pageSize = 500;
            }

            ProviderTradeQueryRequest tradeQueryRequest = new ProviderTradeQueryRequest();
            tradeQueryRequest.setCycleBuyFlag(true);
            //已支付的订单
            TradeState tradeState = new TradeState();
            tradeState.setPayState(PayState.PAID);
            tradeQueryRequest.setTradeState(tradeState);
            //待处理就显示待发货和部分发货
            tradeQueryRequest.setFlowStates(Arrays.asList(FlowState.AUDIT, FlowState.DELIVERED_PART));
            tradeQueryRequest.setReturnHasFlag(false);//没有退单
            tradeQueryRequest.setDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);//周期购待配送
            tradeQueryRequest.setYzOrderFlag(Boolean.FALSE);


            tradeQueryRequest.putSort(tradeQueryRequest.getSortColumn(), tradeQueryRequest.getSortRole());
            tradeQueryRequest.setPageNum(0);
            tradeQueryRequest.setPageSize(pageSize);
            Query query = new Query(tradeQueryRequest.getWhereCriteria());

            List<ProviderTrade> providerTrades = mongoTemplate.find(query.with(tradeQueryRequest.getPageRequest()), ProviderTrade.class);

            for (ProviderTrade providerTrade : providerTrades) {
                log.info("================订单推送周期购订单=====:{}", providerTrade);
                TradeCycleBuyInfo tradeCycleBuyInfo = providerTrade.getTradeCycleBuyInfo();

                List<DeliverCalendar> deliverCalendars = tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendar -> deliverCalendar.getCycleDeliverStatus()!=CycleDeliverStatus.POSTPONE).collect(Collectors.toList());

                List<DeliverCalendar> deliverCalendarList = KsBeanUtil.convert(deliverCalendars,DeliverCalendar.class);

                deliverCalendarList.forEach(deliverCalendar -> {
                    LocalDate startDate = LocalDate.now();
                    LocalDate endDate = deliverCalendar.getDeliverDate();
                    if (deliverCalendar.getCycleDeliverStatus() == CycleDeliverStatus.NOT_SHIPPED
                            && startDate.until(endDate, ChronoUnit.DAYS) <= 1 && deliverCalendar.getPushCount() < 3) {
                        int index = deliverCalendarList.indexOf(deliverCalendar);
                        boolean isFirstCycle = Boolean.FALSE;
                        if (index == 0) {
                            isFirstCycle = Boolean.TRUE;
                        }
                        //推送订单
                        tradePushERPService.pushCycleOrderToERP(providerTrade, deliverCalendar, index + 1, isFirstCycle);

                        log.info("================订单推送周期购订单=====:{}", providerTrade);

                    }
                });
            }
        } catch (Exception e) {
            log.error("#订单推送失败:{}", e);
        } finally {
            //释放锁
            lock.unlock();
        }
    }


    /**
     * 补偿推送普通订单
     */
    public void batchPushOrder(int pageSize, String ptid) {
        RLock lock = redissonClient.getLock(BATCH_PUSH_NORMAL_ORDER_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return;
        }
        lock.lock();
        try {
            if (pageSize <= 0) {
                pageSize = 200;
            }


            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()));

            criterias.add(Criteria.where("tradeState.pushCount").lte(3));
            criterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
            criterias.add(Criteria.where("cycleBuyFlag").is(false));
            criterias.add(Criteria.where("yzTid").exists(false));
            criterias.add(Criteria.where("grouponFlag").is(false));


            //补偿推送已成团的订单
            List<Criteria> grouponCriterias = new ArrayList<>();
            grouponCriterias.add(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()));
            grouponCriterias.add(Criteria.where("tradeState.pushCount").lte(3));
            grouponCriterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
            grouponCriterias.add(Criteria.where("cycleBuyFlag").is(false));
            grouponCriterias.add(Criteria.where("yzTid").exists(false));
            grouponCriterias.add(Criteria.where("grouponFlag").is(true));
            grouponCriterias.add(Criteria.where("tradeGroupon.grouponOrderStatus").is(GrouponOrderStatus.COMPLETE));


            //单个订单推送
            if (StringUtils.isNoneBlank(ptid)) {
                criterias.add(Criteria.where("id").is(ptid));
                grouponCriterias.add(Criteria.where("id").is(ptid));
            } else {
                //默认获取半年内的数据
                LocalDateTime localDateTime = LocalDateTime.now().plusMonths(-6);
                criterias.add(Criteria.where("supplier.storeId").ne(fddsProviderId));  //直冲引起的过滤条件
                criterias.add(Criteria.where("tradeState.createTime").gte(localDateTime));
                criterias.add(Criteria.where("tradeState.erpTradeState").ne(ERPTradePushStatus.PUSHED_SUCCESS.getStateId()));

                grouponCriterias.add(Criteria.where("supplier.storeId").ne(fddsProviderId));  //直冲引起的过滤条件
                grouponCriterias.add(Criteria.where("tradeState.createTime").gte(localDateTime));
                grouponCriterias.add(Criteria.where("tradeState.erpTradeState").ne(ERPTradePushStatus.PUSHED_SUCCESS.getStateId()));

            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("补偿推送周期购订单获取StopWatch");

            Criteria grouponCriteria = new Criteria().andOperator(grouponCriterias.toArray(new Criteria[grouponCriterias.size()]));
            Query grouponQuery = new Query(grouponCriteria).limit(pageSize);
            grouponQuery.with(Sort.by(Sort.Direction.ASC, "tradeState.payTime"));
            List<ProviderTrade> grouponQueryProviderTrades = mongoTemplate.find(grouponQuery, ProviderTrade.class);

            log.info("ProviderTradeService.batchPushOrder grouponCriterias query:{} ", grouponQuery);
            stopWatch.stop();



            stopWatch.start("补偿推送普通订单获取StopWatch");
            Criteria newCriteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
            Query query = new Query(newCriteria).limit(pageSize);
            query.with(Sort.by(Sort.Direction.ASC, "tradeState.payTime"));
            List<ProviderTrade> providerTrades = mongoTemplate.find(query, ProviderTrade.class);
            log.info("ProviderTradeService.batchPushOrder criterias query:{}", query);
            stopWatch.stop();


            List<ProviderTrade> totalProviderTradeList = Stream.of(providerTrades, grouponQueryProviderTrades).flatMap(Collection::stream).distinct().collect(Collectors.toList());

            stopWatch.start("补偿推送订单 StopWatch");
            for (ProviderTrade providerTrade : totalProviderTradeList) {
                log.info("================普通订单补偿推送erp=====:{}", providerTrade);
                // 推送订单
                tradePushERPService.pushOrderToERP(providerTrade);
            }
            stopWatch.stop();

//            log.info("ProviderTradeService.batchPushOrder StopWatch statistics {}", stopWatch.prettyPrint());
            for (StopWatch.TaskInfo taskInfo : stopWatch.getTaskInfo()) {
                log.info("ProviderTradeService.batchPushOrder StopWatch {} cost:{} 秒", taskInfo.getTaskName(), taskInfo.getTimeSeconds());
            }
        } catch (Exception e) {
            log.error("#订单推送失败", e);
        } finally {
            //释放锁
            lock.unlock();
        }
    }


    /**
     * 批量同步发货状态
     *
     * @param pageSize
     */
    public void batchSyncDeliveryStatus(int pageSize, String ptid) {
        RLock lock = redissonClient.getLock(BATCH_UPDATE_DELIVERY_STATUS_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return;
        }
        lock.lock();
        try {
            /**
             * 普通订单发货状态更新
             */
            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()));
            criterias.add(Criteria.where("tradeState.erpTradeState").is(ERPTradePushStatus.PUSHED_SUCCESS.getStateId()));
            criterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
            criterias.add(Criteria.where("tradeState.deliverStatus").ne(DeliverStatus.SHIPPED.getStatusId()));
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
            List<ProviderTrade> providerTrades = mongoTemplate.find(query, ProviderTrade.class);

            /**
             * 周期购订单发货单状态更新
             */
            Criteria cycleBuycriteria = new Criteria();
            cycleBuycriteria.andOperator(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()),
                    Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()),
                    Criteria.where("tradeState.deliverStatus").ne(DeliverStatus.SHIPPED.getStatusId()),
                    Criteria.where("cycleBuyFlag").is(true));

            Query cycleQuery = new Query(cycleBuycriteria).limit(pageSize);
            query.with(Sort.by(Sort.Direction.ASC, "tradeState.payTime"));
            List<ProviderTrade> cycleBuyTradeList = mongoTemplate.find(cycleQuery, ProviderTrade.class);

            List<ProviderTrade> totalTradeList =
                    Stream.of(providerTrades, cycleBuyTradeList).flatMap(Collection::stream).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(totalTradeList)) {
                log.info("#批量同步发货状态的订单:{}", totalTradeList);
                totalTradeList.stream().forEach(providerTrade -> {
                    tradePushERPService.syncDeliveryStatus(providerTrade,null);
                    log.info("#批量同步发货状态的订单:{},订单id:{}", providerTrade.getTradeState(),providerTrade.getId());

                });
            }
        } catch (Exception e) {
            log.error("#批量同步发货状态异常:{}", e);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    /**
     * 重置扫描次数
     *
     * @param ptid
     */
    public void batchResetScanCount(String ptid) {
        RLock lock = redissonClient.getLock(BATCH_UPDATE_ERP_SCAN_COUNT);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return;
        }
        lock.lock();
        try {
            /**
             * 查询所有扫描次数为3的数据
             */
            Query query = this.queryProviderTradeCondition(ptid, OrderTypeEnums.RESET_ORDER_THREE.toValue(), null);
            List<ProviderTrade> providerTrades = mongoTemplate.find(query, ProviderTrade.class);

            if (CollectionUtils.isNotEmpty(providerTrades)) {
                providerTrades.forEach(providerTrade -> {
                    log.info("erpResetStatusSyncCountJobHandler ProviderTradeService tid: {} pid:{}", providerTrade.getParentId(), providerTrade.getId());
                    providerTrade.getTradeState().setScanCount(ScanCount.COUNT_ZERO.toValue());
                });
                this.updateProviderTradeList(providerTrades);
            }
        } catch (Exception e) {
            log.error("Error message ： #批量重置扫描次数为三的数据:{}", e.getMessage(), e);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    /**
     * 扫描未发货订单，并加上扫面次数
     */
    public void scanNotYetShippedTrade(int pageSize, String ptid) {
        RLock lock = redissonClient.getLock(FIND_NOT_YET_SHIPPED_TRADE);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return;
        }
        lock.lock();
        try {
            int scanCount = 0;
            String scanCountStr = redisService.getString(ORDER_DELIVER_SYNC_SCAN_COUNT);
            if (StringUtils.isNotBlank(scanCountStr)) {
                scanCount = Integer.parseInt(scanCountStr);
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("同步普通订单发货状态获取 StopWatch");
            /**
             * 普通订单发货状态更新
             */
            // 查询scanCount小于3或scanCount不存在的数据
            Query query = this.queryProviderTradeCondition(ptid, OrderTypeEnums.REGULAR_ORDER_ZERO.toValue(), scanCount);
            log.info("ProviderTradeService scanNotYetShippedTrade normal query:{}", query);
            List<ProviderTrade> providerTrades = mongoTemplate.find(query.limit(pageSize), ProviderTrade.class);
            stopWatch.stop();

            /**
             * 周期购订单发货单状态更新
             */
            stopWatch.start("同步周期购订单发货状态获取 StopWatch");
            // 查询zhouqigou scanCount小于3或scanCount不存在的数据
            Query cycleQuery = this.queryProviderTradeCondition(ptid, OrderTypeEnums.CYCLE_ORDER_TWO.toValue(), scanCount);
            log.info("ProviderTradeService scanNotYetShippedTrade cycle query:{}", query);
            List<ProviderTrade> cycleBuyTradeList = mongoTemplate.find(cycleQuery.limit(pageSize), ProviderTrade.class);
            stopWatch.stop();

            List<ProviderTrade> totalTradeList = Stream.of(providerTrades, cycleBuyTradeList)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
            List<DeliveryInfoVO> deliveryInfoVOList = new ArrayList<>();

            stopWatch.start("同步订单发货状态/添加canCount StopWatch");
            if (CollectionUtils.isNotEmpty(totalTradeList)) {
                log.info("ProviderTradeService scanNotYetShippedTrade totalTradeList is {} scanCount is {}", totalTradeList.size(), scanCount);
                totalTradeList.stream().forEach(providerTrade -> {

                    log.info("ProviderTradeService scanNotYetShippedTrade  同步erp发货状态的订单:{},订单id:{}", providerTrade.getTradeState(),providerTrade.getId());
                    tradePushERPService.syncDeliveryStatus(providerTrade, deliveryInfoVOList);
                });
            } else {
                log.info("ProviderTradeService scanNotYetShippedTrade totalTradeList is {} scanCount is {} scanCount++", totalTradeList.size(), scanCount);
                scanCount = scanCount +1;
                if (scanCount > ScanCount.COUNT_THREE.toValue()) {
                    scanCount = 0;
                }
                redisService.setString(ORDER_DELIVER_SYNC_SCAN_COUNT, scanCount + "", 24 * 60 * 60);
            }
            stopWatch.stop();
//            log.info("ProviderTradeService scanNotYetShippedTrade StopWatch statistics {}", stopWatch.prettyPrint());
            for (StopWatch.TaskInfo taskInfo : stopWatch.getTaskInfo()) {
                log.info("ProviderTradeService scanNotYetShippedTrade StopWatch {} cost:{} 秒", taskInfo.getTaskName(), taskInfo.getTimeSeconds());
            }
        } catch (Exception e) {
            log.error("Error message ： #批量同步发货状态异常:{}",e.getMessage(), e);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    /**
     * @description 同步历史发货单状态(创建时间大于当前时间7天的订单)
     * @param startTime 发货开始时间
     * @param endTime 发货结束时间
     */
    public void batchSyncHistoryOrderStatus(String startTime,String endTime,int pageSize,int pageNum) {
        RLock lock = redissonClient.getLock(HISTORY_NOT_YET_SHIPPED_ORDER);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return;
        }
        lock.lock();
        try {
            HistoryDeliveryInfoRequest historyDeliveryInfoRequest = HistoryDeliveryInfoRequest.builder()
                    .startDeliveryDate(startTime)
                    .pageSize(pageSize)
                    .endDeliveryDate(endTime)
                    .pageNum(pageNum)
                    .build();

            List<DeliveryInfoVO> deliveryInfoVOList = guanyierpProvider
                    .getHistoryDeliveryStatus(historyDeliveryInfoRequest).getContext().getDeliveryInfoVOList();

            if (CollectionUtils.isNotEmpty(deliveryInfoVOList)){
                deliveryInfoVOList.forEach(deliveryInfoVO -> {
                    // 获取所有子订单的商品信息
                    List<DeliveryInfoVO> deliveryInfoVOS = deliveryInfoVOList
                            .stream()
                            .filter(deliveryInfoVO1 -> deliveryInfoVO1.getPlatformCode().equals(deliveryInfoVO.getPlatformCode()))
                            .collect(Collectors.toList());
                    // 查询数据库中信息
                    ProviderTrade providerTrade = mongoTemplate.findById(deliveryInfoVO.getPlatformCode(), ProviderTrade.class);
                    if (!ObjectUtils.isEmpty(providerTrade) &&
                            DeliverStatus.SHIPPED != providerTrade.getTradeState().getDeliverStatus() &&
                            FlowState.VOID != providerTrade.getTradeState().getFlowState()){
                        log.info("#同步erp发货状态的订单:{},订单id:{},erp返回订单信息:{}", providerTrade.getTradeState(),providerTrade.getId(),deliveryInfoVOS);
                        tradePushERPService.syncDeliveryStatus(providerTrade,deliveryInfoVOS);
                    }
                });
            }
        } catch (Exception e) {
            log.error("Error message ： #同步历史发货单状态:{}",e.getMessage(), e);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    /**
     * 查询订单的前置条件
     *
     * @param ptid 订单子id
     * @return
     */
    public Query queryProviderTradeCondition(String ptid, Integer orderType, Integer scanCount) {
        List<Criteria> criterias = new ArrayList<>();
        // 查询条件组装
        criterias.add(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()));
        criterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
        criterias.add(Criteria.where("tradeState.deliverStatus").ne(DeliverStatus.SHIPPED.getStatusId()));
        criterias.add(Criteria.where("tradeState.erpTradeState").is(ERPTradePushStatus.PUSHED_SUCCESS.getStateId()));

        //单个订单发货状态同步(重置扫描次数,ptid为空)
        if (StringUtils.isNoneBlank(ptid)) {

            criterias.add(Criteria.where("id").is(ptid));
            return new Query(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));
        } else {
            // 重置订单扫描次数
            if (OrderTypeEnums.RESET_ORDER_THREE.toValue() == orderType) {

                criterias.add(Criteria.where("tradeState.scanCount").is(ScanCount.COUNT_THREE.toValue()));
            } else {

                Criteria orCriteria = new Criteria();
                if (scanCount != null) {
                    orCriteria.orOperator(
                            Criteria.where("tradeState.scanCount").exists(false),
                            Criteria.where("tradeState.scanCount").is(scanCount));
                } else {
                    orCriteria.orOperator(
                            Criteria.where("tradeState.scanCount").exists(false),
                            Criteria.where("tradeState.scanCount").lt(ScanCount.COUNT_THREE.toValue()));
                }
                criterias.add(orCriteria);
            }
        }



        // 周期购订单
        if (OrderTypeEnums.CYCLE_ORDER_TWO.toValue() == orderType){

            criterias.add(Criteria.where("cycleBuyFlag").is(true));
        }

        // 普通订单
        if (OrderTypeEnums.REGULAR_ORDER_ZERO.toValue() == orderType){

            criterias.add(Criteria.where("cycleBuyFlag").is(false));
        }

        Criteria newCriteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
        return new Query(newCriteria);
    }



    public String changeTradeProvider(ChangeTradeProviderRequest request) {
        ProviderTrade providerTrade = providerTradeRepository.findFirstById(request.getPid());
        if (providerTrade == null) {
            log.warn("changeTradeProvider发货单不存在，request:{}", request);
            throw new SbcRuntimeException("K-000001");
        }
        if(providerTrade.getTradeState().getFlowState().equals(FlowState.VOID)){
            log.warn("changeTradeProvider已作废，请不要重复操作，request:{}", request);
            throw new SbcRuntimeException("K-000001");
        }
        List<String> oldSkuIds = providerTrade.getTradeItems().stream().map(TradeItem::getSkuNo).collect(Collectors.toList());
        List<String> changeSkuIds = new ArrayList<>(request.getSkuNos().keySet());
        if (!oldSkuIds.containsAll(changeSkuIds)) {
            log.warn("changeTradeProvider商品信息不匹配，request:{}", request);
            throw new SbcRuntimeException("K-000001");
        }
        Optional<Trade> trade = tradeRepository.findById(providerTrade.getParentId());
        if (!trade.isPresent()) {
            log.warn("changeTradeProvider主单信息不存在，request:{}", request);
            throw new SbcRuntimeException("K-000001");
        }
        ProviderTrade newProviderTrade = KsBeanUtil.convert(providerTrade, ProviderTrade.class);
        List<String> newSkuNos = new ArrayList<>(request.getSkuNos().values());
        //查询sku信息
        GoodsInfoListByConditionRequest goodsInfoRequest = GoodsInfoListByConditionRequest.builder()
                .goodsInfoNos(newSkuNos)
                .build();
        BaseResponse<GoodsInfoListByConditionResponse> goodsInfoResponse = goodsInfoQueryProvider.listByCondition(goodsInfoRequest);
        if (goodsInfoResponse == null || goodsInfoResponse.getContext() == null || CollectionUtils.isEmpty(goodsInfoResponse.getContext().getGoodsInfos())) {
            log.warn("changeTradeProvider替换的商品信息为空，request:{}", request);
            throw new SbcRuntimeException("K-000001");
        }

        List<GoodsInfoVO> goodsInfos = goodsInfoResponse.getContext().getGoodsInfos();
        //更新商品信息和供应商信息
        BaseResponse<ListNoDeleteStoreByIdsResponse> storesResposne =
                storeQueryProvider.listNoDeleteStoreByIds(ListNoDeleteStoreByIdsRequest.builder().storeIds(Arrays.asList(defaultProviderId)).build());
        StoreVO provider = storesResposne.getContext().getStoreVOList().get(0);
        List<TradeItem> newTradeItems = newProviderTrade.getTradeItems().stream().filter(p->changeSkuIds.contains(p.getSkuNo())).collect(Collectors.toList());
        newTradeItems.forEach(item -> {
            Optional<GoodsInfoVO> goodsInfoVO = goodsInfos.stream().filter(p -> p.getGoodsInfoNo().equals(request.getSkuNos().get(item.getSkuNo()))).findFirst();
            if (goodsInfoVO.isPresent()) {
                item.setOid(generatorService.generateOid());
                item.setSkuNo(goodsInfoVO.get().getGoodsInfoNo());
                item.setSkuId(goodsInfoVO.get().getGoodsInfoId());
                item.setErpSkuNo(goodsInfoVO.get().getErpGoodsInfoNo());
                item.setErpSpuNo(goodsInfoVO.get().getErpGoodsNo());
                item.setSpuId(goodsInfoVO.get().getGoodsId());
                item.setProviderId(defaultProviderId);
                // 供应商名称
                item.setProviderName(provider.getSupplierName());
                // 供应商编号
                item.setProviderCode(provider.getCompanyInfo().getCompanyCode());
            }
        });
        newProviderTrade.setTradeItems(newTradeItems);
        List<String> oids = providerTrade.getTradeItems().stream().filter(p->changeSkuIds.contains(p.getSkuNo())).map(TradeItem::getOid).collect(Collectors.toList());
        newProviderTrade.getTradeState().setErpTradeState(DeliverStatus.NOT_YET_SHIPPED.toString());
        // 供应商信息
        newProviderTrade.getSupplier().setStoreId(provider.getStoreId());
        newProviderTrade.getSupplier().setSupplierName(provider.getSupplierName());
        newProviderTrade.getSupplier().setSupplierId(provider.getCompanyInfo().getCompanyInfoId());
        newProviderTrade.getSupplier().setSupplierCode(provider.getCompanyInfo().getCompanyCode());
        //若没有剩余商品，作废原订单
        List<TradeItem> leftTradeItems = providerTrade.getTradeItems().stream().filter(p->!changeSkuIds.contains(p.getSkuNo())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(leftTradeItems)) {
            providerTrade.getTradeState().setFlowState(FlowState.VOID);
            providerTrade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .eventType(FlowState.VOID.getDescription())
                    .eventDetail(String.format("发货单更新为管易云发货，作废原订单，更新的商品:%s",JSON.toJSONString(request)))
                    .eventTime(LocalDateTime.now())
                    .build());
        }else{
            providerTrade.setTradeItems(leftTradeItems);
            providerTrade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .eventType(FlowState.VOID.getDescription())
                    .eventDetail(String.format("发货单更新为管易云发货，更新的商品:%s",JSON.toJSONString(request)))
                    .eventTime(LocalDateTime.now())
                    .build());
        }
        providerTradeRepository.save(providerTrade);
        newProviderTrade.setId(generatorService.generateProviderTid());
        newProviderTrade.getTradeState().setPushCount(0);
        newProviderTrade.getTradeState().setFlowState(FlowState.AUDIT);
        newProviderTrade.getTradeState().setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
        //价格
        initPrice(newProviderTrade);
        providerTradeRepository.save(newProviderTrade);
        //更新trade的item信息
        List<TradeItem> tradeItems = new ArrayList<>(trade.get().getTradeItems().size());
        tradeItems.addAll(trade.get().getTradeItems().stream().filter(p->!oids.contains(p.getOid())).collect(Collectors.toList()));
        tradeItems.addAll(newProviderTrade.getTradeItems());
        trade.get().setTradeItems(tradeItems);
        tradeRepository.save(trade.get());
        return newProviderTrade.getId();
    }

    private void initPrice(ProviderTrade providerTrade){

        // 拆单后，重新计算价格信息
        TradePrice tradePrice = providerTrade.getTradePrice();
        // 商品总价
        BigDecimal goodsPrice = BigDecimal.ZERO;
        // 订单总价:实付金额
        BigDecimal orderPrice = BigDecimal.ZERO;
        // 订单供货价总额
        BigDecimal orderSupplyPrice = BigDecimal.ZERO;
        //积分价
        Long buyPoints = NumberUtils.LONG_ZERO;
        for (TradeItem providerTradeItem : providerTrade.getTradeItems()) {
            //积分
            if (Objects.nonNull(providerTradeItem.getBuyPoint())) {
                buyPoints += providerTradeItem.getBuyPoint();
            }
            // 商品总价
            goodsPrice =
                    goodsPrice.add(providerTradeItem.getPrice().multiply(new BigDecimal(providerTradeItem.getNum())));
            // 商品分摊价格
            BigDecimal splitPrice = Objects.isNull(providerTradeItem.getSplitPrice()) ? BigDecimal.ZERO :
                    providerTradeItem.getSplitPrice();
            orderPrice = orderPrice.add(splitPrice);
            // 订单供货价总额
            orderSupplyPrice = orderSupplyPrice.add(providerTradeItem.getTotalSupplyPrice());

        }

        // 商品总价
        tradePrice.setGoodsPrice(goodsPrice);
        tradePrice.setOriginPrice(goodsPrice);
        // 订单总价
        tradePrice.setTotalPrice(orderPrice);
        tradePrice.setTotalPayCash(orderPrice);
        // 订单供货价总额
        tradePrice.setOrderSupplyPrice(orderSupplyPrice);
        //积分价
        tradePrice.setBuyPoints(buyPoints);
        tradePrice.setDeliveryPrice(BigDecimal.ZERO);
        //实际金额
        if(providerTrade.getTradeItems().stream().anyMatch(p->p.getSplitPrice()!=null)){
            tradePrice.setActualPrice(providerTrade.getTradeItems().stream().map(p -> Objects.isNull(p.getSplitPrice()) ? new BigDecimal("0") : p.getSplitPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        Long points = 0L;
        if(providerTrade.getTradeItems().stream().anyMatch(p->p.getPoints()!=null)){
            points=providerTrade.getTradeItems().stream().filter(p->p.getPoints()!=null).mapToLong(TradeItem::getPoints).sum();
        }
        //计算积分的总和
        BigDecimal bigDecimal=new BigDecimal(points);
        //计算积分金额的总和
        tradePrice.setActualPoints(bigDecimal.divide(new BigDecimal(100)));
        if(providerTrade.getTradeItems().stream().anyMatch(p->p.getKnowledge()!=null)){
            tradePrice.setActualKnowledge(providerTrade.getTradeItems().stream().mapToLong(p->Objects.isNull(p.getKnowledge()) ? 0L : p.getKnowledge()).sum());
        }
        providerTrade.setTradePrice(tradePrice);
    }

    /**
     * 重置推送次数
     *
     * @param request
     */
    public void batchResetPushCount(ProviderTradeErpRequest request) {
        RLock lock = redissonClient.getLock(BATCH_UPDATE_ERP_PUSH_COUNT);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return;
        }
        lock.lock();
        try {
            /**
             * 查询所有推送次数为4的数据
             */
            Query query = this.queryProviderTradePushCountCondition(request);
            List<ProviderTrade> providerTrades = mongoTemplate.find(query, ProviderTrade.class);

            if (CollectionUtils.isNotEmpty(providerTrades)) {
                providerTrades.forEach(providerTrade -> {
                    providerTrade.getTradeState().setPushCount(ScanCount.COUNT_ZERO.toValue());
                });
                this.updateProviderTradeList(providerTrades);
            }
        } catch (Exception e) {
            log.error("Error message ： #批量重置推送次数失败:{}", e.getMessage(), e);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    public Query queryProviderTradePushCountCondition(ProviderTradeErpRequest request) {
        List<Criteria> criterias = new ArrayList<>();
        // 查询条件组装
        criterias.add(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()));
        criterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
        criterias.add(Criteria.where("tradeState.deliverStatus").ne(DeliverStatus.SHIPPED.getStatusId()));
        criterias.add(Criteria.where("tradeState.erpTradeState").ne(ERPTradePushStatus.PUSHED_SUCCESS.getStateId()));
        criterias.add(Criteria.where("tradeState.pushCount").gt(3));
        if (StringUtils.isNoneBlank(request.getPtid())) {
            criterias.add(Criteria.where("id").is(request.getPtid()));
            return new Query(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));
        }
        if (request.getPageSize() <= 0) {
            request.setPageSize(200);
        }
        Criteria newCriteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
        return new Query(newCriteria).limit(request.getPageSize());
    }



}
