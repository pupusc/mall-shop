package com.wanmi.sbc.goods.provider.impl.flashsalecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.FlashSaleCateErrorCode;
import com.wanmi.sbc.goods.api.provider.flashsalecate.FlashSaleCateSaveProvider;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateAddRequest;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateDelByIdRequest;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateModifyRequest;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateSortRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.api.response.flashsalecate.FlashSaleCateAddResponse;
import com.wanmi.sbc.goods.api.response.flashsalecate.FlashSaleCateModifyResponse;
import com.wanmi.sbc.goods.flashsalecate.model.root.FlashSaleCate;
import com.wanmi.sbc.goods.flashsalecate.service.FlashSaleCateService;
import com.wanmi.sbc.goods.flashsalegoods.model.root.FlashSaleGoods;
import com.wanmi.sbc.goods.flashsalegoods.service.FlashSaleGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>秒杀分类保存服务接口实现</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@RestController
@Validated
public class FlashSaleCateSaveController implements FlashSaleCateSaveProvider {

	@Autowired
	private FlashSaleCateService flashSaleCateService;

	@Autowired
	private FlashSaleGoodsService flashSaleGoodsService;

	@Override
	public BaseResponse<FlashSaleCateAddResponse> add(@RequestBody @Valid FlashSaleCateAddRequest flashSaleCateAddRequest) {
		FlashSaleCate flashSaleCate = new FlashSaleCate();
		KsBeanUtil.copyPropertiesThird(flashSaleCateAddRequest, flashSaleCate);
		return BaseResponse.success(new FlashSaleCateAddResponse(
				flashSaleCateService.wrapperVo(flashSaleCateService.add(flashSaleCate))));
	}

	@Override
	public BaseResponse<FlashSaleCateModifyResponse> modify(@RequestBody @Valid FlashSaleCateModifyRequest flashSaleCateModifyRequest) {
		FlashSaleCate flashSaleCate = new FlashSaleCate();
		KsBeanUtil.copyPropertiesThird(flashSaleCateModifyRequest, flashSaleCate);
		return BaseResponse.success(new FlashSaleCateModifyResponse(
				flashSaleCateService.wrapperVo(flashSaleCateService.modify(flashSaleCate))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid FlashSaleCateDelByIdRequest flashSaleCateDelByIdRequest) {
		//无法删除已关联进行中/未开始秒杀商品的分类
		List<FlashSaleGoods> flashSaleCateList = flashSaleGoodsService.list(FlashSaleGoodsQueryRequest.builder()
				.cateId(flashSaleCateDelByIdRequest.getCateId())
				.delFlag(DeleteFlag.NO)
				.build());
		if(flashSaleCateList.size() > 0){
			throw new SbcRuntimeException(FlashSaleCateErrorCode.UNABLED_DELETE);
		}
		flashSaleCateService.deleteById(flashSaleCateDelByIdRequest.getCateId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse editSort(@RequestBody @Valid FlashSaleCateSortRequest flashSaleCateSortRequest) {
		flashSaleCateService.editSort(flashSaleCateSortRequest);
		return BaseResponse.SUCCESSFUL();
	}

}

