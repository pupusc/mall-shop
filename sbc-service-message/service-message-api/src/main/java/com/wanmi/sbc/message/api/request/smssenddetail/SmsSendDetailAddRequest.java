package com.wanmi.sbc.message.api.request.smssenddetail;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信发送新增参数</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailAddRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 发送任务id
	 */
	@ApiModelProperty(value = "发送任务id")
	@Max(9223372036854775807L)
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
	@Length(max=64)
	private String bizId;

	/**
	 * 状态（0-失败，1-成功）
	 */
	@ApiModelProperty(value = "状态（0-失败，1-成功）")
	@Max(127)
	private Integer status;

	/**
	 * 请求状态码。
	 */
	@ApiModelProperty(value = "请求状态码。")
	@Length(max=255)
	private String code;

	/**
	 * 任务执行信息
	 */
	@ApiModelProperty(value = "任务执行信息")
	@Length(max=2000)
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