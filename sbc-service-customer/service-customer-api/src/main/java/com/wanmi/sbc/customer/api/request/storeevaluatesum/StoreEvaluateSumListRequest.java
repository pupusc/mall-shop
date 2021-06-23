package com.wanmi.sbc.customer.api.request.storeevaluatesum;

import java.math.BigDecimal;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import java.util.List;

/**
 * <p>店铺评价列表查询请求参数</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateSumListRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-id 主键List
	 */
	private List<Long> sumIdList;

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