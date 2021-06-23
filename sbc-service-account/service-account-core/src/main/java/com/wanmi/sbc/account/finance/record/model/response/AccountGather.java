package com.wanmi.sbc.account.finance.record.model.response;

import com.wanmi.sbc.account.bean.enums.PayWay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>对账汇总返回结构</p>
 * Created by of628-wenzhi on 2017-12-11-下午3:01.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountGather implements Serializable {
    private static final long serialVersionUID = 2739572246730725651L;

    /**
     * 支付方式
     */
    private PayWay payWay;

    /**
     * 汇总金额，格式："￥#,###.00"
     */
    private String sumAmount;

    /**
     * 百分比，格式："##.00%"
     */
    private String percentage;
}
