package com.wanmi.sbc.crm.provider.impl.customerplanconversion;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.customerplanconversion.CustomerPlanConversionSaveProvider;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionAddRequest;
import com.wanmi.sbc.crm.api.response.customerplanconversion.CustomerPlanConversionAddResponse;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionModifyRequest;
import com.wanmi.sbc.crm.api.response.customerplanconversion.CustomerPlanConversionModifyResponse;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionDelByIdListRequest;
import com.wanmi.sbc.crm.customerplanconversion.service.CustomerPlanConversionService;
import com.wanmi.sbc.crm.customerplanconversion.model.root.CustomerPlanConversion;
import javax.validation.Valid;

/**
 * <p>运营计划转化效果保存服务接口实现</p>
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@RestController
@Validated
public class CustomerPlanConversionSaveController implements CustomerPlanConversionSaveProvider {
	@Autowired
	private CustomerPlanConversionService customerPlanConversionService;

	@Override
	public BaseResponse<CustomerPlanConversionAddResponse> add(@RequestBody @Valid CustomerPlanConversionAddRequest customerPlanConversionAddRequest) {
		CustomerPlanConversion customerPlanConversion = new CustomerPlanConversion();
		KsBeanUtil.copyPropertiesThird(customerPlanConversionAddRequest, customerPlanConversion);
		return BaseResponse.success(new CustomerPlanConversionAddResponse(
				customerPlanConversionService.wrapperVo(customerPlanConversionService.add(customerPlanConversion))));
	}

	@Override
	public BaseResponse<CustomerPlanConversionModifyResponse> modify(@RequestBody @Valid CustomerPlanConversionModifyRequest customerPlanConversionModifyRequest) {
		CustomerPlanConversion customerPlanConversion = new CustomerPlanConversion();
		KsBeanUtil.copyPropertiesThird(customerPlanConversionModifyRequest, customerPlanConversion);
		return BaseResponse.success(new CustomerPlanConversionModifyResponse(
				customerPlanConversionService.wrapperVo(customerPlanConversionService.modify(customerPlanConversion))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid CustomerPlanConversionDelByIdRequest customerPlanConversionDelByIdRequest) {
		customerPlanConversionService.deleteById(customerPlanConversionDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid CustomerPlanConversionDelByIdListRequest customerPlanConversionDelByIdListRequest) {
		customerPlanConversionService.deleteByIdList(customerPlanConversionDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

