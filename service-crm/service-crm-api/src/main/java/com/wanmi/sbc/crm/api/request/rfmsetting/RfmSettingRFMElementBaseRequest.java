package com.wanmi.sbc.crm.api.request.rfmsetting;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * <p>rfm参数配置新增参数</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class RfmSettingRFMElementBaseRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 参数
	 */
	@ApiModelProperty(value = "参数")
	@NotNull
	@Max(9999999999L)
	private Integer param;

	/**
	 * 得分
	 */
	@ApiModelProperty(value = "得分")
	@NotNull
	@Max(9999999999L)
	private Integer score;

}