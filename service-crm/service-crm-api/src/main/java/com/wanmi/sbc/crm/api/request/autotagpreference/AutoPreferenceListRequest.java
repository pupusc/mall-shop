package com.wanmi.sbc.crm.api.request.autotagpreference;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoPreferenceListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "标签id")
	private Long tagId;

	@ApiModelProperty(value = "维度关联ID")
	private List<String> dimensionIds;

	private String TabFlag;
}