package com.wanmi.sbc.setting.api.request.wechatloginset;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 查询微信授权登录配置请求参数
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatServiceStatusByStoreIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "门店id")
	@NotNull
	private Long storeId;
}