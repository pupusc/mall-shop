package com.wanmi.sbc.goods.cyclebuy.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import com.wanmi.sbc.goods.bean.enums.GiftGiveMethod;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>周期购活动实体类</p>
 * @author weiwenhao
 * @date 2021-01-21 20:01:50
 */
@Data
@Entity
@Table(name = "cycle_buy")
public class CycleBuy implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 周期购Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 关联商品Id
	 */
	@Column(name = "origin_goods_id")
	private String originGoodsId;

	/**
	 * 创建商品ID
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 周期购活动名称
	 */
	@Column(name = "activity_name")
	private String activityName;

	/**
	 * 活动文案
	 */
	@Column(name = "record_activities")
	private String recordActivities;

	/**
	 * 配送方案 0:商家主导配送 1:客户主导配送
	 */
	@Column(name = "delivery_plan")
	private DeliveryPlan deliveryPlan;

	/**
	 * 赠送方式：0：默认全送 1:可选一种
	 */
	@Column(name = "gift_give_method")
	private GiftGiveMethod giftGiveMethod;

	/**
	 * 配送周期 0:每日一期 1:每周一期 2:每月一期
	 */
	@Column(name = "delivery_cycle")
	@Enumerated
	private DeliveryCycle deliveryCycle;

	/**
	 * 发货日期:1.使用逗号拼接发送日期
	 */
	@Column(name = "send_date_rule")
	private String sendDateRule;

	/**
	 * 周期购运费 =0:每期运费x期数， >0:满X期包邮
	 */
	@Column(name = "cycle_freight_type")
	private Integer cycleFreightType;

	/**
	 * 上下架 0：下架 1:上架
	 */
	@Column(name = "added_flag")
	private AddedFlag addedFlag;

	/**
	 * 商铺Id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 删除标记  0：正常，1：删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * createPerson
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * updatePerson
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

	/**
	 * deletePerson
	 */
	@Column(name = "delete_person")
	private String deletePerson;

}