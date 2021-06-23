package com.wanmi.sbc.customer.api.request.paidcardbuyrecord;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询付费会员请求参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardBuyRecordByIdRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 流水号
	 */
	@ApiModelProperty(value = "流水号")
	@NotNull
	private String payCode;
}