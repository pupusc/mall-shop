package com.wanmi.sbc.account.api.provider.offline;

import com.wanmi.sbc.account.api.request.offline.*;
import com.wanmi.sbc.account.api.response.offline.*;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 *  <p>线下账户查询接口</p>
 *  Created by wanggang on 2018-10-12-上午10:30.
 */
@FeignClient(value = "${application.account.name}", contextId = "OfflineQueryProvider")
public interface OfflineQueryProvider {

    /**
     * 查询线下账户
     *
     * @param request {@link OfflineAccountGetByIdRequest}
     * @return 线下账户信息 {@link OfflineAccountGetByIdResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/get-by-id")
    BaseResponse<OfflineAccountGetByIdResponse> getById(@RequestBody @Valid OfflineAccountGetByIdRequest request);

    /**
     * 账号管理查询
     *
     * @return 线下账户列表 {@link ManagerAccountListResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/list-manager-account")
    BaseResponse<ManagerAccountListResponse> listManagerAccount();

    /**
     * 根据公司id和是否收到平台首次打款查询线下账户列表
     *
     * @param request {@link OfflineAccountListByConditionRequest}
     * @return 线下账户号列表 {@link OfflineAccountListByConditionResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/list-by-condition")
    BaseResponse<OfflineAccountListByConditionResponse> listByCondition(@RequestBody @Valid OfflineAccountListByConditionRequest request);

    /**
     * 查询所有线下账户
     *
     * @return 线下账户列表 {@link OfflineAccountListResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/list")
    BaseResponse<OfflineAccountListResponse> list();

    /**
     * 查询所有有效线下账户
     *
     * @return 线下账户列表 {@link ValidAccountListResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/list-valid-account")
    BaseResponse<ValidAccountListResponse> listValidAccount();

    /**
     * 统计商家财务信息
     *
     * @param request {@link OfflineCountRequest}
     * @return 线下账户列表 {@link OfflineCountResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/count-offline")
    BaseResponse<OfflineCountResponse> countOffline(@RequestBody @Valid OfflineCountRequest request);


    /**
     * 根据银行账号查询所有账户（不包含删除的）
     *
     * @param request {@link OfflineAccountListByBankNoRequest}
     * @return 线下账户列表 {@link OfflineAccountListByBankNoResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/list-by-bank-no")
    BaseResponse<OfflineAccountListByBankNoResponse> listByBankNo(@RequestBody @Valid OfflineAccountListByBankNoRequest request);


    /**
     * 根据银行账号查询所有账户(包含删除的)
     *
     * @param request {@link OfflineAccountListWithoutDeleteFlagByBankNoRequest}
     * @return 线下账户列表 {@link OfflineAccountListWithoutDeleteFlagByBankNoResponse}
     */
    @PostMapping("/account/${application.account.version}/offline/list-without-delete-flag-by-bank-no")
    BaseResponse<OfflineAccountListWithoutDeleteFlagByBankNoResponse> listWithOutDeleteFlagByBankNo(@RequestBody @Valid OfflineAccountListWithoutDeleteFlagByBankNoRequest request);
}
