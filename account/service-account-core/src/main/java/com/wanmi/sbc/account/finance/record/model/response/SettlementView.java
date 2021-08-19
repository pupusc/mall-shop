package com.wanmi.sbc.account.finance.record.model.response;

import com.wanmi.sbc.account.finance.record.model.entity.Settlement;
import lombok.Data;

import java.io.Serializable;

@Data
public class SettlementView extends Settlement implements Serializable {

    private static final long serialVersionUID = 76699713189263537L;

    /**
     * 商铺名称
     */
    private String storeName;

    /**
     * 结算单号
     */
    private String settlementCode;

    /**
     * 商家编码
     */
    private String companyCode;

}
