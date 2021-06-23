package com.wanmi.sbc.setting.api.request.wechatshareset;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询微信分享配置请求参数</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatShareSetByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 微信分享参数配置主键
	 */
	@ApiModelProperty(value = "微信分享参数配置主键")
	@NotNull
	private String shareSetId;
}