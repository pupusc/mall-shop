package com.wanmi.sbc.customer.validator;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.request.employee.EmployeeAddRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

/**
 * 员工信息校验
 * Created by CHENLI on 2017/5/11.
 */
@Component
public class EmployeeValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return EmployeeAddRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmployeeAddRequest saveRequest = (EmployeeAddRequest)target;
        //验证账户名称
        if(StringUtils.isEmpty(saveRequest.getAccountName())){
            throw new SbcRuntimeException("K-000009");
        }
        //验证员工名称
        if(StringUtils.isEmpty(saveRequest.getEmployeeName())){
            throw new SbcRuntimeException("K-000009");
        }
        //验证手机
        if(StringUtils.isEmpty(saveRequest.getEmployeeMobile())){
            throw new SbcRuntimeException("K-000009");
        }

        //验证员工账号密码
        if(StringUtils.isEmpty(saveRequest.getAccountPassword())){
            throw new SbcRuntimeException("K-000009");
        }
        //验证是否是员工
        if(Objects.isNull(saveRequest.getIsEmployee())){
            throw new SbcRuntimeException("K-000009");
        }
    }
}
