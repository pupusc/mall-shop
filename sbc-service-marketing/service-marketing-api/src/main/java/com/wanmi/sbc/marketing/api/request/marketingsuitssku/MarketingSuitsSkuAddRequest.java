package com.wanmi.sbc.marketing.api.request.marketingsuitssku;

import java.math.BigDecimal;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>组合活动关联商品sku表新增参数</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsSkuAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 组合id
	 */
	@ApiModelProperty(value = "组合id")
	@Max(9223372036854775807L)
	private Long suitsId;

	/**
	 * 促销活动id
	 */
	@ApiModelProperty(value = "促销活动id")
	@Max(9223372036854775807L)
	private Long marketingId;

	/**
	 * skuId
	 */
	@ApiModelProperty(value = "skuId")
	@Length(max=32)
	private String skuId;

	/**
	 * 单个优惠价格
	 */
	@ApiModelProperty(value = "单个优惠价格")
	private BigDecimal discountPrice;

	/**
	 * sku数量
	 */
	@ApiModelProperty(value = "sku数量")
	@Max(9223372036854775807L)
	private Long num;

}