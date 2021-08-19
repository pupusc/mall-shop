package com.wanmi.sbc.customer.api.request.storeevaluatenum;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;

/**
 * <p>批量删除店铺统计评分等级人数统计请求参数</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateNumDelByIdListRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-id 主键List
	 */
	@NotEmpty
	private List<String> numIdList;
}