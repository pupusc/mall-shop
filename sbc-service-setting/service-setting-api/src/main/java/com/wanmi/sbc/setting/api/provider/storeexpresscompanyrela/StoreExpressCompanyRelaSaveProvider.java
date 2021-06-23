package com.wanmi.sbc.setting.api.provider.storeexpresscompanyrela;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaAddRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaAddResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaModifyRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaModifyResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaDelByIdRequest;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaDelByIdListRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressRelaRopResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>店铺快递公司关联表保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@FeignClient(value = "${application.setting.name}", contextId = "StoreExpressCompanyRelaSaveProvider")
public interface StoreExpressCompanyRelaSaveProvider {

	/**
	 * 新增店铺快递公司关联表API
	 *
	 * @author lq
	 * @param storeExpressCompanyRelaAddRequest 店铺快递公司关联表新增参数结构 {@link StoreExpressCompanyRelaAddRequest}
	 * @return 新增的店铺快递公司关联表信息 {@link StoreExpressCompanyRelaAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeexpresscompanyrela/add")
	BaseResponse<StoreExpressRelaRopResponse> add(@RequestBody @Valid StoreExpressCompanyRelaAddRequest
                                                                 storeExpressCompanyRelaAddRequest);

	/**
	 * 修改店铺快递公司关联表API
	 *
	 * @author lq
	 * @param storeExpressCompanyRelaModifyRequest 店铺快递公司关联表修改参数结构 {@link StoreExpressCompanyRelaModifyRequest}
	 * @return 修改的店铺快递公司关联表信息 {@link StoreExpressCompanyRelaModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeexpresscompanyrela/modify")
	BaseResponse<StoreExpressCompanyRelaModifyResponse> modify(@RequestBody @Valid StoreExpressCompanyRelaModifyRequest storeExpressCompanyRelaModifyRequest);

	/**
	 * 单个删除店铺快递公司关联表API
	 *
	 * @author lq
	 * @param storeExpressCompanyRelaDelByIdRequest 单个删除参数结构 {@link StoreExpressCompanyRelaDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeexpresscompanyrela/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid StoreExpressCompanyRelaDelByIdRequest storeExpressCompanyRelaDelByIdRequest);

	/**
	 * 批量删除店铺快递公司关联表API
	 *
	 * @author lq
	 * @param storeExpressCompanyRelaDelByIdListRequest 批量删除参数结构 {@link StoreExpressCompanyRelaDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeexpresscompanyrela/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid StoreExpressCompanyRelaDelByIdListRequest storeExpressCompanyRelaDelByIdListRequest);

}

