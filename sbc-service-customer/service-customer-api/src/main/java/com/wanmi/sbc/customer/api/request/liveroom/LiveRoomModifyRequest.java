package com.wanmi.sbc.customer.api.request.liveroom;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播间修改参数</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 直播房间id
	 */
	@ApiModelProperty(value = "直播房间id")
	@NotNull
	@Max(9223372036854775807L)
	private Long roomId;

	/**
	 * 直播房间名
	 */
	@ApiModelProperty(value = "直播房间名")
	@Length(max=255)
	private String name;

	/**
	 * 是否推荐
	 */
	@ApiModelProperty(value = "是否推荐")
	@Length(max=255)
	private Integer recommend;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTime;

	/**
	 * 主播昵称
	 */
	@ApiModelProperty(value = "主播昵称")
	@Length(max=255)
	private String anchorName;

	/**
	 * 主播微信
	 */
	@ApiModelProperty(value = "主播微信")
	@Length(max=255)
	private String anchorWechat;

	/**
	 * 直播背景墙
	 */
	@ApiModelProperty(value = "直播背景墙")
	@Length(max=255)
	private String coverImg;

	/**
	 * 分享卡片封面
	 */
	@ApiModelProperty(value = "分享卡片封面")
	@Length(max=255)
	private String shareImg;

	/**
	 * 直播状态 0: 直播中, 1: 未开始, 2: 已结束, 3: 禁播, 4: 暂停中, 5: 异常, 6: 已过期
	 */
	@ApiModelProperty(value = "直播状态 0: 直播中, 1: 未开始, 2: 已结束, 3: 禁播, 4: 暂停中, 5: 异常, 6: 已过期")
	@Max(127)
	private Integer liveStatus;

	/**
	 * 直播类型，1：推流，0：手机直播
	 */
	@ApiModelProperty(value = "直播类型，1：推流，0：手机直播")
	@Max(127)
	private Integer type;

	/**
	 * 1：横屏，0：竖屏
	 */
	@ApiModelProperty(value = "1：横屏，0：竖屏")
	@Max(127)
	private Integer screenType;

	/**
	 * 1：关闭点赞 0：开启点赞，关闭后无法开启
	 */
	@ApiModelProperty(value = "1：关闭点赞 0：开启点赞，关闭后无法开启")
	@Max(127)
	private Integer closeLike;

	/**
	 * 1：关闭货架 0：打开货架，关闭后无法开启
	 */
	@ApiModelProperty(value = "1：关闭货架 0：打开货架，关闭后无法开启")
	@Max(127)
	private Integer closeGoods;

	/**
	 * 1：关闭评论 0：打开评论，关闭后无法开启
	 */
	@ApiModelProperty(value = "1：关闭评论 0：打开评论，关闭后无法开启")
	@Max(127)
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
	@Length(max=255)
	private String liveCompanyId;

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
	 * 删除人
	 */
	@ApiModelProperty(value = "删除人")
	@Length(max=255)
	private String deletePerson;

	/**
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;


}