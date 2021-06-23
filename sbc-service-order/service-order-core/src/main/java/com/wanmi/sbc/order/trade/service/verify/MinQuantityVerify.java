package com.wanmi.sbc.order.trade.service.verify;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 最小起订量校验
 * Created by jinwei on 24/3/2017.
 */
@Data
@AllArgsConstructor
public class MinQuantityVerify extends Verify {

    private String id;

    private String name;

    private Integer minQuantity;

    private Integer num;

    @Override
    boolean verify() {
        if (num >= minQuantity) {
            return true;
        }
        return false;
    }

    @Override
    void errorMessage() {
        throw new SbcRuntimeException("K-020004", new Object[]{this.name, this.id});
    }
}
