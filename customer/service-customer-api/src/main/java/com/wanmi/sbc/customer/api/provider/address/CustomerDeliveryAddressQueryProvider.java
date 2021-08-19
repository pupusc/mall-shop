package com.wanmi.sbc.customer.api.provider.address;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.address.*;
import com.wanmi.sbc.customer.api.response.address.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员收货地址-查询API
 *
 * @Author: wanggang
 * @CreateDate: 2018/9/11 17:45
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerDeliveryAddressQueryProvider")
public interface CustomerDeliveryAddressQueryProvider {

    /**
     * 根据收货地址ID查询会员收货地址信息
     *
     * @param customerDeliveryAddressByIdRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/address/get-by-id")
    BaseResponse<CustomerDeliveryAddressByIdResponse> getById(@RequestBody @Valid
                                                                      CustomerDeliveryAddressByIdRequest
                                                                      customerDeliveryAddressByIdRequest);

    /**
     * 查询客户默认的收货地址
     * 如果客户没有设置默认地址，则取该客户其中的一条收货地址
     *
     * @param customerDeliveryAddressRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/address/get-default-any-one-by-customer-id")
    BaseResponse<CustomerDeliveryAddressResponse> getDefaultOrAnyOneByCustomerId(@RequestBody @Valid
                                                                                         CustomerDeliveryAddressRequest customerDeliveryAddressRequest);

    /**
     * 根据用户ID查询用户收货地址列表
     *
     * @param customerDeliveryAddressListRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/address/list-by-customer-id")
    BaseResponse<CustomerDeliveryAddressListResponse> listByCustomerId(@RequestBody @Valid
                                                                               CustomerDeliveryAddressListRequest
                                                                               customerDeliveryAddressListRequest);

    /**
     * 只查询客户默认地址
     *
     * @param customerDeliveryAddressDefaultRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/address/get-default-by-customer-id")
    BaseResponse<CustomerDeliveryAddressDefaultResponse> getDefaultByCustomerId(@RequestBody @Valid
                                                                                        CustomerDeliveryAddressDefaultRequest customerDeliveryAddressDefaultRequest);

    /**
     * 查询该客户有多少条收货地址
     *
     * @param customerDeliveryAddressByCustomerIdRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/address/count-by-customer-id")
    BaseResponse<CustomerDeliveryAddressByCustomerIdResponse> countByCustomerId(@RequestBody @Valid
                                                                                        CustomerDeliveryAddressByCustomerIdRequest
                                                                                        customerDeliveryAddressByCustomerIdRequest);
}
