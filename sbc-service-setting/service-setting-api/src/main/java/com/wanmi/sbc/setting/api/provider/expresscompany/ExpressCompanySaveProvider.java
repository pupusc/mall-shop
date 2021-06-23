package com.wanmi.sbc.setting.api.provider.expresscompany;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyAddRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyAddResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyModifyRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyModifyResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyDelByIdRequest;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>物流公司保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@FeignClient(value = "${application.setting.name}", contextId = "ExpressCompanySaveProvider")
public interface ExpressCompanySaveProvider {

	/**
	 * 新增物流公司API
	 *
	 * @author lq
	 * @param expressCompanyAddRequest 物流公司新增参数结构 {@link ExpressCompanyAddRequest}
	 * @return 新增的物流公司信息 {@link ExpressCompanyAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/expresscompany/add")
	BaseResponse<ExpressCompanyAddResponse> add(@RequestBody @Valid ExpressCompanyAddRequest expressCompanyAddRequest);

	/**
	 * 修改物流公司API
	 *
	 * @author lq
	 * @param expressCompanyModifyRequest 物流公司修改参数结构 {@link ExpressCompanyModifyRequest}
	 * @return 修改的物流公司信息 {@link ExpressCompanyModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/expresscompany/modify")
	BaseResponse<ExpressCompanyModifyResponse> modify(@RequestBody @Valid ExpressCompanyModifyRequest
                                                              expressCompanyModifyRequest);

	/**
	 * 单个删除物流公司API
	 *
	 * @author lq
	 * @param expressCompanyDelByIdRequest 单个删除参数结构 {@link ExpressCompanyDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/expresscompany/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid ExpressCompanyDelByIdRequest expressCompanyDelByIdRequest);

	/**
	 * 批量删除物流公司API
	 *
	 * @author lq
	 * @param expressCompanyDelByIdListRequest 批量删除参数结构 {@link ExpressCompanyDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/expresscompany/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid ExpressCompanyDelByIdListRequest expressCompanyDelByIdListRequest);

}

