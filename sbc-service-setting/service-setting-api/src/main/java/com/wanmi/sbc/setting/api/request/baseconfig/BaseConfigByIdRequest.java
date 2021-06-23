package com.wanmi.sbc.setting.api.request.baseconfig;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询基本设置请求参数</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseConfigByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 基本设置ID
	 */
	@ApiModelProperty(value = "基本设置ID")
	@NotNull
	private Integer baseConfigId;
}