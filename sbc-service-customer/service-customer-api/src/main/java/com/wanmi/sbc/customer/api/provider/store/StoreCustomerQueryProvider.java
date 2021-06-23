package com.wanmi.sbc.customer.api.provider.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoQueryRequest;
import com.wanmi.sbc.customer.api.request.store.*;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoGetResponse;
import com.wanmi.sbc.customer.api.response.store.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-11 15:25 <br>
 * @see com.wanmi.sbc.customer.api.provider.store <br>
 * @since V1.0<br>
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreCustomerQueryProvider")
public interface StoreCustomerQueryProvider {

    /**
     * 根据店铺标识查询店铺的会员列表
     *
     * @param storeCustomerQueryRequest {@link StoreCustomerQueryRequest}
     * @return 平台客户列表信息  {@link StoreCustomerResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-customer-by-store-id")
    BaseResponse<StoreCustomerResponse> listCustomerByStoreId(@RequestBody @Valid StoreCustomerQueryRequest
                                                                      storeCustomerQueryRequest);

    /**
     * 根据店铺标识查询店铺的会员列表，不区分会员禁用状态
     *
     * @param request {@link StoreCustomerQueryRequest}
     * @return 平台客户列表信息 {@link StoreCustomerResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-all-customer")
    BaseResponse<StoreCustomerResponse> listAllCustomer(@RequestBody @Valid StoreCustomerQueryRequest request);


    /**
     * 根据店铺标识和用户ID查询店铺的会员列表，不区分会员禁用状态
     *
     * @param request {@link StoreCustomerQueryRequest}
     * @return 平台客户列表信息 {@link StoreCustomerResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-customer-by-ids")
    BaseResponse<StoreCustomerResponse> listCustomerByIds(@RequestBody @Valid StoreCustomerQueryRequest request);

    /**
     * 查询平台所有会员
     *
     * @return 平台客户列表信息 {@link StoreCustomerResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-boss-all-customer")
    BaseResponse<StoreCustomerResponse> listBossAllCustomer();

    /**
     * 根据会员名称查询平台会员
     *
     * @return 平台客户列表信息 {@link StoreCustomerResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-boss-customer-by-name")
    BaseResponse<StoreCustomerResponse> listBossCustomerByName(@RequestBody @Valid StoreCustomerQueryByCustomerNameRequest
                                                                    storeCustomerQueryByCustomerNameRequest);


    /**
     * 根据店铺标识查询店铺的会员列表
     *
     * @param storeCustomerQueryByEmployeeRequest {@link StoreCustomerQueryByEmployeeRequest}
     * @return 平台客户列表信息 {@link StoreCustomerResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-customer")
    BaseResponse<StoreCustomerResponse> listCustomer(@RequestBody @Valid StoreCustomerQueryByEmployeeRequest
                                                             storeCustomerQueryByEmployeeRequest);

    /**
     * 查询平台所有会员
     *
     * @param storeCustomerQueryByEmployeeRequest {@link StoreCustomerQueryByEmployeeRequest}
     * @return 平台客户列表信息 {@link StoreCustomerResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-boss-customer")
    BaseResponse<StoreCustomerResponse> listBossCustomer(@RequestBody @Valid StoreCustomerQueryByEmployeeRequest
                                                             storeCustomerQueryByEmployeeRequest);

    /**
     * 通过客户Id获取商家记录
     *
     * @param companyInfoQueryRequest {@link CompanyInfoQueryRequest}
     * @return 公司信息 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-company-info-by-customer-id")
    BaseResponse<CompanyInfoGetResponse> getCompanyInfoByCustomerId(@RequestBody @Valid CompanyInfoQueryRequest
                                                                            companyInfoQueryRequest);

    /**
     * 获取客户-商家的从属记录，一个客户只会从属于一个商家
     *
     * @param companyInfoQueryRequest {@link CompanyInfoQueryRequest}
     * @return 公司信息 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-company-info-belong-by-customer-id")
    BaseResponse<CompanyInfoGetResponse> getCompanyInfoBelongByCustomerId(@RequestBody @Valid CompanyInfoQueryRequest
                                                                            companyInfoQueryRequest);

    /**
     * 获取客户-商家的关联记录  （原findCustomerRelatedForAll接口和findCustomerRelatedForPlatform合成一个)
     *
     * @param storeCustomerRelaQueryRequest {@link StoreCustomerRelaQueryRequest}
     * @return 客户商家关联信息 {@link StoreCustomerRelaResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-customer-related")
    BaseResponse<StoreCustomerRelaResponse> getCustomerRelated(@RequestBody @Valid StoreCustomerRelaQueryRequest
                                                                       storeCustomerRelaQueryRequest);


    /**
     * 获取和某个商家有关联关系的记录
     *
     * @param storeCustomerRelaQueryRequest {@link StoreCustomerRelaQueryRequest} 只用 companyInfoId
     * @return 客户商家关联列表信息 {@link StoreCustomerRelaListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-related-customer-by-company-id")
    BaseResponse<StoreCustomerRelaListResponse> listRelatedCustomerByCompanyId(@RequestBody @Valid
                                                                                       StoreCustomerRelaQueryRequest
                                                                                       storeCustomerRelaQueryRequest);

    /**
     * 获取客户-商家的从属记录，一个客户只会从属于一个商家
     *
     * @param storeCustomerRelaQueryRequest {@link StoreCustomerRelaQueryRequest} 只用 customerId
     * @return 客户商家关联信息 {@link StoreCustomerRelaResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-customer-belong")
    BaseResponse<StoreCustomerRelaResponse> getCustomerBelong(@RequestBody @Valid StoreCustomerRelaQueryRequest
                                                                      storeCustomerRelaQueryRequest);

    /**
     * 根据条件获取店铺-会员关联数据
     *
     * @param request {@link StoreCustomerRelaListByConditionRequest}
     * @return 客户商家关联列信息 {@link StoreCustomerRelaListByConditionResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-by-condition")
    BaseResponse<StoreCustomerRelaListByConditionResponse> listByCondition(@RequestBody @Valid
                                                                                   StoreCustomerRelaListByConditionRequest
                                                                                   request);
    /**
     * 查询店铺的会员ID集合
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/store/list-customer-id-by-store-id")
    BaseResponse<StoreCustomerRelaListCustomerIdByStoreIdResponse> listCustomerIdByStoreId(@RequestBody @Valid StoreCustomerRelaListCustomerIdByStoreIdRequest request);
}