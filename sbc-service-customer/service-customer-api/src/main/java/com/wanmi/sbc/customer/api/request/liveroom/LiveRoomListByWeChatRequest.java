package com.wanmi.sbc.customer.api.request.liveroom;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>查询直播间列表</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomListByWeChatRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键idList
	 */
	@ApiModelProperty(value = "批量查询-主键idList")
	private List<Long> idList;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 直播房间id
	 */
	@ApiModelProperty(value = "直播房间id")
	private Long roomId;

	/**
	 * 直播房间名
	 */
	@ApiModelProperty(value = "直播房间名")
	private String name;

	/**
	 * 是否推荐
	 */
	@ApiModelProperty(value = "是否推荐")
	private Integer recommend;

	/**
	 * 搜索条件:开始时间开始
	 */
	@ApiModelProperty(value = "搜索条件:开始时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime startTimeBegin;
	/**
	 * 搜索条件:开始时间截止
	 */
	@ApiModelProperty(value = "搜索条件:开始时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime startTimeEnd;

	/**
	 * 搜索条件:结束时间开始
	 */
	@ApiModelProperty(value = "搜索条件:结束时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTimeBegin;
	/**
	 * 搜索条件:结束时间截止
	 */
	@ApiModelProperty(value = "搜索条件:结束时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTimeEnd;

	/**
	 * 主播昵称
	 */
	@ApiModelProperty(value = "主播昵称")
	private String anchorName;

	/**
	 * 主播微信
	 */
	@ApiModelProperty(value = "主播微信")
	private String anchorWechat;

	/**
	 * 直播背景墙
	 */
	@ApiModelProperty(value = "直播背景墙")
	private String coverImg;

	/**
	 * 分享卡片封面
	 */
	@ApiModelProperty(value = "分享卡片封面")
	private String shareImg;

	/**
	 * 直播状态 0: 直播中, 1: 未开始, 2: 已结束, 3: 禁播, 4: 暂停中, 5: 异常, 6: 已过期
	 */
	@ApiModelProperty(value = "直播状态 0: 直播中, 1: 未开始, 2: 已结束, 3: 禁播, 4: 暂停中, 5: 异常, 6: 已过期")
	private Integer liveStatus;

	/**
	 * 直播类型，1：推流，0：手机直播
	 */
	@ApiModelProperty(value = "直播类型，1：推流，0：手机直播")
	private Integer type;

	/**
	 * 1：横屏，0：竖屏
	 */
	@ApiModelProperty(value = "1：横屏，0：竖屏")
	private Integer screenType;

	/**
	 * 1：关闭点赞 0：开启点赞，关闭后无法开启
	 */
	@ApiModelProperty(value = "1：关闭点赞 0：开启点赞，关闭后无法开启")
	private Integer closeLike;

	/**
	 * 1：关闭货架 0：打开货架，关闭后无法开启
	 */
	@ApiModelProperty(value = "1：关闭货架 0：打开货架，关闭后无法开启")
	private Integer closeGoods;

	/**
	 * 1：关闭评论 0：打开评论，关闭后无法开启
	 */
	@ApiModelProperty(value = "1：关闭评论 0：打开评论，关闭后无法开启")
	private Integer closeComment;

	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id")
	private Long storeId;

	/**
	 * 直播商户id
	 */
	@ApiModelProperty(value = "直播商户id")
	private String liveCompanyId;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
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
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updatePerson;

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
	 * 删除人
	 */
	@ApiModelProperty(value = "删除人")
	private String deletePerson;

	/**
	 * 搜索条件:删除时间开始
	 */
	@ApiModelProperty(value = "搜索条件:删除时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTimeBegin;
	/**
	 * 搜索条件:删除时间截止
	 */
	@ApiModelProperty(value = "搜索条件:删除时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTimeEnd;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识,0:未删除1:已删除")
	private DeleteFlag delFlag;


	/**
	 * 起始拉取房间
	 */
	@ApiModelProperty(value = "起始拉取房间")
	@NotNull
	private Integer start;

	/**
	 * 每次拉取的个数上限
	 */
	@ApiModelProperty(value = "每次拉取的个数上限", hidden = true)
	private Integer limit;



}