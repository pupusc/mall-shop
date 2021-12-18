package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerSaveProvider;
import com.wanmi.sbc.customer.api.provider.distribution.performance.DistributionPerformanceProvider;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerModifyRewardRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerUpgradeRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceEnteringRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByCustomerRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerByIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.EsDistributionCustomerByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.EsDistributionCustomerByIdResponse;
import com.wanmi.sbc.customer.api.response.quicklogin.ThirdLoginRelationResponse;
import com.wanmi.sbc.customer.bean.dto.DistributionPerformanceDTO;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.ThirdLoginRelationVO;
import com.wanmi.sbc.distribution.DistributionCacheService;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerProvider;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerAddRequest;
import com.wanmi.sbc.job.model.entity.DistributionTaskTemp;
import com.wanmi.sbc.job.repository.DistributionTaskTempRepository;
import com.wanmi.sbc.marketing.api.provider.distributionrecord.DistributionRecordSaveProvider;
import com.wanmi.sbc.marketing.api.request.distributionrecord.*;
import com.wanmi.sbc.marketing.api.response.distributionrecord.DistributionRecordAddResponse;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.api.provider.distribution.ConsumeRecordProvider;
import com.wanmi.sbc.order.api.provider.thirdplatformtrade.ThirdPlatformTradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.distribution.ConsumeRecordAddRequest;
import com.wanmi.sbc.order.api.request.thirdplatformtrade.ThirdPlatformTradeAddRequest;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.distribution.ConsumeRecordAddResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeQueryFirstCompleteResponse;
import com.wanmi.sbc.order.bean.vo.TradeDistributeItemVO;
import com.wanmi.sbc.order.bean.vo.TradePriceVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 订单处理消费者
 * @Autho qiaokang
 * @Date：2019-03-06 10:08:11
 */
@Service
@Slf4j
@EnableBinding(BossSink.class)
public class OrderConsumerService {

    /**
     * 注入消费记录Provider
     */
    @Autowired
    private ConsumeRecordProvider consumeRecordProvider;

    /**
     * 注入分销任务临时表Repository
     */
    @Autowired
    private DistributionTaskTempRepository distributionTaskTempRepository;

    /**
     * 注入分销记录Provider
     */
    @Autowired
    private DistributionRecordSaveProvider distributionRecordSaveProvider;

    /**
     * 注入订单查询Provider
     */
    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    /**
     * 注入订单Provider
     */
    @Autowired
    private TradeProvider tradeProvider;

    /**
     * 注入订单配置Provider
     */
    @Autowired
    private AuditQueryProvider auditQueryProvider;

    /**
     * 注入分销员设置Provider
     */
    @Autowired
    private DistributionCustomerSaveProvider distributionCustomerSaveProvider;

    /**
     * 注入分销员查询Provider
     */
    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private ThirdLoginRelationQueryProvider thirdLoginRelationQueryProvider;

    @Autowired
    private DistributionPerformanceProvider distributionPerformanceProvider;

    @Autowired
    private ThirdPlatformTradeProvider thirdPlatformTradeProvider;

    /**
     * 注入分销设置缓存service
     */
    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private BinderAwareChannelResolver resolver;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private EsDistributionCustomerProvider esDistributionCustomerProvider;

    @Autowired
    private EsDistributionCustomerQueryProvider esDistributionCustomerQueryProvider;


    /**
     * 订单支付MQ处理
     *
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_PAYED)
    public void doOrderPayed(String json) {
        TradeVO tradeVO = JSONObject.parseObject(json, TradeVO.class);
        RLock rLock = redissonClient.getFairLock("doOrderPayed" + tradeVO.getId());
        try {
            if (rLock.tryLock(10, 10, TimeUnit.SECONDS)) {
                log.info("=============== 订单支付MQ处理start ===============");
                // 新增分销任务临时记录
                this.addTaskTemp(tradeVO);

                // 新增分销记录表记录
                this.addDistributionRecord(tradeVO);

                // 增加分销员奖励信息
                this.dealDistributionCustomerReward(tradeVO, true);

                // 如果购买的是开店礼包，升级为小B
                if (DefaultFlag.YES.equals(tradeVO.getStoreBagsFlag())) {
                    DistributionCustomerUpgradeRequest request = new DistributionCustomerUpgradeRequest();
                    request.setCustomerId(tradeVO.getBuyer().getId());
                    DistributionLimitType distributionLimitType = distributionCacheService.getBaseLimitType();
                    request.setBaseLimitType(Objects.nonNull(distributionLimitType) ? DefaultFlag.fromValue(distributionLimitType.toValue()) : null);
                    request.setDistributorLevelVOList(distributionCacheService.getDistributorLevels());
                    distributionCustomerSaveProvider.upgrade(request);
                    //同步es
                    if (StringUtils.isNotBlank(request.getCustomerId())) {
                        DistributionCustomerByCustomerIdRequest idRequest = new DistributionCustomerByCustomerIdRequest();
                        idRequest.setCustomerId(request.getCustomerId());
                        EsDistributionCustomerByCustomerIdResponse idResponse = distributionCustomerQueryProvider.getByCustomerIdShowPhone(idRequest).getContext();
                        DistributionCustomerShowPhoneVO distributionCustomerVO = idResponse.getDistributionCustomerVO();
                        if(Objects.nonNull(distributionCustomerVO)) {
                            EsDistributionCustomerAddRequest addRequest = new EsDistributionCustomerAddRequest(Collections.singletonList(distributionCustomerVO));
                            esDistributionCustomerProvider.add(addRequest);
                        }
                    }

                }
                log.info("=============== 订单支付MQ处理end ===============");
            } else {
                log.info("订单编号：{}，重复分销任务doOrderPayed", tradeVO.getId());
            }
        } catch (Exception e) {
            log.error("订单支付MQ处理异常! param={}", json, e);
        } finally {
            rLock.unlock();
        }
    }


    /**
     * 订单完成MQ处理
     * 更新分销临时表、分销记录表信息
     *
     * @param tradeId
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_COMPLETE)
    public void doOrderComplete(String tradeId) {
        try {
            log.info("=============== 订单完成MQ处理start ===============");

            // 查询交易信息
            TradeGetByIdRequest getByIdRequest = new TradeGetByIdRequest(tradeId);
            BaseResponse<TradeGetByIdResponse> getByIdResponse = tradeQueryProvider.getById(getByIdRequest);
            log.info("=============进入查询订单详情接口BFF==========================mongoDb返回数据:{}",
                    JSONObject.toJSONString(getByIdResponse));
            if (Objects.nonNull(getByIdResponse.getContext()) && Objects.nonNull(getByIdResponse.getContext().getTradeVO())) {
                TradeVO tradeVO = getByIdResponse.getContext().getTradeVO();
                // 查询订单可退时间
                LocalDateTime returnTime = queryReturnTime();
                // 新增消费记录
                this.addConsumeRecord(tradeVO);
                // 更新分销临时任务表
                this.modifyTaskTemp(tradeVO, returnTime);
                // 更新分销记录表
                this.modifyDistributionRecord(tradeVO);
                //// 修改订单入账时间
                //this.modifyFinalTime(tradeVO.getId(), returnTime);

            } else {
                log.error("未查询到{}的交易信息", tradeId);
            }

            log.info("=============== 订单完成MQ处理end ===============");
        } catch (Exception e) {
            log.error("订单完成MQ处理异常! param={}", tradeId, e);
        }
    }

    ///**
    // * 积分订单完成MQ处理
    // * 更新分销临时表、分销记录表信息
    // * 作废 订单入账时间移到订单完成状态机处理
    // *
    // * @param tradeId
    // */
    //@Deprecated
    //@StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_POINTS_ORDER_COMPLETE)
    //public void doPointsOrderComplete(String tradeId) {
    //    try {
    //        log.info("=============== 积分订单完成MQ处理start ===============");
    //
    //        // 查询交易信息
    //        TradeGetByIdRequest getByIdRequest = new TradeGetByIdRequest(tradeId);
    //        BaseResponse<TradeGetByIdResponse> getByIdResponse = tradeQueryProvider.getById(getByIdRequest);
    //        log.info("=============进入查询积分订单详情接口BFF==========================mongoDb返回数据:{}",
    //                JSONObject.toJSONString(getByIdResponse));
    //        if (Objects.nonNull(getByIdResponse.getContext()) && Objects.nonNull(getByIdResponse.getContext().getTradeVO())) {
    //            TradeVO tradeVO = getByIdResponse.getContext().getTradeVO();
    //            // 查询订单可退时间
    //            LocalDateTime returnTime = queryReturnTime();
    //            // 修改订单入账时间
    //            this.modifyFinalTime(tradeVO.getId(), returnTime);
    //
    //        } else {
    //            log.error("未查询到{}的交易信息", tradeId);
    //        }
    //
    //        log.info("=============== 积分订单完成MQ处理end ===============");
    //    } catch (Exception e) {
    //        log.error("积分订单完成MQ处理异常! param={}", tradeId, e);
    //    }
    //}


    /**
     * 订单支付&订单完成MQ处理
     *
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_PAYED_AND_COMPLETE)
    public void doOrderPayedAndComplete(String json) {
        try {
            log.info("=============== 订单支付&订单完成MQ处理start ===============");
            TradeVO tradeVO = JSONObject.parseObject(json, TradeVO.class);
            doOrderPayed(json);
            doOrderComplete(tradeVO.getId());
            log.info("=============== 订单支付&订单完成MQ处理end ===============");
        } catch (Exception e) {
            log.error("订单支付&订单完成MQ处理异常! param={}", json, e);
        }
    }

    /**
     * 订单退款作废MQ处理
     *
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_REFUND_VOID)
    @Transactional
    public void doOrderRefundVoid(String json) {
        try {
            log.info("=============== 订单退款作废MQ处理start ===============");
            TradeVO tradeVO = JSONObject.parseObject(json, TradeVO.class);

            // 待删除的临时表id集合
            List<String> idList = new ArrayList<>();
            // 根据订单号查询临时表信息
            List<DistributionTaskTemp> taskTempList = distributionTaskTempRepository.findByOrderId(tradeVO.getId());
            if (CollectionUtils.isNotEmpty(taskTempList)) {
                for (DistributionTaskTemp distributionTaskTemp : taskTempList) {
                    if (Objects.nonNull(distributionTaskTemp.getOrderDisableTime())) {
                        // 有订单失效时间，定时任务会处理，这里不操作相关数据
                        continue;
                    }
                    idList.add(distributionTaskTemp.getId());
                }

                if (CollectionUtils.isNotEmpty(idList)) {
                    // 删除临时表数据
                    int count = distributionTaskTempRepository.deleteByIdIn(idList);

                    // 判断临时表数据是否删除成功
                    if (count > 0) {
                        // 减少分销员奖励信息
                        this.dealDistributionCustomerReward(tradeVO, false);

                        // 删除分销记录数据
                        DistributionRecordByTradeIdRequest request =
                                new DistributionRecordByTradeIdRequest(tradeVO.getId());
                        distributionRecordSaveProvider.deleteByTradeId(request);
                    }
                }
            }

            log.info("=============== 订单退款作废MQ处理end ===============");
        } catch (Exception e) {
            log.error("订单退款作废MQ处理异常! param={}", json, e);
        }
    }

    /**
     * 第三方平台-同步订单
     *
     * @param businessId 业务订单号
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_THIRD_PLATFORM_SYNC)
    public void doThirdPlatformSync(String businessId) {
        try {
            log.info("=============== 订单同步至第三方平台MQ处理start ===============");
            thirdPlatformTradeProvider.add(ThirdPlatformTradeAddRequest.builder().businessId(businessId)
                    .thirdPlatformType(ThirdPlatformType.LINKED_MALL).build());
            log.info("=============== 订单同步至第三方平台MQ处理end ===============");
        } catch (Exception e) {
            log.error("订单同步至第三方平台MQ处理异常! param={}", businessId, e);
        }
    }

    /**
     * 更新分销临时任务表
     *
     * @param tradeVO
     */
    private void modifyTaskTemp(TradeVO tradeVO, LocalDateTime returnTime) {
        // 根据交易号查询分销临时表信息
        List<DistributionTaskTemp> taskTempList = distributionTaskTempRepository.findByOrderId(tradeVO.getId());
        if (CollectionUtils.isNotEmpty(taskTempList)) {
            // 根据交易号默认只能查询到一条记录
            DistributionTaskTemp taskTemp = taskTempList.get(0);

            // 判断是否首笔完成订单
            if (this.checkIsFirstCompleteTrade(tradeVO)) {
                taskTemp.setFirstValidBuy(BoolFlag.YES);
            } else {
                taskTemp.setFirstValidBuy(BoolFlag.NO);
            }

            // 订单可退时间
            taskTemp.setOrderDisableTime(returnTime);
            // 更新分销临时表
            distributionTaskTempRepository.save(taskTemp);
        }
    }

    /**
     * 修改订单入账时间
     */
    private void modifyFinalTime(String tradeId, LocalDateTime returnTime) {
        tradeProvider.updateFinalTime(
                TradeFinalTimeUpdateRequest.builder().tid(tradeId).orderReturnTime(returnTime).build());
    }

    /**
     * 查询订单可退时间
     */
    private LocalDateTime queryReturnTime() {
        LocalDateTime returnTime = LocalDateTime.now();
        // 查询已完成订单允许申请退单天数配置
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        // 是否支持退货，不支持退货时，订单可退时间设置为当前时间
        if (config.getStatus() != 0) {
            JSONObject content = JSON.parseObject(config.getContext());
            Long day = content.getObject("day", Long.class);
            returnTime = returnTime.plusDays(day);
        }
        return returnTime;
    }

    /**
     * 更新分销记录信息
     *
     * @param tradeVO
     */
    private void modifyDistributionRecord(TradeVO tradeVO) {
        DistributionRecordModifyRequest recordModifyRequest = new DistributionRecordModifyRequest();
        List<DistributionRecordUpdateInfo> updateInfoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tradeVO.getDistributeItems()) && StringUtils.isNotBlank(tradeVO.getDistributorId())) {
            // 根据交易号更新，无需遍历DistributeItems
            DistributionRecordUpdateInfo recordUpdateInfo = new DistributionRecordUpdateInfo();
            recordUpdateInfo.setTradeId(tradeVO.getId());
            recordUpdateInfo.setFinishTime(tradeVO.getTradeState().getEndTime());
            updateInfoList.add(recordUpdateInfo);

            // 更新分销记录信息
            recordModifyRequest.setUpdateInfos(updateInfoList);
            distributionRecordSaveProvider.modify(recordModifyRequest);
        }
    }

    /**
     * 更新分销员奖励信息
     *
     * @param tradeVO
     * @param increaseFlag true:增加分销奖励 false:减少分销奖励
     */
    private void dealDistributionCustomerReward(TradeVO tradeVO, boolean increaseFlag) {
        List<TradeDistributeItemVO> distributeItemVOList = tradeVO.getDistributeItems();
        String distributorId = tradeVO.getDistributorId();
        // 存在分销商品和分销员id，才更新分销员奖励信息
        if (CollectionUtils.isNotEmpty(distributeItemVOList) && StringUtils.isNotBlank(distributorId)) {
            // 根据分销员id查询分销员信息
            DistributionCustomerByIdRequest distributionCustomerRequest =
                    new DistributionCustomerByIdRequest(distributorId);
            BaseResponse<DistributionCustomerByIdResponse> response =
                    distributionCustomerQueryProvider.getById(distributionCustomerRequest);
            if (Objects.nonNull(response) && Objects.nonNull(response.getContext())
                    && Objects.nonNull(response.getContext().getDistributionCustomerVO())) {
                DistributionCustomerVO distributionCustomerVO = response.getContext().getDistributionCustomerVO();
                DistributionCustomerModifyRewardRequest rewardRequest = new DistributionCustomerModifyRewardRequest();
                // 分销员id
                rewardRequest.setDistributionId(distributorId);
                // 分销订单(笔)
                //已作废得分销订单数量不扣减，前端需要展示所有已支付的分销订单数量
                rewardRequest.setDistributionTradeCount(increaseFlag ? 1 : 0);
               /* // 非客户自己买的订单，才统计销售额
                if (!distributionCustomerVO.getCustomerId().equals(tradeVO.getBuyer().getId())) {*/
                // 销售额
                BigDecimal sales = BigDecimal.ZERO;
                for (TradeDistributeItemVO distributeItemVO : distributeItemVOList) {
                    sales = sales.add(distributeItemVO.getActualPaidPrice());
                }
                rewardRequest.setSales(increaseFlag ? sales : sales.negate());
//                }

                // 未入账分销佣金
                rewardRequest.setCommissionNotRecorded(increaseFlag ? tradeVO.getCommission() :
                        tradeVO.getCommission().negate());
                if (distributionCustomerVO.getForbiddenFlag() == DefaultFlag.NO) {
                    TradeUpdateCommissionFlagRequest updateCommissionFlagRequest =
                            new TradeUpdateCommissionFlagRequest();
                    updateCommissionFlagRequest.setTradeId(tradeVO.getId());
                    // 订单是否返利字段
                    updateCommissionFlagRequest.setCommissionFlag(true);
                    // 更新订单表是否返利字段
                    tradeProvider.updateCommissionFlag(updateCommissionFlagRequest);
                    //非自购分销返利，记录业绩
                    if (increaseFlag && !rewardRequest.getSales().equals(BigDecimal.ZERO)) {
                        //新增业务员销售业绩记录
                        addDistributionPerformance(rewardRequest, distributionCustomerVO.getCustomerId());
                    }
                }

                // 更新分销员奖励信息
                distributionCustomerSaveProvider.modifyReward(rewardRequest);
                //同步es
                DistributionCustomerByIdRequest idRequest = DistributionCustomerByIdRequest.builder().distributionId(distributorId).build();
                EsDistributionCustomerByIdResponse idResponse = distributionCustomerQueryProvider.getByIdShowPhone(idRequest).getContext();
                DistributionCustomerShowPhoneVO entity = idResponse.getDistributionCustomerVO();
                if (Objects.nonNull(entity)) {
                    EsDistributionCustomerAddRequest addRequest = new EsDistributionCustomerAddRequest(Collections.singletonList(entity));
                    esDistributionCustomerProvider.add(addRequest);
                }
            } else {
                log.error("查询分销员信息异常");
            }
        } else {
            log.info("没有分销商品，不更新分销员奖励信息");
        }
    }

    /**
     * 判断是否首笔完成订单
     *
     * @param tradeVO
     * @return
     */
    private boolean checkIsFirstCompleteTrade(TradeVO tradeVO) {
        // 根据订单中客户id查询客户首笔完成的订单号
        TradeQueryFirstCompleteRequest firstCompleteRequest =
                new TradeQueryFirstCompleteRequest(tradeVO.getBuyer().getId());
        BaseResponse<TradeQueryFirstCompleteResponse> firstCompleteResponse =
                tradeQueryProvider.queryFirstCompleteTrade(firstCompleteRequest);
        if (Objects.nonNull(firstCompleteResponse) && Objects.nonNull(firstCompleteResponse.getContext())) {
            String firstTradeId = firstCompleteResponse.getContext().getTradeId();
            if (StringUtils.isBlank(firstTradeId)) {
                // 还没有完成的订单
                return true;
            } else if (firstTradeId.equals(tradeVO.getId())) {
                // 首笔完成的订单就是当前订单
                return true;
            }
        } else {
            log.error("查询客户{}首笔完成订单异常", tradeVO.getBuyer().getId());
        }

        return false;
    }


    /**
     * 新增消费记录
     *
     * @param tradeVO
     */
    private void addConsumeRecord(TradeVO tradeVO) {
        ConsumeRecordAddRequest request = new ConsumeRecordAddRequest();
        request.setOrderId(tradeVO.getId());
        TradePriceVO tradePrice = tradeVO.getTradePrice();
        if (Objects.nonNull(tradePrice)) {
            // 配送费
            BigDecimal deliveryPrice = Objects.nonNull(tradePrice.getDeliveryPrice()) ?
                    tradePrice.getDeliveryPrice() : BigDecimal.ZERO;
            // 消费额要减去配送费
            request.setConsumeSum(tradePrice.getTotalPrice().subtract(deliveryPrice));
        }
        request.setValidConsumeSum(BigDecimal.ZERO);
        if (Objects.nonNull(tradeVO.getSupplier())) {
            request.setStoreId(tradeVO.getSupplier().getStoreId());
        }
        if (StringUtils.isNotBlank(tradeVO.getInviteeId())) {
            request.setDistributionCustomerId(tradeVO.getInviteeId());
        }
        request.setOrderCreateTime(tradeVO.getTradeState().getCreateTime());
        request.setCustomerId(tradeVO.getBuyer().getId());
        request.setCustomerName(tradeVO.getBuyer().getName());
        request.setFlowState(tradeVO.getTradeState().getFlowState().toValue());
        ThirdLoginRelationResponse thirdLoginRelationResponse = thirdLoginRelationQueryProvider
                .listThirdLoginRelationByCustomer
                        (ThirdLoginRelationByCustomerRequest.builder
                                ().customerId(tradeVO.getBuyer().getId()).thirdLoginType(ThirdLoginType.WECHAT).build()).getContext();
        ThirdLoginRelationVO thirdLoginRelationVO = thirdLoginRelationResponse.getThirdLoginRelation();
        if (Objects.nonNull(thirdLoginRelationVO)) {
            request.setHeadImg(thirdLoginRelationVO.getHeadimgurl());
        }
        BaseResponse<ConsumeRecordAddResponse> response = consumeRecordProvider.addConsumeRecord(request);
        // 新增结果是否成功
        boolean addResult =
                Objects.nonNull(response.getContext()) && Objects.nonNull(response.getContext().getConsumeRecordVo());
        if (addResult) {
            log.info("新增消费记录成功");
        } else {
            log.error("新增消费记录失败");
        }
    }

    /**
     * 新增分销任务临时记录
     *
     * @param tradeVO
     */
    private void addTaskTemp(TradeVO tradeVO) {
        DistributionTaskTemp taskTemp = new DistributionTaskTemp();
        taskTemp.setCustomerId(tradeVO.getBuyer().getId());
        taskTemp.setDistributionCustomerId(tradeVO.getInviteeId());
        // 根据是否包含分销商品和分销员id，判断是否是分销订单
        if (CollectionUtils.isEmpty(tradeVO.getDistributeItems()) || StringUtils.isEmpty(tradeVO.getDistributorId())) {
            taskTemp.setDistributionOrder(BoolFlag.NO);
        } else {
            taskTemp.setDistributionOrder(BoolFlag.YES);
        }
        taskTemp.setFirstValidBuy(BoolFlag.NO);
        taskTemp.setOrderId(tradeVO.getId());

        TradeConfigGetByTypeRequest tradeConfigGetByTypeRequest = new TradeConfigGetByTypeRequest();
        tradeConfigGetByTypeRequest.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        ConfigVO configVO = auditQueryProvider.getTradeConfigByType(tradeConfigGetByTypeRequest).getContext();
        if (Objects.nonNull(configVO)) {
            taskTemp.setOrderDisableTime(configVO.getStatus() == 0 ? LocalDateTime.now() : LocalDateTime.now().plusDays(Long.parseLong(JSONObject.parseObject(configVO.getContext()).get("day").toString())));
        }

        // 默认退单数量为0
        taskTemp.setReturnOrderNum(0);

        DistributionTaskTemp temp = distributionTaskTempRepository.save(taskTemp);
        if (Objects.isNull(temp)) {
            log.error("新增分销任务临时记录失败");
        } else {
            log.info("新增分销任务临时记录成功");
        }
    }

    /**
     * 新增分销记录
     *
     * @param tradeVO
     */
    private void addDistributionRecord(TradeVO tradeVO) {
        List<TradeDistributeItemVO> distributeItemVOList = tradeVO.getDistributeItems();
        if (CollectionUtils.isNotEmpty(distributeItemVOList) && StringUtils.isNotBlank(tradeVO.getDistributorId())) {
            // 存在分销商品，才添加分销记录
            // 订单是否完成标志
            boolean orderComplete = false;
            List<DistributionRecordAddInfo> recordAddInfoList = new ArrayList<>();
            DistributionRecordAddInfo recordAddInfo;

            for (TradeDistributeItemVO tradeDistributeItemVO : distributeItemVOList) {
                recordAddInfo = new DistributionRecordAddInfo();
                recordAddInfo.setGoodsInfoId(tradeDistributeItemVO.getGoodsInfoId());
                recordAddInfo.setTradeId(tradeVO.getId());
                recordAddInfo.setStoreId(tradeVO.getSupplier().getStoreId());
                if (Objects.nonNull(tradeVO.getBuyer())) {
                    recordAddInfo.setCustomerId(tradeVO.getBuyer().getId());
                }
                recordAddInfo.setDistributorId(tradeVO.getDistributorId());
                recordAddInfo.setDistributorCustomerId(tradeVO.getInviteeId());
                if (Objects.nonNull(tradeVO.getTradeState())) {
                    recordAddInfo.setPayTime(tradeVO.getTradeState().getPayTime());

                    if (Objects.nonNull(tradeVO.getTradeState().getEndTime())) {
                        // 有订单完成时间，说明订单已完成
                        orderComplete = true;
                        recordAddInfo.setFinishTime(tradeVO.getTradeState().getEndTime());
                    }
                }
                recordAddInfo.setOrderGoodsPrice(tradeDistributeItemVO.getActualPaidPrice());
                recordAddInfo.setOrderGoodsCount(tradeDistributeItemVO.getNum());
                recordAddInfo.setCommissionRate(tradeDistributeItemVO.getCommissionRate());
                recordAddInfo.setCommissionGoods(tradeDistributeItemVO.getCommission());
                recordAddInfoList.add(recordAddInfo);
            }

            DistributionRecordAddRequest recordAddRequest = new DistributionRecordAddRequest();
            recordAddRequest.setDistributionRecordAddInfos(recordAddInfoList);

            BaseResponse<DistributionRecordAddResponse> response = distributionRecordSaveProvider.add(recordAddRequest);
            // 新增结果是否成功
            boolean addResult = false;
            if (Objects.nonNull(response) && Objects.nonNull(response.getContext())) {
                List<DistributionRecordVO> recordVOList = response.getContext().getDistributionRecordVOs();
                addResult = CollectionUtils.isNotEmpty(recordVOList);
            }

            if (addResult) {
                log.info("新增分销记录成功");

                if (orderComplete) {
                    // 订单已完成，更新分销临时表和分销记录表信息
                    this.doOrderComplete(tradeVO.getId());
                }
            } else {
                log.error("新增分销记录失败");
            }
        } else {
            log.info("没有分销商品，无需新增分销记录");
        }
    }

    /**
     * 新增分销业绩记录
     *
     * @param modifyRewardRequest 分销员信息修改参数Bean，此处借用封装
     * @param customerId          分销员对应的customerId
     */
    private void addDistributionPerformance(DistributionCustomerModifyRewardRequest modifyRewardRequest,
                                            String customerId) {
        DistributionPerformanceDTO dto = DistributionPerformanceDTO.builder()
                .commission(modifyRewardRequest.getCommissionNotRecorded())
                .distributionId(modifyRewardRequest.getDistributionId())
                .saleAmount(modifyRewardRequest.getSales())
                .targetDate(LocalDate.now())
                .customerId(customerId)
                .build();
        DistributionPerformanceEnteringRequest request = new DistributionPerformanceEnteringRequest(dto);
        distributionPerformanceProvider.enteringPerformance(request);
    }


    /**
     * 订单推送第三方成功
     * @param
     */

    @RabbitListener(queues = JmsDestinationConstants.PROVIDER_TRADE_ORDER_PUSH_CONFIRM_QUEUE)
    @RabbitHandler
    public void orderPushConsumer(Message message, @Payload String body){
        log.info("order push confirm message:{}",body);
        try{
            ProviderTradeStatusSyncRequest request = JSONObject.parseObject(body,ProviderTradeStatusSyncRequest.class);
            tradeProvider.syncProviderTradeStatus(request);
            log.info("=============== 订单推送完成 ===============");
        }catch (Exception e){
            log.error("provider order push confirm error,body:{}",body,e);
        }
    }

    /**
     * 物流状态查询成功
     * @param message
     * @param body
     */
    @RabbitListener(queues = JmsDestinationConstants.PROVIDER_TRADE_DELIVERY_STATUS_SYNC_CONFIRM_QUEUE)
    @RabbitHandler
    public void deliveryStatusSyncConsumer(Message message, @Payload String body){
        log.info("order delivery status sync confirm message:{}", body);
        try {
            ProviderTradeDeliveryStatusSyncRequest request = JSONObject.parseObject(body, ProviderTradeDeliveryStatusSyncRequest.class);
            tradeProvider.syncProviderTradeDeliveryStatus(request);
            log.info("=============== 订单状态变更完成 ===============");
        }catch (Exception e){
            log.error("provider order delivery status sync confirm error,body:{}",body,e);
        }
    }

}
