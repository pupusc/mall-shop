package com.wanmi.sbc.message.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>友盟推送设备与会员关系VO</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@ApiModel
@Data
public class UmengTokenVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	private String customerId;

	/**
	 * 友盟推送会员设备token
	 */
	@ApiModelProperty(value = "友盟推送会员设备token")
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