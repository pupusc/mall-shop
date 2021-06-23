package com.wanmi.sbc.marketing.api.request.markupleveldetail;

import java.math.BigDecimal;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>加价购活动通用查询请求参数</p>
 * @author he
 * @date 2021-02-04 16:11:24
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkupLevelDetailQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-加价购阶梯详情idList
	 */
	@ApiModelProperty(value = "批量查询-加价购阶梯详情idList")
	private List<Long> idList;

	/**
	 * 加价购阶梯详情id
	 */
	@ApiModelProperty(value = "加价购阶梯详情id")
	private Long id;

	/**
	 * 加价购活动关联id
	 */
	@ApiModelProperty(value = "加价购活动关联id")
	private Long markupId;

	/**
	 * 加价购阶梯关联id
	 */
	@ApiModelProperty(value = "加价购阶梯关联id")
	private Long markupLevelId;

	/**
	 * 加购商品加购价格
	 */
	@ApiModelProperty(value = "加购商品加购价格")
	private BigDecimal markupPrice;

	/**
	 * 加购商品关联sku 
	 */
	@ApiModelProperty(value = "加购商品关联sku ")
	private String goodsInfoId;

}