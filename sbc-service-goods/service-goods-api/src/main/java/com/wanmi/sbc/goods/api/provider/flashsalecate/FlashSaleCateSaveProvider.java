package com.wanmi.sbc.goods.api.provider.flashsalecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateAddRequest;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateDelByIdRequest;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateModifyRequest;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateSortRequest;
import com.wanmi.sbc.goods.api.response.flashsalecate.FlashSaleCateAddResponse;
import com.wanmi.sbc.goods.api.response.flashsalecate.FlashSaleCateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>秒杀分类保存服务Provider</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@FeignClient(value = "${application.goods.name}", contextId = "FlashSaleCateSaveProvider")
public interface FlashSaleCateSaveProvider {

	/**
	 * 新增秒杀分类API
	 *
	 * @author yxz
	 * @param flashSaleCateAddRequest 秒杀分类新增参数结构 {@link FlashSaleCateAddRequest}
	 * @return 新增的秒杀分类信息 {@link FlashSaleCateAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalecate/add")
	BaseResponse<FlashSaleCateAddResponse> add(@RequestBody @Valid FlashSaleCateAddRequest flashSaleCateAddRequest);

	/**
	 * 修改秒杀分类API
	 *
	 * @author yxz
	 * @param flashSaleCateModifyRequest 秒杀分类修改参数结构 {@link FlashSaleCateModifyRequest}
	 * @return 修改的秒杀分类信息 {@link FlashSaleCateModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalecate/modify")
	BaseResponse<FlashSaleCateModifyResponse> modify(@RequestBody @Valid FlashSaleCateModifyRequest flashSaleCateModifyRequest);

	/**
	 * 单个删除秒杀分类API
	 *
	 * @author yxz
	 * @param flashSaleCateDelByIdRequest 单个删除参数结构 {@link FlashSaleCateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalecate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid FlashSaleCateDelByIdRequest flashSaleCateDelByIdRequest);

	/**
	 * 拖拽排序
	 *
	 * @author yxz
	 * @param flashSaleCateSortRequest 拖拽排序参数结构 {@link FlashSaleCateSortRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalecate/edit-sort")
	BaseResponse editSort(@RequestBody @Valid FlashSaleCateSortRequest flashSaleCateSortRequest);

}

