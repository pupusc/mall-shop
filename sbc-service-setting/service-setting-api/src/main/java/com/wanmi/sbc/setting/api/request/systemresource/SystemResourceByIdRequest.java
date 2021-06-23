package com.wanmi.sbc.setting.api.request.systemresource;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询平台素材资源请求参数</p>
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 素材资源ID
	 */
	@ApiModelProperty(value = "素材资源ID")
	@NotNull
	private Long resourceId;
}