package com.wanmi.sbc.customer.api.provider.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaAddRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaDeleteRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaUpdateRequest;
import com.wanmi.sbc.customer.api.response.store.StoreCustomerRelaResponse;
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
@FeignClient(value = "${application.customer.name}", contextId = "StoreCustomerProvider")
public interface StoreCustomerProvider {

    /**
     * 修改平台客户，只能修改等级
     *
     * @param storeCustomerRelaUpdateRequest {@link StoreCustomerRelaUpdateRequest}
     * @return 平台客户信息 {@link StoreCustomerRelaResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/modify-by-customer-id")
    BaseResponse<StoreCustomerRelaResponse> modifyByCustomerId(@RequestBody @Valid StoreCustomerRelaUpdateRequest
                                                                       storeCustomerRelaUpdateRequest);

    /**
     * 添加平台客户
     *
     * @param storeCustomerRelaAddRequest {@link StoreCustomerRelaAddRequest}
     * @return 平台客户信息 {@link StoreCustomerRelaResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/add-platform-related")
    BaseResponse<StoreCustomerRelaResponse> addPlatformRelated(@RequestBody @Valid StoreCustomerRelaAddRequest
                                                                       storeCustomerRelaAddRequest);

    /**
     * 删除平台客户关系
     *
     * @param storeCustomerRelaDeleteRequest {@link StoreCustomerRelaDeleteRequest}
     * @return 删除平台客户关系结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/delete-platform-related")
    BaseResponse deletePlatformRelated(@RequestBody @Valid StoreCustomerRelaDeleteRequest storeCustomerRelaDeleteRequest);

    /**
     * 修改平台客户关系
     *
     * @param storeCustomerRelaUpdateRequest {@link StoreCustomerRelaDeleteRequest}
     * @return 修改店铺客户关系结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/update-store-customer-rela")
    BaseResponse updateStoreCustomerRela(@RequestBody @Valid StoreCustomerRelaUpdateRequest storeCustomerRelaUpdateRequest);
}