package com.wanmi.sbc.crm.api.request.tagparam;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>标签参数通用查询请求参数</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagParamQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Long> idList;

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
     * 批量查询-标签维度idList
     */
    @ApiModelProperty(value = "批量查询-标签维度idList")
    private List<Long> tagDimensionIdList;

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
	 * 批量查询-typeList
	 */
	@ApiModelProperty(value = "维度配置类型，0：top类型，1：聚合结果类型，2：查询条件类型")
	private List<Long> typeList;

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

}