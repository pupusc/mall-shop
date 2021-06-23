package com.wanmi.ares.resolver;

import com.wanmi.ares.base.BaseResponse;
import com.wanmi.ares.exception.AresRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.Locale;

/**
 * 异常统一处理
 * Created by of628-wenzhi on 22/9/2017.
 */
@ControllerAdvice
@Slf4j
public class AresRuntimeExceptionHandler {

    @Resource
    private MessageSource messageSource;

    private static final String LOGGER_FORMAT = "操作执行异常：异常编码{},异常信息：{},堆栈信息：{}";


    @ExceptionHandler(AresRuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse b2bRuntimeExceptionHandler(AresRuntimeException ex) {
        //1、异常链的errorMessage
        Throwable cause = ex.getCause();
        String msg = "";
        if (cause != null) {
            msg = cause.getMessage();
        }

        String errorCode = ex.getErrorCode();
        if (StringUtils.isNotEmpty(errorCode)) {
            //2、如果有异常码，以异常码对应的提示信息为准
            msg = this.getMessage(errorCode, ex.getParams());
        } else if (StringUtils.isEmpty(msg)) {
            //3、异常码为空 & msg为空，提示系统异常
            msg = this.getMessage("R-000001", ex.getParams());
        }
        log.error(LOGGER_FORMAT, ex.getErrorCode(), msg, ex);

        return new BaseResponse(errorCode, ex.getParams());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse validationExceptionHandle(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return new BaseResponse("R-000002");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse constraintViolationExceptionHandle(ConstraintViolationException ex) {
        final StringBuilder sb = new StringBuilder();
        ex.getConstraintViolations().forEach(
                i -> sb
                        .append(i.getRootBeanClass().getName())
                        .append(".")
                        .append(i.getPropertyPath())
                        .append(i.getMessage()).append("\r\n")
        );
        log.error("{}", sb);
        return new BaseResponse("R-000002");
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse illegalStateExceptionHandle(IllegalStateException ex) {
        log.error("{}", ex.getMessage());
        return new BaseResponse("R-000002");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse defaultExceptionHandler(Throwable ex) throws Exception {
        log.error(LOGGER_FORMAT, "", ex.getMessage(), ex);
        if (ex instanceof MethodArgumentNotValidException) {
            return new BaseResponse(CommonErrorCode.PARAMETER_ERROR);
        }
        return BaseResponse.FAILED();
    }

    /**
     * 获取错误码描述
     *
     * @param code
     * @return
     */
    private String getMessage(String code, Object[] params) {
        try {
            return messageSource.getMessage(code, params, Locale.CHINA);
        } catch (NoSuchMessageException e) {
            return code;
        }
    }

}
