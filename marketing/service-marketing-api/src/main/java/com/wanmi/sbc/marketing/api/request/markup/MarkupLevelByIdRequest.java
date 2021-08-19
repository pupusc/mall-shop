package com.wanmi.sbc.marketing.api.request.markup;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.marketing.api.request.market.MarketingBaseRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询加价购活动请求参数</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkupLevelByIdRequest extends MarketingIdRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 客户信息
	 */
	@ApiModelProperty(value = "客户信息")
	private CustomerDTO customer;

	@ApiModelProperty(value = "客户id")
	private String customerId;
}