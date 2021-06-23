package com.wanmi.sbc.goods.goodsrestrictedsale.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.enums.AssignPersonRestrictedType;
import com.wanmi.sbc.goods.bean.enums.PersonRestrictedCycle;
import com.wanmi.sbc.goods.bean.enums.PersonRestrictedType;
import com.wanmi.sbc.goods.bean.enums.RestrictedType;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.model.root.GoodsRestrictedCustomerRela;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>限售配置实体类</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@Data
@Entity
@Table(name = "goods_restricted_sale")
public class GoodsRestrictedSale implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 限售主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restricted_id")
	private Long restrictedId;

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 货品的skuId
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

	/**
	 * 货品信息
	 */
	@OneToOne
	@JoinColumn(name = "goods_info_id", insertable = false, updatable = false)
	private GoodsInfo goodsInfo;

	/**
	 * 限售方式 0: 按会员 1：按订单
	 */
	@Column(name = "restricted_type")
	private RestrictedType restrictedType;

	/**
	 * 是否每人限售标识 
	 */
	@Column(name = "restricted_pre_person_flag")
	private DefaultFlag restrictedPrePersonFlag;

	/**
	 * 是否每单限售的标识
	 */
	@Column(name = "restricted_pre_order_flag")
	private DefaultFlag restrictedPreOrderFlag;

	/**
	 * 是否指定会员限售的标识
	 */
	@Column(name = "restricted_assign_flag")
	private DefaultFlag restrictedAssignFlag;

	/**
	 * 个人限售的方式(  0:终生限售  1:周期限售)
	 */
	@Column(name = "person_restricted_type")
	private PersonRestrictedType personRestrictedType;

	/**
	 * 个人限售的周期 (0:天  1:周  2:月  3:年)
	 */
	@Column(name = "person_restricted_cycle")
	private PersonRestrictedCycle personRestrictedCycle;

	/**
	 * 限售数量
	 */
	@Column(name = "restricted_num")
	private Long restrictedNum;

	/**
	 * 起售数量
	 */
	@Column(name = "start_sale_num")
	private Long startSaleNum;

	/**
	 * 特定会员的限售类型 0: 会员等级  1：指定会员
	 */
	@Column(name = "assign_person_restricted_type")
	private AssignPersonRestrictedType assignPersonRestrictedType;

	/**
	 * 限售配置会员关系表
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "restricted_id",name = "restricted_id",insertable = false, updatable = false)
	private List<GoodsRestrictedCustomerRela> goodsRestrictedCustomerRelas;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 删除标识
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 是否打开限售方式开关
	 */
	@Column(name = "restricted_way")
	@Enumerated
	private DefaultFlag restrictedWay;

	/**
	 * 是否打开起售数量开关
	 */
	@Column(name = "restricted_start_num")
	@Enumerated
	private DefaultFlag restrictedStartNum;
}