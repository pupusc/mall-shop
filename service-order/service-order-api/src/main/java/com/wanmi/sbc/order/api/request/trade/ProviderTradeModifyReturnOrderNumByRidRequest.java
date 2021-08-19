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
public class ProviderTradeModifyReturnOrderNumByRidRequest implements Serializable {

    /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    private String returnOrderId;

    /**
     * 退单数加减状态
     */
    @ApiModelProperty(value = "备注")
    private Boolean addFlag;

}
