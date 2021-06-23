package com.wanmi.sbc.goods.api.provider.restrictedrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordPageRequest;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordPageResponse;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordListRequest;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordListResponse;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordByIdRequest;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>限售查询服务Provider</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@FeignClient(value = "${application.goods.name}", contextId = "RestrictedRecordQueryProvider.class")
public interface RestrictedRecordQueryProvider {

	/**
	 * 分页查询限售API
	 *
	 * @author 限售记录
	 * @param restrictedRecordPageReq 分页请求参数和筛选对象 {@link RestrictedRecordPageRequest}
	 * @return 限售分页列表信息 {@link RestrictedRecordPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/restrictedrecord/page")
	BaseResponse<RestrictedRecordPageResponse> page(@RequestBody @Valid RestrictedRecordPageRequest restrictedRecordPageReq);

	/**
	 * 列表查询限售API
	 *
	 * @author 限售记录
	 * @param restrictedRecordListReq 列表请求参数和筛选对象 {@link RestrictedRecordListRequest}
	 * @return 限售的列表信息 {@link RestrictedRecordListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/restrictedrecord/list")
	BaseResponse<RestrictedRecordListResponse> list(@RequestBody @Valid RestrictedRecordListRequest restrictedRecordListReq);

	/**
	 * 单个查询限售API
	 *
	 * @author 限售记录
	 * @param restrictedRecordByIdRequest 单个查询限售请求参数 {@link RestrictedRecordByIdRequest}
	 * @return 限售详情 {@link RestrictedRecordByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/restrictedrecord/get-by-id")
	BaseResponse<RestrictedRecordByIdResponse> getById(@RequestBody @Valid RestrictedRecordByIdRequest restrictedRecordByIdRequest);

}

