package com.wanmi.sbc.customer.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员VO</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@ApiModel
@Data
public class PaidCardRightsRelVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 所属会员权益id
	 */
	@ApiModelProperty(value = "所属会员权益id")
	private String paidCardId;

	/**
	 * 权益id
	 */
	@ApiModelProperty(value = "权益id")
	private Integer rightsId;

	/**
	 * 权益名称
	 */
	@ApiModelProperty(value = "权益名称")
	private String rightsName;


	/**
	 * 权益完整信息
	 */
	@ApiModelProperty(value = "权益完整信息")
	private CustomerLevelRightsVO customerLevelRights;

}