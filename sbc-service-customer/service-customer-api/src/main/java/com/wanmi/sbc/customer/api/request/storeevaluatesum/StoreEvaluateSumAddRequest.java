package com.wanmi.sbc.customer.api.request.storeevaluatesum;

import java.math.BigDecimal;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

/**
 * <p>店铺评价新增参数</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreEvaluateSumAddRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 店铺id
	 */
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 店铺名称
	 */
	@Length(max=150)
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
	@Max(9999999999L)
	private Integer orderNum;

	/**
	 * 评分周期 0：30天，1：90天，2：180天
	 */
	@Max(127)
	private Integer scoreCycle;

	/**
	 * 综合评分
	 */
	private BigDecimal sumCompositeScore;

}