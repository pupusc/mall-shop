package com.wanmi.sbc.message.api.request.pushdetail;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.bean.enums.PushStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>推送详情列表查询请求参数</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushDetailListRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-友盟推送任务IDList
	 */
	@ApiModelProperty(value = "批量查询-友盟推送任务IDList")
	private List<String> taskIdList;

	/**
	 * 友盟推送任务ID
	 */
	@ApiModelProperty(value = "友盟推送任务ID")
	private String taskId;

	/**
	 * 运营计划ID
	 */
	@ApiModelProperty(value = "运营计划ID")
	private Long planId;

	@ApiModelProperty(value = "taskId对应的平台 0：iOS 1：android")
	private PushPlatform platform;

	/**
	 * 节点ID
	 */
	@ApiModelProperty(value = "节点ID")
	private Long nodeId;

	/**
	 * 实际发送
	 */
	@ApiModelProperty(value = "实际发送")
	private Integer sendSum;

	/**
	 * 打开数
	 */
	@ApiModelProperty(value = "打开数")
	private Integer openSum;

	/**
	 * 发送状态 0-排队中, 1-发送中，2-发送完成，3-发送失败，4-消息被撤销
	 */
	@ApiModelProperty(value = "发送状态 0-排队中, 1-发送中，2-发送完成，3-发送失败，4-消息被撤销")
	private PushStatus sendStatus;

	/**
	 * 失败信息
	 */
	@ApiModelProperty(value = "失败信息")
	private String failRemark;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID")
	private String createPerson;

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
	 * 更新人ID
	 */
	@ApiModelProperty(value = "更新人ID")
	private String updatePerson;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

}