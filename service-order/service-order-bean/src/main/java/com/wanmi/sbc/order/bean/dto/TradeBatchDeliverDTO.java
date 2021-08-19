package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.dto.LogisticsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 批量发货单条数据处理
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeBatchDeliverDTO implements Serializable {

    /**
     * 子订单id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 物流信息
     */
    @ApiModelProperty(value = "物流信息")
    private LogisticsDTO logistics;

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deliverTime;

}
