package com.wanmi.sbc.account.api.provider.offline;

import com.wanmi.sbc.account.api.request.offline.*;
import com.wanmi.sbc.account.api.response.offline.*;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 *  <p>线下账户操作接口</p>
 *  Created by wanggang on 2018-10-12-上午10:30.
 */
@FeignClient(value = "${application.account.name}", contextId = "OfflineProvider")
public interface OfflineProvider {

    /**
     * 新增线下账户
     *
     * @param request {@link OfflineAccountAddRequest}
     * @return 线下账户信息 {@link OfflineAccountAddResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/add")
    BaseResponse<OfflineAccountAddResponse> add(@RequestBody @Valid OfflineAccountAddRequest request);

    /**
     * 修改线下账户
     *
     * @param request {@link OfflineAccountModifyRequest}
     * @return 线下账户信息 {@link OfflineAccountModifyResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/modify")
    BaseResponse<OfflineAccountModifyResponse> modify(@RequestBody @Valid OfflineAccountModifyRequest request);

    /**
     * 删除线下账户
     *
     * @param request {@link OfflineAccountDeleteByIdRequest}
     * @return 删除账户数 {@link OfflineAccountDeleteByIdResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/delete-by-id")
    BaseResponse<OfflineAccountDeleteByIdResponse> deleteById(@RequestBody @Valid OfflineAccountDeleteByIdRequest request);

    /**
     * 禁用银行账号
     *
     * @param request {@link OfflineDisableByIdRequest}
     * @return 禁用账户数 {@link OfflineDisableByIdResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/disable-by-id")
    BaseResponse<OfflineDisableByIdResponse> disableById(@RequestBody @Valid OfflineDisableByIdRequest request);

    /**
     * 启用银行账号
     *
     * @param request {@link OfflineEnableByIdRequest}
     * @return 启用账户数 {@link OfflineEnableByIdResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/enable-by-id")
    BaseResponse<OfflineEnableByIdResponse> enableById(@RequestBody @Valid OfflineEnableByIdRequest request);

    /**
     * 更新商家财务信息(新增、修改、删除)
     *
     * @param request {@link OfflinesRenewalRequest}
     * @return 结果 {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/renewal-offlines")
    BaseResponse renewalOfflines(@RequestBody @Valid OfflinesRenewalRequest request);
}
