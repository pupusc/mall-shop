package com.wanmi.sbc.crm.api.request.autotagpreference;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoPreferencePageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

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

	private String TabFlag;
}