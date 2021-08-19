package com.wanmi.sbc.elastic.api.provider.customer;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.customer.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员详情
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsCustomerDetailProvider")
public interface EsCustomerDetailProvider {

    /**
     * 初始化会员ES
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/init")
    BaseResponse init(@RequestBody @Valid EsCustomerDetailInitRequest request);

    /**
     * 初始化会员ES
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/delete-customer")
    BaseResponse deleteCustomer(@RequestBody @Valid EsCustomerDetailInitRequest request);

    /**
     * 初始化会员ES
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/paid-members/init")
    BaseResponse initPaidMembers(@RequestBody @Valid EsCustomerDetailInitRequest request);

    /**
     * 保存
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/add")
    BaseResponse add(@RequestBody @Valid EsCustomerDetailAddRequest request);

    /**
     * boss修改会员信息
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/modify")
    BaseResponse modify(@RequestBody @Valid EsCustomerDetailModifyRequest request);

    /**
     * 修改账号状态
     *
     * @param request 批量更新账户状态和禁用原因request{@link EsCustomerStateBatchModifyRequest }
     * @return 修改会员状态结果{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/modify-customer-state-by-customer-ids")
    BaseResponse modifyCustomerStateByCustomerId(@RequestBody @Valid EsCustomerStateBatchModifyRequest request);


    /**
     * 审核客户状态
     *
     * @param request {@link EsCustomerCheckStateModifyRequest}
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/modify-customer-check-state")
    BaseResponse modifyCustomerCheckState(@RequestBody @Valid EsCustomerCheckStateModifyRequest request);


    /**
     * 添加平台客户
     *
     * @param request {@link EsStoreCustomerRelaAddRequest}
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/add-platform-related")
    BaseResponse addPlatformRelated(@RequestBody @Valid EsStoreCustomerRelaAddRequest request);

    /**
     * 删除平台客户关系
     *
     * @param request {@link EsStoreCustomerRelaDeleteRequest}
     * @return 删除平台客户关系结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/delete-platform-related")
    BaseResponse deletePlatformRelated(@RequestBody @Valid EsStoreCustomerRelaDeleteRequest request);

    /**
     * 修改平台客户，只能修改等级
     *
     * @param request {@link EsStoreCustomerRelaUpdateRequest}
     * @return
     */
    @PostMapping("/customer/${application.elastic.version}/customer-detail/modify-by-customer-id")
    BaseResponse modifyByCustomerId(@RequestBody @Valid EsStoreCustomerRelaUpdateRequest request);

    @PostMapping("/elastic/${application.elastic.version}/customer-detail/resetEs")
    BaseResponse resetEs(@RequestBody BaseQueryRequest req);
}
