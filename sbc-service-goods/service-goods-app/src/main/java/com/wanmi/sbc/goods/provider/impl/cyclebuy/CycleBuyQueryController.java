package com.wanmi.sbc.goods.provider.impl.cyclebuy;

import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.request.cyclebuy.*;
import com.wanmi.sbc.goods.api.response.cyclebuy.*;
import com.wanmi.sbc.goods.bean.vo.CycleBuyGiftVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuySendDateRuleVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuy;
import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuyGift;
import com.wanmi.sbc.goods.cyclebuy.repository.CycleBuyGiftRepository;
import com.wanmi.sbc.goods.cyclebuy.service.CycleBuyService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>周期购活动查询服务接口实现</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@RestController
@Validated
public class CycleBuyQueryController implements CycleBuyQueryProvider {


	@Autowired
	private CycleBuyService cycleBuyService;

	@Autowired
	private GoodsService goodsService;


	@Autowired
	private CycleBuyGiftRepository cycleBuyGiftRepository;

	@Override
	public BaseResponse<CycleBuyPageResponse> page(@RequestBody @Valid CycleBuyPageRequest cycleBuyPageReq) {
		CycleBuyQueryRequest queryReq = new CycleBuyQueryRequest();
		KsBeanUtil.copyPropertiesThird(cycleBuyPageReq, queryReq);
		Page<CycleBuy> cycleBuyPage = cycleBuyService.page(queryReq);
		Page<CycleBuyVO> newPage = cycleBuyPage.map(entity -> cycleBuyService.wrapperVo(entity));
		MicroServicePage<CycleBuyVO> microPage = new MicroServicePage<>(newPage, cycleBuyPageReq.getPageable());
		CycleBuyPageResponse finalRes = new CycleBuyPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<CycleBuyListResponse> list(@RequestBody @Valid CycleBuyListRequest cycleBuListReq) {
		CycleBuyQueryRequest queryReq = new CycleBuyQueryRequest();
		KsBeanUtil.copyPropertiesThird(cycleBuListReq, queryReq);
		List<CycleBuy> cycleBuyList = cycleBuyService.list(queryReq);
		List<CycleBuyVO> newList = cycleBuyList.stream().map(entity -> cycleBuyService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new CycleBuyListResponse(newList));
	}

	@Override
	public BaseResponse<CycleBuyByIdResponse> getById(@RequestBody @Valid CycleBuyByIdRequest cycleBuyByIdRequest) {
		CycleBuy cycleBuy = cycleBuyService.getById(cycleBuyByIdRequest.getId());

		CycleBuyVO cycleBuyVO = cycleBuyService.wrapperVo(cycleBuy);

		//根据商品Id查询商品信息
		Goods goods = goodsService.getGoodsById(cycleBuy.getOriginGoodsId());

		cycleBuyVO.setGoodsVO(KsBeanUtil.convert(goods, GoodsVO.class));

		//根据商品Id查询周期购商品信息填充erp编码
		Goods erpGoods = goodsService.getGoodsById(cycleBuy.getGoodsId());
		//填充erp编码
		cycleBuyVO.setErpGoodsNo(erpGoods.getErpGoodsNo());

		if (Objects.nonNull(cycleBuy)) {
			//赠品
			List<CycleBuyGift> cycleBuyGiftList = cycleBuyGiftRepository.findByCycleBuyId(cycleBuy.getId());
			cycleBuyVO.setCycleBuyGiftVOList(KsBeanUtil.convertList(cycleBuyGiftList, CycleBuyGiftVO.class));
		}
		return BaseResponse.success(new CycleBuyByIdResponse(cycleBuyVO));
	}

	@Override
	public BaseResponse<CycleBuyByGoodsIdResponse> getByGoodsId(@Valid @RequestBody CycleBuyByGoodsIdRequest cycleBuyByGoodsIdRequest) {
		CycleBuyVO cycleBuyVO = cycleBuyService.getByGoodsId(cycleBuyByGoodsIdRequest.getGoodsId());
		return BaseResponse.success(new CycleBuyByGoodsIdResponse(cycleBuyVO));
	}


	@Override
	public BaseResponse<CycleBuyByGoodsIdResponse> getByGoodsDetailsId(@Valid @RequestBody CycleBuyByGoodsIdRequest cycleBuyByGoodsIdRequest) {
		CycleBuyVO cycleBuyVO = cycleBuyService.getByGoodsDetailsId(cycleBuyByGoodsIdRequest.getGoodsId());
		//根据商品Id查询商品信息
		if (Objects.nonNull(cycleBuyVO)) {
			Goods goods = goodsService.getGoodsById(cycleBuyVO.getOriginGoodsId());
			cycleBuyVO.setGoodsVO(KsBeanUtil.convert(goods, GoodsVO.class));
		}
		return BaseResponse.success(new CycleBuyByGoodsIdResponse(cycleBuyVO));
	}


	@Override
	public BaseResponse<CycleBuySendDateRuleResponse> getSendDateRuleList(@Valid @RequestBody CycleBuySendDateRuleRequest cycleBuySendDateRuleRequest) {
		List<CycleBuySendDateRuleVO> voList = cycleBuyService.getCycleBuySendDateRuleVOList(cycleBuySendDateRuleRequest.getDeliveryCycle(), cycleBuySendDateRuleRequest.getRules());
		return BaseResponse.success(new CycleBuySendDateRuleResponse(voList));
	}

}

