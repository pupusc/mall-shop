package com.wanmi.sbc.order.trade.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.order.redis.RedisService;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.follow.request.TradeSettingRequest;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.setting.api.provider.AuditProvider;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.request.TradeConfigsModifyRequest;
import com.wanmi.sbc.setting.api.response.OrderAutoReceiveConfigGetResponse;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.bean.dto.TradeConfigDTO;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 订单设置服务
 */
@Service
@Slf4j
@Transactional(readOnly = true, timeout = 10)
public class TradeSettingService {
    @Autowired
    private AuditProvider auditProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private RedisService redisService;

    private static final String ORDER_AUTO_RECEIVE_KEY = "ORDER_AUTO_RECEIVE_KEY";

    /**
     * 查询订单设置配置
     *
     * @return List<Config>
     */
    public List<ConfigVO> queryTradeConfigs() {
        return auditQueryProvider.listTradeConfig().getContext().getConfigVOList();
    }

    /**
     * 修改订单配置
     *
     * @param tradeSettingRequests tradeSettingRequests
     */
    @Transactional
    public void updateTradeConfigs(List<TradeSettingRequest> tradeSettingRequests) {
        if (CollectionUtils.isEmpty(tradeSettingRequests)) {
            log.warn("tradeSettingRequests params is empty, can't modify");
            return;
        }

        List<TradeConfigDTO> tradeConfigDTOS = new ArrayList<>();
        KsBeanUtil.copyList(tradeSettingRequests, tradeConfigDTOS);

        TradeConfigsModifyRequest request = new TradeConfigsModifyRequest();
        request.setTradeConfigDTOList(tradeConfigDTOS);

        auditProvider.modifyTradeConfigs(request);
    }

    /**
     * 根据type查询config
     *
     * @param configType configType
     * @return config
     */
    public TradeConfigGetByTypeResponse queryTradeConfigByType(ConfigType configType) {
//        return configRepository.findByConfigTypeAndDelFlag(configType.toValue(), DeleteFlag.NO);
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(configType);

        return auditQueryProvider.getTradeConfigByType(request).getContext();
    }

    /**
     * 订单代发货自动收货
     */
    public void orderAutoReceive(Integer pageNum, Integer pageSize, String starTime, Integer type) {
        log.info("***********自动确认收货 定时任务开始执行 begin******************");
        long beginTime = System.currentTimeMillis();
        //查询符合订单

        try {
            pageSize = pageSize == null || pageSize <= 0 ? 1000 : pageSize;
            int localPageNum = pageNum < 0 ? 0 : pageNum;
            OrderAutoReceiveConfigGetResponse config =auditQueryProvider.getOrderAutoReceiveConfig().getContext();
            Integer day = Integer.valueOf(JSON.parseObject(config.getContext()).get("day").toString());
            LocalDateTime endDate = LocalDateTime.now().minusDays(day);

            List<Trade> tradeList = tradeService.queryTradeByDate(endDate, FlowState.DELIVERED, localPageNum, pageSize, starTime, type);
            log.info("自动确认收货 第 {} 页 获取的数据量为 {}", localPageNum, tradeList.size());
            if(!CollectionUtils.isEmpty(tradeList)){
                //自动确认收货排除有赞商城老订单
                for (Trade deliveredTrade : tradeList) {
                    if (!Objects.isNull(deliveredTrade.getYzTid())) {
                        continue;
                    }
                   try {
                       tradeService.confirmReceive(deliveredTrade.getId(), Operator.builder().platform(Platform.PLATFORM)
                               .name("system").account("system").platform(Platform.PLATFORM).build());
                   } catch (Exception ex) {
                       log.error("orderAutoReceive schedule tid:{} error", deliveredTrade.getId(), ex);
                   }
                }
            }
            log.info("自动确认收货成功");
        } catch (Exception ex) {
            log.error("orderAutoReceive schedule error", ex);
            throw new SbcRuntimeException("K-050129");
        }
        log.info("***********自动确认收货 定时任务开始执行 耗时:{} end******************", System.currentTimeMillis() - beginTime);
    }

//    /**
//     * 订单代发货自动收货
//     */
//    public void orderAutoReceive() {
//        log.info("***********自动确认收货 定时任务开始执行 begin******************");
//        long beginTime = System.currentTimeMillis();
//        //查询符合订单
//        //批量扭转状态
////        Config config = configRepository.findByConfigTypeAndDelFlag(ConfigType.ORDER_SETTING_AUTO_RECEIVE.toString(), DeleteFlag.NO);
//        OrderAutoReceiveConfigGetResponse config =auditQueryProvider.getOrderAutoReceiveConfig().getContext();
//
//        int pageSize = 1000;
//        try {
//            Integer day = Integer.valueOf(JSON.parseObject(config.getContext()).get("day").toString());
//            LocalDateTime endDate = LocalDateTime.now().minusDays(day);
//
//            long total = tradeService.countTradeByDate(endDate, FlowState.DELIVERED);
//
//            log.info("自动确认收货 超过{} 天的数据 总数量为{} ", day, total);
//            int pageNum = 0;
//            boolean loopFlag = true;
//            //超过1000条批量处理
//            while(loopFlag && total > 0){
//
//                List<Trade> tradeList = tradeService.queryTradeByDate(endDate, FlowState.DELIVERED, pageNum, pageSize);
//                log.info("自动确认收货 第 {} 页 获取的数据量为 {}", (pageNum + 1), tradeList.size());
//                if(tradeList != null && !tradeList.isEmpty()){
//                    //自动确认收货排除有赞商城老订单
//                    tradeList.stream().filter(v -> Objects.isNull(v.getYzTid())).forEach(trade -> tradeService.confirmReceive(trade.getId(), Operator.builder().platform(Platform.PLATFORM)
//                            .name("system").account("system").platform(Platform.PLATFORM).build()));
//                    if(tradeList.size() == pageSize){
//                        pageNum++;
//                        continue;
//                    }
//                }
//                loopFlag = false;
//            }
//
//            log.info("自动确认收货成功");
//
//
//        } catch (Exception ex) {
//            log.error("orderAutoReceive schedule error", ex);
//            throw new SbcRuntimeException("K-050129");
//        }
//        log.info("***********自动确认收货 定时任务开始执行 耗时:{} end******************", System.currentTimeMillis() - beginTime);
//    }

    /**
     * 退单自动审核
     */
    @Transactional
    public void returnOrderAutoAudit(Integer day) {
        //查询符合订单
        //批量扭转状态
        val pageSize = 1000;
        try {
            LocalDateTime endDate = LocalDateTime.now().minusDays(day);
            int total = returnOrderService.countReturnOrderByEndDate(endDate, ReturnFlowState.INIT);
            log.info("退单自动审核分页订单数: " + total);
            //超过1000条批量处理
            if (total > pageSize) {
                int pageNum = calPage(total, pageSize);
                for (int i = 0; i < pageNum; i++) {
                    returnOrderService.queryReturnOrderByEndDate(endDate, i * pageSize, i + pageSize + pageSize
                                    , ReturnFlowState.INIT)
                            .forEach(returnOrder ->{
                                try{
                                    processReturnAutoAction(ReturnFlowState.INIT, returnOrder);
                                } catch (SbcRuntimeException brt){
                                    log.error("rid " +  returnOrder.getTid() + "异常： "+ brt.getMessage());
                                }
                            } );
                }
            } else {
                List<ReturnOrder> returnOrders = returnOrderService.queryReturnOrderByEndDate(endDate, 0, total, ReturnFlowState.INIT);
                returnOrders.forEach(returnOrder -> {
                    log.info("执行的退单号: " + returnOrder.getId());
                    try{
                        processReturnAutoAction(ReturnFlowState.INIT, returnOrder);
                    } catch (SbcRuntimeException brt){
                        log.error("rid " +  returnOrder.getTid() + "异常： "+ brt.getMessage());
                    }
                });
            }
            log.info("退单自动审核成功");
        } catch (Exception ex) {
            log.error("returnOrderAutoAudit schedule error");
            ex.printStackTrace();
            throw new SbcRuntimeException("K-050005");
        }
    }



    /**
     * 退单自动确认收货 由于es索引的问题，用mongodb 分页查询，考虑把订单，退单从es中移除
     * @param day day
     */
    @Transactional
    public void returnOrderAutoReceive(Integer day) {
        val pageSize = 1000;
        LocalDateTime endDate = LocalDateTime.now().minusDays(day);
        int total = returnOrderService.countReturnOrderByEndDate(endDate, ReturnFlowState.DELIVERED);
        log.info("退单去人收货分页订单数: " + total);
        //超过1000条批量处理
        if (total > pageSize) {
            int pageNum = calPage(total, pageSize);
            for (int i = 0; i < pageNum; i++) {
                returnOrderService.queryReturnOrderByEndDate(endDate, i * pageSize, i * pageSize + pageSize, ReturnFlowState.DELIVERED)
                        .forEach(returnOrder -> {
                            log.info("执行的退单号: " + returnOrder.getId());
                            try{
                                processReturnAutoAction(ReturnFlowState.DELIVERED, returnOrder);
                            } catch (SbcRuntimeException brt){
                                log.error("rid " +  returnOrder.getTid() + "异常： "+ brt.getMessage());
                            }
                        });
            }
        } else {
            List<ReturnOrder> returnOrders = returnOrderService.queryReturnOrderByEndDate(endDate, 0, total, ReturnFlowState.DELIVERED);

            returnOrders.forEach(returnOrder
                    -> {
                log.info("执行的退单号: " + returnOrder.getId());
                try{
                    processReturnAutoAction(ReturnFlowState.DELIVERED, returnOrder);
                } catch (SbcRuntimeException brt){
                    log.error("rid " +  returnOrder.getTid() + "异常： "+ brt.getMessage());
                }
            });
        }

        log.info("退单收货审核成功");
    }



    /**
     * 退单自动处理任务
     *
     * @param returnFlowState returnFlowState
     * @param returnOrder     returnOrder
     */
    private void processReturnAutoAction(ReturnFlowState returnFlowState, ReturnOrder returnOrder) {
        if (ReturnFlowState.DELIVERED.equals(returnFlowState)) {
            returnOrderService.receive(returnOrder.getId(),
                    Operator.builder().name("system").account("system").platform(Platform.PLATFORM).build());
        } else if (ReturnFlowState.INIT.equals(returnFlowState)) {
            returnOrderService.audit(returnOrder.getId(),
                    Operator.builder().name("system").account("system").platform(Platform.PLATFORM).build(), null);
        }
    }

    /**
     * 计算页码
     *
     * @param count
     * @param size
     * @return
     */
    private int calPage(int count, int size) {
        int page = count / size;
        if (count % size == 0) {
            return page;
        } else {
            return page + 1;
        }
    }

}
