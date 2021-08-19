package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelWithRightsByCustomerIdResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户等级控制器
 *
 * @author yang
 * @since 2019/3/5
 */
@RestController
@RequestMapping("/customer")
@Validated
@Api(tags = "CustomerLevelBaseController", description = "S2B web公用-客户等级API")
public class CustomerLevelBaseController {

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "根据客户ID查询客户等级权益相关信息")
    @RequestMapping(value = "/level/rights", method = RequestMethod.GET)
    public BaseResponse<CustomerLevelWithRightsByCustomerIdResponse> getCustomerLevelRightsInfos() {
        return BaseResponse.success(customerLevelQueryProvider.getCustomerLevelRightsInfos(CustomerLevelByCustomerIdRequest.builder()
                .customerId(commonUtil.getOperatorId()).build()).getContext());
    }
}
