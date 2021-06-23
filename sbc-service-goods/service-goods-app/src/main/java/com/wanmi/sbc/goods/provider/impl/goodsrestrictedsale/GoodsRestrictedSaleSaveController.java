package com.wanmi.sbc.goods.provider.impl.goodsrestrictedsale;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleSaveProvider;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleAddRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSaleAddResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleModifyRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSaleModifyResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleDelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleDelByIdListRequest;
import com.wanmi.sbc.goods.goodsrestrictedsale.service.GoodsRestrictedSaleService;
import com.wanmi.sbc.goods.goodsrestrictedsale.model.root.GoodsRestrictedSale;
import javax.validation.Valid;

/**
 * <p>限售配置保存服务接口实现</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@RestController
@Validated
public class GoodsRestrictedSaleSaveController implements GoodsRestrictedSaleSaveProvider {
	@Autowired
	private GoodsRestrictedSaleService goodsRestrictedSaleService;

	@Override
	public BaseResponse<GoodsRestrictedSaleAddResponse> addBatch(@RequestBody @Valid GoodsRestrictedSaleAddRequest goodsRestrictedSaleAddRequest) {
		return BaseResponse.success(new GoodsRestrictedSaleAddResponse(
				goodsRestrictedSaleService.wrapperVoList(goodsRestrictedSaleService.addBatch(goodsRestrictedSaleAddRequest))));
	}

	@Override
	public BaseResponse<GoodsRestrictedSaleModifyResponse> modify(@RequestBody @Valid GoodsRestrictedSaleModifyRequest goodsRestrictedSaleModifyRequest) {
		GoodsRestrictedSale goodsRestrictedSale = new GoodsRestrictedSale();
		KsBeanUtil.copyPropertiesThird(goodsRestrictedSaleModifyRequest, goodsRestrictedSale);
		GoodsRestrictedSale goodsRestrictedSale1 = goodsRestrictedSaleService.modifyNew(goodsRestrictedSaleModifyRequest);
		return BaseResponse.success(new GoodsRestrictedSaleModifyResponse(
				goodsRestrictedSaleService.wrapperVo(goodsRestrictedSale1)));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid GoodsRestrictedSaleDelByIdRequest goodsRestrictedSaleDelByIdRequest) {
		goodsRestrictedSaleService.deleteById(goodsRestrictedSaleDelByIdRequest.getRestrictedId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid GoodsRestrictedSaleDelByIdListRequest goodsRestrictedSaleDelByIdListRequest) {
		goodsRestrictedSaleService.deleteByIdList(goodsRestrictedSaleDelByIdListRequest.getRestrictedIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

