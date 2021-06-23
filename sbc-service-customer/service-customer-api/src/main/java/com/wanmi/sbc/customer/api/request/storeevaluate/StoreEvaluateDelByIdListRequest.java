package com.wanmi.sbc.customer.api.request.storeevaluate;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;

/**
 * <p>批量删除店铺评价请求参数</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateDelByIdListRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-评价idList
	 */
	@NotEmpty
	private List<String> evaluateIdList;
}