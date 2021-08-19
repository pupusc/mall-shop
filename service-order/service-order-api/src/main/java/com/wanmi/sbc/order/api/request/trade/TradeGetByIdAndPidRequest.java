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
public class TradeGetByIdAndPidRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 供应商id
     */
    private String providerId;

}
