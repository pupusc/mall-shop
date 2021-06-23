package com.wanmi.sbc.message.api.request.appmessage;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.enums.NoticeType;
import com.wanmi.sbc.message.bean.enums.ReadFlag;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>App站内信消息发送表通用查询请求参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMessageQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键idList
	 */
	@ApiModelProperty(value = "批量查询-主键idList")
	private List<String> appMessageIdList;

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
	 * 消息二级类型
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
	 * 搜索条件:发送时间开始
	 */
	@ApiModelProperty(value = "搜索条件:发送时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTimeBegin;
	/**
	 * 搜索条件:发送时间截止
	 */
	@ApiModelProperty(value = "搜索条件:发送时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTimeEnd;

	/**
	 * 是否已读 0：未读、1：已读
	 */
	@ApiModelProperty(value = "是否已读 0：未读、1：已读")
	private ReadFlag isRead;

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
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 搜索条件:修改时间开始
	 */
	@ApiModelProperty(value = "搜索条件:修改时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:修改时间截止
	 */
	@ApiModelProperty(value = "搜索条件:修改时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updatePerson;

	/**
	 * 删除标识 0：未删除、1：删除
	 */
	@ApiModelProperty(value = "删除标识 0：未删除、1：删除")
	private DeleteFlag delFlag;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	private String customerId;

	/**
	 * 消息任务id
	 */
	@ApiModelProperty(value = "消息任务id")
	private Long messageSendId;

	/**
	 * 节点id
	 */
	@ApiModelProperty(value = "节点id")
	private Long nodeId;

}