package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.ReturnLogisticsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 退货发出请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderDeliverRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    @NotBlank
    private String rid;

    /**
     * 退货物流信息
     */
    @ApiModelProperty(value = "退货物流信息")
    private ReturnLogisticsDTO logistics;

    /**
     * 操作人信息
     */
    @ApiModelProperty(value = "操作人信息")
    private Operator operator;
}
