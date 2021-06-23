package com.wanmi.sbc.setting.api.request.qqloginset;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询qq登录信息请求参数</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QqLoginSetByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * qqSetId
	 */
	@ApiModelProperty(value = "qqSetId")
	@NotNull
	private String qqSetId;
}