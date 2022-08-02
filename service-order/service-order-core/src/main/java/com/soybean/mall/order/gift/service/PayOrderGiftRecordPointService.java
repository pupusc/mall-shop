package com.soybean.mall.order.gift.service;
import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.api.request.mq.RecordMessageMq;
import com.soybean.mall.order.api.response.mq.SimpleTradeResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.time.LocalDateTime;

import com.soybean.mall.order.api.request.record.OrderGiftRecordSearchReq;
import com.soybean.mall.order.gift.model.OrderGiftRecord;
import com.soybean.marketing.api.enums.ActivityCategoryEnum;
import com.soybean.marketing.api.resp.SkuNormalActivityResp;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.enums.FanDengChangeTypeEnum;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengAddPointReq;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.order.api.enums.RecordStateEnum;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Description: 返还积分
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 8:27 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class PayOrderGiftRecordPointService extends PayOrderGiftRecordService {

    /**
     * 分类
     * @return
     */
    @Override
    protected ActivityCategoryEnum getActivityCategory() {
        return ActivityCategoryEnum.ACTIVITY_POINT;
    }

    /**
     * 查看购买的商品是否有已经处理在处理返积分的记录
     * @param
     */
    @Override
    protected List<OrderGiftRecord> listValidBackRecord(String activityId, String customerId, String skuId, ActivityCategoryEnum activityCategoryEnum) {
        OrderGiftRecordSearchReq req = new OrderGiftRecordSearchReq();
        req.setActivityId(activityId);
        req.setCustomerId(customerId);
        req.setRecordCategory(activityCategoryEnum.getCode());
        req.setQuoteId(skuId);
        req.setRecordStatus(Arrays.asList(RecordStateEnum.LOCK.getCode(), RecordStateEnum.SUCCESS.getCode()));
        return payOrderGiftRecordRepository.findAll(payOrderGiftRecordRepository.packageWhere(req));
    }


    /**
     * 获取 OrderGiftRecord 对象信息
     * @return
     */
    @Override
    protected OrderGiftRecord getOrderGiftRecordModel(SkuNormalActivityResp skuNormalActivityParam, SimpleTradeResp simpleTradeResp) {
        OrderGiftRecord orderGiftRecord = new OrderGiftRecord();
        orderGiftRecord.setName("返积分活动");
        orderGiftRecord.setCustomerId(simpleTradeResp.getCustomerId());
        orderGiftRecord.setOrderId(simpleTradeResp.getOrderId());
        orderGiftRecord.setActivityId(skuNormalActivityParam.getNormalActivityId() + "");
        orderGiftRecord.setPer(skuNormalActivityParam.getNum());
        orderGiftRecord.setQuoteId(skuNormalActivityParam.getSkuId());
        orderGiftRecord.setQuoteName("goods_info");
        orderGiftRecord.setRecordCategory(ActivityCategoryEnum.ACTIVITY_POINT.getCode());
        orderGiftRecord.setRecordStatus(RecordStateEnum.CREATE.getCode());
        orderGiftRecord.setCreateTime(LocalDateTime.now());
        orderGiftRecord.setUpdateTime(LocalDateTime.now());
        orderGiftRecord.setDelFlag(DeleteFlag.NO);
        return orderGiftRecord;
    }

    /**
     * 获取黑名单
     * @return
     */
    @Override
    protected List<String> listBlackListCustomerId() {
        //查看用户是否在返积分黑名单里面
        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
        goodsBlackListPageProviderRequest.setBusinessCategoryColl(Collections.singletonList(GoodsBlackListCategoryEnum.UN_BACK_POINT_AFTER_PAY.getCode()));
        GoodsBlackListPageProviderResponse context = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest).getContext();
        List<String> normalList = context.getUnBackPointAfterPayBlackListModel().getNormalList();
        return CollectionUtils.isEmpty(normalList) ? new ArrayList<>() : normalList;
    }

    @Override
    protected boolean doSomething(OrderGiftRecord orderGiftRecord) {
        //获取樊登账号信息
        CustomerGetByIdResponse customer = super.getCustomer(orderGiftRecord.getCustomerId());
        FanDengAddPointReq fanDengAddPointReq = new FanDengAddPointReq();
        fanDengAddPointReq.setUserNo(customer.getFanDengUserNo());
        fanDengAddPointReq.setNum(orderGiftRecord.getPer().longValue());
        fanDengAddPointReq.setType(FanDengChangeTypeEnum.plus.getCode());
        fanDengAddPointReq.setSourceId(orderGiftRecord.getOrderId());
        fanDengAddPointReq.setDescription(String.format("商城返积分活动(订单号：%s)", orderGiftRecord.getOrderId()));
        BaseResponse baseResponse = externalProvider.changePoint(fanDengAddPointReq);
        log.info("PayOrderGiftRecordPointService doSomething changePoint response {}", JSON.toJSONString(baseResponse));
        if (Objects.equals(CommonErrorCode.SUCCESSFUL, baseResponse.getCode())) {
            return true;
        }
        return false;
    }


    /**
     * 创建订单调用
     * @param recordMessageMq
     */
    @Override
    public void afterCreateOrder(RecordMessageMq recordMessageMq) {
        super.afterCreateOrder(recordMessageMq);
    }


    /**
     * 支付成功后调用
     * @param recordMessageMq
     */
    @Override
    public void afterPayOrderLock(RecordMessageMq recordMessageMq) {
        super.afterPayOrderLock(recordMessageMq);
    }



    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void runTest() {
        OrderGiftRecord orderGiftRecord = new OrderGiftRecord();
        orderGiftRecord.setName("返积分活动");
        orderGiftRecord.setCustomerId("customer_id123");
        orderGiftRecord.setOrderId("orderId_1234");
        orderGiftRecord.setActivityId("activityId_1234");
        orderGiftRecord.setPer(100);
        orderGiftRecord.setQuoteId("quoteId_13123");
        orderGiftRecord.setQuoteName("goods_info");
        orderGiftRecord.setRecordCategory(ActivityCategoryEnum.ACTIVITY_POINT.getCode());
        orderGiftRecord.setRecordStatus(RecordStateEnum.CREATE.getCode());
        orderGiftRecord.setCreateTime(LocalDateTime.now());
        orderGiftRecord.setUpdateTime(LocalDateTime.now());
        orderGiftRecord.setDelFlag(DeleteFlag.NO);

        OrderGiftRecord save = payOrderGiftRecordRepository.save(orderGiftRecord);
        System.out.println(orderGiftRecord.getId());
        System.out.println(JSON.toJSONString(save));
        System.out.println("-------------end-------------");
    }
}
