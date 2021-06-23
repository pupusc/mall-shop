package com.wanmi.sbc.erp.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 发货单查询参数
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-07 16:00
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryQueryRequest implements Serializable {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String tid;

    /**
     * 发货状态 0:未发货、发货中、发货失败，1:发货成功
     */
    private Integer delivery;
}
