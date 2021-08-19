package com.wanmi.sbc.setting.api.provider.companyinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoAddRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoAddResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoModifyRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoModifyResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoDelByIdRequest;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>公司信息保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@FeignClient(value = "${application.setting.name}", contextId = "CompanyInfoSaveProvider")
public interface CompanyInfoSaveProvider {

	/**
	 * 新增公司信息API
	 *
	 * @author lq
	 * @param companyInfoAddRequest 公司信息新增参数结构 {@link CompanyInfoAddRequest}
	 * @return 新增的公司信息信息 {@link CompanyInfoAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/companyinfo/add")
	BaseResponse<CompanyInfoAddResponse> add(@RequestBody @Valid CompanyInfoAddRequest companyInfoAddRequest);

	/**
	 * 修改公司信息API
	 *
	 * @author lq
	 * @param companyInfoModifyRequest 公司信息修改参数结构 {@link CompanyInfoModifyRequest}
	 * @return 修改的公司信息信息 {@link CompanyInfoModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/companyinfo/modify")
	BaseResponse<CompanyInfoModifyResponse> modify(@RequestBody @Valid CompanyInfoModifyRequest
                                                           companyInfoModifyRequest);

	/**
	 * 单个删除公司信息API
	 *
	 * @author lq
	 * @param companyInfoDelByIdRequest 单个删除参数结构 {@link CompanyInfoDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/companyinfo/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid CompanyInfoDelByIdRequest companyInfoDelByIdRequest);

	/**
	 * 批量删除公司信息API
	 *
	 * @author lq
	 * @param companyInfoDelByIdListRequest 批量删除参数结构 {@link CompanyInfoDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/companyinfo/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid CompanyInfoDelByIdListRequest companyInfoDelByIdListRequest);

}

