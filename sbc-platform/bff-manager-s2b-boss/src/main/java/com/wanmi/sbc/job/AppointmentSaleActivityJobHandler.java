package com.wanmi.sbc.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.OrderProcessType;
import com.wanmi.sbc.order.api.provider.appointmentrecord.AppointmentRecordQueryProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordPageCriteriaRequest;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordListResponse;
import com.wanmi.sbc.order.bean.dto.AppointmentQueryDTO;
import com.wanmi.sbc.goods.bean.vo.AppointmentRecordVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsInfoVO;
import com.wanmi.sbc.mq.MessageSendProducer;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 预约开售通知通知通知定时任务
 */
@Component
@Slf4j
@JobHandler(value = "appointmentSaleActivityJobHandler")
public class AppointmentSaleActivityJobHandler extends IJobHandler {
    @Autowired
    private MessageSendProducer messageSendProducer;


    @Autowired
    private AppointmentRecordQueryProvider appointmentRecordQueryProvider;


    @Override
    public ReturnT<String> execute(String s) throws Exception {

        // 获取已经订阅的未开始的活动
        AppointmentRecordListResponse response = appointmentRecordQueryProvider.listSubscribeNotStartActivity(
                AppointmentRecordPageCriteriaRequest.builder().appointmentQueryDTO
                        (AppointmentQueryDTO.builder().snapUpStartTimeBegin(LocalDateTime.now()).build()).build()).getContext();
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getAppointmentRecordVOList())) {
            log.info("=====暂无预约开售活动通知======");
            return SUCCESS;
        }
        List<AppointmentRecordVO> appointmentRecordList = response.getAppointmentRecordVOList().stream().filter(appointmentRecord -> judgeHalfHourActivity(appointmentRecord.getAppointmentSaleInfo().getSnapUpStartTime()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(appointmentRecordList)) {
            return SUCCESS;
        }
        response.getAppointmentRecordVOList().forEach(appointmentRecord -> {
            try {
                Map<String, Object> map = new HashMap<>(4);
                map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
                map.put("node", OrderProcessType.APPOINTMENT_SALE.toValue());
                map.put("id", appointmentRecord.getId());
                map.put("skuId", appointmentRecord.getGoodsInfoId());
                @NotNull AppointmentSaleGoodsInfoVO appointmentSaleGoodsInfo = appointmentRecord.getAppointmentSaleGoodsInfo();
                List<String> params = Lists.newArrayList(appointmentSaleGoodsInfo.getSkuName());
                String customerId = appointmentRecord.getBuyerId();
                this.sendMessage(NodeType.ORDER_PROGRESS_RATE, OrderProcessType.APPOINTMENT_SALE,
                        params, map, customerId, appointmentSaleGoodsInfo.getSkuPic(), appointmentRecord.getCustomer().getAccount());
            } catch (Exception e) {
                log.error("消息处理失败:" + JSON.toJSONString(appointmentRecord), e);
            }
        });

        return SUCCESS;
    }


    public boolean judgeHalfHourActivity(LocalDateTime snapUpStartTime) {
        if (Duration.between(snapUpStartTime, LocalDateTime.now()).toMinutes() < 30) {
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
