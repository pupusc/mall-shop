package com.wanmi.sbc.crm.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>运营计划App通知参数</p>
 * @author dyt
 * @date 2020-01-10 11:14:29
 */
@ApiModel
@Data
public class CustomerPlanAppPushDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称")
	private String name;

	/**
	 * 消息标题
	 */
	@ApiModelProperty(value = "消息标题")
	private String noticeTitle;

	/**
	 * 消息内容
	 */
	@ApiModelProperty(value = "消息内容")
	private String noticeContext;

	/**
	 * 封面地址
	 */
	@ApiModelProperty(value = "封面地址")
	private String coverUrl;

	/**
	 * 落页地地址
	 */
	@ApiModelProperty(value = "落页地地址")
	private String pageUrl;
}