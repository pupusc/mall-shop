package com.wanmi.sbc.customer.api.provider.customersignrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordAddRequest;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordDelByIdRequest;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordAddResponse;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>用户签到记录保存服务Provider</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerSignRecordSaveProvider")
public interface CustomerSignRecordSaveProvider {

	/**
	 * 新增用户签到记录API
	 *
	 * @author wangtao
	 * @param customerSignRecordAddRequest 用户签到记录新增参数结构 {@link CustomerSignRecordAddRequest}
	 * @return 新增的用户签到记录信息 {@link CustomerSignRecordAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/customersignrecord/add")
    BaseResponse<CustomerSignRecordAddResponse> add(@RequestBody @Valid CustomerSignRecordAddRequest customerSignRecordAddRequest);

	/**
	 * 单个删除用户签到记录API
	 *
	 * @author wangtao
	 * @param customerSignRecordDelByIdRequest 单个删除参数结构 {@link CustomerSignRecordDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/customersignrecord/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid CustomerSignRecordDelByIdRequest customerSignRecordDelByIdRequest);

	/**
	 * 批量删除用户签到记录API
	 *
	 * @author wangtao
	 * @param customerSignRecordDelByIdListRequest 批量删除参数结构 {@link CustomerSignRecordDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/customersignrecord/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid CustomerSignRecordDelByIdListRequest customerSignRecordDelByIdListRequest);

}

