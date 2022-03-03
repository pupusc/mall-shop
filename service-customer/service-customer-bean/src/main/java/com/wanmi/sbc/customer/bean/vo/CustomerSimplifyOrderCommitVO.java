package com.wanmi.sbc.customer.bean.vo;

import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 客户信息主表
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
public class CustomerSimplifyOrderCommitVO implements Serializable {

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;
    /**
     * 樊登id
     */
    @ApiModelProperty(value = "樊登id")
    private String fanDengUserNo;

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
     * 企业购会员审核状态  0：无状态 1：待审核 2：已审核 3：审核未通过
     */
    @ApiModelProperty(value = "企业购会员审核状态")
    private EnterpriseCheckState enterpriseCheckState;

    /**
     * 可用积分
     */
    @ApiModelProperty(value = "可用积分")
    private Long pointsAvailable;

    /**
     * 商家和客户的关联关系
     */
    @ApiModelProperty(value = "商家和客户的关联关系")
    private List<StoreCustomerRelaVO> storeCustomerRelaListByAll;

    /**
     * 会员详情对象
     */
    private CustomerDetailSimplifyVO customerDetail;

    /**
     * openId
     */
    private String openId;

}
