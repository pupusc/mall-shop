package com.wanmi.sbc.message.bean.vo;

import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.bean.enums.PushStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>推送详情VO</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@ApiModel
@Data
public class PushDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 友盟推送任务ID
	 */
	@ApiModelProperty(value = "友盟推送任务ID")
	private String taskId;

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

	@ApiModelProperty(value = "运营计划ID")
	private Long planId;

}