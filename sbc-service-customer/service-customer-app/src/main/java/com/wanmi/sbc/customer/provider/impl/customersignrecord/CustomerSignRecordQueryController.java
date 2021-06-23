package com.wanmi.sbc.customer.provider.impl.customersignrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customersignrecord.CustomerSignRecordQueryProvider;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordGetByDaysRequest;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordListRequest;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordQueryRequest;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordByIdResponse;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordListResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerSignRecordVO;
import com.wanmi.sbc.customer.customersignrecord.model.root.CustomerSignRecord;
import com.wanmi.sbc.customer.customersignrecord.service.CustomerSignRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>用户签到记录查询服务接口实现</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@RestController
@Validated
public class CustomerSignRecordQueryController implements CustomerSignRecordQueryProvider {
	@Autowired
	private CustomerSignRecordService customerSignRecordService;

	@Override
	public BaseResponse<CustomerSignRecordListResponse> list(@RequestBody @Valid CustomerSignRecordListRequest customerSignRecordListReq) {
		CustomerSignRecordQueryRequest queryReq = new CustomerSignRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(customerSignRecordListReq, queryReq);
		List<CustomerSignRecord> customerSignRecordList = customerSignRecordService.list(queryReq);
		List<CustomerSignRecordVO> newList = customerSignRecordList.stream().map(entity -> customerSignRecordService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new CustomerSignRecordListResponse(newList));
	}

	@Override
	public BaseResponse<CustomerSignRecordListResponse> listByMonth(@RequestBody @Valid CustomerSignRecordListRequest customerSignRecordListReq) {
		CustomerSignRecordQueryRequest queryReq = new CustomerSignRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(customerSignRecordListReq, queryReq);
		List<CustomerSignRecord> customerSignRecordList = customerSignRecordService.listByMonth(queryReq);
		List<CustomerSignRecordVO> newList = customerSignRecordList.stream().map(entity -> customerSignRecordService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new CustomerSignRecordListResponse(newList));
	}

	@Override
	public BaseResponse<CustomerSignRecordByIdResponse> getRecordByDays(@RequestBody @Valid CustomerSignRecordGetByDaysRequest request) {
		CustomerSignRecord customerSignRecord = customerSignRecordService.getRecordByDays(request.getCustomerId(),
				request.getDays());
		return BaseResponse.success(new CustomerSignRecordByIdResponse(customerSignRecordService.wrapperVo(customerSignRecord)));
	}

}

