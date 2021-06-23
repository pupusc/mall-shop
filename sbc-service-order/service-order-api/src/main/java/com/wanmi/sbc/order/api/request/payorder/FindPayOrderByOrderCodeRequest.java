package com.wanmi.sbc.order.api.request.payorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class FindPayOrderByOrderCodeRequest  implements Serializable  {

    @ApiModelProperty(value = "订单编号")
    private String value;
}
