package com.wanmi.sbc.crm.api.provider.autotag;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagByIdRequest;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagListRequest;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagPageRequest;
import com.wanmi.sbc.crm.api.response.autotag.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>自动标签查询服务Provider</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@FeignClient(value = "${application.crm.name}", contextId = "AutoTagQueryProvider")
public interface AutoTagQueryProvider {

	/**
	 * 分页查询自动标签API
	 *
	 * @author dyt
	 * @param autoTagPageReq 分页请求参数和筛选对象 {@link AutoTagPageRequest}
	 * @return 自动标签分页列表信息 {@link AutoTagPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/page")
	BaseResponse<AutoTagPageResponse> page(@RequestBody @Valid AutoTagPageRequest autoTagPageReq);

	/**
	 * 列表查询自动标签API
	 *
	 * @author dyt
	 * @param autoTagListReq 列表请求参数和筛选对象 {@link AutoTagListRequest}
	 * @return 自动标签的列表信息 {@link AutoTagListResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/preference-list")
	BaseResponse<PreferenceTagListResponse> getPreferenceList(@RequestBody @Valid AutoTagListRequest autoTagListReq);

	/**
	 * 列表查询自动标签API
	 *
	 * @author dyt
	 * @param autoTagListReq 列表请求参数和筛选对象 {@link AutoTagListRequest}
	 * @return 自动标签的列表信息 {@link AutoTagListResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/list")
	BaseResponse<AutoTagListResponse> list(@RequestBody @Valid AutoTagListRequest autoTagListReq);

	/**
	 * 单个查询自动标签API
	 *
	 * @author dyt
	 * @param autoTagByIdRequest 单个查询自动标签请求参数 {@link AutoTagByIdRequest}
	 * @return 自动标签详情 {@link AutoTagByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/get-by-id")
	BaseResponse<AutoTagByIdResponse> getById(@RequestBody @Valid AutoTagByIdRequest autoTagByIdRequest);

    /**
     * 列表查询系统标签API
     *
     * @author dyt
     * @return 系统标签的列表信息 {@link AutoTagInitListResponse}
     */
    @PostMapping("/crm/${application.crm.version}/autotag/system-list")
    BaseResponse<AutoTagInitListResponse> systemList();

	/**
	 * 非系统标签总数API
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/count")
	BaseResponse<Long> getCount();

}

