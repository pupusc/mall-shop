package com.wanmi.sbc.marketing.marketingsuitssku.model.root;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * <p>组合活动关联商品sku表实体类</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@Data
@Entity
@Table(name = "marketing_suits_sku")
public class MarketingSuitsSku  {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 组合id
	 */
	@Column(name = "suits_id")
	private Long suitsId;

	/**
	 * 促销活动id
	 */
	@Column(name = "marketing_id")
	private Long marketingId;

	/**
	 * skuId
	 */
	@Column(name = "sku_id")
	private String skuId;

	/**
	 * 单个优惠价格
	 */
	@Column(name = "discount_price")
	private BigDecimal discountPrice;

	/**
	 * sku数量
	 */
	@Column(name = "num")
	private Long num;

}