package com.wanmi.sbc.customer.storeevaluatenum.model.root;

import java.math.BigDecimal;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>店铺统计评分等级人数统计实体类</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@Data
@Entity
@Table(name = "store_evaluate_num")
public class StoreEvaluateNum implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id 主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "num_id")
	private String numId;

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
	 * 优秀评分数（5星-4星）
	 */
	@Column(name = "excellent_num")
	private Long excellentNum;

	/**
	 * 中等评分数（3星）
	 */
	@Column(name = "medium_num")
	private Long mediumNum;

	/**
	 * 差的评分数（1星-2星）
	 */
	@Column(name = "difference_num")
	private Long differenceNum;

	/**
	 * 综合评分
	 */
	@Column(name = "sum_composite_score")
	private BigDecimal sumCompositeScore;

	/**
	 * 评分周期 0：30天，1：90天，2：180天
	 */
	@Column(name = "score_cycle")
	private Integer scoreCycle;

	/**
	 * 统计类型 0：商品评分，1：服务评分，2：物流评分
	 */
	@Column(name = "num_type")
	private Integer numType;

}