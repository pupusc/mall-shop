package com.wanmi.sbc.setting.api.request.wechatloginset;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>微信授权登录配置新增参数</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatLoginSetAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * h5-微信授权登录是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "h5-微信授权登录是否启用 0 不启用， 1 启用")
	private DefaultFlag mobileServerStatus;

	/**
	 * h5-AppID(应用ID)
	 */
	@ApiModelProperty(value = "h5-AppID(应用ID)")
	@Length(max=60)
	private String mobileAppId;

	/**
	 * h5-AppSecret(应用密钥)
	 */
	@ApiModelProperty(value = "h5-AppSecret(应用密钥)")
	@Length(max=60)
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
	@Length(max=60)
	private String pcAppId;

	/**
	 * pc-AppSecret(应用密钥)
	 */
	@ApiModelProperty(value = "pc-AppSecret(应用密钥)")
	@Length(max=60)
	private String pcAppSecret;

	/**
	 * app-微信授权登录是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "app-微信授权登录是否启用 0 不启用， 1 启用")
	private DefaultFlag appServerStatus;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	@Length(max=45)
	private String operatePerson;

	@ApiModelProperty(value = "门店id")
	private Long storeId;

}