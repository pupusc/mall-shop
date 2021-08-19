package com.wanmi.sbc.configuration.support;


import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
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
        return BaseRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BaseRequest baseRequest = (BaseRequest) target;
        if (errors.hasErrors()) {
            return;
        }

        try {
            baseRequest.checkParam();
        } catch (Exception ex) {
            log.error("Parameter verification failure:the params:{}", baseRequest.toString());
            if (ex instanceof SbcRuntimeException) {
                throw ex;
            } else {
                log.error("error message: {}", ex.getMessage());
                throw new SbcRuntimeException("K-000009");
            }
        }
    }
}
