package com.wanmi.sbc.setting.api.provider.expresscompany;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyPageRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyPageResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyListRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyListResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyByIdRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>物流公司查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@FeignClient(value = "${application.setting.name}", contextId = "ExpressCompanyQueryProvider")
public interface ExpressCompanyQueryProvider {

	/**
	 * 分页查询物流公司API
	 *
	 * @author lq
	 * @param expressCompanyPageReq 分页请求参数和筛选对象 {@link ExpressCompanyPageRequest}
	 * @return 物流公司分页列表信息 {@link ExpressCompanyPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/expresscompany/page")
	BaseResponse<ExpressCompanyPageResponse> page(@RequestBody @Valid ExpressCompanyPageRequest expressCompanyPageReq);

	/**
	 * 列表查询物流公司API
	 *
	 * @author lq
	 * @return 物流公司的列表信息 {@link ExpressCompanyListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/expresscompany/list")
	BaseResponse<ExpressCompanyListResponse> list();


	/**
	 * 批量查询物流公司API
	 *
	 * @author lq
	 * @return 物流公司的列表信息 {@link ExpressCompanyListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/expresscompany/list-patch")
	BaseResponse<ExpressCompanyListResponse> listByIds(@RequestBody @Valid ExpressCompanyListRequest expressCompanyListRequest);

	/**
	 * 单个查询物流公司API
	 *
	 * @author lq
	 * @param expressCompanyByIdRequest 单个查询物流公司请求参数 {@link ExpressCompanyByIdRequest}
	 * @return 物流公司详情 {@link ExpressCompanyByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/expresscompany/get-by-id")
	BaseResponse<ExpressCompanyByIdResponse> getById(@RequestBody @Valid ExpressCompanyByIdRequest
                                                             expressCompanyByIdRequest);

}

