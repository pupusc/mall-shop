package com.wanmi.sbc.marketing.api.request.grouponrecord;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;

/**
 * <p>拼团活动参团信息表通用查询请求参数</p>
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponRecordQueryRequest extends BaseQueryRequest {


	private static final long serialVersionUID = 4701296587227929123L;

	/**
	 * 拼团活动ID
	 */
	private String grouponActivityId;

	/**
	 * 会员ID
	 */
	private String customerId;

	/**
	 * SPU编号
	 */
	private String goodsId;

	/**
	 * sku编号
	 */
	private String goodsInfoId;


}