package com.wanmi.sbc.marketing.markup.model.root;

import java.math.BigDecimal;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>加价购活动实体类</p>
 * @author he
 * @date 2021-02-04 16:11:24
 */
@Data
@Entity
@Table(name = "markup_level_detail")
public class MarkupLevelDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 加价购阶梯详情id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 加价购活动关联id
	 */
	@Column(name = "markup_id")
	private Long markupId;

	/**
	 * 加价购阶梯关联id
	 */
	@Column(name = "markup_level_id")
	private Long markupLevelId;

	/**
	 * 加购商品加购价格
	 */
	@Column(name = "markup_price")
	private BigDecimal markupPrice;

	/**
	 * 加购商品关联sku 
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

}