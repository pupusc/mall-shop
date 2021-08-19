package com.wanmi.sbc.marketing.api.request.grouponrecord;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>拼团活动参团信息表列表查询请求参数</p>
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponRecordListRequest  {

	/**
	 * 批量查询-grouponRecordIdList
	 */
	private List<String> grouponRecordIdList;

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