package com.wanmi.sbc.customer.liveroomreplay.model.root;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>直播回放实体类</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@Data
@Entity
@Table(name = "live_room_replay")
public class LiveRoomReplay extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 视频过期时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "expire_time")
	private LocalDateTime expireTime;

	/**
	 * 视频回放路径
	 */
	@Column(name = "media_url")
	private String mediaUrl;

	/**
	 * 直播房间id
	 */
	@Column(name = "room_id")
	private Long roomId;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

	/**
	 * 删除人
	 */
	@Column(name = "delete_person")
	private String deletePerson;

	/**
	 * 删除逻辑 0：未删除 1 已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}