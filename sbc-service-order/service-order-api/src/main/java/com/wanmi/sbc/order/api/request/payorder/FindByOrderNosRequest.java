package com.wanmi.sbc.order.api.request.payorder;

import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
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
public class FindByOrderNosRequest   implements Serializable {

    @ApiModelProperty(value = "订单编号s")
    List<String> orderNos;

    @ApiModelProperty(value = "支付状态")
    PayOrderStatus payOrderStatus;
}
