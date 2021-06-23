package com.wanmi.sbc.goods.api.request.ruleinfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.RuleType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>规则说明修改参数</p>
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleInfoModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@Max(9223372036854775807L)
	private Long id;

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
	@NotNull
	private String ruleContent;

	/**
	 * 规则类型 0:预约 1:预售
	 */
	@ApiModelProperty(value = "规则类型 0:预约 1:预售")
	private RuleType ruleType;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

}