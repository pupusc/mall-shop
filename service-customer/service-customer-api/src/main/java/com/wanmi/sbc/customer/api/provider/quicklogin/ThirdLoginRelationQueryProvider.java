package com.wanmi.sbc.customer.api.provider.quicklogin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByCustomerRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByUidRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoGetResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerIdsListResponse;
import com.wanmi.sbc.customer.api.response.quicklogin.ThirdLoginRelationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 第三方登录方式-第三方登录方式查询API
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "ThirdLoginRelationQueryProvider")
public interface ThirdLoginRelationQueryProvider {


    /**
     * 根据 用户Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/third/login/list-third-login-relation-by-customer-id-and-third-login-type")
    BaseResponse<ThirdLoginRelationResponse> listThirdLoginRelationByCustomer(@RequestBody @Valid
                                                                                      ThirdLoginRelationByCustomerRequest
                                                                                      request);

    /**
     * 根据 用户Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/third/login/list-third-login-relation-by-customer-id-and-third-login-type-and-del-flag")
    BaseResponse<ThirdLoginRelationResponse> thirdLoginRelationByCustomerAndDelFlag(@RequestBody @Valid
                                                                                                ThirdLoginRelationByCustomerRequest

                                                                                                request);

    /**
     * 根据 关联Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/third/login/list-third-login-relation-by-uid-and-third-login-type-and-store-id")
    BaseResponse<ThirdLoginRelationResponse> thirdLoginRelationByUidAndStoreId(@RequestBody @Valid
                                                                                 ThirdLoginRelationByUidRequest
                                                                                 request);

    /**
     * 根据 关联Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer" +
            ".version}/third/login/list-third-login-relation-by-uid-and-third-login-type-and-del-flag")
    BaseResponse<ThirdLoginRelationResponse> thirdLoginRelationByUidAndDeleteflagAndStoreId(@RequestBody @Valid
                                                                                              ThirdLoginRelationByUidRequest
                                                                                              request);

    /**
     * 根据 关联Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/third/login/list-third-login-by-customer-ids")
    public BaseResponse<CustomerIdsListResponse> listWithImgByCustomerIds(@RequestBody @Valid CustomerIdsListRequest
                                                                                  request);
}




