package com.wanmi.sbc.customer.api.request.storeevaluatesum;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;

/**
 * <p>单个查询店铺评价请求参数</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateSumByIdRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id 主键
	 */
	@NotNull
	private Long sumId;
}