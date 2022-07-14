package com.soybean.mall.order.gift.service;


import com.soybean.mall.order.api.request.mq.RecordMessageMq;
import com.soybean.mall.order.api.request.record.OrderGiftRecordSearchReq;
import com.soybean.mall.order.api.response.mq.SimpleTradeResp;
import com.soybean.mall.order.gift.model.OrderGiftRecord;
import com.soybean.mall.order.gift.model.OrderGiftRecordLog;
import com.soybean.mall.order.gift.repository.PayOrderGiftRecordLogRepository;
import com.soybean.mall.order.gift.repository.PayOrderGiftRecordRepository;
import com.soybean.marketing.api.enums.ActivityCategoryEnum;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.req.SpuNormalActivityReq;
import com.soybean.marketing.api.resp.SkuNormalActivityResp;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.order.api.enums.RecordStateEnum;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 8:22 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
public abstract class PayOrderGiftRecordService {


    @Autowired
    protected TradeService tradeService;

    @Autowired
    private NormalActivityPointSkuProvider normalActivityPointSkuProvider;

    @Autowired
    private PayOrderGiftRecordLogRepository payOrderGiftRecordLogRepository;

    @Autowired
    protected PayOrderGiftRecordRepository payOrderGiftRecordRepository;

    @Autowired
    protected ExternalProvider externalProvider;

    @Autowired
    protected CustomerQueryProvider customerQueryProvider;

    /**
     * 过滤订单信息
     * @param recordMessageMq
     * @return
     */
    protected SimpleTradeResp filterSimpleTrade(RecordMessageMq recordMessageMq) {

        if (StringUtils.isBlank(recordMessageMq.getOrderId())) {
            return null;
        }
        if (CollectionUtils.isEmpty(recordMessageMq.getChannelTypes())) {
            return null;
        }

        //获取订单信息
        Trade orderDetail = tradeService.detail(recordMessageMq.getOrderId());
        if (orderDetail == null) {
            log.error("PayOrderGiftRecordService filterSimpleTrade orderId:{} 不存在", recordMessageMq.getOrderId());
            return null;
        }

        //获取sku信息
        List<String> skuIds = new ArrayList<>();
        for (TradeItem tradeItem : orderDetail.getTradeItems()) {
            skuIds.add(tradeItem.getSkuId());
        }

        if (CollectionUtils.isEmpty(skuIds)) {
            return null;
        }

        SimpleTradeResp simpleTradeResp = new SimpleTradeResp();
        simpleTradeResp.setOrderId(orderDetail.getId());
        simpleTradeResp.setCustomerId(orderDetail.getBuyer().getId());
        simpleTradeResp.setSkuIds(skuIds);
        return simpleTradeResp;
    }


    /**
     *
     * 获取商品所在运行中的活动
     * @param skuIds
     * @return
     */
    protected List<SkuNormalActivityResp> listRunningNormalActivity(List<String> skuIds, List<Integer> channelTypes) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return new ArrayList<>();
        }
        SpuNormalActivityReq spuNormalActivityReq = new SpuNormalActivityReq();
        spuNormalActivityReq.setSkuIds(skuIds);
        spuNormalActivityReq.setChannelTypes(channelTypes);
        List<SkuNormalActivityResp> context = normalActivityPointSkuProvider.listSpuRunningNormalActivity(spuNormalActivityReq).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return new ArrayList<>();
        }
        return context;
    }


    /**
     * 获取有效的返还记录信息
     * @return
     */
    protected List<OrderGiftRecord> listValidBackRecord(String activityId, String customerId, String quoteId, ActivityCategoryEnum activityCategory) {
        OrderGiftRecordSearchReq req = new OrderGiftRecordSearchReq();
        req.setActivityId(activityId);
        req.setCustomerId(customerId);
        req.setRecordCategory(activityCategory.getCode());
        req.setQuoteId(quoteId);
        req.setRecordStatus(Arrays.asList(RecordStateEnum.LOCK.getCode(), RecordStateEnum.SUCCESS.getCode(), RecordStateEnum.NORMAL_CANCEL.getCode(), RecordStateEnum.FORCE_CANCEL.getCode()));
        return payOrderGiftRecordRepository.findAll(payOrderGiftRecordRepository.packageWhere(req));
    }

    /**
     * 新增日志记录信息
     * @param orderGiftRecord
     */
    private void addGiftRecordLog(OrderGiftRecord orderGiftRecord) {
        //记录日志
        OrderGiftRecordLog orderGiftRecordLog = new OrderGiftRecordLog();
        orderGiftRecordLog.setCustomerId(orderGiftRecord.getCustomerId());
        orderGiftRecordLog.setOrderId(orderGiftRecord.getOrderId());
        orderGiftRecordLog.setActivityId(orderGiftRecord.getActivityId());
        orderGiftRecordLog.setRecordId(orderGiftRecord.getId());
        orderGiftRecordLog.setRecordStatus(orderGiftRecord.getRecordStatus());
        orderGiftRecordLog.setRemark(orderGiftRecord.getName());
        orderGiftRecordLog.setCreateTime(LocalDateTime.now());
        orderGiftRecordLog.setUpdateTime(LocalDateTime.now());
        orderGiftRecordLog.setDelFlag(DeleteFlag.NO);
        payOrderGiftRecordLogRepository.save(orderGiftRecordLog);
    }


    /**
     * 新增记录/更新记录
     * @param orderGiftRecord
     */
    protected OrderGiftRecord saveGiftRecord(OrderGiftRecord orderGiftRecord) {
        orderGiftRecord.setUpdateTime(LocalDateTime.now());
        OrderGiftRecord orderGiftRecordModel = payOrderGiftRecordRepository.save(orderGiftRecord);
        this.addGiftRecordLog(orderGiftRecordModel);
        return orderGiftRecordModel;
    }


    /**
     * 根据客户id获取客户信息
     * @param customerId
     * @return
     */
    protected CustomerGetByIdResponse getCustomer(String customerId){
        return customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
    }

    /**
     * 创建订单成功后调用
     * @param message
     */
    public abstract void afterCreateOrder(String message);

    /**
     * 支付成功后调用
     * @param message
     */
    public abstract void afterPayOrderLock(String message);
}
