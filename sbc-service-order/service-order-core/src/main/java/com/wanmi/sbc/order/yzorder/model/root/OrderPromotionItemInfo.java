package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPromotionItemInfo implements Serializable {

    private static final long serialVersionUID = 7973287788652974026L;

    /**
     * 优惠金额，表示这个oid对应的商品，在该订单级优惠下享受的优惠；单位：元
     */
    private String discount_fee;

    /**
     * 交易明细id
     */
    private String oid;
}
