package com.wanmi.sbc.setting.api.request.umengpushconfig;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

/**
 * <p>友盟push接口配置修改参数</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengPushConfigModifyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@Max(9999999999L)
	private Integer id;

	/**
	 * androidKeyId
	 */
	@ApiModelProperty(value = "androidKeyId")
	@Length(max=64)
	private String androidKeyId;

	/**
	 * androidMsgSecret
	 */
	@ApiModelProperty(value = "androidMsgSecret")
	@Length(max=64)
	private String androidMsgSecret;

	/**
	 * androidKeySecret
	 */
	@ApiModelProperty(value = "androidKeySecret")
	@Length(max=64)
	private String androidKeySecret;

	/**
	 * iosKeyId
	 */
	@ApiModelProperty(value = "iosKeyId")
	@Length(max=64)
	private String iosKeyId;

	/**
	 * iosKeySecret
	 */
	@ApiModelProperty(value = "iosKeySecret")
	@Length(max=64)
	private String iosKeySecret;

}