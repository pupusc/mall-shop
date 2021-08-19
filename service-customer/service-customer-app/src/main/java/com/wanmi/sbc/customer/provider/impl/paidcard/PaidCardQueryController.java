package com.wanmi.sbc.customer.provider.impl.paidcard;

import com.wanmi.sbc.customer.api.request.paidcard.*;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardBuyResponse;
import com.wanmi.sbc.customer.paidcardrule.service.PaidCardRuleService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcard.PaidCardQueryProvider;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardPageResponse;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardListResponse;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardByIdResponse;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.customer.paidcard.service.PaidCardService;
import com.wanmi.sbc.customer.paidcard.model.root.PaidCard;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>付费会员查询服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@RestController
@Validated
public class PaidCardQueryController implements PaidCardQueryProvider {
	@Autowired
	private PaidCardService paidCardService;

	@Autowired
	private PaidCardRuleService paidCardRuleService;


	@Override
	public BaseResponse<PaidCardPageResponse> page(@RequestBody @Valid PaidCardPageRequest paidCardPageReq) {
		PaidCardQueryRequest queryReq = new PaidCardQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardPageReq, queryReq);
		Page<PaidCard> paidCardPage = paidCardService.page(queryReq);
		Page<PaidCardVO> newPage = paidCardPage.map(entity -> {
			PaidCardVO paidCardVO = paidCardService.wrapperVo(entity);
			return paidCardVO;
		});
		MicroServicePage<PaidCardVO> microPage = new MicroServicePage<>(newPage, paidCardPageReq.getPageable());
		PaidCardPageResponse finalRes = new PaidCardPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PaidCardListResponse> list(@RequestBody @Valid PaidCardListRequest paidCardListReq) {
		PaidCardQueryRequest queryReq = new PaidCardQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardListReq, queryReq);
		List<PaidCard> paidCardList = paidCardService.list(queryReq);
		List<PaidCardVO> newList = paidCardList.stream().map(entity -> paidCardService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PaidCardListResponse(newList));
	}

	@Override
	public BaseResponse<PaidCardByIdResponse> getById(@RequestBody @Valid PaidCardByIdRequest paidCardByIdRequest) {
		PaidCard paidCard = paidCardService.getById(paidCardByIdRequest.getId());
		PaidCardVO paidCardVO = paidCardService.wrapperVo(paidCard);
		paidCardVO = paidCardService.fillPaidCardInfo(paidCardVO);
		return BaseResponse.success(new PaidCardByIdResponse(paidCardVO));
	}

	@Override
	public BaseResponse<List<PaidCardVO>> queryCardInfo(@RequestBody CustomerPaidCardQueryRequest request) {
		BaseResponse<List<PaidCardVO>> resp = paidCardService.queryCardInfo(request);
		return resp;
	}

	@Override
	public BaseResponse<PaidCardBuyResponse> commit(@Valid @RequestBody PaidCardBuyRequest req) {
		BaseResponse<PaidCardBuyResponse> resp = this.paidCardRuleService.commit(req);
		return resp;
	}

}

