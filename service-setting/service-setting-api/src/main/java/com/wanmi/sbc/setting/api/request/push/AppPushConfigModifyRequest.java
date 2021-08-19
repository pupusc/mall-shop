package com.wanmi.sbc.setting.api.request.push;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>消息推送修改参数</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppPushConfigModifyRequest extends SettingBaseRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 消息推送配置编号
	 */
	@ApiModelProperty(value = "消息推送配置编号")
	@Max(9223372036854775807L)
	private Long appPushId;

	/**
	 * 消息推送配置名称
	 */
	@ApiModelProperty(value = "消息推送配置名称")
	@Length(max=255)
	private String appPushName;

	/**
	 * 消息推送提供商  0:友盟
	 */
	@ApiModelProperty(value = "消息推送提供商  0:友盟")
	@Length(max=2)
	private String appPushManufacturer;

	/**
	 * Android App Key
	 */
	@ApiModelProperty(value = "Android App Key")
	@Length(max=255)
	private String androidAppKey;

	/**
	 * Android Umeng Message Secret
	 */
	@ApiModelProperty(value = "Android Umeng Message Secret")
	@Length(max=255)
	private String androidUmengMessageSecret;

	/**
	 * Android App Master Secret
	 */
	@ApiModelProperty(value = "Android App Master Secret")
	@Length(max=255)
	private String androidAppMasterSecret;

	/**
	 * IOS App Key
	 */
	@ApiModelProperty(value = "IOS App Key")
	@Length(max=255)
	private String iosAppKey;

	/**
	 * IOS App Master Secret
	 */
	@ApiModelProperty(value = "IOS App Master Secret")
	@Length(max=255)
	private String iosAppMasterSecret;

	/**
	 * 状态,0:未启用1:已启用
	 */
	@ApiModelProperty(value = "状态,0:未启用1:已启用")
	private Integer status;

	/**
	 * 创建日期
	 */
	@ApiModelProperty(value = "创建日期")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新日期
	 */
	@ApiModelProperty(value = "更新日期")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 删除日期
	 */
	@ApiModelProperty(value = "删除日期")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTime;

	/**
	 * 删除标志
	 */
	@ApiModelProperty(value = "删除标志")
	private DeleteFlag delFlag;

}