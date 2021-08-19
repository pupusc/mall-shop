package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class DeliverCalendarVO implements Serializable {

    private static final long serialVersionUID = 961643835358617429L;

    /**
     * 发货日期
     */
    @ApiModelProperty(value = "发货日期")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate deliverDate;

    /**
     * 配送状态
     */
    @ApiModelProperty(value = "配送状态")
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
