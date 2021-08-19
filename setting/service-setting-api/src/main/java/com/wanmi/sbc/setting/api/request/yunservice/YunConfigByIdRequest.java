package com.wanmi.sbc.setting.api.request.yunservice;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个查询系统配置表请求参数</p>
 * @author yang
 * @date 2019-11-05 18:33:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YunConfigByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 *  编号
	 */
	@ApiModelProperty(value = " 编号")
	@NotNull
	private Long id;
}