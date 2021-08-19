package com.wanmi.sbc.order.trade.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliverCalendar {

    /**
     * 发货日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate deliverDate;

    /**
     * 配送状态
     */
    private CycleDeliverStatus cycleDeliverStatus;


    /**
     * 订单推送次数--默认0，推送三次再推送不过去该订单推送失败
     */
    private Integer pushCount=0;


    /**
     * 发货单号   作废订单时用到
     */
    private String deliverId;

    /**
     * erp订单编号
     */
    private String erpTradeCode;

}
