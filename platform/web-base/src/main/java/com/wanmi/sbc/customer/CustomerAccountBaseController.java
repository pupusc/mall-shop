package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountProvider;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountQueryProvider;
import com.wanmi.sbc.customer.api.request.account.*;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountByCustomerAccountResponse;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountListResponse;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountOptionalResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerAccountVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 客户银行账号
 * Created by CHENLI on 2017/4/21.
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "CustomerAccountBaseController", description = "S2B web公用-会员银行账户信息API")
public class CustomerAccountBaseController {

    @Autowired
    private CustomerAccountProvider customerAccountProvider;

    @Autowired
    private CustomerAccountQueryProvider customerAccountQueryProvider;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * 根据会员银行账号ID查询银行账户信息
     *
     * @param customerAccountId
     * @return ResponseEntity<CustomerAccount>
     */
    @ApiOperation(value = "根据会员银行账号ID查询银行账户信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerAccountId", value = "会员银行账户Id", required = true)
    @RequestMapping(value = "/account/{customerAccountId}", method = RequestMethod.GET)
    public BaseResponse<CustomerAccountOptionalResponse> findById(@PathVariable("customerAccountId") String customerAccountId) {
        CustomerAccountOptionalRequest request = new CustomerAccountOptionalRequest();
        request.setCustomerAccountId(customerAccountId);
        BaseResponse<CustomerAccountOptionalResponse> baseResponse = customerAccountQueryProvider.getByCustomerAccountIdAndDelFlag(request);
        CustomerAccountOptionalResponse customerAccountOptionalResponse = baseResponse.getContext();
        if (Objects.nonNull(customerAccountOptionalResponse)) {
            return BaseResponse.success(customerAccountOptionalResponse);
        } else {
            return BaseResponse.error("该银行账户信息不存在");
        }
    }

    /**
     * 根据会员ID查询银行账户信息
     *
     * @return
     */
    @ApiOperation(value = "根据会员ID查询银行账户信息")
    @RequestMapping(value = "/accountList", method = RequestMethod.GET)
    public BaseResponse<List<CustomerAccountVO>> findAll() {
        CustomerAccountListRequest customerAccountListRequest = new CustomerAccountListRequest();
        customerAccountListRequest.setCustomerId(commonUtil.getOperatorId());
        BaseResponse<CustomerAccountListResponse>  customerAccountListResponseBaseResponse = customerAccountQueryProvider.listByCustomerId(customerAccountListRequest);
        CustomerAccountListResponse customerAccountListResponse =  customerAccountListResponseBaseResponse.getContext();
        if (Objects.nonNull(customerAccountListResponse) && CollectionUtils.isNotEmpty(customerAccountListResponse.getCustomerAccountVOList())){
            return BaseResponse.success(customerAccountListResponse.getCustomerAccountVOList());
        }
        return BaseResponse.success(Collections.emptyList());
    }


    /**
     * 保存会员的银行账户信息
     *
     * @param accountSaveRequest
     * @return
     */
    @ApiOperation(value = "保存会员的银行账户信息")
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public BaseResponse addCustomerAccount(@Valid @RequestBody CustomerAccountAddRequest accountSaveRequest) {

        //TODO 银行卡号
        if(StringUtils.isNotEmpty(accountSaveRequest.getCustomerAccountNo())){
            if(!Pattern.matches(commonUtil.REGEX_BANK_CARD,accountSaveRequest.getCustomerAccountNo())){
                return BaseResponse.error("账号格式不正确");
            }
        }else{
            return BaseResponse.error("请输入正确的账号格式");
        }

        CustomerAccountByCustomerIdRequest byCustomerIdRequest = new CustomerAccountByCustomerIdRequest();
        byCustomerIdRequest.setCustomerId(commonUtil.getOperatorId());
        Integer count = customerAccountQueryProvider.countByCustomerId(byCustomerIdRequest).getContext().getResult();
        if (null != count && count >= 5) {
            //会员最多有5条银行账户信息
            return BaseResponse.error("会员最多有5条银行账户信息");
        } else {
            String customerId = commonUtil.getOperatorId();
            accountSaveRequest.setCustomerId(customerId);
            accountSaveRequest.setEmployeeId(customerId);
            customerAccountProvider.add(accountSaveRequest);
            return BaseResponse.SUCCESSFUL();
        }
    }

    /**
     * 修改客户收货地址
     *
     * @param accountSaveRequest
     * @return
     */
    @ApiOperation(value = "修改会员的银行账户信息")
    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    public BaseResponse editCustomerAccount(@RequestBody @Valid CustomerAccountModifyByCustomerIdRequest accountSaveRequest) {
        final boolean[] flag = {false};

        //TODO 银行账号
        if(StringUtils.isNotEmpty(accountSaveRequest.getCustomerAccountNo())){
            if(!Pattern.matches(commonUtil.REGEX_BANK_CARD,accountSaveRequest.getCustomerAccountNo())){
                return BaseResponse.error("账号格式不正确");
            }
        }else{
            return BaseResponse.error("请输入正确的账号格式");
        }

        CustomerAccountByCustomerAccountNoRequest byCustomerAccountNoRequest = new CustomerAccountByCustomerAccountNoRequest();
        byCustomerAccountNoRequest.setCustomerAccountNo(accountSaveRequest.getCustomerAccountNo());
        BaseResponse<CustomerAccountByCustomerAccountResponse> baseResponse = customerAccountQueryProvider.getByCustomerAccountNoAndDelFlag(byCustomerAccountNoRequest);
        CustomerAccountByCustomerAccountResponse response = baseResponse.getContext();
        if (Objects.nonNull(response) && Objects.nonNull(response.getCustomerAccountId())){
            if(!response.getCustomerAccountId().equals(accountSaveRequest.getCustomerAccountId())){
                flag[0] = true;
            }
        }
        if (flag[0]) {
            return BaseResponse.error("银行账号已存在");
        }
        accountSaveRequest.setCustomerId(commonUtil.getOperatorId());
        customerAccountProvider.modifyByCustomerId(accountSaveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除客户收货地址
     *
     * @param accountId
     * @return
     */
    @ApiOperation(value = "删除会员的银行账户信息")
    @RequestMapping(value = "/account/{accountId}", method = RequestMethod.DELETE)
    public BaseResponse deleteCustomerAccount(@PathVariable("accountId") String accountId) {
        CustomerAccountDeleteByCustomerAccountIdAndCustomerIdRequest request = new CustomerAccountDeleteByCustomerAccountIdAndCustomerIdRequest();
        request.setCustomerAccountId(accountId);
        request.setCustomerId(commonUtil.getOperatorId());
        customerAccountProvider.deleteCustomerAccountByCustomerAccountIdAndCustomerId(request);
        return BaseResponse.SUCCESSFUL();
    }
}
