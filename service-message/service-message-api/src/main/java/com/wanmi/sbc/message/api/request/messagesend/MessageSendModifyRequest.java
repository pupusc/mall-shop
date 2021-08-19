package com.wanmi.sbc.message.api.request.messagesend;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.message.bean.enums.MessageSendType;
import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.enums.PushFlag;
import com.wanmi.sbc.message.bean.enums.SendType;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信任务表修改参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long messageId;

	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称")
	private String name;

	/**
	 * 消息类型 0优惠促销
	 */
	@ApiModelProperty(value = "消息类型 0优惠促销")
	private MessageType messageType;

	/**
	 * 消息标题
	 */
	@ApiModelProperty(value = "消息标题")
	private String title;

	/**
	 * 消息内容
	 */
	@ApiModelProperty(value = "消息内容")
	private String content;

	/**
	 * 封面图
	 */
	@ApiModelProperty(value = "封面图")
	private String imgUrl;

	/**
	 * 推送类型 0：全部会员、1：按会员等级、2：按标签、3：按人群、4：指定会员
	 */
	@ApiModelProperty(value = "推送类型 0：全部会员、1：按会员等级、2：按标签、3：按人群、4：指定会员")
	private MessageSendType sendType;

	/**
	 * 发送时间
	 */
	@ApiModelProperty(value = "发送时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人", hidden = true)
	private String updatePerson;

	/**
	 * 是否push消息标识 0：否、1：是
	 */
	@ApiModelProperty(value = "是否push消息标识 0：否、1：是")
	private PushFlag isPush;

	@ApiModelProperty(value = "推送时间类型 0：立即、1：定时")
	private SendType sendTimeType;

	@ApiModelProperty(value = "接收人列表")
	private List<String> joinIds;

	@ApiModelProperty(value = "推送id")
	private String pushId;

	/**
	 * 移动端落地页参数
	 */
	@ApiModelProperty(value = "移动端落地页参数")
	private String routeParams;

	/**
	 * 运营计划id
	 */
	@ApiModelProperty(value = "运营计划id")
	private Long planId;

	/**
	 * PC端落地页参数
	 */
	@ApiModelProperty(value = "PC端落地页参数")
	private String pcRouteParams;

}