package com.wanmi.ares.provider.impl;

import com.github.pagehelper.PageInfo;
import com.wanmi.ares.base.BaseResponse;
import com.wanmi.ares.base.BaseServicePage;
import com.wanmi.ares.provider.CustomerBaseQueryProvider;
import com.wanmi.ares.report.customer.service.CustomerQueryService;
import com.wanmi.ares.request.CustomerQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-6
 * \* Time: 11:13
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@RestController
public class CustomerBaseQueryProviderController implements CustomerBaseQueryProvider {
    @Autowired
    private CustomerQueryService customerQueryService;

    public BaseResponse<List<String>> queryCustomerPhone(@RequestBody CustomerQueryRequest request){
        PageInfo<String> pageInfo = this.customerQueryService.queryPhoneAccount(request);

        return BaseResponse.success(pageInfo.getList());
    }

    public long queryCustomerPhoneCount(@RequestBody CustomerQueryRequest request){

        return this.customerQueryService.queryPhoneCount(request);
    }
}
