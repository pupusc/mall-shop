package com.wanmi.sbc.marketing.api.request.marketingsuits;

import java.math.BigDecimal;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>组合商品主表通用查询请求参数</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键idList
	 */
	@ApiModelProperty(value = "批量查询-主键idList")
	private List<Long> idList;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 促销id
	 */
	@ApiModelProperty(value = "促销id")
	private Long marketingId;

	/**
	 * 套餐主图（图片url全路径）
	 */
	@ApiModelProperty(value = "套餐主图（图片url全路径）")
	private String mainImage;

	/**
	 * 套餐价格
	 */
	@ApiModelProperty(value = "套餐价格")
	private BigDecimal suitsPrice;

}