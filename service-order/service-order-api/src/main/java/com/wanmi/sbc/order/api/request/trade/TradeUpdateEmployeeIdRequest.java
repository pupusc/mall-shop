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
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 9:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeUpdateEmployeeIdRequest implements Serializable {

    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    @NotBlank
    private String employeeId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;
}
