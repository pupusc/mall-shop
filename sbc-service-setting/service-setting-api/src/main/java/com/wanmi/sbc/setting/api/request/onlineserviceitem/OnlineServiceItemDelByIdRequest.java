package com.wanmi.sbc.setting.api.request.onlineserviceitem;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除onlineerviceItem请求参数</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceItemDelByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 在线客服座席id
	 */
	@ApiModelProperty(value = "在线客服座席id")
	@NotNull
	private Integer serviceItemId;
}