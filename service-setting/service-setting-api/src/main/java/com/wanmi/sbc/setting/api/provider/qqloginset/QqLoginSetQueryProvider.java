package com.wanmi.sbc.setting.api.provider.qqloginset;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetPageRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetPageResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetListRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetListResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetByIdRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>qq登录信息查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@FeignClient(value = "${application.setting.name}", contextId = "QqLoginSetQueryProvider")
public interface QqLoginSetQueryProvider {

	/**
	 * 分页查询qq登录信息API
	 *
	 * @author lq
	 * @param qqLoginSetPageReq 分页请求参数和筛选对象 {@link QqLoginSetPageRequest}
	 * @return qq登录信息分页列表信息 {@link QqLoginSetPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/qqloginset/page")
	BaseResponse<QqLoginSetPageResponse> page(@RequestBody @Valid QqLoginSetPageRequest qqLoginSetPageReq);

	/**
	 * 列表查询qq登录信息API
	 *
	 * @author lq
	 * @param qqLoginSetListReq 列表请求参数和筛选对象 {@link QqLoginSetListRequest}
	 * @return qq登录信息的列表信息 {@link QqLoginSetListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/qqloginset/list")
	BaseResponse<QqLoginSetListResponse> list(@RequestBody @Valid QqLoginSetListRequest qqLoginSetListReq);

	/**
	 * 单个查询qq登录信息API
	 *
	 * @author lq
	 * @param qqLoginSetByIdRequest 单个查询qq登录信息请求参数 {@link QqLoginSetByIdRequest}
	 * @return qq登录信息详情 {@link QqLoginSetByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/qqloginset/get-by-id")
	BaseResponse<QqLoginSetByIdResponse> getById(@RequestBody @Valid QqLoginSetByIdRequest qqLoginSetByIdRequest);

}

