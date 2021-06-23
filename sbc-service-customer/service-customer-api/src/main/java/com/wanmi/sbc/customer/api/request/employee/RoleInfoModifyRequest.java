package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 会员角色-修改Request
 */
@ApiModel
@Data
public class RoleInfoModifyRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 1985719847145231576L;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    @NotNull
    private Long roleInfoId;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    @NotNull
    private String roleName;

    /**
     * 公司编号
     */
    @ApiModelProperty(value = "公司编号")
    private Long companyInfoId;
}
