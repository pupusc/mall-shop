package com.wanmi.sbc.setting.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.GrowthValueRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * <p>系统成长值设置修改参数</p>
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemGrowthValueConfigModifyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = -7080294538643269844L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@Length(max=32)
	private String growthValueConfigId;

	/**
	 * 成长值获取规则
	 */
	@ApiModelProperty(value = "成长值获取规则")
	private GrowthValueRule rule;

	/**
	 * 成长值说明
	 */
	@ApiModelProperty(value = "成长值说明")
	private String remark;

	/**
	 * 是否启用标志 0：停用，1：启用
	 */
	@ApiModelProperty(value = "是否启用标志 0：停用，1：启用")
	private EnableStatus status;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是")
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

}