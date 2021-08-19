package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.common.enums.EnableStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * <p>系统成长值开关修改参数</p>
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemGrowthValueStatusModifyRequest extends SettingBaseRequest {

	private static final long serialVersionUID = -1032775665429987533L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@NotBlank
	private String growthValueConfigId;

	/**
	 * 是否启用标志 0：停用，1：启用
	 */
	@ApiModelProperty(value = "是否启用标志 0：停用，1：启用")
	private EnableStatus status;
}