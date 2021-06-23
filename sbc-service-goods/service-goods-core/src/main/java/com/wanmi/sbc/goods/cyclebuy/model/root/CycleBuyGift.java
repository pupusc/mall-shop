package com.wanmi.sbc.goods.cyclebuy.model.root;


import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>周期购活动实体类</p>
 * @author weiwenhao
 * @date 2021-01-21 19:42:48
 */
@Data
@Entity
@Table(name = "cycle_buy_gift")
public class CycleBuyGift implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 周期购赠品表Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 关联周期购主键Id
	 */
	@Column(name = "cycle_buy_id")
	private Long cycleBuyId;

	/**
	 * 赠送商品Id
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

	/**
	 * 赠品数量
	 */
	@Column(name = "free_quantity")
	private Long freeQuantity;

}