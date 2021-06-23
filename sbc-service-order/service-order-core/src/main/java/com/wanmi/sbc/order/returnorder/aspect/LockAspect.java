package com.wanmi.sbc.order.returnorder.aspect;

import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 执行trade加锁解锁操作
 */
@Aspect
@Component
public class LockAspect {
    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("execution(* com.wanmi.sbc.order.returnorder.fsm.ReturnFSMService.*(..))" +
            " && @annotation(com.wanmi.sbc.order.returnorder.aspect.LockUnlock)")
    public void pointcutLock() {
    }

    @Around("pointcutLock()")
    public boolean triggerLockUnlock(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            return false;
        }

        String tid = "";
        if (args[0] instanceof ReturnStateRequest) {
            tid = ((ReturnStateRequest) args[0]).getRid();
        }

        if (!tid.isEmpty()) {
            //加锁
            RLock rLock = redissonClient.getFairLock(tid);
            rLock.lock();
            //执行
            try {
                joinPoint.proceed();
            } finally {
                //解锁
                rLock.unlock();
            }

        }
        return true;
    }
}
