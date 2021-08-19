package com.wanmi.sbc.setting.api.request.storeresource;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询店铺资源库请求参数</p>
 * @author lq
 * @date 2019-11-05 16:12:49
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResourceByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 素材ID
	 */
	@ApiModelProperty(value = "素材ID")
	@NotNull
	private Long resourceId;
}