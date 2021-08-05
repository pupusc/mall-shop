package com.wanmi.sbc.erp.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author machaoyang
 * @className TradeQueryRequest
 * @description TODO
 * @date 2021/6/30 下午2:49
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeQueryRequest implements Serializable {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String tid;

    /**
     * 查询7天内订单或者历史订单(0:7天内订单；1：历史订单)
     */
    @ApiModelProperty(value = "订单号")
    private Integer flag;
}
