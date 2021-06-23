package com.wanmi.sbc.crm.api.request.rfmsetting;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import com.wanmi.sbc.crm.bean.enums.Period;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>rfm参数配置新增参数</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmSettingRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * R配置
	 */
	@ApiModelProperty("R分析配置")
	@NotEmpty
	@Valid
	private List<RfmSettingRElementRequest> r;

	/**
	 * F配置
	 */
	@ApiModelProperty("F分析配置")
	@NotEmpty
	@Valid
	private List<RfmSettingFElementRequest> f;

	/**
	 * M配置
	 */
	@ApiModelProperty("M分析配置")
	@NotEmpty
	@Valid
	private List<RfmSettingMElementRequest> m;

	/**
	 * M配置
	 */
	@ApiModelProperty("统计范围")
	@NotNull
	private Period period;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@Length(max=50)
	private String createPerson;

}