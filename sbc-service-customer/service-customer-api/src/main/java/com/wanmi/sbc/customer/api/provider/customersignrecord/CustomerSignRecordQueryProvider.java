package com.wanmi.sbc.customer.api.provider.customersignrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordGetByDaysRequest;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordListRequest;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordByIdResponse;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>用户签到记录查询服务Provider</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerSignRecordQueryProvider")
public interface CustomerSignRecordQueryProvider {

	/**
	 * 列表查询用户签到记录API
	 *
	 * @author wangtao
	 * @param customerSignRecordListReq 列表请求参数和筛选对象 {@link CustomerSignRecordListRequest}
	 * @return 用户签到记录的列表信息 {@link CustomerSignRecordListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/customersignrecord/list")
    BaseResponse<CustomerSignRecordListResponse> list(@RequestBody @Valid CustomerSignRecordListRequest customerSignRecordListReq);

	@PostMapping("/customer/${application.customer.version}/customersignrecord/list-by-month")
    BaseResponse<CustomerSignRecordListResponse> listByMonth(@RequestBody @Valid CustomerSignRecordListRequest customerSignRecordListReq);

	@PostMapping("/customer/${application.customer.version}/customersignrecord/get-tomorrow-record")
    BaseResponse<CustomerSignRecordByIdResponse> getRecordByDays(@RequestBody @Valid CustomerSignRecordGetByDaysRequest customerSignRecordGetByDaysRequest);
}

