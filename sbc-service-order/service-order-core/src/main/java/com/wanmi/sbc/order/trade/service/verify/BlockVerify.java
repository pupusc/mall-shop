package com.wanmi.sbc.order.trade.service.verify;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 商品屏蔽校验
 * Created by jinwei on 24/3/2017.
 */
@Data
@AllArgsConstructor
public class BlockVerify extends Verify {

    private String id;

    private String name;

    private Integer block;

    @Override
    boolean verify() {
        if (new Integer(1).equals(block)) {
            return true;
        }
        return false;
    }

    @Override
    void errorMessage() {
        throw new SbcRuntimeException("K-020003", new Object[]{this.name, this.id});
    }
}
