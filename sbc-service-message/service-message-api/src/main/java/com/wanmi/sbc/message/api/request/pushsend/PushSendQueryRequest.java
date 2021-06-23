package com.wanmi.sbc.message.api.request.pushsend;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.bean.enums.PushFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员推送信息通用查询请求参数</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendQueryRequest extends BaseQueryRequest {
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

	/**
	 * 搜索条件:推送时间开始
	 */
	@ApiModelProperty(value = "搜索条件:推送时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime pushTimeBegin;
	/**
	 * 搜索条件:推送时间截止
	 */
	@ApiModelProperty(value = "搜索条件:推送时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime pushTimeEnd;

	/**
	 * 预计发送人数
	 */
	@ApiModelProperty(value = "预计发送人数")
	private Integer expectedSendCount;

	/**
	 * 删除标志 0:未删除 1:已删除
	 */
	@ApiModelProperty(value = "删除标志 0:未删除 1:已删除")
	private DeleteFlag delFlag;

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

	@ApiModelProperty(value = "消息标签")
	private PushFlag pushFlag;

}