package com.wanmi.sbc.customer.bean.vo;

import java.math.BigDecimal;
import lombok.Data;
import java.io.Serializable;

/**
 * <p>店铺统计评分等级人数统计VO</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@Data
public class StoreEvaluateNumVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id 主键
	 */
	private String numId;

	/**
	 * 店铺id
	 */
	private Long storeId;

	/**
	 * 店铺名称
	 */
	private String storeName;

	/**
	 * 优秀评分数（5星-4星）
	 */
	private Long excellentNum;

	/**
	 * 中等评分数（3星）
	 */
	private Long mediumNum;

	/**
	 * 差的评分数（1星-2星）
	 */
	private Long differenceNum;

	/**
	 * 综合评分
	 */
	private BigDecimal sumCompositeScore;

	/**
	 * 评分周期 0：30天，1：90天，2：180天
	 */
	private Integer scoreCycle;

	/**
	 * 统计类型 0：商品评分，1：服务评分，2：物流评分
	 */
	private Integer numType;

}