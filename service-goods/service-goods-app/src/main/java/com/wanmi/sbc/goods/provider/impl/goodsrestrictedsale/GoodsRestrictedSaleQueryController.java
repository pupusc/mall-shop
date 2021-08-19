package com.wanmi.sbc.goods.provider.impl.goodsrestrictedsale;

import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.*;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSalePurchaseResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedPurchaseVO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSalePageResponse;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSaleListResponse;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSaleByIdResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedSaleVO;
import com.wanmi.sbc.goods.goodsrestrictedsale.service.GoodsRestrictedSaleService;
import com.wanmi.sbc.goods.goodsrestrictedsale.model.root.GoodsRestrictedSale;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>限售配置查询服务接口实现</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@RestController
@Validated
public class GoodsRestrictedSaleQueryController implements GoodsRestrictedSaleQueryProvider {
	@Autowired
	private GoodsRestrictedSaleService goodsRestrictedSaleService;

	@Override
	public BaseResponse<GoodsRestrictedSalePageResponse> page(@RequestBody @Valid GoodsRestrictedSalePageRequest goodsRestrictedSalePageReq) {
		GoodsRestrictedSaleQueryRequest queryReq = new GoodsRestrictedSaleQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsRestrictedSalePageReq, queryReq);
		Page<GoodsRestrictedSale> goodsRestrictedSalePage = goodsRestrictedSaleService.page(queryReq);
		Page<GoodsRestrictedSaleVO> newPage = goodsRestrictedSalePage.map(entity -> goodsRestrictedSaleService.wrapperVo(entity));
		MicroServicePage<GoodsRestrictedSaleVO> microPage = new MicroServicePage<>(newPage, goodsRestrictedSalePageReq.getPageable());
		GoodsRestrictedSalePageResponse finalRes = new GoodsRestrictedSalePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<GoodsRestrictedSaleListResponse> list(@RequestBody @Valid GoodsRestrictedSaleListRequest goodsRestrictedSaleListReq) {
		GoodsRestrictedSaleQueryRequest queryReq = new GoodsRestrictedSaleQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsRestrictedSaleListReq, queryReq);
		List<GoodsRestrictedSale> goodsRestrictedSaleList = goodsRestrictedSaleService.list(queryReq);
		List<GoodsRestrictedSaleVO> newList = goodsRestrictedSaleList.stream().map(entity -> goodsRestrictedSaleService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GoodsRestrictedSaleListResponse(newList));
	}

	@Override
	public BaseResponse<GoodsRestrictedSaleByIdResponse> getById(@RequestBody @Valid GoodsRestrictedSaleByIdRequest goodsRestrictedSaleByIdRequest) {
		GoodsRestrictedSale goodsRestrictedSale = goodsRestrictedSaleService.getById(goodsRestrictedSaleByIdRequest.getRestrictedId());
		return BaseResponse.success(new GoodsRestrictedSaleByIdResponse(goodsRestrictedSaleService.wrapperVo(goodsRestrictedSale)));
	}

	@Override
	public BaseResponse validateToByImm(@RequestBody @Valid GoodsRestrictedValidateRequest request) {
		goodsRestrictedSaleService.validateGoodsRestricted(request.getGoodsInfoId(),request.getCustomerVO(), request.getBuyNum());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse validateOrderRestricted(@RequestBody @Valid GoodsRestrictedBatchValidateRequest restrictedBatchValidateRequest) {
		goodsRestrictedSaleService.validateBatchGoodsRestricted(restrictedBatchValidateRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse<GoodsRestrictedSalePurchaseResponse> validatePurchaseRestricted(@Valid GoodsRestrictedBatchValidateRequest restrictedBatchValidateRequest) {
		List<GoodsRestrictedPurchaseVO> goodsRestrictedPurchaseVOS = goodsRestrictedSaleService.getGoodsRestrictedInfo(restrictedBatchValidateRequest);
		return BaseResponse.success(GoodsRestrictedSalePurchaseResponse.builder()
				.goodsRestrictedPurchaseVOS(goodsRestrictedPurchaseVOS)
				.build());
	}

	@Override
	public BaseResponse validateOrderRestrictedSimplify(@RequestBody @Valid GoodsRestrictedBatchValidateOrderCommitRequest request) {
		//goodsRestrictedSaleService.validateBatchGoodsRestricted(request);
		return BaseResponse.SUCCESSFUL();
	}

}

