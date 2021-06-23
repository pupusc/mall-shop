package com.wanmi.sbc.customer.configuration.support;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <p>全局参数Validator</p>
 * Created by of628-wenzhi on 2017-07-18-下午4:12.
 */
@Component
@Slf4j
public class GlobalValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerBaseRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerBaseRequest customerBaseRequest = (CustomerBaseRequest) target;
        if (errors.hasErrors()) {
            return;
        }

        try {
            customerBaseRequest.checkParam();
        } catch (Exception ex) {
            log.error("Parameter verification failure:the params:{}", customerBaseRequest.toString());
            if (ex instanceof SbcRuntimeException) {
                throw ex;
            } else {
                log.error("error message: {}", ex.getMessage());
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
    }
}
