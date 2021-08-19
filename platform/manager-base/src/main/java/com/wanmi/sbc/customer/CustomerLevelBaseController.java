package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelListBaseResponse;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "会员等级API", tags = "CustomerLevelController")
@RestController
@RequestMapping("/customer")
public class CustomerLevelBaseController {

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    /**
     * 查询boss所有会员等级
     *
     * @returnF
     */
    @ApiOperation(value = "查询boss所有会员等级")
    @RequestMapping(value = "/levellist", method = RequestMethod.GET)
    public BaseResponse<CustomerLevelListResponse> findAllLevel() {
        return customerLevelQueryProvider.listAllCustomerLevel();
    }

    /**
     * 查询boss所有会员等级
     *
     * @returnF
     */
    @ApiOperation(value = "查询boss所有会员等级")
    @RequestMapping(value = "/find-all-level", method = RequestMethod.GET)
    public BaseResponse<CustomerLevelListBaseResponse> findAllLevelNew() {
        return customerLevelQueryProvider.listAllCustomerLevelNew();
    }
}
