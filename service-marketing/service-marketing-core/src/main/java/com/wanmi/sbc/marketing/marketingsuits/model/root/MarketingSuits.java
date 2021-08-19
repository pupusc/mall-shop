package com.wanmi.sbc.marketing.marketingsuits.model.root;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * <p>组合商品主表实体类</p>
 * @author zhk
 * @date 2020-04-02 10:39:15
 */
@Data
@Entity
@Table(name = "marketing_suits")
public class MarketingSuits {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 促销id
	 */
	@Column(name = "marketing_id")
	private Long marketingId;

	/**
	 * 套餐主图（图片url全路径）
	 */
	@Column(name = "main_image")
	private String mainImage;

	/**
	 * 套餐价格
	 */
	@Column(name = "suits_price")
	private BigDecimal suitsPrice;

}