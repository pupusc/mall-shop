package com.wanmi.sbc.message.bean.vo;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;

import com.wanmi.sbc.message.bean.enums.SmsSettingType;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信配置VO</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:15:28
 */
@ApiModel
@Data
public class SmsSettingVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 调用api参数key
	 */
	@ApiModelProperty(value = "调用api参数key")
	private String accessKeyId;

	/**
	 * 调用api参数secret
	 */
	@ApiModelProperty(value = "调用api参数secret")
	private String accessKeySecret;

	/**
	 * 短信平台类型：0：阿里云短信平台
	 */
	@ApiModelProperty(value = "短信平台类型：0：阿里云短信平台")
	private SmsSettingType type;

	/**
	 * 是否启用：0：未启用；1：启用
	 */
	@ApiModelProperty(value = "是否启用：0：未启用；1：启用")
	private EnableStatus status;

	/**
	 * 删除标识：0：未删除；1：已删除
	 */
	@ApiModelProperty(value = "删除标识：0：未删除；1：已删除")
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime creatTime;

}