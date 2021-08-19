package com.wanmi.sbc.crm.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>偏好标签明细VO</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@ApiModel
@Data
public class PreferenceTagDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "标签id")
	private Long tagId;

	/**
	 * 偏好类标签名称
	 */
	@ApiModelProperty(value = "偏好类标签名称")
	private String detailName;

	/**
	 * 会员人数
	 */
	@ApiModelProperty(value = "会员人数")
	private Long customerCount;

}