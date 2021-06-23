package com.wanmi.sbc.customer.api.request.storeevaluate;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;

/**
 * <p>单个查询店铺评价请求参数</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateByIdRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 评价id
	 */
	@NotNull
	private String evaluateId;
}