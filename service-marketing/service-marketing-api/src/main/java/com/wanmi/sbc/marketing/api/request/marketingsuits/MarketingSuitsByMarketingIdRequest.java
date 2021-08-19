package com.wanmi.sbc.marketing.api.request.marketingsuits;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>单个查询组合商品主表请求参数</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsByMarketingIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 促销id
	 */
	@ApiModelProperty(value = "促销id")
	@NotNull
	private Long marketingId;

}