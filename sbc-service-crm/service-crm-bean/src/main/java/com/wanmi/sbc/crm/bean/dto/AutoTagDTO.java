package com.wanmi.sbc.crm.bean.dto;

import com.wanmi.sbc.crm.bean.enums.TagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>自动标签VO</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@ApiModel
@Data
public class AutoTagDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签组id
	 */
	@ApiModelProperty(value = "id")
	private Long tagId;

	/**
	 * 自动标签名称
	 */
	@ApiModelProperty(value = "自动标签名称")
	private String name;


	/**
	 * 标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签
	 */
	@ApiModelProperty(value = "标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签")
	private TagType type;

	@ApiModelProperty(value = "选中状态")
	private Boolean ifChecked = true;

	/**
	 * 选中的标签值
	 */
	@ApiModelProperty(value = "选中的标签值")
	private List<String> chooseTags;
}