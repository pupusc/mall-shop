package com.wanmi.sbc.customer.provider.impl.paidcardcustomerrel;

import com.wanmi.sbc.customer.api.request.paidcard.PaidCardQueryRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.*;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.*;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.paidcard.model.root.PaidCard;
import com.wanmi.sbc.customer.paidcard.service.PaidCardService;
import com.wanmi.sbc.customer.service.CustomerService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.paidcardcustomerrel.service.PaidCardCustomerRelService;
import com.wanmi.sbc.customer.paidcardcustomerrel.model.root.PaidCardCustomerRel;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>付费会员查询服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@RestController
@Validated
public class PaidCardCustomerRelQueryController implements PaidCardCustomerRelQueryProvider {
	@Autowired
	private PaidCardCustomerRelService paidCardCustomerRelService;

	@Autowired
	private PaidCardService paidCardService;

	@Autowired
	private CustomerService customerService;

	@Override
	public BaseResponse<PaidCardCustomerRelPageResponse> page(@RequestBody @Valid PaidCardCustomerRelPageRequest paidCardCustomerRelPageReq) {
		PaidCardCustomerRelQueryRequest queryReq = new PaidCardCustomerRelQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardCustomerRelPageReq, queryReq);
		Page<PaidCardCustomerRel> paidCardCustomerRelPage = paidCardCustomerRelService.page(queryReq);
		Page<PaidCardCustomerRelVO> newPage = paidCardCustomerRelPage.map(entity -> paidCardCustomerRelService.wrapperVo(entity));
		MicroServicePage<PaidCardCustomerRelVO> microPage = new MicroServicePage<>(newPage, paidCardCustomerRelPageReq.getPageable());
		PaidCardCustomerRelPageResponse finalRes = new PaidCardCustomerRelPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<List<PaidCardCustomerRelVO>> pageByMaxAutoId(@RequestBody @Valid PaidCardCustomerRelQueryRequest request) {
		List<PaidCardCustomerRel> paidCardCustomerRelList = paidCardCustomerRelService.pageByMaxAutoId(request);
		List<PaidCardCustomerRelVO> result = new ArrayList<>();
		for (PaidCardCustomerRel paidCardCustomerRelParam : paidCardCustomerRelList) {
			result.add(paidCardCustomerRelService.wrapperVo(paidCardCustomerRelParam));
		}
		return BaseResponse.success(result);
	}

	@Override
	public BaseResponse<PaidCardCustomerRelListResponse> list(@RequestBody @Valid PaidCardCustomerRelListRequest paidCardCustomerRelListReq) {
		PaidCardCustomerRelQueryRequest queryReq = new PaidCardCustomerRelQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardCustomerRelListReq, queryReq);
		List<PaidCardCustomerRel> paidCardCustomerRelList = paidCardCustomerRelService.list(queryReq);
		List<String> paidCardIdList = paidCardCustomerRelList.stream().map(x -> x.getPaidCardId()).distinct().collect(Collectors.toList());
		List<String> customerIdList = paidCardCustomerRelList.stream().map(x -> x.getCustomerId()).distinct().collect(Collectors.toList());
		List<PaidCard> paidCardList = paidCardService.list(PaidCardQueryRequest.builder().idList(paidCardIdList).build());
		List<Customer> customerList = customerService.findByCustomerIdIn(customerIdList);
		List<PaidCardCustomerRelVO> newList = paidCardCustomerRelList.stream().map(entity -> {
			PaidCard paidCard = paidCardList.stream().filter(x -> x.getId().equals(entity.getPaidCardId())).findFirst().orElse(new PaidCard());
			Customer customer = customerList.stream().filter(x -> x.getCustomerId().equals(entity.getCustomerId())).findFirst().orElse(new Customer());
			entity.setPaidCardName(paidCard.getName());
			entity.setPhone(customer.getCustomerAccount());
			return paidCardCustomerRelService.wrapperVo(entity);
		}).collect(Collectors.toList());
		return BaseResponse.success(new PaidCardCustomerRelListResponse(newList));
	}



	@Override
	public BaseResponse<PaidCardCustomerIdListResponse> listByCustomer(@RequestBody @Valid PaidCardCustomerRelListRequest paidCardCustomerRelListReq) {
		PaidCardCustomerRelQueryRequest queryReq = new PaidCardCustomerRelQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardCustomerRelListReq, queryReq);
		List<PaidCardCustomerRel> paidCardCustomerRelList = paidCardCustomerRelService.list(queryReq);
		List<String> customersIdList = paidCardCustomerRelList.stream().map(
				PaidCardCustomerRel::getCustomerId).collect(Collectors.toList());
		return BaseResponse.success(new PaidCardCustomerIdListResponse(customersIdList));
	}

	@Override
	public BaseResponse<PaidCardCustomerRelByIdResponse> getById(@RequestBody @Valid PaidCardCustomerRelByIdRequest paidCardCustomerRelByIdRequest) {
		PaidCardCustomerRel paidCardCustomerRel = paidCardCustomerRelService.getById(paidCardCustomerRelByIdRequest.getId());
		return BaseResponse.success(new PaidCardCustomerRelByIdResponse(paidCardCustomerRelService.wrapperVo(paidCardCustomerRel)));
	}

	@Override
	public BaseResponse<List<PaidCardCustomerRelVO>> getRelInfo( @RequestBody PaidCardCustomerRelQueryRequest request) {
		BaseResponse<List<PaidCardCustomerRelVO>> response =  this.paidCardCustomerRelService.getRelInfo(request);
		return response;
	}

	@Override
	public BaseResponse<List<PaidCardCustomerRelVO>> listCustomerRelFullInfo( @RequestBody PaidCardCustomerRelListRequest request) {
		List<PaidCardCustomerRelVO> result = this.paidCardCustomerRelService.listCustomerRelFullInfo(request);
/*		if(CollectionUtils.isNotEmpty(result) && result.size()>5){
			result = result.stream().limit(5L).collect(Collectors.toList());
		}*/
		return BaseResponse.success(result);
	}

	@Override
	public BaseResponse<PaidCardCustomerIdPageResponse> listCustomerIdByPageable(@RequestBody PaidCustomerIdPageRequest request) {
		List<String> customerIds = paidCardCustomerRelService.listCustomerIdByPageable(request.getPageRequest());
		return BaseResponse.success(new PaidCardCustomerIdPageResponse(customerIds));
	}

	@Override
	public BaseResponse<List<PaidCardVO>> getMaxDiscountPaidCard(@Valid @RequestBody MaxDiscountPaidCardRequest request) {
		List<PaidCardVO> paidCardVOList =  paidCardCustomerRelService.getMaxDiscountPaidCard(request);
		return BaseResponse.success(paidCardVOList);
	}
}

