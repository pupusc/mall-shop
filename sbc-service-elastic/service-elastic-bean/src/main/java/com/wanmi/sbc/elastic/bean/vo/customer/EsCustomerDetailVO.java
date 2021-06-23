package com.wanmi.sbc.elastic.bean.vo.customer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EsCustomerDetailVO implements Serializable {

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 账户
     */
    private String customerAccount;

    /**
     * 省
     */
    private Long provinceId;

    /**
     * 市
     */
    private Long cityId;

    /**
     * 区
     */
    private Long areaId;

    /**
     * 街道
     */
    private Long streetId;

    /**
     * 详细地址
     */
    private String customerAddress;

    /**
     * 联系人名字
     */
    private String contactName;

    /**
     * 客户等级ID
     */
    private Long customerLevelId;


    /**
     * 联系方式
     */
    private String contactPhone;


    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    private CheckState checkState;


    /**
     * 账号状态 0：启用中  1：禁用中
     */
    private CustomerStatus customerStatus;


    /**
     * 负责业务员
     */
    private String employeeId;


    /**
     * 是否为分销员 0：否  1：是
     */
    private DefaultFlag isDistributor;


    /**
     * 审核驳回理由
     */
    private String rejectReason;

    /**
     * 禁用原因
     */
    private String forbidReason;


    /**
     * 是否是付费会员 0：否 1：是
     */
    private Integer isPaidCardCustomer = 0;

    /**
     * 企业会员审核状态
     */
    private EnterpriseCheckState enterpriseCheckState;


    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;



    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String provinceName;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String cityName;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String areaName;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private String streetName;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;

    /**
     * 成长值
     */
    @ApiModelProperty(value = "成长值")
    private Long growthValue;

    /**
     * 业务员名称
     */
    @ApiModelProperty(value = "业务员名称")
    private String employeeName;

    /**
     * 是否是我的客户（S2b-Supplier使用）
     */
    @ApiModelProperty(value = "是否是我的客户（S2b-Supplier使用")
    private Boolean myCustomer;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private CustomerType customerType;

    /**
     * 企业性质
     */
    @ApiModelProperty(value = "企业性质")
    private Integer businessNatureType;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业性质")
    private String enterpriseName;

}
