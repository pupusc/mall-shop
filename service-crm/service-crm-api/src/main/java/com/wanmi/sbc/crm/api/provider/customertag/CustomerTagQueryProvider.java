package com.wanmi.sbc.crm.api.provider.customertag;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagPageRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagPageResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagListRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagListResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagByIdRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagByIdResponse;
//import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>会员标签查询服务Provider</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@FeignClient(value = "${application.crm.name}",contextId = "CustomerTagQueryProvider")
public interface CustomerTagQueryProvider {

	/**
	 * 分页查询会员标签API
	 *
	 * @author zhanglingke
	 * @param customerTagPageReq 分页请求参数和筛选对象 {@link CustomerTagPageRequest}
	 * @return 会员标签分页列表信息 {@link CustomerTagPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertag/page")
	BaseResponse<CustomerTagPageResponse> page(@RequestBody @Valid CustomerTagPageRequest customerTagPageReq);

	/**
	 * 列表查询会员标签API
	 *
	 * @author zhanglingke
	 * @param customerTagListReq 列表请求参数和筛选对象 {@link CustomerTagListRequest}
	 * @return 会员标签的列表信息 {@link CustomerTagListResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertag/list")
	BaseResponse<CustomerTagListResponse> list(@RequestBody @Valid CustomerTagListRequest customerTagListReq);

	/**
	 * 单个查询会员标签API
	 *
	 * @author zhanglingke
	 * @param customerTagByIdRequest 单个查询会员标签请求参数 {@link CustomerTagByIdRequest}
	 * @return 会员标签详情 {@link CustomerTagByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customertag/get-by-id")
	BaseResponse<CustomerTagByIdResponse> getById(@RequestBody @Valid CustomerTagByIdRequest customerTagByIdRequest);

}

