package com.wanmi.sbc.login;

import com.google.common.collect.Maps;
import com.wanmi.sbc.base.verifycode.VerifyCodeService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.SecurityUtil;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByMobileRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeePasswordModifyByIdRequest;
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
@RestController("supplierPasswordController")
@RequestMapping("/password")
@Api(description = "商品分类服务",tags ="PasswordController")
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

    /**
     * 平台找回密码/重置密码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "平台找回密码/重置密码")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone",
            value = "手机号", required = true, example = "1393521586")
    @RequestMapping(value = "/sms/{phone}", method = RequestMethod.POST)
    public BaseResponse sendSmsVerifyCode(@PathVariable String phone) {
        if (!ValidateUtil.isPhone(phone)) {
            logger.error("手机号:{}不符合要求", phone);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return Optional.ofNullable(employeeQueryProvider.getByMobile(
                    EmployeeByMobileRequest.builder()
                            .mobile(phone).accountType(AccountType.s2bBoss).build()
                ).getContext())
                .map(EmployeeByMobileResponse::getEmployeeMobile)
                .map(mobile -> {
                    if (verifyCodeService.validSmsCertificate(mobile, VerifyType.S2B_BOSS_SEND)) {
                        verifyCodeService.generateSmsCertificate(mobile, VerifyType.S2B_BOSS_SEND, 1, TimeUnit.MINUTES);
                        String verifyCode = verifyCodeService.generateSmsVerifyCode(mobile, VerifyType.S2B_BOSS_CHANGE_PASSWORD, 5, TimeUnit.MINUTES);
                        smsSendUtil.send(SmsTemplate.CHANGE_PASSWORD, new String[]{mobile}, verifyCode);
                    } else {
                        throw new SbcRuntimeException(CommonErrorCode.SEND_FAILURE);
                    }
                    return BaseResponse.SUCCESSFUL();
                })
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SEND_FAILURE));
    }


    /**
     * 平台验证验证码
     *
     * @param phone 手机号码
     * @param code  验证码
     * @return BaseResponse
     */
    @ApiOperation(value = "平台验证验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone",
                    value = "手机号", required = true, example = "1393521586"),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "code",
                    value = "验证码", required = true, example = "000000")
    })
    @RequestMapping(value = "/sms/{phone}/{code}", method = RequestMethod.POST)
    public BaseResponse<String> validateSmsVerifyCode(@PathVariable("phone") String phone, @PathVariable("code") String code) {
        if (!ValidateUtil.isPhone(phone)) {
            logger.error("手机号:{}不符合要求", phone);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        boolean verifyResult = verifyCodeService.validateSmsVerifyCode(phone, code, VerifyType.S2B_BOSS_CHANGE_PASSWORD, 1, TimeUnit.DAYS);
        if (verifyResult) {
            return BaseResponse.success(code);
        } else {
            verifyCodeService.addVerifyCodeErrorCount(phone);
            throw new SbcRuntimeException(CommonErrorCode.VERIFICATION_CODE_ERROR);
        }
    }


    /**
     * 平台设置手机密码
     *
     * @param phone    phone
     * @param password password
     * @return BaseResponse
     */
    @ApiOperation(value = "平台设置手机密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone",
                    value = "手机号", required = true, example = "1393521586"),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "password",
                    value = "密码", required = true, example = "000000"),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "code",
                    value = "验证码", required = true, example = "000000")
    })
    @RequestMapping(value = "/{phone}/{password}/{code}", method = RequestMethod.POST)
    public BaseResponse<EmployeePasswordModifyByIdResponse> setPassword(@PathVariable("phone") String phone,
                                                                        @PathVariable("password") String password,
                                                                        @PathVariable("code") String code) {
        if (!ValidateUtil.isPhone(phone)) {
            logger.error("手机号:{}不符合要求", phone);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if (!ValidateUtil.isBetweenLen(password, 6, 32)) {
            logger.error("密码:{}不符合要求", password);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        boolean hasCert = verifyCodeService.validateSmsVerifyCodeAgain(phone, code, VerifyType.S2B_BOSS_CHANGE_PASSWORD);
        if (hasCert) {
            verifyCodeService.deletePhoneCertificate(phone, VerifyType.S2B_BOSS_CHANGE_PASSWORD);
            return Optional.ofNullable(employeeQueryProvider.getByMobile(
                        EmployeeByMobileRequest.builder()
                                .mobile(phone).accountType(AccountType.s2bBoss).build()
                    ).getContext())
                    .map(employee -> {
                        String encodePwd = SecurityUtil.getStoreLogpwd(String.valueOf(employee.getEmployeeId()), password, employee.getEmployeeSaltVal());

                        Map<String, Object> claims = Maps.newHashMap();
                        claims.put("employeeId", employee.getEmployeeId());
                        claims.put("EmployeeName", employee.getAccountName());
                        claims.put("adminId", 0);
                        claims.put("storeId", 0);
                        claims.put("platform", Platform.PLATFORM.toValue());
                        claims.put("ip", HttpUtil.getIpAddr());

                        operateLogMQUtil.convertAndSend("账户管理", "账号管理", "修改密码", new DefaultClaims(claims));

                        return employeeProvider.modifyPasswordById(
                                EmployeePasswordModifyByIdRequest.builder()
                                        .employeeId(employee.getEmployeeId()).encodePwd(encodePwd).build()
                        );
                    }
            ).orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.FAILED));
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

}
