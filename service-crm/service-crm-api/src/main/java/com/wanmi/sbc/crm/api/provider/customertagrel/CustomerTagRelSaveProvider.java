package com.wanmi.sbc.crm.api.provider.customertagrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelAddRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelAddResponse;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelModifyRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelModifyResponse;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>会员标签关联保存服务Provider</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@FeignClient(value="${application.crm.name}",contextId = "CustomerTagRelSaveProvider")
public interface CustomerTagRelSaveProvider {

	/**
	 * 新增会员标签关联API
	 *
	 * @author dyt
	 * @param customerTagRelAddRequest 会员标签关联新增参数结构 {@link CustomerTagRelAddRequest}
	 * @return 新增的会员标签关联信息 {@link CustomerTagRelAddResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertagrel/add")
	BaseResponse add(@RequestBody @Valid CustomerTagRelAddRequest customerTagRelAddRequest);

	/**
	 * 修改会员标签关联API
	 *
	 * @author dyt
	 * @param customerTagRelModifyRequest 会员标签关联修改参数结构 {@link CustomerTagRelModifyRequest}
	 * @return 修改的会员标签关联信息 {@link CustomerTagRelModifyResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertagrel/modify")
	BaseResponse<CustomerTagRelModifyResponse> modify(@RequestBody @Valid CustomerTagRelModifyRequest
                                                              customerTagRelModifyRequest);

	/**
	 * 单个删除会员标签关联API
	 *
	 * @author dyt
	 * @param customerTagRelDelByIdRequest 单个删除参数结构 {@link CustomerTagRelDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertagrel/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid CustomerTagRelDelByIdRequest customerTagRelDelByIdRequest);

	/**
	 * 批量删除会员标签关联API
	 *
	 * @author dyt
	 * @param customerTagRelDelByIdListRequest 批量删除参数结构 {@link CustomerTagRelDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertagrel/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid CustomerTagRelDelByIdListRequest customerTagRelDelByIdListRequest);

}

