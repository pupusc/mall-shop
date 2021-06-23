package com.wanmi.sbc.customer.api.provider.enterpriseinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoListByCustomerIdsRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoPageRequest;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoPageResponse;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoListRequest;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoListResponse;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByIdRequest;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>企业信息表查询服务Provider</p>
 * @author TangLian
 * @date 2020-03-02 19:05:06
 */
@FeignClient(value = "${application.customer.name}", contextId = "EnterpriseInfoQueryProvider")
public interface EnterpriseInfoQueryProvider {

	/**
	 * 分页查询企业信息表API
	 *
	 * @author TangLian
	 * @param enterpriseInfoPageReq 分页请求参数和筛选对象 {@link EnterpriseInfoPageRequest}
	 * @return 企业信息表分页列表信息 {@link EnterpriseInfoPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/page")
	BaseResponse<EnterpriseInfoPageResponse> page(@RequestBody @Valid EnterpriseInfoPageRequest enterpriseInfoPageReq);

	/**
	 * 列表查询企业信息表API
	 *
	 * @author TangLian
	 * @param enterpriseInfoListReq 列表请求参数和筛选对象 {@link EnterpriseInfoListRequest}
	 * @return 企业信息表的列表信息 {@link EnterpriseInfoListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/list")
	BaseResponse<EnterpriseInfoListResponse> list(@RequestBody @Valid EnterpriseInfoListRequest enterpriseInfoListReq);

	/**
	 * 单个查询企业信息表API
	 *
	 * @author TangLian
	 * @param enterpriseInfoByIdRequest 单个查询企业信息表请求参数 {@link EnterpriseInfoByIdRequest}
	 * @return 企业信息表详情 {@link EnterpriseInfoByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/get-by-id")
	BaseResponse<EnterpriseInfoByIdResponse> getById(@RequestBody @Valid EnterpriseInfoByIdRequest enterpriseInfoByIdRequest);

	/**
	 * 查询企业会员企业信息
	 * @param enterpriseInfoByCustomerIdRequest
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/getByCustomerId")
	BaseResponse<EnterpriseInfoByCustomerIdResponse> getByCustomerId(@RequestBody @Valid EnterpriseInfoByCustomerIdRequest enterpriseInfoByCustomerIdRequest);


	/**
	 * 根据customerIdList批量查询企业信息表API
	 *
	 * @author TangLian
	 * @param request 列表请求参数和筛选对象 {@link EnterpriseInfoListByCustomerIdsRequest}
	 * @return 根据customerIdList查询企业信息表API {@link EnterpriseInfoListByCustomerIdsRequest}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/list-by-customerId-list")
	BaseResponse<EnterpriseInfoListResponse> listByCustomerIdList(@RequestBody @Valid EnterpriseInfoListByCustomerIdsRequest request);

}

