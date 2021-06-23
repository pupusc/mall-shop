package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 员工请求参数
 * Created by zhangjin on 2017/4/18.
 */
@ApiModel
@Data
public class EmployeeRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -824463572817809811L;

    @ApiModelProperty(value = "员工编号")
    private String employeeId;

    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "员工姓名")
    private String userName;

    /**
     * 用户手机
     */
    @ApiModelProperty(value = "用户手机")
    private String userPhone;

    /**
     * 账户名称
     */
    @ApiModelProperty(value = "账户名称")
    private String accountName;

    /**
     * 是否是业务员
     */
    @ApiModelProperty(value = "是否是员工(0 是 1否)")
    private Integer isEmployee;

    /**
     * 账户状态
     */
    @ApiModelProperty(value = "账户状态")
    private AccountState accountState;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private String roleId;


    @ApiModelProperty(value = "员工编号列表")
    private List<String> employeeIds;

    /**
     * 账号类型 0 b2b账号 1 s2b平台端账号 2 s2b商家端账号
     */
    @ApiModelProperty(value = "账号类型")
    private AccountType accountType;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 员工禁用原因
     */
    @ApiModelProperty(value = "员工禁用原因")
    private String accountDisableReason;

    /**
     * 是否主账号
     */
    @ApiModelProperty(value = "是否主账号", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isMasterAccount;
}
