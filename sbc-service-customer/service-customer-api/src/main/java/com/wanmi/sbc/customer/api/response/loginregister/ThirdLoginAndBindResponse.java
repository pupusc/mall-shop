package com.wanmi.sbc.customer.api.response.loginregister;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.response.store.StoreCustomerRelaResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@Data
public class ThirdLoginAndBindResponse {

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 客户等级ID
     */
    @ApiModelProperty(value = "客户等级ID")
    private Long customerLevelId;

    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String customerAccount;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String customerPassword;

    /**
     * 密码安全等级：20危险 40低、60中、80高
     */
    @ApiModelProperty(value = "密码安全等级")
    private Integer safeLevel;

    /**
     * 盐值，用于密码加密
     */
    @ApiModelProperty(value = "盐值")
    private String customerSaltVal;

    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "审核状态")
    private CheckState checkState;

    /**
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志")
    private DeleteFlag delFlag;

    /**
     * 登录IP
     */
    @ApiModelProperty(value = "登录IP")
    private String loginIp;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
   @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime loginTime;

    /**
     * 密码错误次数
     */
    @ApiModelProperty(value = "密码错误次数")
    private Integer loginErrorCount = 0;

    /**
     * 创建|注册时间
     */
    @ApiModelProperty(value = "创建|注册时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 会员的详细信息
     */
    @ApiModelProperty(value = "会员的详细信息")
    private CustomerDetailVO customerDetail;

    /**
     * 客户类型（0:平台客户,1:商家客户）
     */
    @ApiModelProperty(value = "客户类型")
    private CustomerType customerType;

    /**
     * 锁定时间
     */
    @ApiModelProperty(value = "锁定时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime loginLockTime;

    /**
     * 商家和客户的关联关系
     */
    @ApiModelProperty(value = "商家和客户的关联关系")
    private List<StoreCustomerRelaResponse> storeCustomerRelaListByAll;

    /**
     * 是否是新用户
     */
    @ApiModelProperty(value = "是否是新用户")
    private Boolean isNewCustomer;

    /**
     * 企业购会员审核状态  0：无状态 1：待审核 2：已审核 3：审核未通过
     */
    @ApiModelProperty(value = "企业购会员审核状态")
    private EnterpriseCheckState enterpriseCheckState;

    /**
     * 企业购会员审核原因
     */
    @ApiModelProperty(value = "企业购会员审核原因")
    private String enterpriseCheckReason;
}

