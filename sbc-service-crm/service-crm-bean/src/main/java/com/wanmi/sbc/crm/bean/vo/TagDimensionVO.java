package com.wanmi.sbc.crm.bean.vo;

import com.wanmi.sbc.crm.bean.enums.TagDimensionFirstLastType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>标签维度VO</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@ApiModel
@Data
public class TagDimensionVO implements Serializable {
	private static final long serialVersionUID = 1L;

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
	 * 首次末次类型
	 */
	@ApiModelProperty(value = "首次末次类型")
	private TagDimensionFirstLastType firstLastType;

	/**
	 * 首次末次类型
	 */
	@ApiModelProperty(value = "参数列表")
	private List<TagParamVO> tagParamVOList;

}