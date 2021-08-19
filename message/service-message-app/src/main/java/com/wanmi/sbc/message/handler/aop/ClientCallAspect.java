package com.wanmi.sbc.message.handler.aop;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ClientCallAspect {
    @Pointcut("execution(* com.wanmi.sbc..*Provider.*(..))")
    public void pointcutLock() {}

    @AfterReturning(pointcut="pointcutLock()", returning = "res")
    public void after(JoinPoint joinPoint, Object res) throws SbcRuntimeException {
        String errCode = ((BaseResponse)res).getCode();
        String errMsg = ((BaseResponse)res).getMessage();

        if (!CommonErrorCode.SUCCESSFUL.equals(errCode)) {
            throw new SbcRuntimeException(errCode, errMsg);
        }
    }
}
