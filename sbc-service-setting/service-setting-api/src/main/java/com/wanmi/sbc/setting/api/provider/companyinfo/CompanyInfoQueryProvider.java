package com.wanmi.sbc.setting.api.provider.companyinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoPageRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoPageResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoListRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoListResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoByIdRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoByIdResponse;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoRopResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>公司信息查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@FeignClient(value = "${application.setting.name}", contextId = "SettingCompanyInfoQueryProvider")
public interface CompanyInfoQueryProvider {

	/**
	 * 分页查询公司信息API
	 *
	 * @author lq
	 * @param companyInfoPageReq 分页请求参数和筛选对象 {@link CompanyInfoPageRequest}
	 * @return 公司信息分页列表信息 {@link CompanyInfoPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/companyinfo/page")
	BaseResponse<CompanyInfoPageResponse> page(@RequestBody @Valid CompanyInfoPageRequest companyInfoPageReq);

	/**
	 * 列表查询公司信息API
	 *
	 * @author lq
	 * @param companyInfoListReq 列表请求参数和筛选对象 {@link CompanyInfoListRequest}
	 * @return 公司信息的列表信息 {@link CompanyInfoListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/companyinfo/list")
	BaseResponse<CompanyInfoListResponse> list(@RequestBody @Valid CompanyInfoListRequest companyInfoListReq);

	/**
	 * 单个查询公司信息API
	 *
	 * @author lq
	 * @param companyInfoByIdRequest 单个查询公司信息请求参数 {@link CompanyInfoByIdRequest}
	 * @return 公司信息详情 {@link CompanyInfoByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/companyinfo/get-by-id")
	BaseResponse<CompanyInfoByIdResponse> getById(@RequestBody @Valid CompanyInfoByIdRequest companyInfoByIdRequest);


	/**
	 * 不带参查找公司信息
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/companyinfo/get-companyinfos")
	BaseResponse<CompanyInfoRopResponse> findCompanyInfos();
}

