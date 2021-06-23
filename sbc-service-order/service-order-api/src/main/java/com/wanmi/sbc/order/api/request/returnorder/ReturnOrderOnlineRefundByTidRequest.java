package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.common.base.Operator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderOnlineRefundByTidRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;


    /**
     * 退单ID
     */
    @ApiModelProperty(value = "退单Id")
    @NotBlank
    private String returnOrderCode;

    /**
     * 操作人
     */
    private Operator operator;


}
