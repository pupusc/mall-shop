package com.wanmi.sbc.order.api.request.payorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FindPayOrderByPayOrderIdsRequest  implements Serializable {

    @ApiModelProperty(value = "付款单id列表")
    List<String> payOrderIds;
}
