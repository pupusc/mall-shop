package com.wanmi.sbc.message.bean.vo;

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
 * <p>短信发送VO</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@Data
public class SmsSendDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 发送任务id
	 */
	@ApiModelProperty(value = "发送任务id")
	private Long sendId;

	/**
	 * 接收短信的号码
	 */
	@ApiModelProperty(value = "接收短信的号码")
	private String phoneNumbers;

	/**
	 * 回执id
	 */
	@ApiModelProperty(value = "回执id")
	private String bizId;

	/**
	 * 状态（0-失败，1-成功）
	 */
	@ApiModelProperty(value = "状态（0-失败，1-成功）")
	private Integer status;

	/**
	 * 请求状态码。
	 */
	@ApiModelProperty(value = "请求状态码。")
	private String code;

	/**
	 * 任务执行信息
	 */
	@ApiModelProperty(value = "任务执行信息")
	private String message;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * sendTime
	 */
	@ApiModelProperty(value = "sendTime")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTime;

}