package com.wanmi.sbc.crm.api.request.rfmsetting;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import com.wanmi.sbc.crm.bean.enums.Period;
import com.wanmi.sbc.crm.bean.enums.RFMType;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>rfm参数配置修改参数</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmSettingModifyRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@Max(9223372036854775807L)
	private Long id;

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

	/**
	 * 参数类型：0:R,1:F,2:M
	 */
	@ApiModelProperty(value = "参数类型：0:R,1:F,2:M")
	@NotNull
	@Max(127)
	private RFMType type;

	/**
	 * 统计周期：0:近一个月，1:近3个月，2:近6个月，3:近一年
	 */
	@ApiModelProperty(value = "统计周期：0:近一个月，1:近3个月，2:近6个月，3:近一年")
	@Max(127)
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

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	@Length(max=50)
	private String updatePerson;

	/**
	 * 删除标识,0:未删除，1:已删除
	 */
	@ApiModelProperty(value = "删除标识,0:未删除，1:已删除")
	private DeleteFlag delFlag;

}