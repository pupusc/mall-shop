package com.wanmi.sbc.setting.api.provider.storeresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.storeresource.*;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourceAddResponse;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourceModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺资源库保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:12:49
 */
@FeignClient(value = "${application.setting.name}", contextId = "StoreResourceSaveProvider")
public interface StoreResourceSaveProvider {

	/**
	 * 新增店铺资源库API
	 *
	 * @author lq
	 * @param storeResourceAddRequest 店铺资源库新增参数结构 {@link StoreResourceAddRequest}
	 * @return 新增的店铺资源库信息 {@link StoreResourceAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresource/add")
	BaseResponse<StoreResourceAddResponse> add(@RequestBody @Valid StoreResourceAddRequest storeResourceAddRequest);

	/**
	 * 修改店铺资源库API
	 *
	 * @author lq
	 * @param storeResourceModifyRequest 店铺资源库修改参数结构 {@link StoreResourceModifyRequest}
	 * @return 修改的店铺资源库信息 {@link StoreResourceModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresource/modify")
	BaseResponse<StoreResourceModifyResponse> modify(@RequestBody @Valid StoreResourceModifyRequest
                                                             storeResourceModifyRequest);

	/**
	 * 单个删除店铺资源库API
	 *
	 * @author lq
	 * @param storeResourceDelByIdRequest 单个删除参数结构 {@link StoreResourceDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresource/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid StoreResourceDelByIdRequest storeResourceDelByIdRequest);

	/**
	 * 移动店铺素材资源API
	 *
	 * @author lq
	 * @param moveRequest 素材资源修改参数结构  {@link StoreResourceMoveRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresource/move")
	BaseResponse move(@RequestBody @Valid StoreResourceMoveRequest
									moveRequest);

	/**
	 * 批量删除店铺资源库API
	 *
	 * @author lq
	 * @param storeResourceDelByIdListRequest 批量删除参数结构 {@link StoreResourceDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresource/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid StoreResourceDelByIdListRequest storeResourceDelByIdListRequest);

}

