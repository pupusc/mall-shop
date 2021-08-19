package com.wanmi.sbc.customer.api.provider.quicklogin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationDeleteByCustomerRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 第三方登录方式-第三方登录方式添加/修改/删除API
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "ThirdLoginRelationProvider")
public interface ThirdLoginRelationProvider {


    /**
     * 修改第三方登录方式
     *
     * @param request 修改公司信息request{@link ThirdLoginRelationModifyRequest}
     * @return 修改第三方登录方式结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/third/login/modify-third-login-relation")
    BaseResponse modifyThirdLoginRelation(@RequestBody @Valid ThirdLoginRelationModifyRequest request);

    /**
     * 修改公司工商信息
     *
     * @param request 修改公司信息request{@link ThirdLoginRelationDeleteByCustomerRequest}
     * @return 修改公司工商信息结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/third/login/delete-third-login-relation-by-customer-id-and-third-login-type")
    BaseResponse deleteThirdLoginRelationByCustomerIdAndStoreId(@RequestBody @Valid ThirdLoginRelationDeleteByCustomerRequest
                                                              request);
}
