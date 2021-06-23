package com.wanmi.sbc.customer.api.provider.invoice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.invoice.*;
import com.wanmi.sbc.customer.api.response.invoice.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员增票资质-查询API
 * @Author: wanggang
 * @CreateDate: 2018/9/18 10:20
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerInvoiceQueryProvider")
public interface CustomerInvoiceQueryProvider {

    /**
     * 分页查询会员增票资质
     * @param customerInvoicePageRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/invoice/page")
    BaseResponse<CustomerInvoicePageResponse> page(@RequestBody @Valid CustomerInvoicePageRequest
                                                           customerInvoicePageRequest);

    /**
     * 根据用户ID查询会员增票资质（未删除且已审核）
     * @param customerInvoiceByCustomerIdRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/invoice/get-by-customer-id-and-del-flag-and-check-state")
    BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse> getByCustomerIdAndDelFlagAndCheckState
    (@RequestBody @Valid CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest customerInvoiceByCustomerIdRequest);

    /**
     * 根据会员ID查询会员增票资质（已审核）
     * @param customerInvoiceByCustomerIdAndCheckStateRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/invoice/get-by-customer-id-and-check-state")
    BaseResponse<CustomerInvoiceByCustomerIdAndCheckStateResponse> getByCustomerIdAndCheckState(@RequestBody @Valid
                                                                                                        CustomerInvoiceByCustomerIdAndCheckStateRequest customerInvoiceByCustomerIdAndCheckStateRequest);

    /**
     * 根据用户ID查询会员增票资质（未删除）
     * @param customerInvoiceByCustomerIdRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/invoice/get-by-customer-id-and-del-flag")
    BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagResponse> getByCustomerIdAndDelFlag(@RequestBody @Valid CustomerInvoiceByCustomerIdAndDelFlagRequest customerInvoiceByCustomerIdRequest);

    /**
     * 根据用户ID查询是否有会员增票资质
     * @param customerInvoiceByCustomerIdRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/invoice/get-by-customer-id")
    BaseResponse<CustomerInvoiceByCustomerIdResponse> getByCustomerId(@RequestBody @Valid CustomerInvoiceByCustomerIdRequest customerInvoiceByCustomerIdRequest);

    /**
     * 根据增票资质ID查询会员增票资质（未删除）
     * @param customerInvoiceByCustomerIdRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/invoice/get-by-id-and-del-flag")
    BaseResponse<CustomerInvoiceByIdAndDelFlagResponse> getByIdAndDelFlag(@RequestBody @Valid CustomerInvoiceByIdAndDelFlagRequest customerInvoiceByCustomerIdRequest);

    /**
     * 查询增票资质配置
     *
     * 不再需要，可以直接调用auditQueryProvider.getInvoiceConfig()接口获取，避免setting-api传递依赖
     * @return
     */
//    @PostMapping("/customer/${application.customer.version}/invoice/get-customer-invoice-config")
//    BaseResponse<CustomerInvoiceConfigResponse> getCustomerInvoiceConfig();

    /**
     * 根据纳税人识别号查询客户的增专票信息（未删除）
     * @param customerInvoiceByTaxpayerNumberRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/invoice/get-by-taxpayer-number")
    BaseResponse<CustomerInvoiceByTaxpayerNumberResponse> getByTaxpayerNumber(@RequestBody @Valid CustomerInvoiceByTaxpayerNumberRequest customerInvoiceByTaxpayerNumberRequest);

}
