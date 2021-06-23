package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountProvider;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountQueryProvider;
import com.wanmi.sbc.customer.api.request.account.*;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountByCustomerAccountResponse;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountListResponse;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountOptionalResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerAccountVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 客户银行账号
 *
 * @author CHENLI
 * @date 2017/4/21
 */
@Api(tags = "CustomerAccountController", description = "客户银行账号")
@RestController
@RequestMapping("/customer")
@Validated
public class CustomerAccountController {
    @Autowired
    private CustomerAccountQueryProvider customerAccountQueryProvider;

    @Autowired
    private CustomerAccountProvider customerAccountProvider;

    /**
     * 查询会员的银行账户信息
     * @param customerId
     * @return
     */
    @ApiOperation(value = "查询会员的银行账户信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "用户Id", required = true)
    @RequestMapping(value = "/accountList/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<List<CustomerAccountVO>> findAll(@PathVariable("customerId") String customerId){
        CustomerAccountListRequest customerAccountListRequest = new CustomerAccountListRequest();
        customerAccountListRequest.setCustomerId(customerId);
        BaseResponse<CustomerAccountListResponse>  customerAccountListResponseBaseResponse = customerAccountQueryProvider.listByCustomerId(customerAccountListRequest);
        CustomerAccountListResponse customerAccountListResponse =  customerAccountListResponseBaseResponse.getContext();
        if (Objects.nonNull(customerAccountListResponse) && CollectionUtils.isNotEmpty(customerAccountListResponse.getCustomerAccountVOList())){
            return ResponseEntity.ok(customerAccountListResponse.getCustomerAccountVOList());
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    /**
     * 查询会员银行账户信息
     * @param customerAccountId
     * @return ResponseEntity<CustomerAccount>
     */
    @ApiOperation(value = "查询会员银行账户信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerAccountId", value = "用户银行账号Id",
            required = true)
    @RequestMapping(value = "/account/{customerAccountId}", method = RequestMethod.GET)
    public ResponseEntity<CustomerAccountOptionalResponse> findById(@PathVariable("customerAccountId") String customerAccountId) {
        CustomerAccountOptionalRequest request = new CustomerAccountOptionalRequest();
        request.setCustomerAccountId(customerAccountId);
        BaseResponse<CustomerAccountOptionalResponse> baseResponse = customerAccountQueryProvider.getByCustomerAccountIdAndDelFlag(request);
        CustomerAccountOptionalResponse response = baseResponse.getContext();
        return ResponseEntity.ok(response);
    }

    /**
     * 保存会员的银行账户信息
     * @param accountSaveRequest
     * @return
     */
    @ApiOperation(value = "保存会员的银行账户信息")
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> addCustomerAccount(@Valid @RequestBody CustomerAccountAddRequest accountSaveRequest, HttpServletRequest request){
        //查询会员有几条银行账户信息
        CustomerAccountByCustomerIdRequest byCustomerIdRequest = new CustomerAccountByCustomerIdRequest();
        byCustomerIdRequest.setCustomerId(accountSaveRequest.getCustomerId());
        Integer count = customerAccountQueryProvider.countByCustomerId(byCustomerIdRequest).getContext().getResult();
        if(null != count && count == 5){
            //会员最多有5条银行账户信息
            throw new SbcRuntimeException("K-010005");
        }else {
            String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
            accountSaveRequest.setEmployeeId(employeeId);
            customerAccountProvider.add(accountSaveRequest);
            return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
        }
    }

    /**
     * 修改客户银行账户信息
     * @param accountSaveRequest
     * @return
     */
    @ApiOperation(value = "修改客户银行账户信息")
    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> editCustomerAccount(@Valid @RequestBody CustomerAccountModifyRequest accountSaveRequest, HttpServletRequest request){
        final boolean[] flag = {false};
        CustomerAccountByCustomerAccountNoRequest byCustomerAccountNoRequest = new CustomerAccountByCustomerAccountNoRequest();
        byCustomerAccountNoRequest.setCustomerAccountNo(accountSaveRequest.getCustomerAccountNo());
        BaseResponse<CustomerAccountByCustomerAccountResponse> baseResponse = customerAccountQueryProvider.getByCustomerAccountNoAndDelFlag(byCustomerAccountNoRequest);
        CustomerAccountByCustomerAccountResponse response = baseResponse.getContext();
        if (Objects.nonNull(response) && Objects.nonNull(response.getCustomerAccountId())){
            if(!response.getCustomerAccountId().equals(accountSaveRequest.getCustomerAccountId())){
                flag[0] = true;
            }
        }
        if(flag[0]){
            return ResponseEntity.ok(BaseResponse.error("银行账号不允许重复"));
        }
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        accountSaveRequest.setEmployeeId(employeeId);
        customerAccountProvider.modify(accountSaveRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 删除客户银行账户信息
     * @param accountId
     * @return
     */
    @ApiOperation(value = "删除客户银行账户信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "accountId", value = "账户Id",
            required = true)
    @RequestMapping(value = "/account/{accountId}", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> deleteCustomerAccount(@PathVariable("accountId") String accountId, HttpServletRequest request){
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest byCustomerAccountIdAndCustomerIdRequest = new CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest();
        byCustomerAccountIdAndCustomerIdRequest.setEmployeeId(employeeId);
        byCustomerAccountIdAndCustomerIdRequest.setCustomerAccountId(accountId);
        customerAccountProvider.deleteByCustomerAccountIdAndEmployeeId(byCustomerAccountIdAndCustomerIdRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }
}
