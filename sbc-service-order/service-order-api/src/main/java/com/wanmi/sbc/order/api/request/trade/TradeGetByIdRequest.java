package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeGetByIdRequest implements Serializable {

    private static final long serialVersionUID = -4153498256754887224L;
    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 是否需要查询linkedmall子订单
     */
    @ApiModelProperty(value = "是否需要查询linkedmall子订单")
    private Boolean needLmOrder;

    public TradeGetByIdRequest(String tid) {
        this.tid = tid;
    }
}
