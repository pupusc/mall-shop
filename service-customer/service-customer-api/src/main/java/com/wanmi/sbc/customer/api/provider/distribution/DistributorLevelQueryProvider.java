package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByIdsRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelBaseResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelListAllResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 分销员等级-查询接口
 * @author: Geek Wang
 * @createDate: 2019/6/13 16:27
 * @version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributorLevelQueryProvider")
public interface DistributorLevelQueryProvider {

	/**
	 * 查询分销员等级基础信息列表（仅包含字段：分销员等级ID、分销员等级名称）
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/distributor/level/list-base-info")
	BaseResponse<DistributorLevelBaseResponse> listBaseInfo();

    /**
     * 查询分销等级列表
     */
    @PostMapping("/customer/${application.customer.version}/distributor-level/list-all")
	BaseResponse<DistributorLevelListAllResponse> listAll();

	/**
	 * 根据会员ID查询分销员等级信息
	 * @param request
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/distributor/level/get-by-customer-id")
	BaseResponse<DistributorLevelByCustomerIdResponse> getByCustomerId(@RequestBody @Valid DistributorLevelByCustomerIdRequest request);


	/**
	 * 根据分销员id查询分销员等级
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/distributor/level/list-by-ids")
	BaseResponse<DistributorLevelBaseResponse> listByIds(@RequestBody @Valid DistributorLevelByIdsRequest request);
}
