package com.wanmi.sbc.crm.api.request.tagdimension;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.crm.bean.enums.TagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>标签维度列表查询请求参数</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionListRequest extends BaseQueryRequest {
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
	 * 维度名
	 */
	@ApiModelProperty(value = "维度名")
	private String name;

	/**
	 * 维度对应表明
	 */
	@ApiModelProperty(value = "维度对应表明")
	private String tableName;

	/**
	 * 标签类型
	 */
	@ApiModelProperty(value = "标签类型 0: 偏好类 1：标值 2：指标值范围 3：综合")
	@NotNull
	private TagType tagType;



}