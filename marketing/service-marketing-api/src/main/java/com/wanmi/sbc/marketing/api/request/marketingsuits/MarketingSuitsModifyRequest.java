package com.wanmi.sbc.marketing.api.request.marketingsuits;

import java.math.BigDecimal;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>组合商品主表修改参数</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 促销id
	 */
	@ApiModelProperty(value = "促销id")
	@NotNull
	@Max(9223372036854775807L)
	private Long marketingId;

	/**
	 * 套餐主图（图片url全路径）
	 */
	@ApiModelProperty(value = "套餐主图（图片url全路径）")
	@Length(max=255)
	private String mainImage;

	/**
	 * 套餐价格
	 */
	@ApiModelProperty(value = "套餐价格")
	private BigDecimal suitsPrice;

}