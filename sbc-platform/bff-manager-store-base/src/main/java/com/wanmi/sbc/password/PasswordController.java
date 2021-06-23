package com.wanmi.sbc.password;

import com.google.common.collect.Maps;
import com.wanmi.sbc.base.verifycode.VerifyCodeService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByMobileRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeePasswordModifyByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByCompanyInfoIdRequest;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByMobileResponse;
import com.wanmi.sbc.customer.api.response.employee.EmployeePasswordModifyByIdResponse;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.setting.bean.enums.VerifyType;
import com.wanmi.sbc.util.OperateLogMQUtil;
import com.wanmi.sbc.util.sms.SmsSendUtil;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author: songhanlin
 * @Date: Created In 下午9:42 2017/11/2
 * @Description: 密码Controller
 */
@Api(tags = "PasswordController", description = "密码服务API")
@RestController("supplierPasswordController")
@RequestMapping("/password")
public class PasswordController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private SmsSendUtil smsSendUtil;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    /**
     * 商家找回密码/重置密码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "商家找回密码/重置密码")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone", value = "手机号码", required = true)
    @RequestMapping(value = "/sms/{phone}", method = RequestMethod.POST)
    public BaseResponse sendSmsVerifyCode(@PathVariable String phone) {
        return handleOption(phone,AccountType.s2bSupplier);
    }

    /**
     *供应商找回密码/重置密码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "供应商找回密码/重置密码")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone", value = "手机号码", required = true)
    @RequestMapping(value = "/provider/sms/{phone}", method = RequestMethod.POST)
    public BaseResponse sendSmsProviderVerifyCode(@PathVariable String phone) {
        return handleOption(phone,AccountType.s2bProvider);
    }


    /**
     * 统一处理
     * @param phone
     * @param type
     * @return
     */
    public BaseResponse handleOption(String phone,AccountType type){

        if (!ValidateUtil.isPhone(phone)) {
            logger.error("手机号:{}不符合要求", phone);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        Optional<EmployeeByMobileResponse> optional = Optional.ofNullable(employeeQueryProvider.getByMobile(
                EmployeeByMobileRequest.builder()
                        .mobile(phone).accountType(type)
                        .build()
        ).getContext());
        return optional.map(EmployeeByMobileResponse::getEmployeeMobile)
                .map(mobile -> {
                    //商家
                    if (AccountType.s2bSupplier.equals(type) && verifyCodeService.validSmsCertificate(mobile, VerifyType.SUPPLIER_SEND)) {
                        verifyCodeService.generateSmsCertificate(mobile, VerifyType.SUPPLIER_SEND, 1, TimeUnit.MINUTES);
                        String verifyCode = verifyCodeService.generateSmsVerifyCode(mobile,
                                VerifyType.SUPPLIER_CHANGE_PASSWORD, 5, TimeUnit.MINUTES);
                        smsSendUtil.send(SmsTemplate.CHANGE_PASSWORD, new String[]{mobile}, verifyCode);
                    }
                    //供应商
                    else if (AccountType.s2bProvider.equals(type) && verifyCodeService.validSmsCertificate(mobile, VerifyType.PROVIDER_SEND)) {
                        verifyCodeService.generateSmsCertificate(mobile, VerifyType.PROVIDER_SEND, 1, TimeUnit.MINUTES);
                        String verifyCode = verifyCodeService.generateSmsVerifyCode(mobile,
                                VerifyType.PROVIDER_CHANGE_PASSWORD, 5, TimeUnit.MINUTES);
                        smsSendUtil.send(SmsTemplate.CHANGE_PASSWORD, new String[]{mobile}, verifyCode);
                    }else {
                        throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
                    }
                    return BaseResponse.SUCCESSFUL();
                })
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SEND_FAILURE));
    }


    /**
     * 商家验证验证码
     *
     * @param phone 手机号码
     * @param code  验证码
     * @return BaseResponse
     */
    @ApiOperation(value = "商家验证验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "phone", value = "手机号码", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "code", value = "验证码", required = true)
    })
    @RequestMapping(value = "/sms/{phone}/{code}", method = RequestMethod.POST)
    public BaseResponse<String> validateSmsVerifyCode(@PathVariable("phone") String phone,
                                                      @PathVariable("code") String code) {
        return commonValidateVerifyCode(phone,code,VerifyType.SUPPLIER_CHANGE_PASSWORD);
    }


    /**
     * 品牌商验证验证码
     *
     * @param phone 手机号码
     * @param code  验证码
     * @return BaseResponse
     */
    @ApiOperation(value = "商家验证验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "phone", value = "手机号码", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "code", value = "验证码", required = true)
    })
    @RequestMapping(value = "/provider/sms/{phone}/{code}", method = RequestMethod.POST)
    public BaseResponse<String> validateMallSmsVerifyCode(@PathVariable("phone") String phone,
                                                      @PathVariable("code") String code) {
        return commonValidateVerifyCode(phone,code,VerifyType.PROVIDER_CHANGE_PASSWORD);
    }

    /**
     * 验证验证码统一处理
     * @param phone
     * @param code
     * @param type
     * @return
     */
    public BaseResponse<String> commonValidateVerifyCode(String phone,String code,VerifyType type){

        if (!ValidateUtil.isPhone(phone)) {
            logger.error("手机号:{}不符合要求", phone);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        boolean verifyResult = verifyCodeService.validateSmsVerifyCode(phone, code,
                type, 1, TimeUnit.DAYS);
        if (verifyResult) {
            return BaseResponse.success(code);
        } else {
            verifyCodeService.addVerifyCodeErrorCount(phone);
            throw new SbcRuntimeException(CommonErrorCode.VERIFICATION_CODE_ERROR);
        }
    }


    /**
     * 商家设置手机密码
     *
     * @param phone    phone
     * @param password password
     * @return BaseResponse
     */
    @ApiOperation(value = "商家设置手机密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "phone", value = "手机号码", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "password", value = "密码", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "code", value = "验证码", required = true)
    })
    @RequestMapping(value = "/{phone}/{password}/{code}", method = RequestMethod.POST)
    public BaseResponse<EmployeePasswordModifyByIdResponse> setPassword(@PathVariable("phone") String phone,
                                                                        @PathVariable("password") String password,
                                                                        @PathVariable("code") String code) {
        return commonSetPassword(phone,password,code,VerifyType.SUPPLIER_CHANGE_PASSWORD,AccountType.s2bSupplier);
    }


    /**
     * 品牌设置手机密码
     *
     * @param phone    phone
     * @param password password
     * @return BaseResponse
     */
    @ApiOperation(value = "商家设置手机密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "phone", value = "手机号码", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "password", value = "密码", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "code", value = "验证码", required = true)
    })
    @RequestMapping(value = "provider/{phone}/{password}/{code}", method = RequestMethod.POST)
    public BaseResponse<EmployeePasswordModifyByIdResponse> setMallPassword(@PathVariable("phone") String phone,
                                                                        @PathVariable("password") String password,
                                                                        @PathVariable("code") String code) {
      return commonSetPassword(phone,password,code,VerifyType.PROVIDER_CHANGE_PASSWORD,AccountType.s2bProvider);
    }

    /**
     * 统一设置代码
     * @param phone
     * @param password
     * @param code
     * @return
     */
    public BaseResponse<EmployeePasswordModifyByIdResponse> commonSetPassword(String phone,String password,String code,
                                                                              VerifyType type,AccountType accountType){
        if (!ValidateUtil.isPhone(phone)) {
            logger.error("手机号:{}不符合要求", phone);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if (!ValidateUtil.isBetweenLen(password, 6, 32)) {
            logger.error("密码:{}不符合要求", password);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        boolean hasCert = verifyCodeService.validateSmsVerifyCodeAgain(phone, code, type);
        if (hasCert) {
            verifyCodeService.deletePhoneCertificate(phone, type);
            return Optional.ofNullable(employeeQueryProvider.getByMobile(
                    EmployeeByMobileRequest.builder()
                            .mobile(phone).accountType(accountType)
                            .build()
            ).getContext()).map(
                    employee -> {
                        String encodePwd = SecurityUtil.getStoreLogpwd(String.valueOf(employee.getEmployeeId()),
                                password, employee.getEmployeeSaltVal());

                        Long companyInfoId = employee.getCompanyInfo().getCompanyInfoId();
                        Map<String, Object> claims = Maps.newHashMap();
                        claims.put("employeeId", employee.getEmployeeId());
                        claims.put("EmployeeName", employee.getAccountName());
                        claims.put("adminId", companyInfoId);
                        claims.put("storeId", storeQueryProvider.getStoreByCompanyInfoId(
                                new StoreByCompanyInfoIdRequest(companyInfoId)).getContext().getStoreVO().getStoreId());
                        if(AccountType.s2bProvider.equals(type)){
                            claims.put("platform", Platform.PROVIDER.toValue());
                        }else{
                            claims.put("platform", Platform.SUPPLIER.toValue());
                        }
                        claims.put("ip", HttpUtil.getIpAddr());
                        operateLogMQUtil.convertAndSend("账户管理", "账号管理", "修改密码", new DefaultClaims(claims));
                        return employeeProvider.modifyPasswordById(
                                EmployeePasswordModifyByIdRequest.builder()
                                        .employeeId(employee.getEmployeeId()).encodePwd(encodePwd).build()
                        );
                    }
            ).orElseThrow(() -> new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_PASSWORD_ERROR));
        } else {
            throw new SbcRuntimeException(CommonErrorCode.VERIFICATION_CODE_ERROR);
        }
    }
}
