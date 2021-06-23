package com.wanmi.sbc.goods.api.provider.thirdgoodscate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.thirdgoodscate.*;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCateAddResponse;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>保存服务Provider</p>
 * @author 
 * @date 2020-08-17 14:46:43
 */
@FeignClient(value = "${application.goods.name}", contextId = "ThirdGoodsCateProvider")
public interface ThirdGoodsCateProvider {

	/**
	 * 新增API
	 *
	 * @author 
	 * @param thirdGoodsCateAddRequest 新增参数结构 {@link ThirdGoodsCateAddRequest}
	 * @return 新增的信息 {@link ThirdGoodsCateAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/add")
	BaseResponse<ThirdGoodsCateAddResponse> add(@RequestBody @Valid ThirdGoodsCateAddRequest thirdGoodsCateAddRequest);

	/**
	 * 修改API
	 *
	 * @author 
	 * @param thirdGoodsCateModifyRequest 修改参数结构 {@link ThirdGoodsCateModifyRequest}
	 * @return 修改的信息 {@link ThirdGoodsCateModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/modify")
	BaseResponse<ThirdGoodsCateModifyResponse> modify(@RequestBody @Valid ThirdGoodsCateModifyRequest thirdGoodsCateModifyRequest);

	/**
	 * 单个删除API
	 *
	 * @author 
	 * @param thirdGoodsCateDelByIdRequest 单个删除参数结构 {@link ThirdGoodsCateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid ThirdGoodsCateDelByIdRequest thirdGoodsCateDelByIdRequest);

	/**
	 * 批量删除API
	 *
	 * @author 
	 * @param thirdGoodsCateDelByIdListRequest 批量删除参数结构 {@link ThirdGoodsCateDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid ThirdGoodsCateDelByIdListRequest thirdGoodsCateDelByIdListRequest);

	/**
	 * 全量更新某渠道所有类目
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/updateAll")
	BaseResponse updateAll(@RequestBody @Valid UpdateAllRequest request);
}

