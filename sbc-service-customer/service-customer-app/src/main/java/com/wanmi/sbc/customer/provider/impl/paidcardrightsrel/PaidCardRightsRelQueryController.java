package com.wanmi.sbc.customer.provider.impl.paidcardrightsrel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardrightsrel.PaidCardRightsRelQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelPageRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelQueryRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelPageResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelListRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelListResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelByIdRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelByIdResponse;
import com.wanmi.sbc.customer.bean.vo.PaidCardRightsRelVO;
import com.wanmi.sbc.customer.paidcardrightsrel.service.PaidCardRightsRelService;
import com.wanmi.sbc.customer.paidcardrightsrel.model.root.PaidCardRightsRel;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>付费会员查询服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@RestController
@Validated
public class PaidCardRightsRelQueryController implements PaidCardRightsRelQueryProvider {
	@Autowired
	private PaidCardRightsRelService paidCardRightsRelService;

	@Override
	public BaseResponse<PaidCardRightsRelPageResponse> page(@RequestBody @Valid PaidCardRightsRelPageRequest paidCardRightsRelPageReq) {
		PaidCardRightsRelQueryRequest queryReq = new PaidCardRightsRelQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardRightsRelPageReq, queryReq);
		Page<PaidCardRightsRel> paidCardRightsRelPage = paidCardRightsRelService.page(queryReq);
		Page<PaidCardRightsRelVO> newPage = paidCardRightsRelPage.map(entity -> paidCardRightsRelService.wrapperVo(entity));
		MicroServicePage<PaidCardRightsRelVO> microPage = new MicroServicePage<>(newPage, paidCardRightsRelPageReq.getPageable());
		PaidCardRightsRelPageResponse finalRes = new PaidCardRightsRelPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PaidCardRightsRelListResponse> list(@RequestBody @Valid PaidCardRightsRelListRequest paidCardRightsRelListReq) {
		PaidCardRightsRelQueryRequest queryReq = new PaidCardRightsRelQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardRightsRelListReq, queryReq);
		List<PaidCardRightsRel> paidCardRightsRelList = paidCardRightsRelService.list(queryReq);
		List<PaidCardRightsRelVO> newList = paidCardRightsRelList.stream().map(entity -> paidCardRightsRelService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PaidCardRightsRelListResponse(newList));
	}

	@Override
	public BaseResponse<PaidCardRightsRelByIdResponse> getById(@RequestBody @Valid PaidCardRightsRelByIdRequest paidCardRightsRelByIdRequest) {
		PaidCardRightsRel paidCardRightsRel = paidCardRightsRelService.getById(paidCardRightsRelByIdRequest.getId());
		return BaseResponse.success(new PaidCardRightsRelByIdResponse(paidCardRightsRelService.wrapperVo(paidCardRightsRel)));
	}

}

