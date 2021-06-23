package com.wanmi.sbc.setting.api.request.systemprivacypolicy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * <p>隐私政策修改参数</p>
 * @author yangzhen
 * @date 2020-09-23 14:52:35
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemPrivacyPolicyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 隐私政策id
	 */
	@ApiModelProperty(value = "隐私政策id")
	@Length(max=32)
	private String privacyPolicyId;

	/**
	 * 隐私政策
	 */
	@ApiModelProperty(value = "隐私政策")
	private String privacyPolicy;

	/**
	 * 隐私政策弹窗
	 */
	@ApiModelProperty(value = "隐私政策弹窗")
	private String privacyPolicyPop;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@Length(max=32)
	private String createPerson;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	@Length(max=32)
	private String updatePerson;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是")
	private DeleteFlag delFlag;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	private Operator operator;

}