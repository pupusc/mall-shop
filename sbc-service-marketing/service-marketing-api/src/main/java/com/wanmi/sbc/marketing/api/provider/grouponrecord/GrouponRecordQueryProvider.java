package com.wanmi.sbc.marketing.api.provider.grouponrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordByCustomerRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordListRequest;
import com.wanmi.sbc.marketing.api.response.grouponrecord.GrouponRecordByCustomerResponse;
import com.wanmi.sbc.marketing.api.response.grouponrecord.GrouponRecordListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>参团拼团信息表查询服务Provider</p>
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponRecordQueryProvider")
public interface GrouponRecordQueryProvider {
	/**
	 * sku维度单个查询C端用户参团拼团活动信息表API
	 *
	 * @param  grouponRecordByCustomerRequest 单个查询拼团活动参团信息表请求参数 {@link GrouponRecordByCustomerRequest}
	 * @return 拼团活动参团信息表详情 {@link GrouponRecordByCustomerResponse}
	 * @author groupon
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponrecord/get-by-customer-and-sku")
	BaseResponse<GrouponRecordByCustomerResponse> getByCustomerIdAndGoodsInfoId(@RequestBody @Valid GrouponRecordByCustomerRequest
																		grouponRecordByCustomerRequest);





	/**
	 * 列表查询拼团活动参团信息表API
	 *
	 * @author groupon
	 * @param grouponRecordListReq 列表请求参数和筛选对象 {@link GrouponRecordListRequest}
	 * @return 拼团活动参团信息表的列表信息 {@link GrouponRecordListResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponrecord/list")
	BaseResponse<GrouponRecordListResponse> list(@RequestBody @Valid GrouponRecordListRequest grouponRecordListReq);

}

