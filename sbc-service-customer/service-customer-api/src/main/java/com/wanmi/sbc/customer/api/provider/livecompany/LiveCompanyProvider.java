package com.wanmi.sbc.customer.api.provider.livecompany;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyAddRequest;
import com.wanmi.sbc.customer.api.response.livecompany.LiveCompanyAddResponse;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyModifyRequest;
import com.wanmi.sbc.customer.api.response.livecompany.LiveCompanyModifyResponse;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyDelByIdRequest;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播商家保存服务Provider</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@FeignClient(value = "${application.customer.name}", contextId = "LiveCompanyProvider")
public interface LiveCompanyProvider {

	/**
	 * 新增直播商家API
	 *
	 * @author zwb
	 * @param liveCompanyAddRequest 直播商家新增参数结构 {@link LiveCompanyAddRequest}
	 * @return 新增的直播商家信息 {@link LiveCompanyAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/livecompany/add")
	BaseResponse<LiveCompanyAddResponse> add(@RequestBody @Valid LiveCompanyAddRequest liveCompanyAddRequest);

	/**
	 * 修改直播商家API
	 *
	 * @author zwb
	 * @param liveCompanyModifyRequest 直播商家修改参数结构 {@link LiveCompanyModifyRequest}
	 * @return 修改的直播商家信息 {@link LiveCompanyModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/livecompany/modify")
	BaseResponse<LiveCompanyModifyResponse> modify(@RequestBody @Valid LiveCompanyModifyRequest liveCompanyModifyRequest);

	/**
	 * 单个删除直播商家API
	 *
	 * @author zwb
	 * @param liveCompanyDelByIdRequest 单个删除参数结构 {@link LiveCompanyDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/livecompany/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid LiveCompanyDelByIdRequest liveCompanyDelByIdRequest);

	/**
	 * 批量删除直播商家API
	 *
	 * @author zwb
	 * @param liveCompanyDelByIdListRequest 批量删除参数结构 {@link LiveCompanyDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/livecompany/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid LiveCompanyDelByIdListRequest liveCompanyDelByIdListRequest);

}

