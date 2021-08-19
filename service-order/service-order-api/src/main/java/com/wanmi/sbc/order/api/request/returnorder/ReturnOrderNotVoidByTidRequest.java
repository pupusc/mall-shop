package com.wanmi.sbc.order.api.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 查询退单列表请求结构(不包含已作废状态以及拒绝收货的退货单与拒绝退款的退款单)
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderNotVoidByTidRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @NotBlank
    private String tid;

}
