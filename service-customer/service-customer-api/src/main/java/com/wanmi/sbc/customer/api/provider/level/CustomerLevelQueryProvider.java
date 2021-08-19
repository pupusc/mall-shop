package com.wanmi.sbc.customer.api.provider.level;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.level.*;
import com.wanmi.sbc.customer.api.response.level.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员等级-会员等级查询API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerLevelQueryProvider")
public interface CustomerLevelQueryProvider {

    /**
     * 根据会员id查询未删除的会员等级
     *
     * @param request 查询参数 {@link CustomerLevelPageRequest}
     * @return 会员等级 {@link CustomerLevelPageResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/page-customer-level")
    BaseResponse<CustomerLevelPageResponse> pageCustomerLevel(@RequestBody @Valid
                                                                      CustomerLevelPageRequest
                                                                      request);

    /**
     * 统计客户等级信息
     *
     * @return 会员等级总数 {@link CustomerLevelCountResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/count-customer-level")
    BaseResponse<CustomerLevelCountResponse> countCustomerLevel();

    /**
     * 查询所有客户等级
     *
     * @return 会员等级 {@link CustomerLevelListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/list-all-customer-level")
    BaseResponse<CustomerLevelListResponse> listAllCustomerLevel();

    /**
     * 查询单个客户等级
     *
     * @param request 查询参数 {@link CustomerLevelGetRequest}
     * @return 会员等级 {@link CustomerLevelGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/get-customer-level")
    BaseResponse<CustomerLevelGetResponse> getCustomerLevel(@RequestBody CustomerLevelGetRequest request);

    /**
     * 根据Id获取客户等级
     *
     * @param request Id参数 {@link CustomerLevelByIdRequest}
     * @return 会员等级 {@link CustomerLevelByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/get-customer-level-by-id")
    BaseResponse<CustomerLevelByIdResponse> getCustomerLevelById(@RequestBody @Valid CustomerLevelByIdRequest request);

    /**
     * 根据Id获取默认客户等级
     *
     * @param request Id参数 {@link CustomerLevelWithDefaultByIdRequest}
     * @return 会员等级 {@link CustomerLevelWithDefaultByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/get-customer-level-with-default-by-id")
    BaseResponse<CustomerLevelWithDefaultByIdResponse> getCustomerLevelWithDefaultById(@RequestBody @Valid
                                                                                               CustomerLevelWithDefaultByIdRequest
                                                                                               request);

    /**
     * 根据客户Id获取默认客户等级（仅单店铺下使用）
     *
     * @param request Id参数 {@link CustomerLevelByIdRequest}
     * @return 会员等级 {@link CustomerLevelWithDefaultByCustomerIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/get-customer-level-with-default-by-customer-id")
    BaseResponse<CustomerLevelWithDefaultByCustomerIdResponse> getCustomerLevelWithDefaultByCustomerId(@RequestBody
                                                                                                       @Valid
                                                                                                               CustomerLevelWithDefaultByCustomerIdRequest
                                                                                                               request);

    /**
     * 根据Ids查询客户等级列表
     *
     * @param request 等级Ids参数 {@link CustomerLevelByIdsRequest}
     * @return 会员等级列表 {@link CustomerLevelByIdsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/list-customer-level-by-ids")
    BaseResponse<CustomerLevelByIdsResponse> listCustomerLevelByIds(@RequestBody @Valid
                                                                            CustomerLevelByIdsRequest
                                                                            request);

    /**
     * 根据会员ID和店铺Ids查询各店铺与会员等级的Map结果
     *
     * @param request 会员Id和店铺Ids参数 {@link CustomerLevelMapByCustomerIdAndStoreIdsRequest}
     * @return 各店铺与会员等级一一映射的响应结果 {@link CustomerLevelMapGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/map-customer-level-by-customer-id-and-store-ids")
    BaseResponse<CustomerLevelMapGetResponse> listCustomerLevelMapByCustomerIdAndIds(@RequestBody @Valid
                                                                                             CustomerLevelMapByCustomerIdAndStoreIdsRequest
                                                                                             request);

    /**
     * 根据会员Id和店铺Id获取单个会员等级
     *
     * @param request 会员Id和店铺Id参数 {@link CustomerLevelByCustomerIdAndStoreIdRequest}
     * @return 会员等级 {@link CustomerLevelByCustomerIdAndStoreIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/get-customer-level-by-customer-id-and-store-id")
    BaseResponse<CustomerLevelByCustomerIdAndStoreIdResponse> getCustomerLevelByCustomerIdAndStoreId(@RequestBody @Valid
                                                                                                             CustomerLevelByCustomerIdAndStoreIdRequest
                                                                                                             request);

    /**
     * 查询默认客户等级
     *
     * @return 会员等级 {@link CustomerLevelDefaultResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/get-default-customer-level")
    BaseResponse<CustomerLevelDefaultResponse> getDefaultCustomerLevel();

    /**
     * 查询客户编号查询等级相关权益信息
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/level/get-level-rights")
    BaseResponse<CustomerLevelWithRightsByCustomerIdResponse> getCustomerLevelRightsInfos(@RequestBody @Valid CustomerLevelByCustomerIdRequest request);


    /**
     * 查询平台等级权益信息列表
     *
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/level/list-level-rights")
    BaseResponse<CustomerLevelWithRightsResponse> listCustomerLevelRightsInfo();

    /**
     * 查询会员ID集合平台等级信息列表
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/level/list-by-customer-ids")
    BaseResponse<CustomerLevelListByCustomerIdsResponse> listByCustomerIds(@RequestBody @Valid CustomerLevelListByCustomerIdsRequest request);

    /**
     * 根据营销会员等级查询会员等级名称集合
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/level/list-by-customer-level-name")
    BaseResponse<CustomerLevelListByCustomerLevelNameResponse> listByCustomerLevelName(@RequestBody @Valid CustomerLevelListByCustomerLevelNameRequest request);

    /**
     * 查询所有客户等级
     *
     * @return 会员等级 {@link CustomerLevelListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/list-all-customer-level-new")
    BaseResponse<CustomerLevelListBaseResponse> listAllCustomerLevelNew();
}
