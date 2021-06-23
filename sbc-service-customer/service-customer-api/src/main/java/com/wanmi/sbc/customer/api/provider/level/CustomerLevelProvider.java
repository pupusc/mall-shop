package com.wanmi.sbc.customer.api.provider.level;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelAddRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelDeleteByIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelModifyRequest;
import com.wanmi.sbc.customer.api.request.level.StoreCustomerLevelInitRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员等级-会员等级添加/修改/删除API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerLevelProvider")
public interface CustomerLevelProvider {

    /**
     * 新增会员等级
     *
     * @param request 会员等级 {@link CustomerLevelAddRequest}
     * @return 新增会员等级结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/add-customer-level")
    BaseResponse addCustomerLevel(@RequestBody @Valid CustomerLevelAddRequest request);

    /**
     * 修改会员等级
     *
     * @param request 会员等级{@link CustomerLevelModifyRequest}
     * @return 修改会员等级结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/modify-customer-level")
    BaseResponse modifyCustomerLevel(@RequestBody @Valid CustomerLevelModifyRequest request);

    /**
     * 删除会员等级
     *
     * @param request 会员等级IdRequest{@link CustomerLevelDeleteByIdRequest}
     * @return 删除会员等级结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/level/delete-customer-level")
    BaseResponse deleteCustomerLevel(@RequestBody @Valid CustomerLevelDeleteByIdRequest request);

}
