package com.wanmi.sbc.crm.api.provider.customertag;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagAddRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagAddResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagModifyRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagModifyResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>会员标签保存服务Provider</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@FeignClient(value="${application.crm.name}",contextId = "CustomerTagSaveProvider")
public interface CustomerTagSaveProvider {

	/**
	 * 新增会员标签API
	 *
	 * @author zhanglingke
	 * @param customerTagAddRequest 会员标签新增参数结构 {@link CustomerTagAddRequest}
	 * @return 新增的会员标签信息 {@link CustomerTagAddResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertag/add")
	BaseResponse<CustomerTagAddResponse> add(@RequestBody @Valid CustomerTagAddRequest customerTagAddRequest);

	/**
	 * 修改会员标签API
	 *
	 * @author zhanglingke
	 * @param customerTagModifyRequest 会员标签修改参数结构 {@link CustomerTagModifyRequest}
	 * @return 修改的会员标签信息 {@link CustomerTagModifyResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertag/modify")
	BaseResponse<CustomerTagModifyResponse> modify(@RequestBody @Valid CustomerTagModifyRequest customerTagModifyRequest);

	/**
	 * 单个删除会员标签API
	 *
	 * @author zhanglingke
	 * @param customerTagDelByIdRequest 单个删除参数结构 {@link CustomerTagDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertag/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid CustomerTagDelByIdRequest customerTagDelByIdRequest);

	/**
	 * 批量删除会员标签API
	 *
	 * @author zhanglingke
	 * @param customerTagDelByIdListRequest 批量删除参数结构 {@link CustomerTagDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	/*@PostMapping("/crm/${application.crm.version}/customertag/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid CustomerTagDelByIdListRequest customerTagDelByIdListRequest);
*/
}

