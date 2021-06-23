package com.wanmi.sbc.customer.api.request.storeevaluatenum;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;

/**
 * <p>单个查询店铺统计评分等级人数统计请求参数</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateNumByIdRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id 主键
	 */
	@NotNull
	private String numId;
}