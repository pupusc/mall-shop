package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.common.enums.RuleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>规则说明VO</p>
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@ApiModel
@Data
public class RuleInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 规则名称
	 */
	@ApiModelProperty(value = "规则名称")
	private String ruleName;

	/**
	 * 规则说明
	 */
	@ApiModelProperty(value = "规则说明")
	private String ruleContent;

	/**
	 * 规则类型 0:预约 1:预售
	 */
	@ApiModelProperty(value = "规则类型 0:预约 1:预售")
	private RuleType ruleType;

}