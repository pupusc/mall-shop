package com.wanmi.sbc.setting.api.provider.systemresourcecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateAddRequest;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateAddResponse;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateModifyRequest;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateModifyResponse;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateDelByIdRequest;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>平台素材资源分类保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemResourceCateSaveProvider")
public interface SystemResourceCateSaveProvider {

	/**
	 * 新增平台素材资源分类API
	 *
	 * @author lq
	 * @param systemResourceCateAddRequest 平台素材资源分类新增参数结构 {@link SystemResourceCateAddRequest}
	 * @return 新增的平台素材资源分类信息 {@link SystemResourceCateAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemresourcecate/add")
	BaseResponse<SystemResourceCateAddResponse> add(@RequestBody @Valid SystemResourceCateAddRequest
                                                            systemResourceCateAddRequest);

	/**
	 * 修改平台素材资源分类API
	 *
	 * @author lq
	 * @param systemResourceCateModifyRequest 平台素材资源分类修改参数结构 {@link SystemResourceCateModifyRequest}
	 * @return 修改的平台素材资源分类信息 {@link SystemResourceCateModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemresourcecate/modify")
	BaseResponse<SystemResourceCateModifyResponse> modify(@RequestBody @Valid SystemResourceCateModifyRequest systemResourceCateModifyRequest);

	/**
	 * 单个删除平台素材资源分类API
	 *
	 * @author lq
	 * @param systemResourceCateDelByIdRequest 单个删除参数结构 {@link SystemResourceCateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemresourcecate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid SystemResourceCateDelByIdRequest systemResourceCateDelByIdRequest);

	/**
	 * 批量删除平台素材资源分类API
	 *
	 * @author lq
	 * @param systemResourceCateDelByIdListRequest 批量删除参数结构 {@link SystemResourceCateDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemresourcecate/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid SystemResourceCateDelByIdListRequest systemResourceCateDelByIdListRequest);








}

