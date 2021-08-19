package com.wanmi.sbc.SupplierRegister;

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
import com.wanmi.sbc.customer.api.response.employee.EmployeeRegisterResponse;
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
 * @Author: songhanlin
 * @Date: Created In 下午7:03 2017/11/1
 * @Description: 公司信息Controller
 */
@Api(tags = "SupplierCompanyInfoController", description = "公司信息 API")
@RestController("SupplierCompanyInfoController")
@RequestMapping("/company")
public class SupplierCompanyInfoController {

    private static final Logger logger = LoggerFactory.getLogger(SupplierCompanyInfoController.class);

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
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "account", value = "手机号码", required = true)
    @RequestMapping(value = "/verify-code/{account}/{type}", method = RequestMethod.POST)
    public BaseResponse sendVerifyCode(@PathVariable String account,@PathVariable Integer type) {
        //发送验证码，验证手机号
        if(!ValidateUtil.isPhone(account)){
            logger.error("手机号码:{}格式错误", account);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //传了类型则校验同商家类型下该手机号是否已注册，不传类型则默认为商家，校验是否存在该手机号注册的商家
        if (Objects.nonNull(type) && employeeQueryProvider.getByAccountName(EmployeeByAccountNameRequest.builder()
                .accountName(account).accountType(AccountType.fromValue(type)).build()).getContext().getEmployee() != null
              || Objects.isNull(type) &&  employeeQueryProvider.getByAccountName(EmployeeByAccountNameRequest.builder()
                .accountName(account).accountType(AccountType.s2bSupplier).build()).getContext().getEmployee() != null ){
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
        //传了type且为供应商，发送验证码
        if(Objects.nonNull(type) && AccountType.fromValue(type).equals(AccountType.s2bProvider)){
            return employeeProvider.sms(
                    EmployeeSmsRequest.builder().redisKey(CacheKeyConstant.YZM_PROVIDER_REGISTER)
                            .mobile(account)
                            .smsTemplate(SmsTemplate.REGISTRY).build()
            );
        }
        //发送验证码--商家
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
    @ApiOperation(value = "商家注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseResponse register(@Valid @RequestBody EmployeeLoginRequest loginRequest){
        EmployeeLoginRequest employeeLoginRequest = new EmployeeLoginRequest();
        BeanUtils.copyProperties(loginRequest,employeeLoginRequest);
        //没传账号类型，默认2，商家
        if(Objects.isNull(loginRequest.getAccountType())){
            employeeLoginRequest.setAccountType(2);
        }
        //没传店铺类型，默认1，商家
        if(Objects.isNull(loginRequest.getStoreType())){
            employeeLoginRequest.setStoreType(1);
        }
        //验证手机号
        if(!ValidateUtil.isPhone(employeeLoginRequest.getAccount())){
            logger.error("手机号码:{}格式错误", loginRequest.getAccount());
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //验证验证码
        if(StringUtils.isBlank(employeeLoginRequest.getVerifyCode())){
            logger.error("手机验证码为空");
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //同种商家类型的该手机号是否已注册
        if ( employeeQueryProvider.getByAccountName(EmployeeByAccountNameRequest.builder()
                .accountName(employeeLoginRequest.getAccount()).accountType(AccountType.fromValue(employeeLoginRequest.getAccountType())).build()).getContext().getEmployee() != null){
            logger.error("手机号码:{}已注册", employeeLoginRequest.getAccount());
            throw new SbcRuntimeException(EmployeeErrorCode.ALREADY_EXIST);
        }
        //验证验证码
        String t_verifyCode = null;
        //如果是品牌商类型
        if(AccountType.s2bProvider.toValue()==employeeLoginRequest.getAccountType()){
            t_verifyCode  = redisService.getString(CacheKeyConstant.YZM_PROVIDER_REGISTER.concat(employeeLoginRequest.getAccount()));
        }
        //如果是供应商类型
        if(AccountType.s2bSupplier.toValue()==employeeLoginRequest.getAccountType()){
            t_verifyCode  = redisService.getString(CacheKeyConstant.YZM_SUPPLIER_REGISTER.concat(employeeLoginRequest.getAccount()));
        }
        if (StringUtils.isBlank(t_verifyCode) || !StringUtils.equalsIgnoreCase(t_verifyCode,employeeLoginRequest.getVerifyCode())) {
            logger.error("手机号码:{}验证码错误", employeeLoginRequest.getAccount());
            throw new SbcRuntimeException(CommonErrorCode.VERIFICATION_CODE_ERROR);
        }
        EmployeeRegisterRequest registerRequest = new EmployeeRegisterRequest();
        KsBeanUtil.copyPropertiesThird(employeeLoginRequest, registerRequest);
        StoreInformationResponse response = employeeProvider.registerGetStoreInfo(registerRequest).getContext();
        if (Objects.nonNull(response)){
            //删除验证码缓存--区分商家类型
            if(AccountType.s2bSupplier.toValue()==employeeLoginRequest.getAccountType()){
                redisService.delete(CacheKeyConstant.YZM_SUPPLIER_REGISTER.concat(employeeLoginRequest.getAccount()));
            }
            if(AccountType.s2bProvider.toValue()==employeeLoginRequest.getAccountType()){
                redisService.delete(CacheKeyConstant.YZM_PROVIDER_REGISTER.concat(employeeLoginRequest.getAccount()));
            }
            StoreInformationRequest storeInformationRequest = new StoreInformationRequest();
            //存储到es中
            KsBeanUtil.copyPropertiesThird(response, storeInformationRequest);
            esStoreInformationProvider.initStoreInformation(storeInformationRequest);

            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }
}
