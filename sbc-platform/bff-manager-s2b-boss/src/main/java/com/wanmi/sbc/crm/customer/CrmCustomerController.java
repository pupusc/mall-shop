package com.wanmi.sbc.crm.customer;

import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.customer.response.CustomerGetForCrmResponse;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByIdRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeOptionalByIdRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelWithDefaultByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByIdResponse;
import com.wanmi.sbc.customer.api.response.employee.EmployeeOptionalByIdResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodePageRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodePageResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 会员
 * Created by hht on 2017/4/19.
 */
@Api(description = "CRM会员API", tags = "CrmCustomerController")
@RestController
@RequestMapping(value = "/customer")
public class CrmCustomerController {

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private CouponCodeQueryProvider couponCodeQueryProvider;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 查询单条会员信息
     *
     * @param customerId
     * @return
     */
    @ApiOperation(value = "查询会员详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员ID", required = true)
    @RequestMapping(value = "/crm/{customerId}", method = RequestMethod.GET)
    public BaseResponse<CustomerGetForCrmResponse> findById(@PathVariable String customerId) {
        CustomerGetForCrmResponse response = new CustomerGetForCrmResponse();
        CustomerGetByIdResponse customer =
                customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        KsBeanUtil.copyPropertiesThird(customer, response);
        KsBeanUtil.copyPropertiesThird(customer.getCustomerDetail(), response);
        if(Objects.nonNull(customer.getCustomerLevelId())) {
            CustomerLevelVO customerLevel = customerLevelQueryProvider.getCustomerLevelWithDefaultById(
                    CustomerLevelWithDefaultByIdRequest.builder().customerLevelId(customer.getCustomerLevelId()).build())
                    .getContext();
            response.setCustomerLevelName(customerLevel.getCustomerLevelName());
        }
        CustomerFundsByCustomerIdRequest fundRequest = new CustomerFundsByCustomerIdRequest();
        fundRequest.setCustomerId(customerId);
        CustomerFundsByCustomerIdResponse fundsResponse = customerFundsQueryProvider.getByCustomerId(fundRequest).getContext();
        if(Objects.nonNull(fundsResponse)){
            response.setAccountBalance(fundsResponse.getAccountBalance());
        }
        DistributionCustomerVO distributionVO = distributionCustomerQueryProvider.getByCustomerIdAndDistributorFlagAndDelFlag(
                DistributionCustomerByCustomerIdRequest.builder().customerId(customerId).build()).getContext().getDistributionCustomerVO();
        if(Objects.nonNull(distributionVO)){
            response.setDistributorFlag(distributionVO.getDistributorFlag());
            response.setInviteCount(distributionVO.getInviteCount());
            response.setInviteAvailableCount(distributionVO.getInviteAvailableCount());
            response.setRewardCash(distributionVO.getRewardCash());
            response.setRewardCashNotRecorded(distributionVO.getRewardCashNotRecorded());
            response.setDistributorLevelName(distributionVO.getDistributorLevelName());
        }

        if(Objects.nonNull(response.getEmployeeId())){
            EmployeeOptionalByIdResponse employee = employeeQueryProvider.getOptionalById(EmployeeOptionalByIdRequest.builder()
                    .employeeId(response.getEmployeeId()).build()).getContext();
            if(Objects.nonNull(employee)){
                response.setEmployeeName(employee.getEmployeeName());
                response.setEmployeeMobile(employee.getEmployeeMobile());
            }
        }

        if(!Objects.equals(EnterpriseCheckState.INIT,customer.getEnterpriseCheckState())){
            BaseResponse<EnterpriseInfoByCustomerIdResponse> enterpriseInfo = enterpriseInfoQueryProvider.getByCustomerId(EnterpriseInfoByCustomerIdRequest.builder()
                    .customerId(customerId)
                    .build());
            if(Objects.nonNull(enterpriseInfo.getContext())){
                response.setEnterpriseInfo(enterpriseInfo.getContext().getEnterpriseInfoVO());
            }
            response.setEnterpriseCustomerName(commonUtil.getIepSettingInfo().getEnterpriseCustomerName());
        }
        return BaseResponse.success(response);
    }

    /**
     * 客户优惠券列表
     * @param request
     * @return
     */
    @ApiOperation(value = "客户优惠券列表")
    @RequestMapping(value = "/coupons", method = RequestMethod.POST)
    public BaseResponse<CouponCodePageResponse> listMyCouponList(@RequestBody CouponCodePageRequest request){
        return couponCodeQueryProvider.page(request);
    }
}
