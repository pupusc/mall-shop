package com.wanmi.sbc.goods.provider.impl.flashsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsSaveProvider;
import com.wanmi.sbc.goods.api.request.flashsalegoods.*;
import com.wanmi.sbc.goods.api.response.flashsalegoods.FlashSaleGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.flashsalegoods.FlashSaleGoodsModifyResponse;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.flashsalegoods.model.root.FlashSaleGoods;
import com.wanmi.sbc.goods.flashsalegoods.service.FlashSaleGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>抢购商品表保存服务接口实现</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@RestController
@Validated
public class FlashSaleGoodsSaveController implements FlashSaleGoodsSaveProvider {
	@Autowired
	private FlashSaleGoodsService flashSaleGoodsService;

	@Override
	public BaseResponse<FlashSaleGoodsAddResponse> batchAdd(@RequestBody @Valid FlashSaleGoodsBatchAddRequest flashSaleGoodsAddRequest) {

		List<FlashSaleGoods> flashSaleGoodsList =
				KsBeanUtil.convertList(flashSaleGoodsAddRequest.getFlashSaleGoodsVOList(),
				FlashSaleGoods.class);
		List<FlashSaleGoodsVO> flashSaleGoodsVOS =
				flashSaleGoodsService.batchAdd(flashSaleGoodsList).stream().map(flashSaleGoods-> flashSaleGoodsService.wrapperVo(flashSaleGoods)).collect(Collectors.toList());
		return BaseResponse.success(new FlashSaleGoodsAddResponse(flashSaleGoodsVOS));
	}

	@Override
	public BaseResponse<FlashSaleGoodsModifyResponse> modify(@RequestBody @Valid FlashSaleGoodsModifyRequest flashSaleGoodsModifyRequest) {
		FlashSaleGoods flashSaleGoods = new FlashSaleGoods();
		KsBeanUtil.copyPropertiesThird(flashSaleGoodsModifyRequest, flashSaleGoods);
		return BaseResponse.success(new FlashSaleGoodsModifyResponse(
				flashSaleGoodsService.wrapperVo(flashSaleGoodsService.modify(flashSaleGoods))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid FlashSaleGoodsDelByIdRequest flashSaleGoodsDelByIdRequest) {
		flashSaleGoodsService.deleteById(flashSaleGoodsDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid FlashSaleGoodsDelByIdListRequest flashSaleGoodsDelByIdListRequest) {
		flashSaleGoodsService.deleteByIdList(flashSaleGoodsDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByTimeList(@RequestBody @Valid FlashSaleGoodsDelByTimeListRequest flashSaleGoodsDelByTimeListRequest) {
		flashSaleGoodsService.deleteByTimeList(flashSaleGoodsDelByTimeListRequest.getActivityTimeList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse batchMinusStock(@RequestBody @Valid FlashSaleGoodsBatchMinusStockRequest flashSaleGoodsBatchMinusStockRequest) {
		flashSaleGoodsService.batchMinusStock(flashSaleGoodsBatchMinusStockRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse addFlashSaleGoodsStock(@RequestBody @Valid FlashSaleGoodsBatchAddStockRequest request){
		flashSaleGoodsService.addStockById(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse batchPlusSalesVolume(@RequestBody @Valid FlashSaleGoodsBatchPlusSalesVolumeRequest request){
		flashSaleGoodsService.batchPlusSalesVolumeById(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse subSalesVolumeById(@RequestBody @Valid FlashSaleGoodsBatchStockAndSalesVolumeRequest request){
		flashSaleGoodsService.subSalesVolumeById(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse batchStockAndSalesVolume(@RequestBody @Valid FlashSaleGoodsBatchStockAndSalesVolumeRequest request) {
		flashSaleGoodsService.batchStockAndSalesVolume(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse subStockAndSalesVolume(@RequestBody @Valid FlashSaleGoodsBatchStockAndSalesVolumeRequest request){
		flashSaleGoodsService.subStockAndSalesVolume(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse activityEndReturnStock(@RequestBody @Valid FlashSaleGoodsQueryRequest request) {
		flashSaleGoodsService.activityEndReturnStock(request);
		return BaseResponse.SUCCESSFUL();
	}

}

