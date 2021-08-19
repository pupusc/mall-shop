package com.wanmi.sbc.setting.api.request.systemprivacypolicy;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除隐私政策请求参数</p>
 * @author yangzhen
 * @date 2020-09-23 14:52:35
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemPrivacyPolicyDelByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 隐私政策id
	 */
	@ApiModelProperty(value = "隐私政策id")
	@NotNull
	private String privacyPolicyId;
}