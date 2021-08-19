package com.wanmi.sbc.customer.api.provider.follow;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.follow.*;
import com.wanmi.sbc.customer.api.response.follow.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 店铺收藏-店铺收藏查询API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreCustomerFollowQueryProvider")
public interface StoreCustomerFollowQueryProvider {


    /**
     * 店铺收藏分页列表
     *
     * @param request 条件查询参数 {@link StoreCustomerFollowPageRequest}
     * @return 店铺收藏分页列表 {@link StoreCustomerFollowPageResponse}
     */
    @PostMapping("/customer/${application.customer.version}/follow/page-store-customer-follow")
    BaseResponse<StoreCustomerFollowPageResponse> pageStoreCustomerFollow(@RequestBody @Valid
                                                                                  StoreCustomerFollowPageRequest
                                                                                  request);

    /**
     * 查询已关注店铺的ID
     *
     * @param request 店铺的ids以及客户id参数 {@link StoreCustomerFollowExistsBatchRequest}
     * @return 已关注店铺的ids {@link StoreCustomerFollowExistsBatchResponse}
     */
    @PostMapping("/customer/${application.customer.version}/follow/query-store-customer-follow-by-store-ids")
    BaseResponse<StoreCustomerFollowExistsBatchResponse> queryStoreCustomerFollowByStoreIds(@RequestBody @Valid
                                                                                                    StoreCustomerFollowExistsBatchRequest request);

    /**
     * 查询已关注店铺的ID
     *
     * @param request 店铺的id以及客户id参数 {@link StoreCustomerFollowExistsRequest}
     * @return 是否关注 {@link StoreCustomerFollowExistsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/follow/query-store-customer-follow-by-store-id")
    BaseResponse<StoreCustomerFollowExistsResponse> queryStoreCustomerFollowByStoreId(@RequestBody @Valid
                                                                                              StoreCustomerFollowExistsRequest
                                                                                              request);

    /**
     * 根据客户ID统计店铺关注数量
     *
     * @param request 客户id参数 {@link StoreCustomerFollowCountRequest}
     * @return 返回待审核统计 {@link StoreCustomerFollowCountResponse}
     */
    @PostMapping("/customer/${application.customer.version}/follow/query-store-customer-follow-count-by-customer-id")
    BaseResponse<StoreCustomerFollowCountResponse> queryStoreCustomerFollowCountByCustomerId(@RequestBody @Valid
                                                                                                     StoreCustomerFollowCountRequest
                                                                                                     request);

    /*
     * @Description: 查询店铺关注总数
     * @param:  StoreFollowBystoreIdRequest 店铺ID
     * @Author: Bob
     * @Date: 2019-04-03 10:07
     */
    @PostMapping("/customer/${application.customer.version}/follow/query-store-customer-follow-count-by-store-id")
    BaseResponse<StoreFollowCountBystoreIdResponse> queryStoreCustomerFollowCountByStoreId(@RequestBody @Valid
                                                                                                   StoreFollowBystoreIdRequest
                                                                                                   request);

}
