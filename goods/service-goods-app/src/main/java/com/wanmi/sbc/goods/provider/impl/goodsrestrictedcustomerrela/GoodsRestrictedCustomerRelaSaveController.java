package com.wanmi.sbc.goods.provider.impl.goodsrestrictedcustomerrela;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaSaveProvider;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaAddRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaAddResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaModifyRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaModifyResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaDelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaDelByIdListRequest;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.service.GoodsRestrictedCustomerRelaService;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.model.root.GoodsRestrictedCustomerRela;
import javax.validation.Valid;

/**
 * <p>限售配置保存服务接口实现</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@RestController
@Validated
public class GoodsRestrictedCustomerRelaSaveController implements GoodsRestrictedCustomerRelaSaveProvider {
	@Autowired
	private GoodsRestrictedCustomerRelaService goodsRestrictedCustomerRelaService;

	@Override
	public BaseResponse<GoodsRestrictedCustomerRelaAddResponse> add(@RequestBody @Valid GoodsRestrictedCustomerRelaAddRequest goodsRestrictedCustomerRelaAddRequest) {
		GoodsRestrictedCustomerRela goodsRestrictedCustomerRela = new GoodsRestrictedCustomerRela();
		KsBeanUtil.copyPropertiesThird(goodsRestrictedCustomerRelaAddRequest, goodsRestrictedCustomerRela);
		return BaseResponse.success(new GoodsRestrictedCustomerRelaAddResponse(
				goodsRestrictedCustomerRelaService.wrapperVo(goodsRestrictedCustomerRelaService.add(goodsRestrictedCustomerRela))));
	}

	@Override
	public BaseResponse<GoodsRestrictedCustomerRelaModifyResponse> modify(@RequestBody @Valid GoodsRestrictedCustomerRelaModifyRequest goodsRestrictedCustomerRelaModifyRequest) {
		GoodsRestrictedCustomerRela goodsRestrictedCustomerRela = new GoodsRestrictedCustomerRela();
		KsBeanUtil.copyPropertiesThird(goodsRestrictedCustomerRelaModifyRequest, goodsRestrictedCustomerRela);
		return BaseResponse.success(new GoodsRestrictedCustomerRelaModifyResponse(
				goodsRestrictedCustomerRelaService.wrapperVo(goodsRestrictedCustomerRelaService.modify(goodsRestrictedCustomerRela))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid GoodsRestrictedCustomerRelaDelByIdRequest goodsRestrictedCustomerRelaDelByIdRequest) {
		goodsRestrictedCustomerRelaService.deleteById(goodsRestrictedCustomerRelaDelByIdRequest.getRelaId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid GoodsRestrictedCustomerRelaDelByIdListRequest goodsRestrictedCustomerRelaDelByIdListRequest) {
		goodsRestrictedCustomerRelaService.deleteByIdList(goodsRestrictedCustomerRelaDelByIdListRequest.getRelaIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

