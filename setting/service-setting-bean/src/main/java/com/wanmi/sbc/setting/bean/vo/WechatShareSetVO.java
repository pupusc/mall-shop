package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>微信分享配置VO</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@ApiModel
@Data
public class WechatShareSetVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 微信分享参数配置主键
	 */
	@ApiModelProperty(value = "微信分享参数配置主键")
	private String shareSetId;

	/**
	 * 微信公众号App ID
	 */
	@ApiModelProperty(value = "微信公众号App ID")
	private String shareAppId;

	/**
	 * 微信公众号 App Secret
	 */
	@ApiModelProperty(value = "微信公众号 App Secret")
	private String shareAppSecret;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	private String operatePerson;

}