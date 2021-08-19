package com.wanmi.sbc.crm.api.response.rfmsetting;

import com.wanmi.sbc.crm.bean.enums.Period;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>rfm参数配置新增参数</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmSettingResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * R配置
	 */
	@ApiModelProperty("R分析配置")
	@NotEmpty
	@Valid
	private List<RfmSettingRFMElementBaseResponse> r;

	/**
	 * F配置
	 */
	@ApiModelProperty("F分析配置")
	@NotEmpty
	@Valid
	private List<RfmSettingRFMElementBaseResponse> f;

	/**
	 * M配置
	 */
	@ApiModelProperty("M分析配置")
	@NotEmpty
	@Valid
	private List<RfmSettingRFMElementBaseResponse> m;

	/**
	 * M配置
	 */
	@ApiModelProperty("统计范围")
	@NotNull
	private Period period;

}