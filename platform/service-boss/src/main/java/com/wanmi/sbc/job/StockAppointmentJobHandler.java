package com.wanmi.sbc.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.OrderProcessType;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoListRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoListResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentRecordVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsInfoVO;
import com.wanmi.sbc.mq.MessageSendProducer;
import com.wanmi.sbc.order.api.provider.appointmentrecord.AppointmentRecordQueryProvider;
import com.wanmi.sbc.order.api.provider.stockAppointment.StockAppointmentProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordPageCriteriaRequest;
import com.wanmi.sbc.order.api.request.stockAppointment.AppointmentRequest;
import com.wanmi.sbc.order.api.request.stockAppointment.StockAppointmentRequest;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordListResponse;
import com.wanmi.sbc.order.bean.dto.AppointmentQueryDTO;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预约开售通知通知通知定时任务
 */
@Component
@Slf4j
@JobHandler(value = "stockAppointmentJobHandler")
public class StockAppointmentJobHandler extends IJobHandler {
    @Autowired
    private MessageSendProducer messageSendProducer;

    @Autowired
    private StockAppointmentProvider appointmentProvider;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;


    @Override
    public ReturnT<String> execute(String s) throws Exception {

        // 获取已经订阅的未开始的活动
        AppointmentRequest context = appointmentProvider.finAll().getContext();

        if (Objects.isNull(context) || CollectionUtils.isEmpty(context.getAppointmentList())) {
            log.info("=====暂无预约开售活动通知======");
            return SUCCESS;
        }
        List<StockAppointmentRequest> appointmentList = context.getAppointmentList();
        List<EsGoodsInfoVO> esGoodsInfoVOS = initGoods(appointmentList);
        esGoodsInfoVOS.stream().filter(item->item.getGoodsInfo().getStock()>0).forEach(item->{
            appointmentList.stream().filter(app->app.getGoodsInfo().equals(item.getGoodsInfo().getGoodsInfoId())).forEach(app->{
                try {
                    Map<String, Object> map = new HashMap<>(4);
                    map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
                    map.put("node", OrderProcessType.APPOINTMENT_SALE.toValue());
                    map.put("id", item.getId());
                    map.put("skuId", item.getGoodsInfo());
                    List<String> params = Lists.newArrayList(item.getGoodsInfo().getGoodsInfoName());
                    String customerId = app.getCustomer();
                    this.sendMessage(NodeType.ORDER_PROGRESS_RATE, OrderProcessType.APPOINTMENT_SALE,
                            params, map, customerId, app.getAccount());
                    appointmentProvider.delete(app.getId());
                } catch (Exception e) {
                    log.error("消息处理失败:" + JSON.toJSONString(item), e);
                }
            });
        });
        return SUCCESS;
    }

    public List<EsGoodsInfoVO> initGoods(List<StockAppointmentRequest> appointmentList){
        List<String> ids=new ArrayList<>();
        appointmentList.forEach(item->{
            ids.add(item.getGoodsInfo());
        });
        EsGoodsInfoListRequest request=new EsGoodsInfoListRequest();
        request.setGoodsInfoIds(ids);
        EsGoodsInfoListResponse esGoodsInfoListResponse = esGoodsInfoElasticQueryProvider.listByIds(request).getContext();
        List<EsGoodsInfoVO> goodsInfos = esGoodsInfoListResponse.getGoodsInfos();
        return goodsInfos;
    }

    /**
     * 发送消息
     *
     * @param nodeType
     * @param nodeCode
     * @param params
     * @param customerId
     */
    private void sendMessage(NodeType nodeType, OrderProcessType nodeCode, List<String> params, Map<String, Object> routeParam, String customerId,String mobile) {
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeCode(nodeCode.getType());
        messageMQRequest.setNodeType(nodeType.toValue());
        messageMQRequest.setParams(params);
        messageMQRequest.setRouteParam(routeParam);
        messageMQRequest.setCustomerId(customerId);
        messageMQRequest.setMobile(mobile);
        messageSendProducer.sendMessage(messageMQRequest);
    }
}
