package com.wanmi.sbc.customer.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>分销员VO</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
public class DistributionCustomerSimVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 会员的详情的Id
	 */
    @ApiModelProperty(value = "分销员标识UUID")
	private String customerDetailId;

	/**
	 * 会员ID
	 */
    @ApiModelProperty(value = "会员ID")
	private String customerId;

	/**
	 * 分销员ID
	 */
	@ApiModelProperty(value = "分销员ID")
	private String distributionId;

	/**
	 * 分销员等级ID
	 */
	@ApiModelProperty(value = "分销员等级ID")
	private String distributorLevelId;

	/**
	 * 会员名称
	 */
    @ApiModelProperty(value = "会员名称")
	private String customerName;

	/**
	 * 是否为分销员
	 */
	@ApiModelProperty(value = "是否为分销员")
	private DefaultFlag distributorFlag;

	/**
	 * 是否被禁用
	 */
	@ApiModelProperty(value = "是否被禁用")
	private DefaultFlag forbiddenFlag;

	/**
	 * 分销员等级规则说明
	 */
	@ApiModelProperty(value = "分销员等级规则说明")
	private String distributorLevelDesc;


}