package com.wanmi.sbc.company;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.*;
import com.wanmi.sbc.customer.api.response.employee.StoreInformationResponse;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.elastic.api.provider.storeInformation.EsStoreInformationProvider;
import com.wanmi.sbc.elastic.api.request.storeInformation.StoreInformationRequest;
import com.wanmi.sbc.redis.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Created by hht on 2017/11/23.
 */
@Api(tags = "CompanyController", description = "商家注册API")
@RestController
@RequestMapping("/company")
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private EsStoreInformationProvider esStoreInformationProvider;

    /**
     * 商家注册时发送验证码
     *
     * @param account
     * @return BaseResponse
     */
    @ApiOperation(value = "商家注册时发送验证码")
    @ApiImplicitParam( paramType = "path", dataType = "String",name = "account", value = "手机号", required = true)
    @RequestMapping(value = "/verify-code/{account}", method = RequestMethod.POST)
    public BaseResponse sendVerifyCode(@PathVariable String account) {
        //发送验证码，验证手机号
        if(!ValidateUtil.isPhone(account)){
            logger.error("手机号码:{}格式错误", account);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //该手机号是否已注册
        if (employeeQueryProvider.getByAccountName(EmployeeByAccountNameRequest.builder()
                .accountName(account).accountType(AccountType.s2bSupplier).build()).getContext().getEmployee() != null){
            logger.error("手机号码:{}已注册", account);
            throw new SbcRuntimeException(EmployeeErrorCode.ALREADY_EXIST);
        }
        //同一个手机是否操作频繁
        boolean isSendSms = employeeQueryProvider.mobileIsSms(
                EmployeeMobileSmsRequest.builder().mobile(account).build()
        ).getContext().isSendSms();
        if(!isSendSms){
            logger.error("手机号码:{}操作频繁", account);
            throw new SbcRuntimeException(EmployeeErrorCode.FREQUENT_OPERATION);
        }
        //发送验证码
        return employeeProvider.sms(
                EmployeeSmsRequest.builder().redisKey(CacheKeyConstant.YZM_SUPPLIER_REGISTER)
                .mobile(account)
                .smsTemplate(SmsTemplate.REGISTRY).build()
        );
    }

    /**
     * 商家注册
     * 验证手机号
     * 验证验证码
     * @param loginRequest
     * @return
     */
    @ApiOperation(value = "商家注册验证验证码")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseResponse register(@Valid @RequestBody EmployeeLoginRequest loginRequest){
        //验证手机号
        if(!ValidateUtil.isPhone(loginRequest.getAccount())){
            logger.error("手机号码:{}格式错误", loginRequest.getAccount());
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //验证验证码
        if(StringUtils.isBlank(loginRequest.getVerifyCode())){
            logger.error("手机验证码为空");
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //该手机号是否已注册
        if (employeeQueryProvider.getByAccountName(EmployeeByAccountNameRequest.builder()
                .accountName(loginRequest.getAccount()).accountType(AccountType.s2bSupplier).build()).getContext().getEmployee() != null){
            logger.error("手机号码:{}已注册", loginRequest.getAccount());
            throw new SbcRuntimeException(EmployeeErrorCode.ALREADY_EXIST);
        }
        //验证验证码
        String t_verifyCode = redisService.getString(CacheKeyConstant.YZM_SUPPLIER_REGISTER.concat(loginRequest.getAccount()));
        if (StringUtils.isBlank(t_verifyCode) || !StringUtils.equalsIgnoreCase(t_verifyCode,loginRequest.getVerifyCode())) {
            logger.error("手机号码:{}验证码错误", loginRequest.getAccount());
            throw new SbcRuntimeException(CommonErrorCode.VERIFICATION_CODE_ERROR);
        }
        EmployeeRegisterRequest registerRequest = new EmployeeRegisterRequest();
        KsBeanUtil.copyPropertiesThird(loginRequest, registerRequest);
        registerRequest.setAccountType(2);
        registerRequest.setStoreType(1);
        StoreInformationResponse response = employeeProvider.registerGetStoreInfo(registerRequest).getContext();
        if (Objects.nonNull(response)
        ){
            //删除验证码缓存
            redisService.delete(CacheKeyConstant.YZM_SUPPLIER_REGISTER.concat(loginRequest.getAccount()));

            StoreInformationRequest storeInformationRequest = new StoreInformationRequest();
            //存储到es中
            KsBeanUtil.copyPropertiesThird(response, storeInformationRequest);
            esStoreInformationProvider.initStoreInformation(storeInformationRequest);
            return BaseResponse.SUCCESSFUL();
        }


        return BaseResponse.FAILED();
    }


}
