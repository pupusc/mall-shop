package com.wanmi.sbc.account.finance.record.model.request;

import com.wanmi.sbc.account.api.request.finance.record.BasePageRequest;
import com.wanmi.sbc.account.bean.enums.SettleStatus;
import lombok.Data;

import java.util.List;

/**
 * 结算单批量修改状态实体
 */
@Data
public class SettlementStatusChangeRequest extends BasePageRequest {

    private List<Long> settleIdList;

    private SettleStatus status;

}
