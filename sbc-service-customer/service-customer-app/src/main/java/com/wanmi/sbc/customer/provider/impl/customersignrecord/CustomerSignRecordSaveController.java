package com.wanmi.sbc.customer.provider.impl.customersignrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customersignrecord.CustomerSignRecordSaveProvider;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordAddRequest;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordAddResponse;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordDelByIdListRequest;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordDelByIdRequest;
import com.wanmi.sbc.customer.customersignrecord.model.root.CustomerSignRecord;
import com.wanmi.sbc.customer.customersignrecord.service.CustomerSignRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>用户签到记录保存服务接口实现</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@RestController
@Validated
public class CustomerSignRecordSaveController implements CustomerSignRecordSaveProvider {
	@Autowired
	private CustomerSignRecordService customerSignRecordService;

	@Override
	public BaseResponse<CustomerSignRecordAddResponse> add(@RequestBody @Valid CustomerSignRecordAddRequest customerSignRecordAddRequest) {
		CustomerSignRecord customerSignRecord = new CustomerSignRecord();
		KsBeanUtil.copyPropertiesThird(customerSignRecordAddRequest, customerSignRecord);
		return BaseResponse.success(new CustomerSignRecordAddResponse(
				customerSignRecordService.wrapperVo(customerSignRecordService.add(customerSignRecord))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid CustomerSignRecordDelByIdRequest customerSignRecordDelByIdRequest) {
		customerSignRecordService.deleteById(customerSignRecordDelByIdRequest.getSignRecordId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid CustomerSignRecordDelByIdListRequest customerSignRecordDelByIdListRequest) {
		customerSignRecordService.deleteByIdList(customerSignRecordDelByIdListRequest.getSignRecordIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

