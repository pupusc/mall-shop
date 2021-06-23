package com.wanmi.sbc.customer.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>用户分销排行榜新增参数</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@ApiModel
@Data
public class DistributionCustomerRankingAddRequest implements Serializable {


	private static final long serialVersionUID = -6702031201923073216L;
	/**
	 * 邀新人数
	 */
    @ApiModelProperty(value = "邀新人数")
	@Max(9999999999L)
	private Integer inviteCount;

	/**
	 * 有效邀新人数
	 */
    @ApiModelProperty(value = "有效邀新人数")
	@Max(9999999999L)
	private Integer inviteAvailableCount;

	/**
	 * 销售额(元) 
	 */
    @ApiModelProperty(value = "销售额(元) ")
	private BigDecimal saleAmount;

	/**
	 * 预估收益
	 */
    @ApiModelProperty(value = "预估收益")
	private BigDecimal commission;

}