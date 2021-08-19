package com.wanmi.sbc.job;


import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storeconsumerstatistics.StoreConsumerStatisticsProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaUpdateRequest;
import com.wanmi.sbc.customer.api.request.storeconsumerstatistics.StoreConsumerStatisticsModifyRequest;
import com.wanmi.sbc.customer.api.request.storeconsumerstatistics.StoreConsumerStatisticsQueryRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelQueryRequest;
import com.wanmi.sbc.customer.bean.dto.StoreCustomerRelaDTO;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import com.wanmi.sbc.customer.bean.vo.StoreConsumerStatisticsVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdRequest;
import com.wanmi.sbc.order.api.provider.growthvalue.OrderGrowthValueTempProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.growthvalue.OrderGrowthValueTempQueryRequest;
import com.wanmi.sbc.order.api.request.returnorder.CanReturnItemNumByTidRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderListByTidRequest;
import com.wanmi.sbc.order.api.request.trade.TradeVerifyAfterProcessingRequest;
import com.wanmi.sbc.order.api.response.returnorder.CanReturnItemNumByTidResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderListByTidResponse;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.vo.OrderGrowthValueTempVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 定时任务Handler（Bean模式）
 * 订单过退货期后 计算成长值,是否需要晋升平台等级/计算累积满几笔订单,多少金额,是否需要晋升店铺等级
 *
 * @author bail 2019-3-24
 */
@JobHandler(value = "orderGrowthValueIncreaseJobHandler")
@Component
@Slf4j
public class OrderGrowthValueIncreaseJobHandler extends IJobHandler {

    @Autowired
    private OrderGrowthValueTempProvider orderGrowthValueTempProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private CustomerGrowthValueProvider customerGrowthValueProvider;

    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;

    @Autowired
    private StoreConsumerStatisticsProvider storeConsumerStatisticsProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private StoreCustomerProvider storeCustomerProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Override
    public ReturnT<String> execute(String param) {
        // 查询过退货期的订单
        List<OrderGrowthValueTempVO> orderGrowthValueTempVOList = orderGrowthValueTempProvider
                .list(OrderGrowthValueTempQueryRequest.builder().returnEndTime(LocalDateTime.now()).build())
                .getContext().getOrderGrowthValueTempVOList();

        // 遍历订单
        List<Long> willDeleteTempIdList = new ArrayList<>();
        for (OrderGrowthValueTempVO vo: orderGrowthValueTempVOList) {
            // 验证订单是否存在售后申请
            if (!tradeQueryProvider.verifyAfterProcessing(TradeVerifyAfterProcessingRequest.builder().tid(vo.getOrderNo()).build())
                    .getContext().getVerifyResult()) {
                // 查询订单信息
                TradeVO trade;
                try {
                    BaseResponse<CanReturnItemNumByTidResponse> canReturnItemNumByTidResponseBaseResponse = returnOrderQueryProvider.queryCanReturnItemNumByTid(
                            CanReturnItemNumByTidRequest.builder().tid(vo.getOrderNo()).build());
                    trade = canReturnItemNumByTidResponseBaseResponse.getContext();
                }catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                // 查询商家信息
                StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder()
                        .storeId(trade.getSupplier().getStoreId()).build())
                        .getContext().getStoreVO();
                // 遍历订单商品，获取商品类目下成长值增长比例，统计成长值
                BigDecimal growthValueCount = trade.getTradeItems().stream()
                        .map(item -> {
                            // 获取商品类目下成长值增长比例
                            BigDecimal growthValueRate = goodsCateQueryProvider.getById(new GoodsCateByIdRequest(item.getCateId()))
                                    .getContext().getGrowthValueRate();
                            growthValueRate = growthValueRate == null ? BigDecimal.ZERO :
                                    growthValueRate.divide(new BigDecimal(100));
                            Integer canReturnNum = item.getCanReturnNum();
                            canReturnNum = canReturnNum == null ? BigDecimal.ROUND_UP : canReturnNum;
                            Long num = item.getNum();
                            num = num == null ? BigDecimal.ROUND_UP : num;
                            return item.getSplitPrice().multiply(growthValueRate).multiply(new BigDecimal(canReturnNum)).divide(new BigDecimal(num));
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_DOWN);
                if (growthValueCount.longValue() != 0) {
                    // 成长值不为0,增加成长值明细
                    customerGrowthValueProvider.increaseGrowthValue(CustomerGrowthValueAddRequest.builder()
                            .customerId(trade.getBuyer().getId())
                            .type(OperateType.GROWTH)
                            .serviceType(GrowthValueServiceType.ORDERCOMPLETION)
                            .growthValue(growthValueCount.longValue())
                            .tradeNo(trade.getId())
                            .opTime(LocalDateTime.now())
                            .build());
                }

                // 遍历订单商品，获取商品类目下积分增长比例，统计积分
                BigDecimal pointsCount = trade.getTradeItems().stream()
                        .map(item -> {
                            // 获取商品类目下成长值增长比例
                            BigDecimal pointsRate = goodsCateQueryProvider.getById(new GoodsCateByIdRequest(item.getCateId()))
                                    .getContext().getPointsRate();
                            pointsRate = pointsRate == null ? BigDecimal.ZERO :
                                    pointsRate.divide(new BigDecimal(100));
                            Integer canReturnNum = item.getCanReturnNum();
                            canReturnNum = canReturnNum == null ? BigDecimal.ROUND_UP : canReturnNum;
                            Long num = item.getNum();
                            num = num == null ? BigDecimal.ROUND_UP : num;
                            return item.getSplitPrice().multiply(pointsRate).multiply(new BigDecimal(canReturnNum)).divide(new BigDecimal(num));
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_DOWN);
                if (pointsCount.longValue() != 0) {
                    // 积分不为0,增加积分明细
                    customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                            .customerId(trade.getBuyer().getId())
                            .type(OperateType.GROWTH)
                            .serviceType(PointsServiceType.ORDERCOMPLETION)
                            .points(pointsCount.longValue())
                            .content(JSONObject.toJSONString(Collections.singletonMap("orderNo", trade.getId())))
                            .opTime(LocalDateTime.now())
                            .build());
                }

                if (store.getCompanyType().toValue() == 1) {
                    // 非自营店铺订单
                    // 根据客户id和店铺id查询用户在该店铺的消费统计
                    StoreConsumerStatisticsVO consumerStatistics = storeConsumerStatisticsProvider
                            .getByCustomerIdAndStoreId(StoreConsumerStatisticsQueryRequest
                                    .builder()
                                    .customerId(trade.getBuyer().getId())
                                    .storeId(store.getStoreId())
                                    .build())
                            .getContext().getStoreConsumerStatisticsVO();

                    BaseResponse<ReturnOrderListByTidResponse> returnListRes =
                            returnOrderQueryProvider.listByTid(ReturnOrderListByTidRequest.builder().tid(trade.getId()).build());
                    List<ReturnOrderVO> returnList = returnListRes.getContext().getReturnOrderList();
                    BigDecimal returnSumPrice = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(returnList)) {
                        // 计算已经完成的退单总金额
                        returnSumPrice = returnList.stream().filter(r -> r.getReturnFlowState().equals(ReturnFlowState.COMPLETED))
                                .map(r -> r.getReturnPrice().getActualReturnPrice()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO)
                                .setScale(2, BigDecimal.ROUND_DOWN);
                    }
                    // 更新消费统计
                    StoreConsumerStatisticsModifyRequest modifyRequest = new StoreConsumerStatisticsModifyRequest();
                    if (Objects.nonNull(consumerStatistics)) {
                        modifyRequest.setId(consumerStatistics.getId());
                    }
                    modifyRequest.setCustomerId(trade.getBuyer().getId());
                    modifyRequest.setStoreId(store.getStoreId());
                    modifyRequest.setTradeCount(Objects.nonNull(consumerStatistics) ?
                            consumerStatistics.getTradeCount() + 1 : 1);
                    // 已累计金额 + 订单金额 - 退单总金额
                    modifyRequest.setTradePriceCount(Objects.nonNull(consumerStatistics) ?
                            consumerStatistics.getTradePriceCount().add(trade.getTradePrice().getTotalPrice()).subtract(returnSumPrice)
                            : trade.getTradePrice().getTotalPrice().subtract(returnSumPrice));
                    modifyRequest.setUpdateTime(LocalDateTime.now());
                    storeConsumerStatisticsProvider.modify(modifyRequest);

                    // 判断当前会员可满足的最高店铺等级
                    StoreLevelVO topStoreLevel = storeLevelQueryProvider.queryByLevelUpCondition(
                            StoreLevelQueryRequest.builder()
                                    .amountConditions(modifyRequest.getTradePriceCount())
                                    .orderConditions(modifyRequest.getTradeCount())
                                    .storeId(modifyRequest.getStoreId()).build())
                            .getContext().getStoreLevelVO();

                    if (Objects.nonNull(topStoreLevel)) {
                        // 修改客户等级
                        updateStoreCustomerRela(trade.getBuyer().getId(), store, topStoreLevel.getStoreLevelId());
                    }
                }
                willDeleteTempIdList.add(vo.getId());
            }
        };

        if (CollectionUtils.isNotEmpty(willDeleteTempIdList)) {
            // 删除临时表记录
            orderGrowthValueTempProvider.deleteByIdList(
                    OrderGrowthValueTempQueryRequest.builder().idList(willDeleteTempIdList).build());
        }
        return SUCCESS;
    }

    /**
     * 更新客户的店铺等级关系
     *
     * @param customerId
     * @param storeInfo
     * @param storeLevelId
     */
    private void updateStoreCustomerRela(String customerId, StoreVO storeInfo, Long storeLevelId) {
        StoreCustomerRelaUpdateRequest request = new StoreCustomerRelaUpdateRequest();
        StoreCustomerRelaDTO storeCustomerRelaDTO = new StoreCustomerRelaDTO();
        storeCustomerRelaDTO.setCustomerId(customerId);
        storeCustomerRelaDTO.setStoreLevelId(storeLevelId);
        storeCustomerRelaDTO.setStoreId(storeInfo.getStoreId());
        storeCustomerRelaDTO.setCompanyInfoId(storeInfo.getCompanyInfo().getCompanyInfoId());
        // 店铺关联的客户 类型
        storeCustomerRelaDTO.setCustomerType(CustomerType.PLATFORM);
        request.setStoreCustomerRelaDTO(storeCustomerRelaDTO);
        storeCustomerProvider.updateStoreCustomerRela(request);
    }

}
