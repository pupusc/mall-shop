package com.wanmi.sbc.order.settlement.model.root;

import com.wanmi.sbc.order.settlement.model.value.SettleGood;
import com.wanmi.sbc.order.settlement.model.value.SettleTrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hht on 2017/12/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementDetail {

    /**
     * 主键
     */
    private String id;

    /**
     * 结算明细Id
     */
    private String settleUuid;

    /**
     * 账期开始时间
     */
    private String startTime;

    /**
     * 账期结束时间
     */
    private String endTime;

    /**
     * 店铺Id
     */
    private Long storeId;

    /**
     * 是否特价
     */
    private boolean isSpecial;

    /**
     * 订单信息
     */
    private SettleTrade settleTrade;

    /**
     * 订单商品信息
     */
    private List<SettleGood> settleGoodList;

    /**
     * 订单和退单是否属于同一个账期
     */
    private boolean tradeAndReturnInSameSettle;

}
