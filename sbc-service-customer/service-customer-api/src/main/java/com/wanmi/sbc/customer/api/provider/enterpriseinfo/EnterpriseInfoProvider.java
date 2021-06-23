package com.wanmi.sbc.customer.api.provider.enterpriseinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.*;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoAddResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>企业信息表保存服务Provider</p>
 * @author TangLian
 * @date 2020-03-02 19:05:06
 */
@FeignClient(value = "${application.customer.name}", contextId = "EnterpriseInfoProvider")
public interface EnterpriseInfoProvider {

	/**
	 * 新增企业信息表API
	 *
	 * @author TangLian
	 * @param enterpriseInfoAddRequest 企业信息表新增参数结构 {@link EnterpriseInfoAddRequest}
	 * @return 新增的企业信息表信息 {@link EnterpriseInfoAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/add")
	BaseResponse<EnterpriseInfoAddResponse> add(@RequestBody @Valid EnterpriseInfoAddRequest enterpriseInfoAddRequest);

	/**
	 * 修改企业信息表API
	 *
	 * @author TangLian
	 * @param enterpriseInfoModifyRequest 企业信息表修改参数结构 {@link EnterpriseInfoModifyRequest}
	 * @return 修改的企业信息表信息 {@link EnterpriseInfoModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/modify")
	BaseResponse<EnterpriseInfoModifyResponse> modify(@RequestBody @Valid EnterpriseInfoModifyRequest enterpriseInfoModifyRequest);

	/**
	 * c端-修改企业信息表API
	 *
	 * @author TangLian
	 * @param enterpriseInfoModifyForWebRequest 企业信息表修改参数结构 {@link EnterpriseInfoModifyForWebRequest}
	 * @return 修改的企业信息表信息 {@link EnterpriseInfoModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/modifyForWeb")
	BaseResponse<EnterpriseInfoModifyResponse> modifyForWeb(@RequestBody @Valid EnterpriseInfoModifyForWebRequest enterpriseInfoModifyForWebRequest);

	/**
	 * 单个删除企业信息表API
	 *
	 * @author TangLian
	 * @param enterpriseInfoDelByIdRequest 单个删除参数结构 {@link EnterpriseInfoDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid EnterpriseInfoDelByIdRequest enterpriseInfoDelByIdRequest);

	/**
	 * 批量删除企业信息表API
	 *
	 * @author TangLian
	 * @param enterpriseInfoDelByIdListRequest 批量删除参数结构 {@link EnterpriseInfoDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/enterpriseinfo/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid EnterpriseInfoDelByIdListRequest enterpriseInfoDelByIdListRequest);

}

