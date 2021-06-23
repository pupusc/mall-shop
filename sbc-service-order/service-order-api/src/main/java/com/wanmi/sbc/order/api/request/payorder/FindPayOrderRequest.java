package com.wanmi.sbc.order.api.request.payorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FindPayOrderRequest  implements Serializable {

    @ApiModelProperty(value = "请求参数value")
    String value;
}