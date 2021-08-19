package com.wanmi.sbc.marketing.api.request.marketingsuitssku;

import java.math.BigDecimal;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>组合活动关联商品sku表分页查询请求参数</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsSkuPageRequest extends BaseQueryRequest {
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
	 * 组合id
	 */
	@ApiModelProperty(value = "组合id")
	private Long suitsId;

	/**
	 * 促销活动id
	 */
	@ApiModelProperty(value = "促销活动id")
	private Long marketingId;

	/**
	 * skuId
	 */
	@ApiModelProperty(value = "skuId")
	private String skuId;

	/**
	 * 单个优惠价格（优惠多少）
	 */
	@ApiModelProperty(value = "单个优惠价格（优惠多少）")
	private BigDecimal discountPrice;

	/**
	 * sku数量
	 */
	@ApiModelProperty(value = "sku数量")
	private Long num;

}