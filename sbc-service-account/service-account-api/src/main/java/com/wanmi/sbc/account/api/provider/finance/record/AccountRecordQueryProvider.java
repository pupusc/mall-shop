package com.wanmi.sbc.account.api.provider.finance.record;

import com.wanmi.sbc.account.api.request.finance.record.AccountDetailsExportRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountDetailsPageRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountGatherListRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordPageRequest;
import com.wanmi.sbc.account.api.response.finance.record.AccountDetailsExportResponse;
import com.wanmi.sbc.account.api.response.finance.record.AccountDetailsPageResponse;
import com.wanmi.sbc.account.api.response.finance.record.AccountGatherListResponse;
import com.wanmi.sbc.account.api.response.finance.record.AccountRecordPageResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对账记录查询接口</p>
 * Created by of628-wenzhi on 2018-10-12-下午5:51.
 */
@FeignClient(value = "${application.account.name}", contextId = "AccountRecordQueryProvider")
public interface AccountRecordQueryProvider {

    /**
     * 分页查询对账记录
     *
     * @param request 查询条件 {@link AccountRecordPageRequest}
     * @return 带分页的对账记录列表 {@link AccountRecordPageResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/page-account-record")
    BaseResponse<AccountRecordPageResponse> pageAccountRecord(@RequestBody @Valid AccountRecordPageRequest
                                                                      request);

    /**
     * 对账记录汇总
     *
     * @param request 汇总条件 {@link AccountGatherListRequest}
     * @return 对账汇总列表 {@link AccountGatherListResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/list-account-gather")
    BaseResponse<AccountGatherListResponse> listAccountGather(@RequestBody @Valid AccountGatherListRequest
                                                                      request);

    /**
     * 分页查询对账明细
     *
     * @param request 带分页的对账明细查询参数 {@link AccountDetailsPageRequest}
     * @return 对账明细分页记录 {@link AccountDetailsPageResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/page-account-details")
    BaseResponse<AccountDetailsPageResponse> pageAccountDetails(@RequestBody @Valid AccountDetailsPageRequest
                                                                        request);

    /**
     * 对账明细(收入/退款)导出数据查询
     * @param request 账户明细导出数据查询请求参数 {@link AccountDetailsExportRequest}
     * @return （收入/退款）明细导出查询返回数据结构 {@link AccountDetailsExportResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/export-account-details-load")
    BaseResponse<AccountDetailsExportResponse> exportAccountDetailsLoad(@RequestBody @Valid AccountDetailsExportRequest
                                                                                request);
}
