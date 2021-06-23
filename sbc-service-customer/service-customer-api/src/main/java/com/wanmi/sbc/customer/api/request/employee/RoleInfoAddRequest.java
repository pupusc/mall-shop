package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 会员角色-新增Request
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleInfoAddRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -2017057877111921252L;
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
