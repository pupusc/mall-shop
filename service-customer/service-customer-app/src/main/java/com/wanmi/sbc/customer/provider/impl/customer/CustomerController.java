package com.wanmi.sbc.customer.provider.impl.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.constant.RedisConstant;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerAccountModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerAddRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerCheckStateModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerEnterpriseCheckStateModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerFandengModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerSalesManModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerToDistributorModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomersDeleteRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengKnowledgeRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerAccountModifyResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerAddResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerCheckStateModifyResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerModifyResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomersDeleteResponse;
import com.wanmi.sbc.customer.api.response.fandeng.FanDengKnowledgeResponse;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailToEsVO;
import com.wanmi.sbc.customer.fandeng.ExternalService;
import com.wanmi.sbc.customer.redis.RedisService;
import com.wanmi.sbc.customer.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class CustomerController implements CustomerProvider {
    @Autowired
    private CustomerService customerService;

//    @Autowired
//    private  CounselorRepository counselorRepository;

    @Autowired
    private ExternalService externalService;

    @Autowired
    private RedisService redisService;
    /**
     * 审核客户状态
     *
     * {@link CustomerService#updateCheckState}
     */
    @Override
    public BaseResponse<CustomerCheckStateModifyResponse> modifyCustomerCheckState(@RequestBody @Valid
                                                                                               CustomerCheckStateModifyRequest request) {
        int count = customerService.updateCheckState(request);

        return BaseResponse.success(new CustomerCheckStateModifyResponse(count));
    }

    /**
     * 审核企业会员
     *
     */
    @Override
    public BaseResponse<CustomerCheckStateModifyResponse> checkEnterpriseCustomer(@RequestBody @Valid
                                                                                              CustomerEnterpriseCheckStateModifyRequest request) {
        int count = customerService.checkEnterpriseCustomer(request);

        return BaseResponse.success(new CustomerCheckStateModifyResponse(count));
    }

    /**
     * boss批量删除会员
     * 删除会员
     * 删除会员详情表
     *
     * {@link CustomerService#delete}
     */
    @Override

    public BaseResponse<CustomersDeleteResponse> deleteCustomers(@RequestBody @Valid CustomersDeleteRequest request) {

        int count = customerService.delete(request.getCustomerIds());

        return BaseResponse.success(new CustomersDeleteResponse(count));
    }

    /**
     * 新增客户共通
     *
     * {@link CustomerService#saveCustomerAll}
     */
    @Override

    public BaseResponse<CustomerAddResponse> saveCustomer(@RequestBody @Valid CustomerAddRequest request) {
        CustomerAddResponse response = customerService.saveCustomerAll(request);
        return BaseResponse.success(response);
    }


    /**
     * Boss端修改会员
     * 修改会员表，修改会员详细信息
     *
     * {@link CustomerService#updateCustomerAll}
     */
    @Override

    public BaseResponse<CustomerModifyResponse> modifyCustomer(@RequestBody @Valid CustomerModifyRequest request) {
        CustomerDetailToEsVO customerDetailToEsVO = customerService.updateCustomerAll(request);

        return BaseResponse.success(CustomerModifyResponse.builder().customerDetailToEsVO(customerDetailToEsVO).build());
    }

    /**
     * 修改绑定手机号
     *
     * {@link CustomerService#updateCustomerAccount}
     */
    @Override

    public BaseResponse<CustomerAccountModifyResponse> modifyCustomerAccount(@RequestBody @Valid CustomerAccountModifyRequest request) {
        int count = customerService.updateCustomerAccount(request);

        return BaseResponse.success(new CustomerAccountModifyResponse(count));
    }

    /**
     * 修改樊登id
     *
     * {@link CustomerService#updateCustomerAccount}
     */
    @Override
    public BaseResponse<CustomerAccountModifyResponse> modifyCustomerFanDengIdTime(@RequestBody @Valid CustomerFandengModifyRequest request) {
        int count = customerService.updateFanDengId(request.getCustomerId(),request.getFanDengId(),request.getLoginIp());

        return BaseResponse.success(new CustomerAccountModifyResponse(count));
    }

    @Override
    public BaseResponse modifyCustomerOpenIdAndUnionId(@RequestParam("customerId") String customerId, @RequestParam("openId") String openId, @RequestParam("unionId") String unionId) {
        customerService.updateOpenIdAndUnionId(customerId, openId, unionId);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改已有的业务员
     *
     * {@link CustomerService#updateCustomerSalesMan}
     */
    @Override

    public BaseResponse modifyCustomerSalesMan(@RequestBody @Valid CustomerSalesManModifyRequest request) {
        customerService.updateCustomerSalesMan(request.getEmployeeId(), request.getAccountType());

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新会员为销售员
     *
     * @param request {@link CustomerToDistributorModifyRequest}
     * @return
     */
    @Override
    public BaseResponse modifyToDistributor(@RequestBody @Valid CustomerToDistributorModifyRequest request){
        customerService.updateCustomerToDistributor(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 获取知识顾问信息
     *
     * @return
     */
    @Override
    public BaseResponse<String> isCounselorCache(Integer userId) {
        String isKnowledgeUser = redisService.getString(RedisConstant.CUSTOMER_KNOWLEDGE_PREFIX + userId);
        if (StringUtils.isBlank(isKnowledgeUser)) {
            FanDengKnowledgeRequest request = new FanDengKnowledgeRequest();
            request.setUserNo(userId.toString());
            BaseResponse<FanDengKnowledgeResponse> response = externalService.getKnowledgeByFanDengNo(request);
            if (response.getContext() == null) {
                redisService.setString(RedisConstant.CUSTOMER_KNOWLEDGE_PREFIX + userId, "false", 24 * 60 * 60);
                return BaseResponse.success("false");
            } else {
                CounselorDto counselorDto = new CounselorDto();
                counselorDto.setProfit(response.getContext().getBeans().intValue());
                redisService.setString(RedisConstant.CUSTOMER_KNOWLEDGE_PREFIX + userId, "true", 24 * 60 * 60);
                return BaseResponse.success("true");
            }
        } else {
            return BaseResponse.success(isKnowledgeUser);
        }
    }


    /**
     * 获取知识顾问信息
     *
     * @return
     */
    @Override
    public BaseResponse isCounselor(Integer userId) {
        String isKnowledgeUser = redisService.getString(RedisConstant.CUSTOMER_KNOWLEDGE_PREFIX + userId);
        if (StringUtils.isBlank(isKnowledgeUser) || "true".equals(isKnowledgeUser)) {
            FanDengKnowledgeRequest request = new FanDengKnowledgeRequest();
            request.setUserNo(userId.toString());
            BaseResponse<FanDengKnowledgeResponse> response = externalService.getKnowledgeByFanDengNo(request);
            if (response.getContext() == null) {
                redisService.setString(RedisConstant.CUSTOMER_KNOWLEDGE_PREFIX + userId, "false", 24 * 60 * 60);
                return BaseResponse.SUCCESSFUL();
            } else {
                CounselorDto counselorDto = new CounselorDto();
                counselorDto.setProfit(response.getContext().getBeans().intValue());
                redisService.setString(RedisConstant.CUSTOMER_KNOWLEDGE_PREFIX + userId, "true", 24 * 60 * 60);
                return BaseResponse.success(counselorDto);
            }
        } else {
            return BaseResponse.SUCCESSFUL();
        }
    }

}
