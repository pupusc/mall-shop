package com.wanmi.sbc.customer.api.request.storeevaluatesum;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;

/**
 * <p>批量删除店铺评价请求参数</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateSumDelByIdListRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-id 主键List
	 */
	@NotEmpty
	private List<Long> sumIdList;
}