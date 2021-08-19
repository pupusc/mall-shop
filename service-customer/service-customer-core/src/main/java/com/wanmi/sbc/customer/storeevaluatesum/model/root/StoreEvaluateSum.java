package com.wanmi.sbc.customer.storeevaluatesum.model.root;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>店铺评价实体类</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@Data
@Entity
@Table(name = "store_evaluate_sum")
public class StoreEvaluateSum implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sum_id")
	private Long sumId;

	/**
	 * 店铺id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 店铺名称
	 */
	@Column(name = "store_name")
	private String storeName;

	/**
	 * 服务综合评分
	 */
	@Column(name = "sum_server_score")
	private BigDecimal sumServerScore;

	/**
	 * 商品质量综合评分
	 */
	@Column(name = "sum_goods_score")
	private BigDecimal sumGoodsScore;

	/**
	 * 物流综合评分
	 */
	@Column(name = "sum_logistics_score_score")
	private BigDecimal sumLogisticsScoreScore;

	/**
	 * 订单数
	 */
	@Column(name = "order_num")
	private Integer orderNum;

	/**
	 * 评分周期 0：30天，1：90天，2：180天
	 */
	@Column(name = "score_cycle")
	private Integer scoreCycle;

	/**
	 * 综合评分
	 */
	@Column(name = "sum_composite_score")
	private BigDecimal sumCompositeScore;

}