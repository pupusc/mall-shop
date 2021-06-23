package com.wanmi.sbc.order.api.request.payorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class FindPayOrderByOrderCodesRequest implements Serializable  {

    @ApiModelProperty(value = "订单编号")
    private List<String> value;
}
