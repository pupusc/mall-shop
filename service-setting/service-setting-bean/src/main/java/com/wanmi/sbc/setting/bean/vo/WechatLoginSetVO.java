package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>微信授权登录配置VO</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@ApiModel
@Data
public class WechatLoginSetVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 微信授权登录配置主键
	 */
	@ApiModelProperty(value = "微信授权登录配置主键")
	private String wechatSetId;

	/**
	 * h5-微信授权登录是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "h5-微信授权登录是否启用 0 不启用， 1 启用")
	private DefaultFlag mobileServerStatus;

	/**
	 * h5-AppID(应用ID)
	 */
	@ApiModelProperty(value = "h5-AppID(应用ID)")
	private String mobileAppId;

	/**
	 * h5-AppSecret(应用密钥)
	 */
	@ApiModelProperty(value = "h5-AppSecret(应用密钥)")
	private String mobileAppSecret;

	/**
	 * pc-微信授权登录是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "pc-微信授权登录是否启用 0 不启用， 1 启用")
	private DefaultFlag pcServerStatus;

	/**
	 * pc-AppID(应用ID)
	 */
	@ApiModelProperty(value = "pc-AppID(应用ID)")
	private String pcAppId;

	/**
	 * pc-AppSecret(应用密钥)
	 */
	@ApiModelProperty(value = "pc-AppSecret(应用密钥)")
	private String pcAppSecret;

	/**
	 * app-微信授权登录是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "app-微信授权登录是否启用 0 不启用， 1 启用")
	private DefaultFlag appServerStatus;

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