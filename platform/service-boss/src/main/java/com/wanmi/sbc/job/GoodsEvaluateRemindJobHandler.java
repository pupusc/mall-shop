package com.wanmi.sbc.job;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.OrderProcessType;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.goods.api.provider.goodsevaluate.GoodsEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluateListRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateListRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import com.wanmi.sbc.goods.bean.vo.StoreTobeEvaluateVO;
import com.wanmi.sbc.mq.MessageSendProducer;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeListAllRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品评价、服务评价通知定时任务
 */
@Component
@Slf4j
@JobHandler(value="goodsEvaluateRemindJobHandler")
public class GoodsEvaluateRemindJobHandler extends IJobHandler {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private GoodsEvaluateQueryProvider goodsEvaluateQueryProvider;

    @Autowired
    private MessageSendProducer messageSendProducer;

    @Autowired
    private StoreTobeEvaluateQueryProvider storeTobeEvaluateQueryProvider;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        String checkDate = DateUtil.format(LocalDateTime.now().minusDays(7), "YYYY-MM-dd");
        TradeListAllRequest tradeListAllRequest = TradeListAllRequest.builder()
                .tradeQueryDTO(TradeQueryDTO.builder()
                        .completionBeginTime(checkDate)
                        .completionEndTime(checkDate)
                        .tradeState(TradeStateDTO.builder().flowState(FlowState.COMPLETED).build())
                        .build()).build();
        List<TradeVO> tradeVOList = tradeQueryProvider.listAll(tradeListAllRequest).getContext().getTradeVOList();

        if(CollectionUtils.isNotEmpty(tradeVOList)){
            this.handleGoodsEvaluate(tradeVOList);
            this.handleStoreEvaluate(tradeVOList);
        }

        return SUCCESS;
    }

    /**
     * 商品评价处理
     * @param tradeVOList
     */
    private void handleGoodsEvaluate(List<TradeVO> tradeVOList){
        log.info("商品评价提醒定时处理");
        List<String> tradeIds = tradeVOList.stream().map(TradeVO::getId).collect(Collectors.toList());
        GoodsEvaluateListRequest goodsEvaluateListRequest = GoodsEvaluateListRequest.builder().orderNos(tradeIds).build();
        List<GoodsEvaluateVO> goodsEvaluateVOList = goodsEvaluateQueryProvider
                .list(goodsEvaluateListRequest)
                .getContext()
                .getGoodsEvaluateVOList();
        List<TradeVO> list;
        if(CollectionUtils.isNotEmpty(goodsEvaluateVOList)){
            list = tradeVOList.stream().filter(tradeVO ->
                    !goodsEvaluateVOList.stream()
                            .map(GoodsEvaluateVO::getOrderNo)
                            .collect(Collectors.toList())
                            .contains(tradeVO.getId()))
                    .collect(Collectors.toList());
        }else{
            list = tradeVOList;
        }
        for (TradeVO trade : list) {
            List<TradeItemVO> tradeItems = trade.getTradeItems();
            if(CollectionUtils.isNotEmpty(tradeItems)){
                tradeItems.stream().forEach(tradeItemVO -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
                    map.put("node", OrderProcessType.GOODS_EVALUATION.toValue());
                    map.put("storeId", trade.getSupplier().getStoreId());
                    map.put("tid",trade.getId());
                    map.put("skuId", tradeItemVO.getSkuId());
                    List<String> params = Lists.newArrayList(tradeItemVO.getSkuName());
                    String customerId = trade.getBuyer().getId();
                    this.sendMessage(NodeType.ORDER_PROGRESS_RATE, OrderProcessType.GOODS_EVALUATION,
                            params, map, customerId, tradeItemVO.getPic(), trade.getBuyer().getAccount());
                });
            }
        }
        log.info("商品评价提醒消息数：{}", list.size());
    }

    /**
     * 服务评价处理
     * @param tradeVOList
     */
    private void handleStoreEvaluate(List<TradeVO> tradeVOList){
        log.info("服务评价提醒定时处理");
        List<String> tradeIds = tradeVOList.stream().map(TradeVO::getId).collect(Collectors.toList());
        StoreTobeEvaluateListRequest listRequest = StoreTobeEvaluateListRequest.builder().orderNos(tradeIds).build();
        List<StoreTobeEvaluateVO> storeTobeEvaluateVOList = storeTobeEvaluateQueryProvider
                .list(listRequest).getContext().getStoreTobeEvaluateVOList();
        List<TradeVO> list;
        if(CollectionUtils.isNotEmpty(storeTobeEvaluateVOList)){
            list = tradeVOList.stream().filter(tradeVO ->
                    storeTobeEvaluateVOList.stream()
                            .map(StoreTobeEvaluateVO::getOrderNo)
                            .collect(Collectors.toList())
                            .contains(tradeVO.getId()))
                    .collect(Collectors.toList());
        }else{
            list = tradeVOList;
        }

        for (TradeVO trade : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
            map.put("node", OrderProcessType.SERVICE_EVALUATION.toValue());
            map.put("storeId", trade.getSupplier().getStoreId());
            map.put("tid",trade.getId());
            map.put("skuId", "-1");
            List<String> params = Lists.newArrayList(trade.getTradeItems().get(0).getSkuName());
            String pic = trade.getTradeItems().get(0).getPic();
            String customerId = trade.getBuyer().getId();
            this.sendMessage(NodeType.ORDER_PROGRESS_RATE, OrderProcessType.SERVICE_EVALUATION,
                    params, map, customerId, pic, trade.getBuyer().getAccount());
        }
        log.info("服务评价提醒消息数：{}", list.size());
    }

    /**
     * 发送消息
     * @param nodeType
     * @param nodeCode
     * @param params
     * @param customerId
     */
    private void sendMessage(NodeType nodeType, OrderProcessType nodeCode, List<String> params, Map<String, Object> routeParam, String customerId, String pic, String mobile){
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeCode(nodeCode.getType());
        messageMQRequest.setNodeType(nodeType.toValue());
        messageMQRequest.setParams(params);
        messageMQRequest.setRouteParam(routeParam);
        messageMQRequest.setCustomerId(customerId);
        messageMQRequest.setPic(pic);
        messageMQRequest.setMobile(mobile);

        messageSendProducer.sendMessage(messageMQRequest);
    }
}
