package com.wanmi.ares.provider;

import com.wanmi.ares.base.BaseResponse;
import com.wanmi.ares.base.BaseServicePage;
import com.wanmi.ares.request.CustomerQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-6
 * \* Time: 11:09
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@FeignClient(value = "${application.ares.name}",contextId = "CustomerBaseQueryProvider")
public interface CustomerBaseQueryProvider {

    @PostMapping("/ares/${application.ares.version}/customerQuery/queryCustomerPhone")
    BaseResponse<List<String>> queryCustomerPhone(@RequestBody CustomerQueryRequest request);

    @PostMapping("/ares/${application.ares.version}/customerQuery/queryCustomerPhoneCount")
    long queryCustomerPhoneCount(@RequestBody CustomerQueryRequest request);
}
