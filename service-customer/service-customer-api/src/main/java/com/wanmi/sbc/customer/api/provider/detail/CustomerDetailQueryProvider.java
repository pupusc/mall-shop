package com.wanmi.sbc.customer.api.provider.detail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.detail.*;
import com.wanmi.sbc.customer.api.response.detail.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员明细-会员明细查询API
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerDetailQueryProvider")
public interface CustomerDetailQueryProvider {


    /**
     * 根据会员id查询未删除的会员明细
     * @param request 包含会员id的查询参数 {@link CustomerDetailWithNotDeleteByCustomerIdRequest}
     * @return 会员明细 {@link CustomerDetailGetWithNotDeleteByCustomerIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/get-customer-detail-with-not-delete-by-customer-id")
    BaseResponse<CustomerDetailGetWithNotDeleteByCustomerIdResponse> getCustomerDetailWithNotDeleteByCustomerId(@RequestBody @Valid
                                                                                                                        CustomerDetailWithNotDeleteByCustomerIdRequest
                                                                                                                        request);

    /**
     * 根据会员id查询会员明细
     * @param request 包含会员id的查询参数 {@link CustomerDetailByCustomerIdRequest}
     * @return 会员明细 {@link CustomerDetailGetCustomerIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/get-customer-detail-by-customer-id")
    BaseResponse<CustomerDetailGetCustomerIdResponse> getCustomerDetailByCustomerId(@RequestBody @Valid
                                                                                            CustomerDetailByCustomerIdRequest
                                                                                            request);

    /**
     * 根据id查询会员明细
     * @param request 包含会员明细id的查询参数 {@link CustomerDetailByIdRequest}
     * @return 会员明细 {@link CustomerDetailGetByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/get-customer-detail-by-id")
    BaseResponse<CustomerDetailGetByIdResponse> getCustomerDetailById(@RequestBody @Valid CustomerDetailByIdRequest
                                                                              request);

    /**
     * 多条件查询会员详细信息
     * @param request {@link CustomerDetailListByConditionRequest}
     * @return {@link CustomerDetailListByConditionResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/list-customer-detail-by-condition")
    BaseResponse<CustomerDetailListByConditionResponse> listCustomerDetailByCondition(@RequestBody @Valid
                                                                                              CustomerDetailListByConditionRequest request);

    /**
     * 根据会员id集合查询会员详情基础数据集合
     * @param request {@link CustomerDetailListByCustomerIdsRequest}
     * @return {@link CustomerDetailListByCustomerIdsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/list-customer-detail-id-by-customer-ids")
    BaseResponse<CustomerDetailListByCustomerIdsResponse> listCustomerDetailBaseByCustomerIds(@RequestBody @Valid CustomerDetailListByCustomerIdsRequest request);  /**

     * 根据customerid查询会员是否被禁用以及禁用原因
     * @param request {@link CustomerDetailStatusByCustomerIdsRequest}
     * @return {@link CustomerDetailStatusResponse}
     */
    @PostMapping("/customer/${application.customer.version}/detail/get-customer-status")
    BaseResponse<CustomerDetailStatusResponse> getCustomerStatus(@RequestBody @Valid CustomerDetailStatusByCustomerIdsRequest request);
}
