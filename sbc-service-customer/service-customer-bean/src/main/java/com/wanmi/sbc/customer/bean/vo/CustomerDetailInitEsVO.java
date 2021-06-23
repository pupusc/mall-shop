package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerDetailInitEsVO implements Serializable {

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

    private List<StoreCustomerRelaVO> esStoreCustomerRelaList;

    private EnterpriseInfoVO enterpriseInfo;

    /**
     * 企业购会员审核状态  0：无状态 1：待审核 2：已审核 3：审核未通过
     */
    private EnterpriseCheckState enterpriseCheckState;

    /**
     * 企业购会员审核拒绝原因
     */
    private String enterpriseCheckReason;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 付费会员
     */
    private List<PaidCardSimpleVO> esPaidCardList = new ArrayList<>();

}
