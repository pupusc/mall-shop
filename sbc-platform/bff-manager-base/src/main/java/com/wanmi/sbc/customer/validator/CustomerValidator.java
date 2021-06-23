package com.wanmi.sbc.customer.validator;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.request.CustomerEditRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * 新增会员验证
 * Created by CHENLI on 2017/5/10.
 */
@Component
public class CustomerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerEditRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerEditRequest saveRequest = (CustomerEditRequest)target;

        //验证客户账户
        if(StringUtils.isEmpty(saveRequest.getCustomerAccount())){
            throw new SbcRuntimeException("K-000009");
        }
        //验证客户名称
        if(StringUtils.isEmpty(saveRequest.getCustomerName())){
            throw new SbcRuntimeException("K-000009");
        }
        //验证联系方式
        if(StringUtils.isEmpty(saveRequest.getContactPhone())){
            throw new SbcRuntimeException("K-000009");
        }
        //负责业务员
//        if(StringUtils.isEmpty(saveRequest.getEmployeeId())){
//            throw new SbcRuntimeException("K-000009");
//        }
    }
}
