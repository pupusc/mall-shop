package com.wanmi.sbc.setting.api.response.wechat;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>微信设置响应结果</p>
 * @author dyt
 * @date 2020-11-05 16:15:25
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatSetResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * h5-是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "h5-是否启用 0 不启用， 1 启用")
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
	 * pc-是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "pc-是否启用 0 不启用， 1 启用")
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
	 * 小程序-是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "小程序-是否启用 0 不启用， 1 启用")
	private DefaultFlag miniProgramServerStatus;

	/**
	 * 小程序-AppID(应用ID)
	 */
	@ApiModelProperty(value = "小程序-AppID(应用ID)")
	private String miniProgramAppAppId;

	/**
	 * 小程序-AppSecret(应用密钥)
	 */
	@ApiModelProperty(value = "小程序-AppSecret(应用密钥)")
	private String miniProgramAppSecret;

	/**
	 * app-是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "app-是否启用 0 不启用， 1 启用")
	private DefaultFlag appServerStatus;

	/**
	 * app-AppID(应用ID)
	 */
	@ApiModelProperty(value = "app-AppID(应用ID)")
	private String appAppId;

	/**
	 * app-AppSecret(应用密钥)
	 */
	@ApiModelProperty(value = "app-AppSecret(应用密钥)")
	private String appAppSecret;

}