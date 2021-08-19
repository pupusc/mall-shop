package com.wanmi.sbc.setting.api.provider.storeresourcecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.storeresourcecate.*;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateAddResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺资源资源分类表保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@FeignClient(value = "${application.setting.name}", contextId = "StoreResourceCateSaveProvider")
public interface StoreResourceCateSaveProvider {

	/**
	 * 新增店铺资源资源分类表API
	 *
	 * @author lq
	 * @param storeResourceCateAddRequest 店铺资源资源分类表新增参数结构 {@link StoreResourceCateAddRequest}
	 * @return 新增的店铺资源资源分类表信息 {@link StoreResourceCateAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/add")
	BaseResponse<StoreResourceCateAddResponse> add(@RequestBody @Valid StoreResourceCateAddRequest
                                                           storeResourceCateAddRequest);

	/**
	 * 修改店铺资源资源分类表API
	 *
	 * @author lq
	 * @param storeResourceCateModifyRequest 店铺资源资源分类表修改参数结构 {@link StoreResourceCateModifyRequest}
	 * @return 修改的店铺资源资源分类表信息 {@link StoreResourceCateModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/modify")
	BaseResponse<StoreResourceCateModifyResponse> modify(@RequestBody @Valid StoreResourceCateModifyRequest storeResourceCateModifyRequest);

	/**
	 * 单个删除店铺资源资源分类表API
	 *
	 * @author lq
	 * @param storeResourceCateDelByIdRequest 单个删除参数结构 {@link StoreResourceCateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/delete-by-id")
	BaseResponse delete(@RequestBody @Valid StoreResourceCateDelByIdRequest storeResourceCateDelByIdRequest);



	/**
	 * 初始化店铺默认分类
	 *
	 * @author lq
	 * @param storeResourceCate 批量删除参数结构 {@link StoreResourceCateDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/init")
	BaseResponse init(@RequestBody @Valid StoreResourceCateInitRequest storeResourceCate);

}

