package com.wanmi.sbc.account.provider.impl.funds;

import com.wanmi.sbc.account.api.provider.funds.CustomerFundsDetailProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailAddRequest;
import com.wanmi.sbc.account.funds.service.CustomerFundsDetailService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>会员资金明细</p>
 *
 * @author yang
 * @since 2019/2/27
 */
@RestController
@Validated
public class CustomerFundsDetailController implements CustomerFundsDetailProvider {

    @Autowired
    private CustomerFundsDetailService customerFundsDetailService;

    @Override
    public BaseResponse batchAdd(@RequestBody List<CustomerFundsDetailAddRequest> customerFundsDetailAddRequestList) {
        customerFundsDetailAddRequestList.forEach(
                customerFundsDetailAddRequest -> customerFundsDetailService.add(customerFundsDetailAddRequest));
        return BaseResponse.SUCCESSFUL();
    }
}
