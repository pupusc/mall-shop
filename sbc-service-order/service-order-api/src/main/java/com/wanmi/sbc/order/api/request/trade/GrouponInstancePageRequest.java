package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import lombok.*;

/**
 * <p>团明细</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponInstancePageRequest  extends BaseQueryRequest {


	private static final long serialVersionUID = 208932344606542513L;
	/**
	 * 团号
	 */
	private String grouponNo;

	/**
	 * 拼团活动id
	 */
	private String grouponActivityId;

	/**
	 * 拼团状态
	 */
	private GrouponOrderStatus grouponStatus;


	/**
	 * 团长用户id
	 */
	private String customerId;

}