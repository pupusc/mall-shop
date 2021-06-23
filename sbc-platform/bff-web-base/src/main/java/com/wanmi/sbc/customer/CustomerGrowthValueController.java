package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueQueryProvider;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValuePageRequest;
import com.wanmi.sbc.setting.api.provider.SystemGrowthValueConfigQueryProvider;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员成长值
 */
@Api(description = "PC、H5会员API", tags = "BossCustomerController")
@RestController
@RequestMapping(value = "/customer")
public class CustomerGrowthValueController {

    @Autowired
    private CustomerGrowthValueQueryProvider customerGrowthValueQueryProvider;

    @Autowired
    private SystemGrowthValueConfigQueryProvider systemGrowthValueConfigQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询会员成长值")
    @RequestMapping(value = "/queryToGrowthValue", method = RequestMethod.POST)
    public BaseResponse queryGrowthValue(@RequestBody CustomerGrowthValuePageRequest customerGrowthValuePageRequest) {
        customerGrowthValuePageRequest.setCustomerId(commonUtil.getOperatorId());
        customerGrowthValuePageRequest.setExcludeZeroFlag(DefaultFlag.YES);
        return BaseResponse.success(customerGrowthValueQueryProvider.page(customerGrowthValuePageRequest).getContext()
                .getCustomerGrowthValueVOPage());
    }

    /**
     * 查询成长值说明
     * @return
     */
    @ApiOperation(value = "查询成长值说明")
    @RequestMapping(value = "/growthValue/introduction", method = RequestMethod.GET)
    public BaseResponse queryGrowthValueIntroduction() {
        return systemGrowthValueConfigQueryProvider.querySystemGrowthValueConfig();
    }

}
