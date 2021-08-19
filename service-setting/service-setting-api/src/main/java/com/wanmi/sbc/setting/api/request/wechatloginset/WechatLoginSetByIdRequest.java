package com.wanmi.sbc.setting.api.request.wechatloginset;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询微信授权登录配置请求参数</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatLoginSetByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 微信授权登录配置主键
	 */
	@ApiModelProperty(value = "微信授权登录配置主键")
	@NotNull
	private String wechatSetId;
}