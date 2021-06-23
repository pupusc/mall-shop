package com.wanmi.sbc.order.trade.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 增值税/专用发票
 * Created by jinwei on 7/5/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecialInvoice {

    /**
     * 增值税发票id，新增与修改时必传
     */
    private Long id;

    /**
     * 以下信息无需传入
     **/
    private String companyName;

    private String companyNo;

    private String phoneNo;

    private String address;

    private String account;

    private String bank;

    /**
     * 纳税人识别号
     */
    private String identification;
}
