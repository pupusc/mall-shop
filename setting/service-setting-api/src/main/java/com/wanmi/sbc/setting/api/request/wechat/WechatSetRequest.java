package com.wanmi.sbc.setting.api.request.wechat;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>微信设置参数</p>
 * @author dyt
 * @date 2020-11-05 16:15:54
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatSetRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty(value = "终端: 0 pc 1 h5 2 app 3 mini", required = true)
	private TerminalType terminalType;

	@NotNull
	@ApiModelProperty(value = "是否启用 0 不启用， 1 启用", required = true)
	private DefaultFlag status;

	@NotBlank
	@ApiModelProperty(value = "App ID", required = true)
	private String appId;

	@NotBlank
	@ApiModelProperty(value = "App Secret", required = true)
	private String secret;

}