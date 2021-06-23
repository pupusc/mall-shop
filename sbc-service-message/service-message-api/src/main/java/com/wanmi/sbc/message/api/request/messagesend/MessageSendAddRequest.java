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
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信任务表新增参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称")
	@Length(max=40)
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
	@Length(max=40)
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
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createPerson;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人", hidden = true)
	private String updatePerson;

	/**
	 * 消息同步标识 0：push消息、1：运营计划
	 */
	@ApiModelProperty(value = "消息同步标识 0：push消息、1：运营计划")
	private PushFlag pushFlag;

	/**
	 * 推送时间类型 0：立即、1：定时
	 */
	@ApiModelProperty(value = "推送时间类型 0：立即、1：定时")
	private SendType sendTimeType;

	/**
	 * 推送标识id
	 */
	@ApiModelProperty(value = "推送标识id")
	private String pushId;

	/**
	 * 运营计划id
	 */
	@ApiModelProperty(value = "运营计划id")
	private Long planId;

	/**
	 * 接收人列表
	 */
	@ApiModelProperty(value = "接收人列表")
	private List<String> joinIds;

	/**
	 * 移动端落地页参数
	 */
	@ApiModelProperty(value = "移动端落地页参数")
	private String routeParams;

	/**
	 * PC端落地页参数
	 */
	@ApiModelProperty(value = "PC端落地页参数")
	private String pcRouteParams;

}