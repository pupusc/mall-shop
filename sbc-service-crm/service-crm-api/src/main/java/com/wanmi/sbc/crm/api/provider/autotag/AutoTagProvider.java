package com.wanmi.sbc.crm.api.provider.autotag;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.autotag.*;
import com.wanmi.sbc.crm.api.response.autotag.AutoTagAddResponse;
import com.wanmi.sbc.crm.api.response.autotag.AutoTagModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>自动标签保存服务Provider</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@FeignClient(value = "${application.crm.name}", contextId = "AutoTagProvider")
public interface AutoTagProvider {

	/**
	 * 新增自动标签API
	 *
	 * @author dyt
	 * @param autoTagAddRequest 自动标签新增参数结构 {@link AutoTagAddRequest}
	 * @return 新增的自动标签信息 {@link AutoTagAddResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/add")
	BaseResponse<AutoTagAddResponse> add(@RequestBody @Valid AutoTagAddRequest autoTagAddRequest);

	/**
	 * 修改自动标签API
	 *
	 * @author dyt
	 * @param autoTagModifyRequest 自动标签修改参数结构 {@link AutoTagModifyRequest}
	 * @return 修改的自动标签信息 {@link AutoTagModifyResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/modify")
	BaseResponse<AutoTagModifyResponse> modify(@RequestBody @Valid AutoTagModifyRequest autoTagModifyRequest);

	/**
	 * 单个删除自动标签API
	 *
	 * @author dyt
	 * @param autoTagDelByIdRequest 单个删除参数结构 {@link AutoTagDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid AutoTagDelByIdRequest autoTagDelByIdRequest);

	/**
	 * 批量删除自动标签API
	 *
	 * @author dyt
	 * @param autoTagDelByIdListRequest 批量删除参数结构 {@link AutoTagDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid AutoTagDelByIdListRequest autoTagDelByIdListRequest);

    /**
     * 初始化标签API
     *
     * @author dyt
     * @param autoTagInitRequest 初始化标签参数结构 {@link AutoTagInitRequest}
     * @return 初始化结果 {@link BaseResponse}
     */
    @PostMapping("/crm/${application.crm.version}/autotag/init")
    BaseResponse init(@RequestBody @Valid AutoTagInitRequest autoTagInitRequest);

	/**
	 * 定时任务生成sql
	 */
	@PostMapping("/crm/${application.crm.version}/autotag/getSql")
	BaseResponse getSql(@RequestBody @Valid AutoTagPageRequest request);

}

