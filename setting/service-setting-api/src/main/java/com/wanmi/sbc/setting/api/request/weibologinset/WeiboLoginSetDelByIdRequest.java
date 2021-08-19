package com.wanmi.sbc.setting.api.request.weibologinset;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除微信登录配置请求参数</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeiboLoginSetDelByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * weiboSetId
	 */
	@ApiModelProperty(value = "weiboSetId")
	@NotNull
	private String weiboSetId;
}