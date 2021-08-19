package com.wanmi.sbc.crm.customer.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.GenderType;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel
@Data
public class CustomerGetForCrmResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headImg;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String customerAccount;

    /**
     * 账号状态 0：启用中  1：禁用中
     */
    @ApiModelProperty(value = "账号状态")
    private CustomerStatus customerStatus;

    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "审核状态")
    private CheckState checkState;

    /**
     * 禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    private String forbidReason;

    /**
     * 联系人名字
     */
    @ApiModelProperty(value = "联系人名字")
    private String contactName;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private Long provinceId;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Long cityId;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Long areaId;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private Long streetId;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String customerAddress;

    /**
     * 业务员
     */
    @ApiModelProperty(value = "业务员")
    private String employeeId;

    /**
     * 业务员姓名
     */
    @ApiModelProperty(value = "业务员姓名")
    private String employeeName;
    /**
     * 业务员手机号
     */
    @ApiModelProperty(value = "业务员手机号")
    private String employeeMobile;

    /**
     * 客户等级ID
     */
    @ApiModelProperty(value = "客户等级ID")
    private Long customerLevelId;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;

    /**
     * 客户成长值
     */
    @ApiModelProperty(value = "客户成长值")
    private Long growthValue;

    /**
     * 可用积分
     */
    @ApiModelProperty(value = "可用积分")
    private Long pointsAvailable;

    /**
     * 账户余额
     */
    @ApiModelProperty(value = "账户余额")
    private BigDecimal accountBalance;

    /**
     * 是否为分销员
     */
    @ApiModelProperty(value = "是否为分销员")
    private DefaultFlag isDistributor;

    /**
     * 是否有分销员资格0：否，1：是
     */
    @ApiModelProperty(value = "是否有分销员资格0：否，1：是")
    private DefaultFlag distributorFlag;

    /**
     * 分销员等级名称
     */
    @ApiModelProperty(value = "分销员等级名称 ")
    private String distributorLevelName;

    /**
     * 邀新人数
     */
    @ApiModelProperty(value = "邀新人数")
    private Integer inviteCount;

    /**
     * 有效邀新人数
     */
    @ApiModelProperty(value = "有效邀新人数")
    private Integer inviteAvailableCount;

    /**
     * 邀新奖金(元)
     */
    @ApiModelProperty(value = "邀新奖金(元)")
    private BigDecimal rewardCash;

    /**
     * 未入账邀新奖金(元)
     */
    @ApiModelProperty(value = "未入账邀新奖金(元)")
    private BigDecimal rewardCashNotRecorded;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate birthDay;

    /**
     * 性别，0女，1男
     */
    @ApiModelProperty(value = "性别，0女，1男")
    private GenderType gender;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

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

    /**
     * 企业信息
     */
    @ApiModelProperty(value = "企业信息")
    private EnterpriseInfoVO enterpriseInfo;

    /**
     * 企业会员名称
     */
    @ApiModelProperty(value = "企业会员名称")
    private String enterpriseCustomerName;
}
