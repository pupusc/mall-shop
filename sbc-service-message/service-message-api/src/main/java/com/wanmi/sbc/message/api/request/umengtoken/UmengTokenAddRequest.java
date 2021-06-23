package com.wanmi.sbc.message.api.request.umengtoken;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * <p>友盟推送设备与会员关系新增参数</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengTokenAddRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	@Length(max=32)
	private String customerId;

	/**
	 * 友盟推送会员设备token
	 */
	@ApiModelProperty(value = "友盟推送会员设备token")
	@Length(max=64)
	@NotBlank
	private String devlceToken;

	/**
	 * 友盟推送会员设备token平台类型
	 */
	@ApiModelProperty(value = "token平台类型")
	private PushPlatform platform;

	/**
	 * 绑定时间
	 */
	@ApiModelProperty(value = "绑定时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bindingTime;

}