package com.wanmi.sbc.message.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.bean.enums.PushFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员推送信息VO</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@ApiModel
@Data
public class PushSendVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 友盟安卓任务ID
	 */
	@ApiModelProperty(value = "友盟安卓任务ID")
	private String androidTaskId;

	/**
	 * 友盟iOS任务ID
	 */
	@ApiModelProperty(value = "友盟iOS任务ID")
	private String iosTaskId;

	/**
	 * 消息名称
	 */
	@ApiModelProperty(value = "消息名称")
	private String msgName;

	/**
	 * 消息标题
	 */
	@ApiModelProperty(value = "消息标题")
	private String msgTitle;

	/**
	 * 消息内容
	 */
	@ApiModelProperty(value = "消息内容")
	private String msgContext;

	/**
	 * 消息封面图片
	 */
	@ApiModelProperty(value = "消息封面图片")
	private String msgImg;

	/**
	 * 点击消息跳转的页面路由
	 */
	@ApiModelProperty(value = "点击消息跳转的页面路由")
	private String msgRouter;

	/**
	 * 消息接受人 0:全部会员 1:按会员等级 2:按标签 3:按人群 4:指定会员
	 */
	@ApiModelProperty(value = "消息接受人 0:全部会员 1:按会员等级 2:按标签 3:按人群 4:指定会员")
	private Integer msgRecipient;

	/**
	 * 等级、标签、人群ID。逗号分割
	 */
	@ApiModelProperty(value = "等级、标签、人群ID。逗号分割")
	private String msgRecipientDetail;

	@ApiModelProperty(value = "等级、标签、人群对应名称。逗号分割")
	private List<String> msgRecipientNames;

	private List<String> accountList;

	/**
	 * 推送时间
	 */
	@ApiModelProperty(value = "推送时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime pushTime;

	/**
	 * 预计发送人数
	 */
	@ApiModelProperty(value = "预计发送人数")
	private Integer expectedSendCount;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "推送详情")
	private PushDetailVO androidPushDetail;

	@ApiModelProperty(value = "推送详情")
	private PushDetailVO iosPushDetail;

	@ApiModelProperty(value = "消息标签")
	private PushFlag pushFlag;

	@ApiModelProperty(value = "运营计划ID")
	private Long planId;

}