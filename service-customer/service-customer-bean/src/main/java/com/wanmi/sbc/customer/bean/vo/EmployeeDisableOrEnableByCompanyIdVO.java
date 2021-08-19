package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel
@Data
public class EmployeeDisableOrEnableByCompanyIdVO implements Serializable {

    private static final long serialVersionUID = 3683742388168504593L;

    @ApiModelProperty(value = "业务员id")
    private String employeeId;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String employeeName;

    /**
     * 会员电话
     */
    @ApiModelProperty(value = "会员电话")
    private String employeeMobile;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    /**
     * 0 是 1否
     */
    @ApiModelProperty(value = "是否业务员(0 是 1否)")
    private Integer isEmployee;

    /**
     * 账户名
     */
    @ApiModelProperty(value = "账户名")
    private String accountName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String accountPassword;

    /**
     * salt
     */
    @ApiModelProperty(value = "salt")
    private String employeeSaltVal;

    /**
     * 账号状态
     */
    @ApiModelProperty(value = "账号状态")
    private AccountState accountState;

    /**
     * 账号禁用原因
     */
    @ApiModelProperty(value = "账号禁用原因")
    private String accountDisableReason;

    /**
     * 第三方店铺id
     */
    @ApiModelProperty(value = "第三方店铺id")
    private String thirdId;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;

    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志")
    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updatePerson;

    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTime;

    @ApiModelProperty(value = "删除人")
    private String deletePerson;

    /**
     * 登录失败次数
     */
    @ApiModelProperty(value = "登录失败次数")
    private Integer loginErrorTime = 0;

    /**
     * 锁定时间
     */
    @ApiModelProperty(value = "锁定时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime loginLockTime;

    /**
     * 会员登录时间
     */
    @ApiModelProperty(value = "会员登录时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime loginTime;

    /**
     * 商家Id
     */
    @ApiModelProperty(value = "商家Id")
    private Long companyInfoId;

    /**
     * 是否是主账号
     */
    @ApiModelProperty(value = "是否是主账号", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isMasterAccount;

    /**
     * 账号类型 0 b2b账号 1 s2b平台端账号 2 s2b商家端账号
     */
    @ApiModelProperty(value = "账号类型")
    private AccountType accountType;
}
