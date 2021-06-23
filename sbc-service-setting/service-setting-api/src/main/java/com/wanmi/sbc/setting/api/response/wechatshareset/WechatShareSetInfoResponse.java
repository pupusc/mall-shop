package com.wanmi.sbc.setting.api.response.wechatshareset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>微信分享配置VO</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatShareSetInfoResponse implements Serializable {
	private static final long serialVersionUID = 1L;

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

}