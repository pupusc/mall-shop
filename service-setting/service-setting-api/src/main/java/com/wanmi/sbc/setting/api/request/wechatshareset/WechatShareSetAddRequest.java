package com.wanmi.sbc.setting.api.request.wechatshareset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * <p>微信分享配置新增参数</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatShareSetAddRequest extends SettingBaseRequest {
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
	@Length(max=45)
	private String operatePerson;

	@ApiModelProperty(value = "门店id")
	private Long storeId;

}