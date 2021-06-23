package com.wanmi.sbc.account.finance.record.model.response;

import com.wanmi.sbc.account.finance.record.model.entity.Settlement;
import lombok.Data;

import java.io.Serializable;

@Data
public class SettlementIds implements Serializable {

    private static final long serialVersionUID = 76699713189263537L;

    /**
     *结算单号
     *
     **/
    private Long settleId;
}
