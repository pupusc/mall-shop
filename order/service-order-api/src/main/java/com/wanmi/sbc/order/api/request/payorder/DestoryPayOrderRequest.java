package com.wanmi.sbc.order.api.request.payorder;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
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
public class DestoryPayOrderRequest  implements Serializable {

    @ApiModelProperty(value = "付款单列表")
    List<PayOrderDTO> payOrders;

    /**
     * 操作员
     */
    @ApiModelProperty(value = "操作员")
    private Operator operator;
}
