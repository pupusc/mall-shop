package com.wanmi.sbc.message.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;

import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.enums.NoticeType;
import com.wanmi.sbc.message.bean.enums.ReadFlag;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>App站内信消息发送表VO</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@ApiModel
@Data
public class AppMessageVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private String appMessageId;

	/**
	 * 消息一级类型：0优惠促销、1服务通知
	 */
	@ApiModelProperty(value = "消息一级类型：0优惠促销、1服务通知")
	private MessageType messageType;

	/**
	 * 消息二级类型（目前只有服务通知有分类）
	 */
	@ApiModelProperty(value = "消息二级类型")
	private NoticeType messageTypeDetail;

	/**
	 * 封面图
	 */
	@ApiModelProperty(value = "封面图")
	private String imgUrl;

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
	 * 跳转路由
	 */
	@ApiModelProperty(value = "跳转路由")
	private String routeName;

	/**
	 * 移动端路由参数
	 */
	@ApiModelProperty(value = "移动端路由参数")
	private String routeParam;

	/**
	 * PC端路由参数
	 */
	@ApiModelProperty(value = "PC端路由参数")
	private String pcRouteParam;

	/**
	 * 发送时间
	 */
	@ApiModelProperty(value = "发送时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTime;

	/**
	 * 是否已读 0：未读、1：已读
	 */
	@ApiModelProperty(value = "是否已读 0：未读、1：已读")
	private ReadFlag isRead;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	private String customerId;

	/**
	 * 关联的任务或节点id
	 */
	@ApiModelProperty(value = "关联的任务或节点id")
	private Long joinId;

}