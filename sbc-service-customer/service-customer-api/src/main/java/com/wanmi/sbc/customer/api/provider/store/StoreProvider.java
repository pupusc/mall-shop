package com.wanmi.sbc.customer.api.provider.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.store.*;
import com.wanmi.sbc.customer.api.response.store.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺操作Provider</p>
 * Created by of628-wenzhi on 2018-09-12-下午6:23.
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreProvider")
public interface StoreProvider {

    /**
     * B2B模式下根据商家id返回店铺信息，如果不存在则初始化
     *
     * @param initStoreByCompanyRequest 包含商家id {@link InitStoreByCompanyRequest}
     * @return 店铺信息 {@link InitStoreByCompanyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/init-store-by-company")
    BaseResponse<InitStoreByCompanyResponse> initStoreByCompany(@RequestBody @Valid InitStoreByCompanyRequest
                                                                        initStoreByCompanyRequest);

    /**
     * 添加店铺
     *
     * @param storeAddRequest 店铺添加参数结构 {@link StoreAddRequest}
     * @return 已添加的店铺信息 {@link StoreAddResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/add")
    BaseResponse<StoreAddResponse> add(@RequestBody @Valid StoreAddRequest storeAddRequest);

    /**
     * 修改店铺logo与店招信息
     *
     * @param storeModifyLogoRequest 店铺修改参数结构 {@link StoreModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/modify-store-base-info")
    BaseResponse modifyStoreBaseInfo(@RequestBody @Valid StoreModifyLogoRequest storeModifyLogoRequest);

    /**
     * 修改店铺信息
     *
     * @param storeModifyRequest 店铺修改参数结构 {@link StoreModifyRequest}
     * @return {@link StoreModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/modify-store-info")
    BaseResponse<StoreModifyResponse> modifyStoreInfo(@RequestBody @Valid StoreModifyRequest storeModifyRequest);

    /**
     * 店铺结算日期修改
     *
     * @param request 包含店铺id和计算日期字符串的参数结构 {@link AccountDateModifyRequest}
     * @return 结算日期修改后返回店铺信息 {@link AccountDateModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/account-date-modify")
    BaseResponse<AccountDateModifyResponse> accountDateModify(@RequestBody @Valid AccountDateModifyRequest request);

    /**
     * 开关店
     *
     * @param storeSwitchRequest 店铺开关选项参数 {@link StoreSwitchRequest}
     * @return 开关店操作后返回店铺信息 {@link StoreSwitchResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/switch-store")
    BaseResponse<StoreSwitchResponse> switchStore(@RequestBody @Valid StoreSwitchRequest storeSwitchRequest);

    /**
     * 店铺审核
     *
     * @param storeAuditRequest 审核请求参数 {@link StoreAuditRequest}
     * @return 店铺审核操作后返回店铺信息 {@link StoreAuditResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/audit-store")
    BaseResponse<StoreAuditResponse> auditStore(@RequestBody @Valid StoreAuditRequest storeAuditRequest);

    /**
     * 店铺校验
     *
     * @param storeCheckRequest 包含需要校验店铺id集合的请求参数 {@link StoreCheckRequest}
     * @return 校验结果 {@link StoreCheckResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/check-store")
    BaseResponse<StoreCheckResponse> checkStore(@RequestBody @Valid StoreCheckRequest storeCheckRequest);

    /**
     * 修改店铺签约日期和商家类型等基础信息
     *
     * @param storeContractModifyRequest 店铺修改信息 {@link StoreContractModifyRequest}
     * @return 修改后的店铺信息 {@link StoreContractModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/modify-store-contract")
    BaseResponse<StoreContractModifyResponse> modifyStoreContract(@RequestBody @Valid StoreContractModifyRequest
                                                                          storeContractModifyRequest);


    /**
     * 修改店铺审核状态
     *
     * @param request  {@link StoreAuditStateModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/modify-audit-state")
    BaseResponse modifyAuditState(@RequestBody @Valid StoreAuditStateModifyRequest request);


    @PostMapping("/customer/${application.customer.version}/store/update-miniProgram-qrcode")
    BaseResponse updateStoreSmallProgram(@RequestBody @Valid StoreInfoSmallProgramRequest request);

    @PostMapping("/customer/${application.customer.version}/store/clear-miniProgram-qrcode")
    BaseResponse clearStoreProgramCode();



    @PostMapping("/customer/${application.customer.version}/store/query-esStore-info-by-page")
    BaseResponse<EsStoreInfoResponse> queryEsStoreInfoByPage(@RequestBody @Valid EsStoreInfoQueryRequest request);


    /**
     * 批量开关店
     *
     * @param storeSwitchBatchRequest 店铺开关选项参数 {@link StoreSwitchBatchRequest}
     * @return 开关店操作后返回店铺信息 {@link StoreSwitchResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/switch-store-batch")
    BaseResponse switchStoreBatch(@RequestBody @Valid StoreSwitchBatchRequest storeSwitchBatchRequest);

}
