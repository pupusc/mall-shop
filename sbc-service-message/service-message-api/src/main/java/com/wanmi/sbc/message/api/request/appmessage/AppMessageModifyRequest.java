package com.wanmi.sbc.message.api.request.appmessage;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>App站内信消息发送表修改参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMessageModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@Length(max=32)
	private String appMessageId;

	/**
	 * 消息一级类型：0优惠促销、1服务通知
	 */
	@ApiModelProperty(value = "消息一级类型：0优惠促销、1服务通知")
	@Max(9999999999L)
	private Integer messageType;

	/**
	 * 消息二级类型
	 */
	@ApiModelProperty(value = "消息二级类型")
	@Max(9999999999L)
	private Integer messageTypeDetail;

	/**
	 * 封面图
	 */
	@ApiModelProperty(value = "封面图")
	@Length(max=255)
	private String imgUrl;

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
	@Length(max=255)
	private String content;

	/**
	 * 跳转路由
	 */
	@ApiModelProperty(value = "跳转路由")
	@Length(max=50)
	private String routeName;

	/**
	 * 移动端路由参数
	 */
	@ApiModelProperty(value = "移动端路由参数")
	@Length(max=255)
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
	@Max(9999999999L)
	private Integer isRead;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人", hidden = true)
	private String updatePerson;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	@Length(max=32)
	private String customerId;

	/**
	 * 消息任务id
	 */
	@ApiModelProperty(value = "消息任务id")
	@Max(9223372036854775807L)
	private Long messageSendId;

	/**
	 * 节点id
	 */
	@ApiModelProperty(value = "节点id")
	@Max(9223372036854775807L)
	private Long nodeId;

}