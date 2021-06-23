package com.wanmi.sbc.customer.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员中心返回数据
 * Created by CHENLI on 2017/7/17.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCenterResponse {

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 客户账号
     */
    @ApiModelProperty(value = "客户账号")
    private String customerAccount;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;

    /**
     * 等级徽章图
     */
    @ApiModelProperty(value = "等级徽章图")
    private String rankBadgeImg;

    /**
     * 客户成长值
     */
    @ApiModelProperty(value = "客户成长值")
    private Long growthValue;

    /**
     * 客户头像
     */
    @ApiModelProperty(value = "客户头像")
    private String headImg;

    /**
     * 可用积分
     */
    @ApiModelProperty(value = "可用积分")
    private Long pointsAvailable;

    /**
     * 已用积分
     */
    @ApiModelProperty(value = "已用积分")
    private Long pointsUsed;

    /**
     * 考虑到后面可能会有很多类似“企业会员”的标签，用List存放标签内容
     */
    @ApiModelProperty(value = "会员标签")
    private List<String> customerLabelList = new ArrayList<>();

    /**
     * 企业会员名称
     */
    @ApiModelProperty(value = "企业会员名称")
    private String enterpriseCustomerName;

    /**
     * 企业会员logo 
     */
    @ApiModelProperty(value = "企业会员logo")
    private String enterpriseCustomerLogo;
}
