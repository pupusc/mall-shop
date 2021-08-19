package com.wanmi.sbc.customer.api.provider.detail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailAddRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailDeleteRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailModifyRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerStateBatchModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员明细-会员明细添加/修改/删除API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerDetailProvider")
public interface CustomerDetailProvider {

    /**
     * 新增会员明细
     *
     * @param request 批量更新账户状态和禁用原因request{@link CustomerStateBatchModifyRequest}
     * @return 修改会员状态结果{@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/modify-customer-state-by-customer-id")
    BaseResponse modifyCustomerStateByCustomerId(@RequestBody @Valid CustomerStateBatchModifyRequest request);

    /**
     * 删除会员明细
     *
     * @param request 删除会员明细request{@link CustomerDetailDeleteRequest}
     * @return 删除会员结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/delete-customer-detail-by-customer-ids")
    BaseResponse deleteCustomerDetailByCustomerIds(@RequestBody @Valid CustomerDetailDeleteRequest request);

    /**
     * 新增会员明细
     *
     * @param request 新增会员明细request{@link CustomerDetailAddRequest}
     * @return 新增会员结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/add-customer-detail")
    BaseResponse addCustomerDetail(@RequestBody @Valid CustomerDetailAddRequest request);

    /**
     * 修改会员明细
     *
     * @param request 修改会员明细request{@link CustomerDetailModifyRequest}
     * @return 修改会员结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/modify-customer-detail")
    BaseResponse modifyCustomerDetail(@RequestBody @Valid CustomerDetailModifyRequest request);
}
