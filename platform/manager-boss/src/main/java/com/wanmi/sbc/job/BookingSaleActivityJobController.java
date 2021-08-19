package com.wanmi.sbc.job;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.OrderProcessType;
import com.wanmi.sbc.mq.MessageSendProducer;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeListAllRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预售订单尾款支付通知定时任务
 */
@Component
@Slf4j
@JobHandler(value = "bookingSaleActivityJobController")
public class BookingSaleActivityJobController extends IJobHandler {
    @Autowired
    private MessageSendProducer messageSendProducer;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        TradeListAllRequest tradeListAllRequest = TradeListAllRequest.builder()
                .tradeQueryDTO(TradeQueryDTO.builder()
                        .bookingTailTime(LocalDateTime.now())
                        .tradeState(TradeStateDTO.builder().payState(PayState.PAID_EARNEST).build())
                        .isBookingSaleGoods(true)
                        .bookingType(BookingType.EARNEST_MONEY)
                        .build()).build();
        List<TradeVO> tradeVOS = tradeQueryProvider.listAll(tradeListAllRequest).getContext().getTradeVOList();
        if (CollectionUtils.isEmpty(tradeVOS)) {
            log.warn("=========暂无需要通知的预售活动信息===========");
            return SUCCESS;
        }

        List<TradeVO> tradeList = tradeVOS.stream().filter(a -> judgeHalfHourActivity(a.getTradeState().getTailStartTime()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tradeList)) {
            return SUCCESS;
        }
        tradeList.forEach(a -> {
            try {
                Map<String, Object> map = new HashMap<>(4);
                map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
                map.put("node", OrderProcessType.BOOKING_SALE.toValue());
                map.put("id", a.getId());
                map.put("skuId", a.getTradeItems().get(0).getSkuId());
                List<String> params = Lists.newArrayList(a.getTradeItems().get(0).getSkuName());
                String customerId = a.getBuyer().getId();
                this.sendMessage(NodeType.ORDER_PROGRESS_RATE, OrderProcessType.BOOKING_SALE,
                        params, map, customerId, a.getTradeItems().get(0).getPic(), a.getTailNoticeMobile());
            } catch (Exception e) {
                log.error("消息处理失败,订单号:" + a.getId(), e);
            }
        });

        return SUCCESS;
    }


    public boolean judgeHalfHourActivity(LocalDateTime bookingTailStartTime) {
        if (Duration.between(bookingTailStartTime, LocalDateTime.now()).toMinutes() < 30) {
            return true;
        }
        return false;
    }


    /**
     * 发送消息
     *
     * @param nodeType
     * @param nodeCode
     * @param params
     * @param customerId
     */
    private void sendMessage(NodeType nodeType, OrderProcessType nodeCode, List<String> params, Map<String, Object> routeParam, String customerId, String pic, String mobile) {
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
