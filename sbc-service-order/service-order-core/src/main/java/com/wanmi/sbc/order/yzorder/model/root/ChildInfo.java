package com.wanmi.sbc.order.yzorder.model.root;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 送礼订单子单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildInfo implements Serializable {

    private static final long serialVersionUID = -5478688213387315425L;

    /**
     * 子单详情
     */
    private List<ChildOrder> child_orders;

    /**
     * 送礼编号
     */
    private String gift_no;

    /**
     * 送礼标记
     */
    private String gift_sign;
}
