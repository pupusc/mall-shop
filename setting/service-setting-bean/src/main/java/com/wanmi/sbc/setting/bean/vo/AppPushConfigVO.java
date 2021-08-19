package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>消息推送VO</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@ApiModel
@Data
public class AppPushConfigVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 消息推送配置编号
	 */
	@ApiModelProperty(value = "消息推送配置编号")
	private Long appPushId;

	/**
	 * 消息推送配置名称
	 */
	@ApiModelProperty(value = "消息推送配置名称")
	private String appPushName;

	/**
	 * 消息推送提供商  0:友盟
	 */
	@ApiModelProperty(value = "消息推送提供商  0:友盟")
	private String appPushManufacturer;

	/**
	 * Android App Key
	 */
	@ApiModelProperty(value = "Android App Key")
	private String androidAppKey;

	/**
	 * Android Umeng Message Secret
	 */
	@ApiModelProperty(value = "Android Umeng Message Secret")
	private String androidUmengMessageSecret;

	/**
	 * Android App Master Secret
	 */
	@ApiModelProperty(value = "Android App Master Secret")
	private String androidAppMasterSecret;

	/**
	 * IOS App Key
	 */
	@ApiModelProperty(value = "IOS App Key")
	private String iosAppKey;

	/**
	 * IOS App Master Secret
	 */
	@ApiModelProperty(value = "IOS App Master Secret")
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