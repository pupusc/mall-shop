package com.wanmi.sbc.customer;

import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.email.CustomerEmailProvider;
import com.wanmi.sbc.customer.api.provider.email.CustomerEmailQueryProvider;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailAddRequest;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailDeleteRequest;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailModifyRequest;
import com.wanmi.sbc.customer.api.request.email.NoDeleteCustomerEmailListByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.email.CustomerEmailAddResponse;
import com.wanmi.sbc.customer.api.response.email.CustomerEmailModifyResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerEmailVO;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeSendEmailToFinanceRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 客户财务邮箱控制器
 */
@RestController
@RequestMapping("/customer")
@Validated
@Api(tags = "CustomerDeliveryAddressBaseController", description = "S2B web公用-客户财务邮箱管理API")
public class CustomerEmailBaseController {

    @Autowired
    private CustomerEmailQueryProvider customerEmailQueryProvider;

    @Autowired
    private CustomerEmailProvider customerEmailProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private BaseConfigQueryProvider baseConfigQueryProvider;


    /**
     * 根据客户ID查询客户的财务邮箱列表
     *
     * @return
     */
    @ApiOperation(value = "根据客户ID查询客户的财务邮箱列表")
    @RequestMapping(value = "/emailList", method = RequestMethod.GET)
    public BaseResponse<List<CustomerEmailVO>> listCustomerEmailByCustomerId() {
        return BaseResponse.success(customerEmailQueryProvider.list(new NoDeleteCustomerEmailListByCustomerIdRequest
                (commonUtil.getOperatorId())).getContext().getCustomerEmails());
    }

    /**
     * 新增客户财务邮箱
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "新增客户财务邮箱")
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public BaseResponse<CustomerEmailAddResponse> addCouponCate(@RequestBody @Valid CustomerEmailAddRequest request) {

        if(StringUtils.isNotEmpty(request.getEmailAddress())){
            if(!Pattern.matches(commonUtil.REGEX_EMAIL,request.getEmailAddress())){
                return BaseResponse.error("您填写的邮箱格式有误，请检查后重新输入");
            }
        }else{
            return BaseResponse.error("请输入邮箱地址");
        }

        request.setCustomerId(commonUtil.getOperatorId());
        request.setCreatePerson(commonUtil.getOperatorId());
        request.setCreateTime(LocalDateTime.now());
        CustomerEmailAddResponse customerEmailResponse = customerEmailProvider.add(request).getContext();
        return BaseResponse.success(customerEmailResponse);
    }

    /**
     * 修改客户财务邮箱
     *
     * @return
     */
    @ApiOperation(value = "修改客户财务邮箱")
    @RequestMapping(value = "/email", method = RequestMethod.PUT)
    public BaseResponse modifyCouponCate(@RequestBody @Valid CustomerEmailModifyRequest request) {

        if( !commonUtil.getOperatorId().equals(request.getCustomerId())){
            return BaseResponse.error("非法越权操作");
        }

        if(StringUtils.isNotEmpty(request.getEmailAddress())){
            if(!Pattern.matches(commonUtil.REGEX_EMAIL,request.getEmailAddress())){
                return BaseResponse.error("您填写的邮箱格式有误，请检查后重新输入");
            }
        }else{
            return BaseResponse.error("请输入邮箱地址");
        }

        request.setUpdatePerson(commonUtil.getOperatorId());
        request.setUpdateTime(LocalDateTime.now());
        CustomerEmailModifyResponse customerEmailResponse = customerEmailProvider.modify(request).getContext();
        return BaseResponse.success(customerEmailResponse);
    }

    /**
     * 根据客户邮箱ID删除财务邮箱
     *
     * @param customerEmailId
     * @return
     */
    @ApiOperation(value = "根据客户邮箱ID删除财务邮箱")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerEmailId", value = "客户邮箱ID", required = true)
    @RequestMapping(value = "/email/{customerEmailId}", method = RequestMethod.DELETE)
    public BaseResponse deleteCustomerEmailByCustomerEmailId(@PathVariable String customerEmailId) {

        List<CustomerEmailVO> emailVOS = customerEmailQueryProvider.list(new NoDeleteCustomerEmailListByCustomerIdRequest
                (commonUtil.getOperatorId())).getContext().getCustomerEmails().stream()
                .filter(customerEmailVO -> customerEmailVO.getCustomerEmailId().equals(customerEmailId)).collect(Collectors.toList());
        if(emailVOS.isEmpty()){
            return BaseResponse.error("操作失败");
        }

        CustomerEmailDeleteRequest request = new CustomerEmailDeleteRequest();
        request.setCustomerEmailId(customerEmailId);
        request.setDelTime(LocalDateTime.now());
        request.setDelPerson(commonUtil.getOperatorId());
        customerEmailProvider.deleteByCustomerId(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 银联企业支付通知财务
     *
     * @return
     */
    @ApiOperation(value = "银联企业支付通知财务")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "orderId", value = "订单id", required = true)
    @RequestMapping(value = "/email/sendToFinance/{orderId}", method = RequestMethod.POST)
    public BaseResponse sendEmailToFinance(@PathVariable String orderId) {
        // 获取PC端地址
        BaseConfigRopResponse response = baseConfigQueryProvider.getBaseConfig().getContext();
//        CompositeResponse<BaseConfigRopResponse> response = sdkClient.buildClientRequest()
//                .get(BaseConfigRopResponse.class, "baseConfig.query", "1.0.0");
        tradeQueryProvider.sendEmailToFinance(TradeSendEmailToFinanceRequest.builder()
                .customerId(commonUtil.getOperatorId()).orderId(orderId).url(response.getPcWebsite()).build());
        return BaseResponse.SUCCESSFUL();
    }

}
