package com.wanmi.sbc.customer;

import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailPageRequest;
import com.wanmi.sbc.customer.api.request.customer.EnterpriseCustomerPageRequest;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailPageTwoRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业会员
 */
@Api(tags = "EnterpriseCustomerBaseController", description = "企业会员 Api")
@RestController
@RequestMapping("/enterpriseCustomer")
public class EnterpriseCustomerBaseController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private EsCustomerDetailQueryProvider esCustomerDetailQueryProvider;

    /**
     * 分页查询企业会员
     * @return 企业会员信息
     */
    @ApiOperation(value = "分页查询企业会员")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> pageForEnterpriseCustomer(@RequestBody EsCustomerDetailPageTwoRequest enterpriseCustomerPageRequest) {
        if(!commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        enterpriseCustomerPageRequest.setEnterpriseCustomer(Boolean.TRUE);
        enterpriseCustomerPageRequest.setFillArea(Boolean.FALSE);
        return ResponseEntity.ok(esCustomerDetailQueryProvider.pageForEnterpriseCustomer(enterpriseCustomerPageRequest));
    }
}
