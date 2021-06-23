package com.wanmi.sbc.customer.api.request.storeevaluatenum;

import java.math.BigDecimal;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

/**
 * <p>店铺统计评分等级人数统计修改参数</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreEvaluateNumModifyRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id 主键
	 */
	@Length(max=32)
	private String numId;

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
	 * 优秀评分数（5星-4星）
	 */
	@Max(9223372036854775807L)
	private Long excellentNum;

	/**
	 * 中等评分数（3星）
	 */
	@Max(9223372036854775807L)
	private Long mediumNum;

	/**
	 * 差的评分数（1星-2星）
	 */
	@Max(9223372036854775807L)
	private Long differenceNum;

	/**
	 * 综合评分
	 */
	private BigDecimal sumCompositeScore;

	/**
	 * 评分周期 0：30天，1：90天，2：180天
	 */
	@Max(127)
	private Integer scoreCycle;

	/**
	 * 统计类型 0：商品评分，1：服务评分，2：物流评分
	 */
	@Max(127)
	private Integer numType;

}