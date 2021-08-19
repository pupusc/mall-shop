package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EmployeeAccountByIdResponse implements Serializable {

    private static final long serialVersionUID = -775306039981028920L;

    /**
     * 账号名
     */
    @ApiModelProperty(value = "账号名")
    private String accountName;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String employeeName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 是否是主账号
     */
    @ApiModelProperty(value = "是否是主账号", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isMasterAccount;
}
