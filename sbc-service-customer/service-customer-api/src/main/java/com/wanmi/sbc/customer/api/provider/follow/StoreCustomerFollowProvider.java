package com.wanmi.sbc.customer.api.provider.follow;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowAddRequest;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowDeleteRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 店铺收藏-店铺收藏添加/修改/删除API
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreCustomerFollowProvider")
public interface StoreCustomerFollowProvider {


    /**
     * 新增店铺收藏
     *
     * @param request 新增店铺收藏request {@link StoreCustomerFollowAddRequest}
     * @return 新增店铺收藏结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/follow/add-store-customer-follow")
    BaseResponse addStoreCustomerFollow(@RequestBody @Valid StoreCustomerFollowAddRequest request);

    /**
     * 删除店铺收藏
     *
     * @param request 删除店铺收藏request {@link StoreCustomerFollowDeleteRequest}
     * @return 删除店铺收藏结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/follow/delete-store-customer-follow")
    BaseResponse deleteStoreCustomerFollow(@RequestBody @Valid StoreCustomerFollowDeleteRequest request);
}
