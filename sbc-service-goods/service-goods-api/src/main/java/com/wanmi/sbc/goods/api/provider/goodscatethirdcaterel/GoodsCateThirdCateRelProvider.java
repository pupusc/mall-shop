package com.wanmi.sbc.goods.api.provider.goodscatethirdcaterel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelAddRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelAddResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelModifyRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelModifyResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelDelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.ArrayList;

/**
 * <p>平台类目和第三方平台类目映射保存服务Provider</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCateThirdCateRelProvider")
public interface GoodsCateThirdCateRelProvider {

	/**
	 * 新增平台类目和第三方平台类目映射API
	 *
	 * @author 
	 * @param goodsCateThirdCateRelAddRequest 平台类目和第三方平台类目映射新增参数结构 {@link GoodsCateThirdCateRelAddRequest}
	 * @return 新增的平台类目和第三方平台类目映射信息 {@link GoodsCateThirdCateRelAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodscatethirdcaterel/add")
	BaseResponse<GoodsCateThirdCateRelAddResponse> addBatch(@RequestBody @Valid GoodsCateThirdCateRelAddRequest goodsCateThirdCateRelAddRequest);

	/**
	 * 修改平台类目和第三方平台类目映射API
	 *
	 * @author 
	 * @param goodsCateThirdCateRelModifyRequest 平台类目和第三方平台类目映射修改参数结构 {@link GoodsCateThirdCateRelModifyRequest}
	 * @return 修改的平台类目和第三方平台类目映射信息 {@link GoodsCateThirdCateRelModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodscatethirdcaterel/modify")
	BaseResponse<GoodsCateThirdCateRelModifyResponse> modify(@RequestBody @Valid GoodsCateThirdCateRelModifyRequest goodsCateThirdCateRelModifyRequest);

	/**
	 * 单个删除平台类目和第三方平台类目映射API
	 *
	 * @author 
	 * @param goodsCateThirdCateRelDelByIdRequest 单个删除参数结构 {@link GoodsCateThirdCateRelDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodscatethirdcaterel/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GoodsCateThirdCateRelDelByIdRequest goodsCateThirdCateRelDelByIdRequest);

	/**
	 * 批量删除平台类目和第三方平台类目映射API
	 *
	 * @author 
	 * @param goodsCateThirdCateRelDelByIdListRequest 批量删除参数结构 {@link GoodsCateThirdCateRelDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodscatethirdcaterel/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid GoodsCateThirdCateRelDelByIdListRequest goodsCateThirdCateRelDelByIdListRequest);

}

