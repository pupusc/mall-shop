package com.wanmi.sbc.goods.liveroomlivegoodsrel.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;

/**
 * <p>直播房间和直播商品关联表实体类</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@Data
@Entity
@Table(name = "live_room_live_goods_rel")
public class LiveRoomLiveGoodsRel extends BaseEntity {
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
	 * 直播商品id
	 */
	@Column(name = "goods_id")
	private Long goodsId;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;


	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;





}