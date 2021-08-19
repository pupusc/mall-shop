package com.wanmi.sbc.customer.api.request.storelevel;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;

/**
 * <p>单个查询商户客户等级表请求参数</p>
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelByIdRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@NotNull
	private Long storeLevelId;
}