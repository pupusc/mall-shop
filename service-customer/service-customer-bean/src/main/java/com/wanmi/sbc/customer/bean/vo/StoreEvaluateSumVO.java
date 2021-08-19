package com.wanmi.sbc.customer.bean.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>店铺评价VO</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEvaluateSumVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id 主键
	 */
	private Long sumId;

	/**
	 * 店铺id
	 */
	private Long storeId;

	/**
	 * 店铺名称
	 */
	private String storeName;

	/**
	 * 服务综合评分
	 */
	private BigDecimal sumServerScore;

	/**
	 * 商品质量综合评分
	 */
	private BigDecimal sumGoodsScore;

	/**
	 * 物流综合评分
	 */
	private BigDecimal sumLogisticsScoreScore;

	/**
	 * 订单数
	 */
	private Integer orderNum;

	/**
	 * 评分周期 0：30天，1：90天，2：180天
	 */
	private Integer scoreCycle;

	/**
	 * 综合评分
	 */
	private BigDecimal sumCompositeScore;

}