package com.wanmi.sbc.setting.api.request.businessconfig;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询招商页设置请求参数</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessConfigByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 招商页设置主键
	 */
	@ApiModelProperty(value = "招商页设置主键")
	@NotNull
	private Integer businessConfigId;
}