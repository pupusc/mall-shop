package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhangjin on 2017/5/12.
 */
@ApiModel
@Data
@Builder
public class EmployeeAccountResponse implements Serializable {

    private static final long serialVersionUID = 7967405262492571018L;

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
