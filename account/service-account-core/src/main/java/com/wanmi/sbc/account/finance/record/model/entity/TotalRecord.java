package com.wanmi.sbc.account.finance.record.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <p>对账总计结构</p>
 * Created by of628-wenzhi on 2017-12-07-下午7:34.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalRecord {

    /**
     * 商家id
     */
    private Long supplierId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 总计
     */
    private BigDecimal totalAmount;


}
