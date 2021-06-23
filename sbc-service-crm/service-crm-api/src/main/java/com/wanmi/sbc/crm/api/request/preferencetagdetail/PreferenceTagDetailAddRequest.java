package com.wanmi.sbc.crm.api.request.preferencetagdetail;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>偏好标签明细新增参数</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceTagDetailAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "标签id")
	@Max(9223372036854775807L)
	private Long tagId;

	/**
	 * 偏好类标签名称
	 */
	@ApiModelProperty(value = "偏好类标签名称")
	@Length(max=255)
	private String detailName;

	/**
	 * 会员人数
	 */
	@ApiModelProperty(value = "会员人数")
	@Max(9223372036854775807L)
	private Long customerCount;

}