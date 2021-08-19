package com.wanmi.sbc.customer.validator;


import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelEditRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 客户等级验证
 * Created by CHENLI on 2017/5/10.
 */
@Component
public class CustomerLevelValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerLevelEditRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerLevelEditRequest saveRequest = (CustomerLevelEditRequest) target;
        //验证客户等级名称
        if (StringUtils.isBlank(saveRequest.getCustomerLevelName())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }
}
