package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 根据customerId删除订单商品快照请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeItemDeleteByCustomerIdRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private String customerId;

    private String terminalToken;
}
