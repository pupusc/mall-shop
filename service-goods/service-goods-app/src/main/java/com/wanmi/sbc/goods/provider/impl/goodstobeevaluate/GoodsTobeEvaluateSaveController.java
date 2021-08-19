package com.wanmi.sbc.goods.provider.impl.goodstobeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.*;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateAddResponse;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateByIdResponse;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateModifyResponse;
import com.wanmi.sbc.goods.goodstobeevaluate.model.root.GoodsTobeEvaluate;
import com.wanmi.sbc.goods.goodstobeevaluate.service.GoodsTobeEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>订单商品待评价保存服务接口实现</p>
 * @author lzw
 * @date 2019-03-20 11:21:41
 */
@RestController
@Validated
public class GoodsTobeEvaluateSaveController implements GoodsTobeEvaluateSaveProvider {
	@Autowired
	private GoodsTobeEvaluateService goodsTobeEvaluateService;

	@Override
	public BaseResponse<GoodsTobeEvaluateAddResponse> add(@RequestBody @Valid GoodsTobeEvaluateAddRequest goodsTobeEvaluateAddRequest) {
		GoodsTobeEvaluate goodsTobeEvaluate = new GoodsTobeEvaluate();
		KsBeanUtil.copyPropertiesThird(goodsTobeEvaluateAddRequest, goodsTobeEvaluate);
		return BaseResponse.success(new GoodsTobeEvaluateAddResponse(
				goodsTobeEvaluateService.wrapperVo(goodsTobeEvaluateService.add(goodsTobeEvaluate))));
	}

	@Override
	public BaseResponse<GoodsTobeEvaluateModifyResponse> modify(@RequestBody @Valid GoodsTobeEvaluateModifyRequest goodsTobeEvaluateModifyRequest) {
		GoodsTobeEvaluate goodsTobeEvaluate = new GoodsTobeEvaluate();
		KsBeanUtil.copyPropertiesThird(goodsTobeEvaluateModifyRequest, goodsTobeEvaluate);
		return BaseResponse.success(new GoodsTobeEvaluateModifyResponse(
				goodsTobeEvaluateService.wrapperVo(goodsTobeEvaluateService.modify(goodsTobeEvaluate))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid GoodsTobeEvaluateDelByIdRequest goodsTobeEvaluateDelByIdRequest) {
		goodsTobeEvaluateService.deleteById(goodsTobeEvaluateDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid GoodsTobeEvaluateDelByIdListRequest goodsTobeEvaluateDelByIdListRequest) {
		goodsTobeEvaluateService.deleteByIdList(goodsTobeEvaluateDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

	/**
	 * @param goodsTobeEvaluateQueryRequest
	 * @Description: 订单ID和skuID删除
	 * @Author: Bob
	 * @Date: 2019-04-12 16:29
	 */
	@Override
	public BaseResponse<Integer> deleteByOrderAndSku(@RequestBody @Valid GoodsTobeEvaluateQueryRequest goodsTobeEvaluateQueryRequest) {
		return BaseResponse.success(goodsTobeEvaluateService.delByOrderIDAndSkuID(goodsTobeEvaluateQueryRequest));
	}

	/**
	 * @param goodsTobeEvaluateQueryRequest
	 * @Description: 动态条件查询
	 * @Author: Bob
	 * @Date: 2019-04-12 17:19
	 */
	@Override
	public BaseResponse<GoodsTobeEvaluateByIdResponse> query(@RequestBody @Valid GoodsTobeEvaluateQueryRequest goodsTobeEvaluateQueryRequest) {
		GoodsTobeEvaluateByIdResponse response =
				GoodsTobeEvaluateByIdResponse.builder().goodsTobeEvaluateVO(goodsTobeEvaluateService
						.query(goodsTobeEvaluateQueryRequest)).build();
		return BaseResponse.success(response);
	}

}

