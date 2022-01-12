package com.wanmi.sbc.common.handler;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.*;
import java.util.Locale;
import java.util.Objects;

/**
 * 异常统一处理
 */
@ControllerAdvice
@Slf4j
public class SbcExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private static final String LOGGER_FORMAT = "操作执行异常：异常编码{},异常信息：{},堆栈信息：{}";

    @ExceptionHandler(SbcRuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse SbcRuntimeExceptionHandler(SbcRuntimeException ex) {
        //1、异常链的errorMessage
        String msg = "";
        if (ex.getCause() != null) {
            msg = ex.getCause().getMessage();
        }

        String errorCode = ex.getErrorCode();
        if (StringUtils.isNotEmpty(errorCode)) {
            if(CommonErrorCode.SPECIFIED.equals(errorCode) && ex.getParams() != null && ex.getParams().length>0){
                msg = Objects.toString(ex.getParams()[0]);
            }else {
                msg = this.getMessage(errorCode, ex.getParams());
            }

            // 如果异常码在本系统中有对应异常信息，以异常码对应的提示信息为准
            if (!errorCode.equals(msg)) {
                ex.setResult(msg);
            }

            if (StringUtils.isNotBlank(ex.getResult()) && !"fail".equals(ex.getResult())) {
                //log.error(LOGGER_FORMAT, ex.getErrorCode(), ex.getResult(), ex);
                log.info(LOGGER_FORMAT, ex.getErrorCode(), ex.getResult(), "--");
                return BaseResponse.info(errorCode, ex.getResult());
            }
        } else if (StringUtils.isEmpty(msg)) {
            //3、异常码为空 & msg为空，提示系统异常
            msg = this.getMessage(CommonErrorCode.FAILED, ex.getParams());
        }

        if(StringUtils.isEmpty(errorCode)) {
            errorCode = CommonErrorCode.FAILED;
        }
        log.error(LOGGER_FORMAT, errorCode, msg, ex);

        return new BaseResponse(errorCode, ex.getParams());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse validationExceptionHandle(MethodArgumentNotValidException ex) {
        log.warn(ex.getMessage());
        return new BaseResponse(CommonErrorCode.PARAMETER_ERROR);
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
        log.warn("{}", sb);
        return new BaseResponse(CommonErrorCode.PARAMETER_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse illegalStateExceptionHandle(IllegalStateException ex) {
        log.info("{}", ex.getMessage());
        return new BaseResponse(CommonErrorCode.PARAMETER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse defaultExceptionHandler(Throwable ex) throws Exception {
        log.error(LOGGER_FORMAT, "", ex.getMessage(), ex);
        if (ex.getCause() instanceof GenericJDBCException) {
            if (1366 == ((GenericJDBCException) ex.getCause()).getSQLException().getErrorCode()) {
                return new BaseResponse(CommonErrorCode.ILLEGAL_CHARACTER);
            }
        }
        if (ex instanceof MethodArgumentNotValidException) {
            return new BaseResponse(CommonErrorCode.PARAMETER_ERROR);
        }
        return BaseResponse.FAILED();
    }

    /**
     * jwt异常处理
     *
     * @param sx
     * @return
     */
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse jwtExceptionHandler(SignatureException sx, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msg = sx.getMessage();
        response.setStatus(200);
        /*response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type,x-requested-with");
        response.addHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Max-Age", "1800");
        response.addHeader("Allow", "Allow:GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        response.addHeader("Vary", "Origin");*/
        if ("Invalid jwtToken.".equals(msg) || "Expired jwtToken.".equals(msg) || "Missing jwtToken.".equals(msg)) {
            return new BaseResponse("K-000015");
        } else {
            return new BaseResponse(CommonErrorCode.FAILED);
        }
    }

    /**
     * 获取错误码描述
     *
     * @param code 错误码
     * @param params 输出替换参数
     * @return 错误信息
     */
    protected String getMessage(String code, Object[] params) {
        try {
            return messageSource.getMessage(code, params, Locale.CHINA);
        } catch (NoSuchMessageException e) {
            return code;
        }
    }
}
