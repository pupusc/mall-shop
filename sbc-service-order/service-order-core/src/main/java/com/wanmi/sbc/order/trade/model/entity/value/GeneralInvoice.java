package com.wanmi.sbc.order.trade.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 普通发票
 * Created by jinwei on 7/5/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralInvoice {

    /**
     * 0:个人 1:单位，必传
     */
    private Integer flag;

    /**
     * 抬头，单位发票必传
     */
    private String title;

    /**
     * 纸质发票单位纳税人识别码
     */
    private String identification;
}
