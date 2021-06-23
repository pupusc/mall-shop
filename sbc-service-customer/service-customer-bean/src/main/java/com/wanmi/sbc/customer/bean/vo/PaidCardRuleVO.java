package com.wanmi.sbc.customer.bean.vo;

import java.math.BigDecimal;

import com.wanmi.sbc.customer.bean.enums.PaidCardRuleTypeEnum;
import com.wanmi.sbc.customer.bean.enums.StatusEnum;
import com.wanmi.sbc.customer.bean.enums.TimeUnitEnum;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员VO</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@ApiModel
@Data
public class PaidCardRuleVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 配置类型 0：付费配置；1：续费配置
	 */
	@ApiModelProperty(value = "配置类型 0：付费配置；1：续费配置")
	private PaidCardRuleTypeEnum type;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;

	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格")
	private BigDecimal price;

	/**
	 * 0:禁用；1：启用
	 */
	@ApiModelProperty(value = "0:禁用；1：启用")
	private StatusEnum status;

	/**
	 * 付费会员类型id
	 */
	@ApiModelProperty(value = "付费会员类型id")
	private String paidCardId;

	/**
	 * 时间单位：0天，1月，2年
	 */
	@ApiModelProperty(value = "时间单位：0天，1月，2年")
	private TimeUnitEnum timeUnit;

	/**
	 * 时间（数值）
	 */
	@ApiModelProperty(value = "时间（数值）")
	private Integer timeVal;
	/**
	 * erpSkuCode
	 */
	@ApiModelProperty(value = "erpSkuCode")
	private String erpSkuCode;


	private PaidCardVO paidCard;

}