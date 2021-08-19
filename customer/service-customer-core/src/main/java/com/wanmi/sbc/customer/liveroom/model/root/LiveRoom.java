package com.wanmi.sbc.customer.liveroom.model.root;

import com.wanmi.sbc.customer.bean.enums.LiveRoomStatus;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>直播间实体类</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@Data
@Entity
@Table(name = "live_room")
public class LiveRoom extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 直播房间id
	 */
	@Column(name = "room_id")
	private Long roomId;

	/**
	 * 直播房间名
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 是否推荐
	 */
	@Column(name = "recommend")
	private Integer recommend;

	/**
	 * 开始时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "start_time")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "end_time")
	private LocalDateTime endTime;

	/**
	 * 主播昵称
	 */
	@Column(name = "anchor_name")
	private String anchorName;

	/**
	 * 主播微信
	 */
	@Column(name = "anchor_wechat")
	private String anchorWechat;

	/**
	 * 直播背景墙
	 */
	@Column(name = "cover_img")
	private String coverImg;

	/**
	 * 分享卡片封面
	 */
	@Column(name = "share_img")
	private String shareImg;

	/**
	 * 直播状态 0: 直播中, 1: 暂停, 2: 异常, 3: 未开始, 4: 已结束, 5: 禁播, 6: 已过期
	 */
	@Column(name = "live_status")
	private LiveRoomStatus liveStatus;

	/**
	 * 直播类型，1：推流，0：手机直播
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 1：横屏，0：竖屏
	 */
	@Column(name = "screen_type")
	private Integer screenType;

	/**
	 * 1：关闭点赞 0：开启点赞，关闭后无法开启
	 */
	@Column(name = "close_like")
	private Integer closeLike;

	/**
	 * 1：关闭货架 0：打开货架，关闭后无法开启
	 */
	@Column(name = "close_goods")
	private Integer closeGoods;

	/**
	 * 1：关闭评论 0：打开评论，关闭后无法开启
	 */
	@Column(name = "close_comment")
	private Integer closeComment;

	/**
	 * 店铺id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 直播商户id
	 */
	@Column(name = "live_company_id")
	private String liveCompanyId;

	/**
	 * 删除人
	 */
	@Column(name = "delete_person")
	private String deletePerson;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}