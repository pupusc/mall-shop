package com.wanmi.sbc.enterpriseInfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoModifyForWebRequest;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoModifyResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 更新企业信息
 * @author tanglian
 */
@RestController
@RequestMapping("/enterpriseInfo")
@Validated
@Api(tags = "EnterpriseInfoBaseController", description = "S2B web公用-企业信息API")
public class EnterpriseInfoBaseController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EnterpriseInfoProvider enterpriseInfoProvider;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;


    @ApiOperation(value = "查询企业信息")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<EnterpriseInfoByCustomerIdResponse> getEnterpriseInfo() {
        return enterpriseInfoQueryProvider.getByCustomerId(EnterpriseInfoByCustomerIdRequest.builder()
                .customerId(commonUtil.getCustomer().getCustomerId())
                .build());
    }

    @ApiOperation(value = "更新企业信息")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse<EnterpriseInfoModifyResponse> updateEnterpriseInfo(@Valid @RequestBody EnterpriseInfoModifyForWebRequest request) {
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        return enterpriseInfoProvider.modifyForWeb(request);
    }


}
