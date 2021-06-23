package com.wanmi.sbc.goods.provider.impl.goodsrestrictedcustomerrela;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaQueryProvider;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaPageRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaQueryRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaPageResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaListRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaListResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaByIdRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaByIdResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedCustomerRelaVO;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.service.GoodsRestrictedCustomerRelaService;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.model.root.GoodsRestrictedCustomerRela;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>限售配置查询服务接口实现</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@RestController
@Validated
public class GoodsRestrictedCustomerRelaQueryController implements GoodsRestrictedCustomerRelaQueryProvider {
	@Autowired
	private GoodsRestrictedCustomerRelaService goodsRestrictedCustomerRelaService;

	@Override
	public BaseResponse<GoodsRestrictedCustomerRelaPageResponse> page(@RequestBody @Valid GoodsRestrictedCustomerRelaPageRequest goodsRestrictedCustomerRelaPageReq) {
		GoodsRestrictedCustomerRelaQueryRequest queryReq = new GoodsRestrictedCustomerRelaQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsRestrictedCustomerRelaPageReq, queryReq);
		Page<GoodsRestrictedCustomerRela> goodsRestrictedCustomerRelaPage = goodsRestrictedCustomerRelaService.page(queryReq);
		Page<GoodsRestrictedCustomerRelaVO> newPage = goodsRestrictedCustomerRelaPage.map(entity -> goodsRestrictedCustomerRelaService.wrapperVo(entity));
		MicroServicePage<GoodsRestrictedCustomerRelaVO> microPage = new MicroServicePage<>(newPage, goodsRestrictedCustomerRelaPageReq.getPageable());
		GoodsRestrictedCustomerRelaPageResponse finalRes = new GoodsRestrictedCustomerRelaPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<GoodsRestrictedCustomerRelaListResponse> list(@RequestBody @Valid GoodsRestrictedCustomerRelaListRequest goodsRestrictedCustomerRelaListReq) {
		GoodsRestrictedCustomerRelaQueryRequest queryReq = new GoodsRestrictedCustomerRelaQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsRestrictedCustomerRelaListReq, queryReq);
		List<GoodsRestrictedCustomerRela> goodsRestrictedCustomerRelaList = goodsRestrictedCustomerRelaService.list(queryReq);
		List<GoodsRestrictedCustomerRelaVO> newList = goodsRestrictedCustomerRelaList.stream().map(entity -> goodsRestrictedCustomerRelaService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GoodsRestrictedCustomerRelaListResponse(newList));
	}

	@Override
	public BaseResponse<GoodsRestrictedCustomerRelaByIdResponse> getById(@RequestBody @Valid GoodsRestrictedCustomerRelaByIdRequest goodsRestrictedCustomerRelaByIdRequest) {
		GoodsRestrictedCustomerRela goodsRestrictedCustomerRela = goodsRestrictedCustomerRelaService.getById(goodsRestrictedCustomerRelaByIdRequest.getRelaId());
		return BaseResponse.success(new GoodsRestrictedCustomerRelaByIdResponse(goodsRestrictedCustomerRelaService.wrapperVo(goodsRestrictedCustomerRela)));
	}

}

