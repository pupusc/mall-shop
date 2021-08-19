package com.wanmi.sbc.crm.api.request.tagparam;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>标签参数新增参数</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagParamAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签维度id
	 */
	@ApiModelProperty(value = "标签维度id")
	@Max(9223372036854775807L)
	private Long tagDimensionId;

	/**
	 * 维度配置名称
	 */
	@ApiModelProperty(value = "维度配置名称")
	@Length(max=255)
	private String name;

	/**
	 * 字段名称
	 */
	@ApiModelProperty(value = "字段名称")
	@Length(max=255)
	private String columnName;

	/**
	 * 维度配置类型，0：top类型，1：聚合结果类型，2：查询条件类型
	 */
	@ApiModelProperty(value = "维度配置类型，0：top类型，1：聚合结果类型，2：查询条件类型")
	@Max(127)
	private Integer type;

	/**
	 * 标签维度类型，0：top类型，1：count计数类型，2：sum求和，3：avg平均值，4：max最大值，5：min最小值，6：in包含类型，7：等于，8、区间类，9：多重期间or关系
	 */
	@ApiModelProperty(value = "标签维度类型，0：top类型，1：count计数类型，2：sum求和，3：avg平均值，4：max最大值，5：min最小值，6：in包含类型，7：等于，8、区间类，9：多重期间or关系")
	@Max(127)
	private Integer tagDimensionType;

}