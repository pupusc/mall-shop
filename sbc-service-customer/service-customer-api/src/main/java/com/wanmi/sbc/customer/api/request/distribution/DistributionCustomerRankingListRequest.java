package com.wanmi.sbc.customer.api.request.distribution;

import java.math.BigDecimal;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.util.List;

/**
 * <p>用户分销排行榜列表查询请求参数</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerRankingListRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-会员IDList
	 */
	@ApiModelProperty(value = "批量查询-会员IDList")
	private List<String> customerIdList;

	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	private String customerId;

	/**
	 * 邀新人数
	 */
	@ApiModelProperty(value = "邀新人数")
	private Integer inviteCount;

	/**
	 * 有效邀新人数
	 */
	@ApiModelProperty(value = "有效邀新人数")
	private Integer inviteAvailableCount;

	/**
	 * 销售额(元) 
	 */
	@ApiModelProperty(value = "销售额(元)")
	private BigDecimal saleAmount;

	/**
	 * 预估收益
	 */
	@ApiModelProperty(value = "预估收益")
	private BigDecimal commission;

}