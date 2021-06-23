package com.wanmi.sbc.customer.api.provider.livecompany;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.livecompany.*;
import com.wanmi.sbc.customer.api.response.livecompany.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播商家查询服务Provider</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@FeignClient(value = "${application.customer.name}", contextId = "LiveCompanyQueryProvider")
public interface LiveCompanyQueryProvider {

	/**
	 * 分页查询直播商家API
	 *
	 * @author zwb
	 * @param liveCompanyPageReq 分页请求参数和筛选对象 {@link LiveCompanyPageRequest}
	 * @return 直播商家分页列表信息 {@link LiveCompanyPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/livecompany/page")
	BaseResponse<LiveCompanyPageResponse> page(@RequestBody @Valid LiveCompanyPageRequest liveCompanyPageReq);

	/**
	 * 列表查询直播商家API
	 *
	 * @author zwb
	 * @param liveCompanyListReq 列表请求参数和筛选对象 {@link LiveCompanyListRequest}
	 * @return 直播商家的列表信息 {@link LiveCompanyListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/livecompany/list")
	BaseResponse<LiveCompanyListResponse> list(@RequestBody @Valid LiveCompanyListRequest liveCompanyListReq);

	/**
	 * 单个查询直播商家API
	 *
	 * @author zwb
	 * @param liveCompanyByIdRequest 单个查询直播商家请求参数 {@link LiveCompanyByIdRequest}
	 * @return 直播商家详情 {@link LiveCompanyByIdResponse}
	 */
	@PostMapping ("/customer/${application.customer.version}/livecompany/get-by-id")
	BaseResponse<LiveCompanyByIdResponse> getById(@RequestBody @Valid LiveCompanyByIdRequest liveCompanyByIdRequest);


	/**
	 * 分页查询直播商家API
	 *
	 * @author zwb
	 * @param liveCompanyPageReq 分页请求参数和筛选对象 {@link LiveCompanyPageRequest}
	 * @return 直播商家分页列表信息 {@link LiveCompanyPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/livecompany/page-new")
	BaseResponse<LiveCompanyPagePackResponse> pageNew(@RequestBody @Valid LiveCompanyQueryRequest liveCompanyPageReq);

}

