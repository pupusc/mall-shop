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
 * 根据客户id更新退单中所有业务员请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderModifyEmployeeIdRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 业务员
     */
    @ApiModelProperty(value = "业务员")
    @NotBlank
    private String employeeId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;
}
