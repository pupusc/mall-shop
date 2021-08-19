package com.wanmi.sbc.crm.api.request.tagdimension;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.crm.bean.enums.TagDimensionFirstLastType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>标签维度通用查询请求参数</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionQueryRequest extends BaseQueryRequest {
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
	@ApiModelProperty(value = "标签类型")
	private TagType tagType;

	/**
	 * 首次末次类型
	 */
	@ApiModelProperty(value = "首次末次类型")
	private TagDimensionFirstLastType firstLastType;

}