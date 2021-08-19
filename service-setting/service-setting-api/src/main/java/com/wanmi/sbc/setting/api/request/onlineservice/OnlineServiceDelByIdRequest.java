package com.wanmi.sbc.setting.api.request.onlineservice;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除onlineService请求参数</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceDelByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 在线客服主键
	 */
	@ApiModelProperty(value = "在线客服主键")
	@NotNull
	private Integer onlineServiceId;
}