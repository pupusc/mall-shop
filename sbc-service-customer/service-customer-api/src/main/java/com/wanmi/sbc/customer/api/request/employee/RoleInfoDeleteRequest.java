package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotNull;

/**
 * 会员角色-修改Request
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoDeleteRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 1985719847145231576L;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    @NotNull
    private Long roleInfoId;
}
