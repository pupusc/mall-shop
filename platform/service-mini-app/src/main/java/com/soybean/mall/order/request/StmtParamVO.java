package com.soybean.mall.order.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-20 13:58:00
 */
@Data
public class StmtParamVO {
    /**
     * 订单营销信息快照
     */
    @NotEmpty
    private List<SettlementParamVO$Marketing> marketings;

//    /**
//     * 商品信息，必传
//     */
//    @NotEmpty
//    private List<TradeItemRequest> tradeItems;

//    /**
//     * 是否强制确认，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
//     */
//    public boolean forceConfirm;
//    /**
//     * 收货地址区的地址码（切换地址）
//     */
//    public String areaId;
}
