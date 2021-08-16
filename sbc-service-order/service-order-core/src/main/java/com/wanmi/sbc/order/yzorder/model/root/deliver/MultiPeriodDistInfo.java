package com.wanmi.sbc.order.yzorder.model.root.deliver;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 多期发货记录配送信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiPeriodDistInfo {

    /**
     * 是否允许修改物流
     */
    private Boolean allow_modify_express;

    /**
     * 配送单
     */
    private DistOrder dist_order;
}
