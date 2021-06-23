package com.wanmi.sbc.goods.api.provider.goodslabel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodslabel.*;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelAddResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelModifyResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelModifySortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品标签保存服务Provider</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsLabelSaveProvider")
public interface GoodsLabelSaveProvider {

	/**
	 * 新增商品标签API
	 *
	 * @author zjl
	 * @param goodsLabelAddRequest 商品标签新增参数结构 {@link GoodsLabelAddRequest}
	 * @return 新增的商品标签信息 {@link GoodsLabelAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/add")
    BaseResponse<GoodsLabelAddResponse> add(@RequestBody @Valid GoodsLabelAddRequest goodsLabelAddRequest);

	/**
	 * 修改商品标签API
	 *
	 * @author zjl
	 * @param goodsLabelModifyRequest 商品标签修改参数结构 {@link GoodsLabelModifyRequest}
	 * @return 修改的商品标签信息 {@link GoodsLabelModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/modify")
    BaseResponse<GoodsLabelModifyResponse> modify(@RequestBody @Valid GoodsLabelModifyRequest goodsLabelModifyRequest);

	/**
	 * 修改商品标签API
	 *
	 * @author zjl
	 * @param request 商品标签修改参数结构 {@link GoodsLabelModifyVisibleRequest}
	 * @return 修改的商品标签信息 {@link GoodsLabelModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/modify-visible")
	BaseResponse modifyVisible(@RequestBody @Valid GoodsLabelModifyVisibleRequest request);

	/**
	 * 单个删除商品标签API
	 *
	 * @author zjl
	 * @param goodsLabelDelByIdRequest 单个删除参数结构 {@link GoodsLabelDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid GoodsLabelDelByIdRequest goodsLabelDelByIdRequest);

	/**
	 * 批量删除商品标签API
	 *
	 * @author zjl
	 * @param goodsLabelDelByIdListRequest 批量删除参数结构 {@link GoodsLabelDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid GoodsLabelDelByIdListRequest goodsLabelDelByIdListRequest);

    /**
     * 拖拽排序
     *
     * @author zjl
     * @param goodsLabelSortRequest 拖拽排序参数结构 {@link GoodsLabelSortRequest}
     * @return 删除结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/goodslabel/edit-sort")
    BaseResponse<GoodsLabelModifySortResponse> editSort(@RequestBody @Valid GoodsLabelSortRequest goodsLabelSortRequest);


}

