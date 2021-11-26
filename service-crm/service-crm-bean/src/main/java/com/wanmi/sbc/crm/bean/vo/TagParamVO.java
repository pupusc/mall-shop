package com.wanmi.sbc.crm.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>标签参数VO</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@ApiModel
@Data
public class TagParamVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 标签维度id
	 */
	@ApiModelProperty(value = "标签维度id")
	private Long tagDimensionId;

	/**
	 * 维度配置名称
	 */
	@ApiModelProperty(value = "维度配置名称")
	private String name;

	/**
	 * 字段名称
	 */
	@ApiModelProperty(value = "字段名称")
	private String columnName;

	/**
	 * 维度配置类型，0：top类型，1：聚合结果类型，2：查询条件类型
	 */
	@ApiModelProperty(value = "维度配置类型，0：top类型，1：聚合结果类型，2：查询条件类型")
	private Integer type;

	/**
	 * 标签维度类型，0：top类型，1：count计数类型，2：sum求和，3：avg平均值，4：max最大值，5：min最小值，6：in包含类型，7：等于，8、区间类，9：多重期间or关系
	 */
	@ApiModelProperty(value = "标签维度类型，0：top类型，1：count计数类型，2：sum求和，3：avg平均值，4：max最大值，5：min最小值，6：in包含类型，7：等于，8、区间类，9：多重期间or关系")
	private Integer tagDimensionType;

    /**
     * 标签参数类型 1:指标值参数
     */
    @ApiModelProperty(value = "标签参数类型 1:指标值参数")
    private Integer tagType;

	/**
	 * 字段类型
	 */
	@ApiModelProperty(value = "字段类型 0:input输入 1：范围输入 2:下拉选择")
    private Integer columnType;

	/**
	 * 默认初始值
	 */
	private List<?> defaultValue;

}