package com.wanmi.sbc.message.api.request.smssenddetail;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.message.bean.enums.SendDetailStatus;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信发送通用查询请求参数</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Long> idList;

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
	private SendDetailStatus status;

	/**
	 * 状态（0-失败，1-成功）
	 */
	@ApiModelProperty(value = "状态（0-失败，1-成功）")
	private SendDetailStatus notStatus;

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
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 搜索条件:sendTime开始
	 */
	@ApiModelProperty(value = "搜索条件:sendTime开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTimeBegin;
	/**
	 * 搜索条件:sendTime截止
	 */
	@ApiModelProperty(value = "搜索条件:sendTime截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTimeEnd;



}