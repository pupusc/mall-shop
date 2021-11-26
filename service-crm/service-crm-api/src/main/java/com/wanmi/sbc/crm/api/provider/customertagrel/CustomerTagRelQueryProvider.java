package com.wanmi.sbc.crm.api.provider.customertagrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelPageRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelPageResponse;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelListRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelListResponse;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelByIdRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>会员标签关联查询服务Provider</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@FeignClient(value="${application.crm.name}",contextId = "CustomerTagRelQueryProvider")
public interface CustomerTagRelQueryProvider {

	/**
	 * 分页查询会员标签关联API
	 *
	 * @author dyt
	 * @param customerTagRelPageReq 分页请求参数和筛选对象 {@link CustomerTagRelPageRequest}
	 * @return 会员标签关联分页列表信息 {@link CustomerTagRelPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertagrel/page")
	BaseResponse<CustomerTagRelPageResponse> page(@RequestBody @Valid CustomerTagRelPageRequest customerTagRelPageReq);

	/**
	 * 列表查询会员标签关联API
	 *
	 * @author dyt
	 * @param customerTagRelListReq 列表请求参数和筛选对象 {@link CustomerTagRelListRequest}
	 * @return 会员标签关联的列表信息 {@link CustomerTagRelListResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertagrel/list")
	BaseResponse<CustomerTagRelListResponse> list(@RequestBody @Valid CustomerTagRelListRequest customerTagRelListReq);

	/**
	 * 单个查询会员标签关联API
	 *
	 * @author dyt
	 * @param customerTagRelByIdRequest 单个查询会员标签关联请求参数 {@link CustomerTagRelByIdRequest}
	 * @return 会员标签关联详情 {@link CustomerTagRelByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertagrel/get-by-id")
	BaseResponse<CustomerTagRelByIdResponse> getById(@RequestBody @Valid CustomerTagRelByIdRequest
                                                             customerTagRelByIdRequest);

}

