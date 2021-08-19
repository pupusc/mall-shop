package com.wanmi.sbc.configuration.support;


import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.SpringContextHolder;
import com.wanmi.sbc.sensitivewords.service.SensitiveWordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

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
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }

        //敏感词验证
        String sensitiveWord = baseRequest.checkSensitiveWord();
        if (StringUtils.isNotBlank(sensitiveWord)) {
            Object bean = SpringContextHolder.getBean("sensitiveWordService");
            if(bean != null){
                SensitiveWordService sensitiveWordService = (SensitiveWordService)bean;
                Set<String> res = sensitiveWordService.getBadWord(sensitiveWord);
                if (CollectionUtils.isNotEmpty(res)) {
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,
                            new Object[]{"包含敏感词[".concat(StringUtils.join(res,",")).concat("]")});
                }
            }
        }
    }
}
