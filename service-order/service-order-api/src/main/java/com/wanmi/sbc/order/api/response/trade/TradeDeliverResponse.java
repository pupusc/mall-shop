package com.wanmi.sbc.order.api.response.trade;

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
public class TradeDeliverResponse implements Serializable {

    /**
     * 物流编号
     */
    @ApiModelProperty(value = "物流编号")
    private String deliverId;

}
