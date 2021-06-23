package com.wanmi.sbc.order.trade.service.verify;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 校验商品是否删除
 * Created by jinwei on 23/3/2017.
 */
@Data
@AllArgsConstructor
public class DropVerify extends Verify {

    private String id;

    private String name;

    private Integer disable;

    @Override
    boolean verify() {
        if (new Integer(1).equals(this.disable)) {
            return true;
        }
        return false;
    }

    @Override
    void errorMessage() {
        throw new SbcRuntimeException("K-020001", new Object[]{this.name, this.id});
    }
}
