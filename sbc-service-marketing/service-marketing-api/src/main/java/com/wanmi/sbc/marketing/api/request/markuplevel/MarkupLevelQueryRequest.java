package com.wanmi.sbc.marketing.api.request.markuplevel;

import java.math.BigDecimal;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>加价购活动通用查询请求参数</p>
 * @author he
 * @date 2021-02-04 16:11:01
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkupLevelQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-加价购阶梯idList
	 */
	@ApiModelProperty(value = "批量查询-加价购阶梯idList")
	private List<Long> idList;

	/**
	 * 加价购阶梯id
	 */
	@ApiModelProperty(value = "加价购阶梯id")
	private Long id;

	/**
	 * 加价购id
	 */
	@ApiModelProperty(value = "加价购id")
	private Long markupId;
	/**
	 * 加价购id 批量
	 */
	@ApiModelProperty(value = "加价购id")
	private List<Long> markupIds;

	/**
	 * 加价购阶梯满足金额
	 */
	@ApiModelProperty(value = "加价购阶梯满足金额")
	private BigDecimal levelAmount;

}