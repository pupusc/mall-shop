package com.wanmi.sbc.account.finance.record.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>退款明细查询返回参数结构</p>
 * Created by of628-wenzhi on 2017-12-08-下午5:14.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class RefundDetails extends AccountDetails {

    private static final long serialVersionUID = -6244465126897721768L;

    /**
     * 退单号
     */
    private String returnOrderCode;
}
