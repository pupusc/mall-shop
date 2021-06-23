package com.wanmi.sbc.order.trade.service.verify;

import lombok.Data;

/**
 * 校验抽象类
 * Created by jinwei on 23/3/2017.
 */
@Data
public abstract class Verify {

    private Verify next;

    /**
     * 调用verify
     * @return
     */
    public void handler(){
        if(!verify()){
            errorMessage();
        }else if(next != null){
            next.handler();
        }
    }

    /**
     * 校验逻辑
     * @return
     */
    abstract boolean verify();

    /**
     * 异常信息
     */
    abstract void errorMessage();
}
