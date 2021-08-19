package com.wanmi.sbc.customer.api.request.employee;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.customer.bean.enums.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@ApiModel
@Data
public class EmployeeAddRequest implements Serializable {


    private static final long serialVersionUID = 1934803974883175791L;

    /**
     * 员工名称
     */
    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String employeeMobile;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private List<Long> roleIdList;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 账户名称
     */
    @ApiModelProperty(value = "账户名称")
    private String accountName;

    /**
     * 员工账号
     */
    @ApiModelProperty(value = "员工账号")
    private String accountPassword;

    /**
     * 是否是员工 0 是 1否
     */
    @ApiModelProperty(value = "是否是员工(0 是 1否)", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isEmployee;

    /**
     * 是否发生短信
     */
    @ApiModelProperty(value = "是否发送短信")
    private Boolean isSendPassword;

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
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 工号
     */
    @ApiModelProperty(value = "工号")
    private String jobNo;

    /**
     * 职位
     */
    @ApiModelProperty(value = "职位")
    private String position;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate birthday;

    /**
     * 性别，0：保密，1：男，2：女
     */
    @ApiModelProperty(value = "性别，0：保密，1：男，2：女")
    private GenderType sex;


    /**
     * 归属部门
     */
    @ApiModelProperty(value = "归属部门")
    private List<String> departmentIdList;

}
