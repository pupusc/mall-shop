package com.wanmi.sbc.goods.api.request.ruleinfo;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.RuleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * <p>规则说明新增参数</p>
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleInfoAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 规则名称
	 */
	@ApiModelProperty(value = "规则名称")
	@Length(max=128)
	private String ruleName;

	/**
	 * 规则说明
	 */
	@ApiModelProperty(value = "规则说明")
	@Length(max=500)
	private String ruleContent;

	/**
	 * 规则类型 0:预约 1:预售
	 */
	@ApiModelProperty(value = "规则类型 0:预约 1:预售")
	@NotNull
	private RuleType ruleType;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是", hidden = true)
	private DeleteFlag delFlag;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createPerson;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

}