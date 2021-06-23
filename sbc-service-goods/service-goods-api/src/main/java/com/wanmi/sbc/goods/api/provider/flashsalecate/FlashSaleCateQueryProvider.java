package com.wanmi.sbc.goods.api.provider.flashsalecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateListRequest;
import com.wanmi.sbc.goods.api.response.flashsalecate.FlashSaleCateListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>秒杀分类查询服务Provider</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@FeignClient(value = "${application.goods.name}", contextId = "FlashSaleCateQueryProvider")
public interface FlashSaleCateQueryProvider {

	/**
	 * 列表查询秒杀分类API
	 *
	 * @author yxz
	 * @param flashSaleCateListReq 列表请求参数和筛选对象 {@link FlashSaleCateListRequest}
	 * @return 秒杀分类的列表信息 {@link FlashSaleCateListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalecate/list")
	BaseResponse<FlashSaleCateListResponse> list(@RequestBody @Valid FlashSaleCateListRequest flashSaleCateListReq);

}

