package com.wanmi.sbc.account.api.provider.funds;

import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailAddRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsDetailPageResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 会员资金明细查询接口
 *
 * @author: Geek Wang
 * @createDate: 2019/2/19 14:33
 * @version: 1.0
 */
@FeignClient(value = "${application.account.name}", contextId = "CustomerFundsDetailProvider")
public interface CustomerFundsDetailProvider {

    /**
     * 新增会员资金明细
     *
     * @return {@link BaseResponse<CustomerFundsDetailPageResponse>}
     */
    @PostMapping("/account/${application.account.version}/funds/detail/batchAdd")
    BaseResponse batchAdd(@RequestBody List<CustomerFundsDetailAddRequest> customerFundsDetailAddRequestList);
}
