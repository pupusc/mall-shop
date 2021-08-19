package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.goods.bean.enums.AssignPersonRestrictedType;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售配置VO</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@ApiModel
@Data
public class GoodsRestrictedCustomerRelaVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 限售会员的关系主键
	 */
	@ApiModelProperty(value = "限售会员的关系主键")
	private Long relaId;

	/**
	 * 限售主键
	 */
	@ApiModelProperty(value = "限售主键")
	private Long restrictedSaleId;

	/**
	 * 特定会员的限售类型 0: 会员等级  1：指定会员
	 */
	@ApiModelProperty(value = "特定会员的限售类型 0: 会员等级  1：指定会员")
	private AssignPersonRestrictedType assignPersonRestrictedType;

	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	private String customerId;

	/**
	 * 会员等级ID
	 */
	@ApiModelProperty(value = "会员等级ID")
	private Long customerLevelId;

}