package com.wanmi.sbc.order.trade.service.verify;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 库存校验
 * Created by jinwei on 24/3/2017.
 */
@Data
@AllArgsConstructor
public class StockVerify extends Verify {

    private String id;

    private String name;

    /**
     * 负库存标记 是否设置负库存 1是  0否
     */
    private Integer negativeFlag;

    private Integer stock;

    private Integer num;

    @Override
    boolean verify() {
        if (negativeFlag == 1) {
            return true;
        } else if (stock >= num) {
            return true;
        }
        return false;
    }

    @Override
    void errorMessage() {
        throw new SbcRuntimeException("K-020005", new Object[]{this.name, this.id});
    }
}
