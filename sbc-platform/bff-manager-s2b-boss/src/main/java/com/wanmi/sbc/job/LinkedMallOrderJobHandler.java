package com.wanmi.sbc.job;

import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.order.SbcOrderListQueryRequest;
import com.wanmi.sbc.linkedmall.api.response.order.SbcOrderListQueryResponse;
import com.wanmi.sbc.order.api.provider.thirdplatformtrade.ThirdPlatformTradeProvider;
import com.wanmi.sbc.order.api.provider.thirdplatformtrade.ThirdPlatformTradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradeCountCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradePageCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradeUpdateStateRequest;
import com.wanmi.sbc.order.bean.dto.ThirdPlatformTradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.ThirdPlatformTradeUpdateStateDTO;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 定时任务Handler（Bean模式）
 * linkedMall订单定时任务
 */
@JobHandler(value = "linkedMallOrderJobHandler")
@Component
@Slf4j
public class LinkedMallOrderJobHandler extends IJobHandler {

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private ThirdPlatformTradeQueryProvider thirdPlatformTradeQueryProvider;

    @Autowired
    private LinkedMallOrderQueryProvider linkedMallOrderQueryProvider;

    @Autowired
    private ThirdPlatformTradeProvider thirdPlatformTradeProvider;

    /**
     * linkedMall订单定时任务
     */
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        ThirdPlatformConfigResponse response = thirdPlatformConfigQueryProvider.get(
                ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build())
                .getContext();
        if(Objects.isNull(response)){
            return SUCCESS;
        }
        XxlJobLogger.log("linkedmall订单定时任务执行 " + LocalDateTime.now());
        long start = System.currentTimeMillis();
        log.info("linkedmall订单定时任务执行 " + LocalDateTime.now());

        this.thirdPlatformTradeStateSync();
        log.info("linkedmall订单定时任务执行结束，耗时：{}ms ", (System.currentTimeMillis() - start));
        return SUCCESS;
    }

    public void thirdPlatformTradeStateSync() {
        ThirdPlatformTradeQueryDTO queryRequest = new ThirdPlatformTradeQueryDTO();

        // 查询trade表所有linkedmall订单，排除 已作废、已完成状态、未支付
        List<FlowState> notFlowStates = new ArrayList<>();
        notFlowStates.add(FlowState.VOID);
        notFlowStates.add(FlowState.COMPLETED);
        notFlowStates.add(FlowState.REFUND);
        queryRequest.setNotFlowStates(notFlowStates);
        queryRequest.setNotPayStates(Collections.singletonList(PayState.NOT_PAID));
        queryRequest.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
        queryRequest.setOrderType(OrderType.ALL_ORDER);
        Long totalCount = thirdPlatformTradeQueryProvider.countCriteria(ThirdPlatformTradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount();
        log.info("获取符合条件的linkedmall订单总数为：{}条记录",totalCount);

        if (totalCount <= 0) {
            return;
        }

        int pageSize = 100;//每批查询100个linkedmall订单
        long pageCount = 0L;
        long m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        queryRequest.setPageSize(100);

        for (int i = 0; i < pageCount; i++) {
            queryRequest.setPageNum(i);

            List<TradeVO> tradeVOList =
                    thirdPlatformTradeQueryProvider.pageCriteria(ThirdPlatformTradePageCriteriaRequest.builder().tradePageDTO(queryRequest).build())
                    .getContext().getTradePage().getContent();
            List<ThirdPlatformTradeUpdateStateDTO> tradeUpdateStates = this.fillLinkedMallOrderStatus(tradeVOList);
            log.info("需要更新状态的linkedmall订单总数为：{}条记录",tradeUpdateStates.size());
            // 更新linkedmall订单状态
            tradeUpdateStates.forEach(tradeUpdateState -> {
                thirdPlatformTradeProvider.updateTradeState(ThirdPlatformTradeUpdateStateRequest.builder().tradeUpdateStateDTO(tradeUpdateState).build());
                log.info("linkedmall订单号：{}，更新订单状态，更新后订单状态：{}，物流状态：{}",tradeUpdateState.getId(),
                        tradeUpdateState.getFlowState(), tradeUpdateState.getDeliverStatus());
            });
        }
    }

    // 填充linkedMall订单状态 及 发货状态
    private List<ThirdPlatformTradeUpdateStateDTO> fillLinkedMallOrderStatus(List<TradeVO> tradeVOList) {
        List<ThirdPlatformTradeUpdateStateDTO> tradeUpdateStates = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tradeVOList)) {
            // 按购买用户id分类
            Map<String, List<TradeVO>> map = new HashMap<String, List<TradeVO>>();
            tradeVOList.stream()
                    .filter(tradeVO -> Objects.nonNull(tradeVO.getThirdPlatformType()) &&
                            ThirdPlatformType.LINKED_MALL.equals(tradeVO.getThirdPlatformType())
                            && CollectionUtils.isNotEmpty(tradeVO.getThirdPlatformOrderIds()))
                    .forEach(tradeVO -> {
                        if(Objects.nonNull(tradeVO.getBuyer()) && StringUtils.isNotBlank(tradeVO.getBuyer().getId())) {
                            String customerId = tradeVO.getBuyer().getId();
                            if (map.containsKey(customerId)) {
                                map.get(customerId).add(tradeVO);
                            } else {
                                List<TradeVO> list = new ArrayList<>();
                                list.add(tradeVO);
                                map.put(customerId, list);
                            }
                        }
                    });

            map.forEach((customerId, tradeVOs) -> {
                List<String> lmOrderIds =
                        tradeVOs.stream().map(t -> t.getThirdPlatformOrderIds().get(0)).collect(Collectors.toList());
                Map<String, TradeVO> tradeVOMap =
                        tradeVOs.stream().collect(Collectors.toMap(t -> t.getThirdPlatformOrderIds().get(0), Function.identity(), (key1, key2) -> key2));

                // 查询linkedmall订单详情
                SbcOrderListQueryResponse response =
                        linkedMallOrderQueryProvider.queryOrderDetail(SbcOrderListQueryRequest.builder()
                                .lmOrderList(lmOrderIds).bizUid(customerId).allFlag(Boolean.TRUE).build()).getContext();
                if (Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getLmOrderListItems())) {
                    response.getLmOrderListItems().forEach(lmOrderListItem -> {
                        Integer orderStatus = lmOrderListItem.getOrderStatus(); // linkedMall 订单状态
                        Integer logisticsStatus = lmOrderListItem.getLogisticsStatus(); // linkedMall 物流状态
                        String lmOrderId = Objects.toString(lmOrderListItem.getLmOrderId());
                        if(tradeVOMap.containsKey(lmOrderId)) {
                            this.fillTradeStatusByLinkedMallOrderStatus(tradeUpdateStates, tradeVOMap.get(lmOrderId), orderStatus, logisticsStatus);
                        }
                    });
                }

            });
        }
        return tradeUpdateStates;
    }

    // 根据linkedmall订单状态 填充 商城订单状态
    private void fillTradeStatusByLinkedMallOrderStatus(List<ThirdPlatformTradeUpdateStateDTO> tradeUpdateStates, TradeVO tradeVO, Integer orderStatus,
                                                        Integer logisticsStatus) {
        /*
        "orderStatus":"12=待支付，2=已支付，4=已退款关闭，6=交易成功，8=被淘宝关闭 ",
        "logisticsStatus":" 1=未发货 -> 等待卖家发货 2=已发货 -> 等待买家确认收货 3=已收货 -> 交易成功 4=已经退货 -> 交易失败 5=部分收货 -> 交易成功 6=部分发货中
        8=还未创建物流订单",
         */
        FlowState flowState = null;
        DeliverStatus deliverStatus = null;
        if (Integer.valueOf(6).equals(orderStatus) || Integer.valueOf(3).equals(logisticsStatus)) {
            flowState = FlowState.COMPLETED;
        } else if (Integer.valueOf(4).equals(orderStatus)) {
            flowState = FlowState.VOID;
        } else if (Integer.valueOf(8).equals(orderStatus)) {
            flowState = FlowState.VOID;
        } else if (Integer.valueOf(12).equals(orderStatus)) {
            flowState = FlowState.AUDIT;
        } else if (Integer.valueOf(2).equals(orderStatus) && (Integer.valueOf(2).equals(logisticsStatus) ||
                Integer.valueOf(5).equals(logisticsStatus) || Integer.valueOf(6).equals(logisticsStatus))) {
            flowState = FlowState.DELIVERED;
        } else if (Integer.valueOf(2).equals(orderStatus) &&
                (Integer.valueOf(1).equals(logisticsStatus) || Integer.valueOf(8).equals(logisticsStatus))) {
            flowState = FlowState.AUDIT;
        } else if (Integer.valueOf(4).equals(logisticsStatus)) {
            flowState = FlowState.VOID;
        }

        if (Integer.valueOf(1).equals(logisticsStatus) ||
                (Integer.valueOf(8).equals(logisticsStatus) && Integer.valueOf(12).equals(orderStatus))) {
            deliverStatus = DeliverStatus.NOT_YET_SHIPPED;
        } else if (Integer.valueOf(2).equals(logisticsStatus) || Integer.valueOf(3).equals(logisticsStatus)) {
            deliverStatus = DeliverStatus.SHIPPED;
        } else if (Integer.valueOf(5).equals(logisticsStatus) || Integer.valueOf(6).equals(logisticsStatus)) {
            deliverStatus = DeliverStatus.PART_SHIPPED;
        } else if (Integer.valueOf(4).equals(logisticsStatus) || Integer.valueOf(4).equals(orderStatus) ||
                (Integer.valueOf(8).equals(logisticsStatus) && Integer.valueOf(8).equals(orderStatus))) {
            deliverStatus = DeliverStatus.VOID;
        }

        boolean updateFlag = false;
        if (Objects.nonNull(flowState) && !flowState.equals(tradeVO.getTradeState().getFlowState())) {
            tradeVO.getTradeState().setFlowState(flowState);
            updateFlag = true;
        }
        if (Objects.nonNull(deliverStatus) && !deliverStatus.equals(tradeVO.getTradeState().getDeliverStatus())) {
            tradeVO.getTradeState().setDeliverStatus(deliverStatus);
            updateFlag = true;
        }

        if(PayState.UNCONFIRMED.equals(tradeVO.getTradeState().getPayState()) && Integer.valueOf(2).equals(orderStatus)) {
            tradeVO.getTradeState().setPayState(PayState.PAID);
            updateFlag = true;
        }

        if(PayState.UNCONFIRMED.equals(tradeVO.getTradeState().getPayState()) && Integer.valueOf(12).equals(orderStatus)) {
            tradeVO.getTradeState().setPayState(PayState.NOT_PAID);
            updateFlag = true;
        }

        if (updateFlag) {
            ThirdPlatformTradeUpdateStateDTO tradeUpdateStateDTO = new ThirdPlatformTradeUpdateStateDTO();
            tradeUpdateStateDTO.setId(tradeVO.getId());
            tradeUpdateStateDTO.setParentId(tradeVO.getParentId());
            tradeUpdateStateDTO.setTradeId(tradeVO.getTradeId());
            tradeUpdateStateDTO.setFlowState(tradeVO.getTradeState().getFlowState());
            tradeUpdateStateDTO.setDeliverStatus(tradeVO.getTradeState().getDeliverStatus());
            tradeUpdateStateDTO.setPayState(tradeVO.getTradeState().getPayState());
            tradeUpdateStates.add(tradeUpdateStateDTO);
        }
    }
}
