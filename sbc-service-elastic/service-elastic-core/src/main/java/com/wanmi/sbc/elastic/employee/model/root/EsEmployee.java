package com.wanmi.sbc.elastic.employee.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.GenderType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Document(indexName = EsConstants.DOC_EMPLOYEE_TYPE, type = EsConstants.DOC_EMPLOYEE_TYPE)
@Data
public class EsEmployee implements Serializable {
    /**
     * 主键
     */
    @Id
    private String employeeId;

    /**
     * 会员名称
     */
    @Field(type = FieldType.Text)
    private String employeeName;

    /**
     * 会员电话
     */
    @Field(type = FieldType.Text)
    private String employeeMobile;

    /**
     * 角色id
     */
    @Field(type = FieldType.Keyword)
    private List<String> roleIds;

    /**
     * 0 是 1否
     */
    @Field(type = FieldType.Integer)
    private Integer isEmployee;

    /**
     * 账户名
     */
    @Field(type = FieldType.Text)
    private String accountName;

    /**
     * 密码
     */
    @Field(index = false,type = FieldType.Text)
    private String accountPassword;

    /**
     * salt
     */
    @Field(index = false,type = FieldType.Text)
    private String employeeSaltVal;

    /**
     * 账号状态
     */
    @Field(type = FieldType.Integer)
    private AccountState accountState;

    /**
     * 账号禁用原因
     */
    @Field(type = FieldType.Text)
    private String accountDisableReason;

    /**
     * 第三方店铺id
     */
    @Field(type = FieldType.Keyword)
    private String thirdId;

    /**
     * 会员id
     */
    @Field(type = FieldType.Keyword)
    private String customerId;

    /**
     * 删除标志
     */
    @Field(type = FieldType.Integer)
    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Field(type = FieldType.Keyword)
    private String createPerson;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @Field(type = FieldType.Keyword)
    private String updatePerson;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTime;

    @Field(type = FieldType.Keyword)
    private String deletePerson;

    /**
     * 登录失败次数
     */
    @Field(type = FieldType.Integer)
    private Integer loginErrorTime = 0;

    /**
     * 锁定时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime loginLockTime;

    /**
     * 会有登录时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime loginTime;

    /**
     * 商家Id
     */
    @Field(type = FieldType.Long)
    private Long companyInfoId;

    /**
     * 是否是主账号
     */
    @Field(type = FieldType.Integer)
    private Integer isMasterAccount;

    /**
     * 账号类型 0 b2b账号 1 s2b平台端账号 2 s2b商家端账号 3 s2b品牌商端账号
     */
    @Field(type = FieldType.Integer)
    private AccountType accountType;

    /**
     * 邮箱
     */
    @Field(type = FieldType.Keyword)
    private String email;

    /**
     * 工号
     */
    @Field(type = FieldType.Text)
    private String jobNo;

    /**
     * 职位
     */
    @Field(type = FieldType.Keyword)
    private String position;

    /**
     * 生日
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate birthday;

    /**
     * 性别，0：保密，1：男，2：女
     */
    @Field(type = FieldType.Integer)
    private GenderType sex;

    /**
     * 是否激活会员账号，0：否，1：是
     */
    @Field(type = FieldType.Integer)
    private Integer becomeMember;

    /**
     * 交接人员工ID
     */
    @Field(type = FieldType.Keyword)
    private String heirEmployeeId;

    /**
     * 所属部门集合
     */
    @Field(type = FieldType.Keyword)
    private List<String> departmentIds;

    /**
     * 管理部门集合
     */
    @Field(type = FieldType.Keyword)
    private List<String> manageDepartmentIds;

    /**
     * 是否是主管
     */
    @Field(type = FieldType.Integer)
    private Integer isLeader;

}
