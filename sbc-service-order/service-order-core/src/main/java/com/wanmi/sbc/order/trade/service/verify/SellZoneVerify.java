package com.wanmi.sbc.order.trade.service.verify;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 区域销售校验
 * Created by jinwei on 24/3/2017.
 */
@Data
@AllArgsConstructor
public class SellZoneVerify extends Verify{

    private String adminId;

    private String provinceCode;

    private String cityCode;

    @Override
    boolean verify() {
        return false;
    }

    @Override
    void errorMessage() {

    }
}
