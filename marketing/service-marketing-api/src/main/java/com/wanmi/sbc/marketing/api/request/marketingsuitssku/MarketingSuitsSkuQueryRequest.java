package com.wanmi.sbc.marketing.api.request.marketingsuitssku;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>组合活动关联商品sku表通用查询请求参数</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsSkuQueryRequest extends BaseQueryRequest {


	private static final long serialVersionUID = 5863972072819949502L;
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

	/**
	 * 营销类型
	 */
	private MarketingType marketingType;

	/**
	 * 删除标志
	 */
	private DefaultFlag delFlag;


}