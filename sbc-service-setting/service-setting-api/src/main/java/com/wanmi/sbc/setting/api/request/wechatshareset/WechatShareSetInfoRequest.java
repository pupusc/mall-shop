package com.wanmi.sbc.setting.api.request.wechatshareset;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

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
public class WechatShareSetInfoRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	private String operatePerson;
}