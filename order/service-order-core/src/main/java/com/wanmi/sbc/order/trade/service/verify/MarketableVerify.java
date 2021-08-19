package com.wanmi.sbc.order.trade.service.verify;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 商品上下架校验
 * Created by jinwei on 24/3/2017.
 */
@Data
@AllArgsConstructor
public class MarketableVerify extends Verify {

    private String id;

    private String name;

    /**
     * 是否上架 0 否 1是
     */
    private Integer marketable;

    @Override
    boolean verify() {
        if (new Integer(1).equals(marketable)) {
            return true;
        }
        return false;
    }

    @Override
    void errorMessage() {
        throw new SbcRuntimeException("K-020002", new Object[]{this.name, this.id});
    }
}
