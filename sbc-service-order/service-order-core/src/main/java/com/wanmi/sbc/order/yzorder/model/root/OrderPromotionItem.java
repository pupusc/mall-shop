package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 商品级优惠明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPromotionItem implements Serializable {

    private static final long serialVersionUID = -2907818660654969996L;

    /**
     * 规格id，有赞生成的商品规格唯一id
     */
    private Integer sku_id;

    /**
     * 是否是赠品，false：不是，true：是
     */
    private Boolean is_present;

    /**
     * 交易明细id
     */
    private String oid;

    /**
     * 商品id，有赞生成的商品唯一id
     */
    private Integer item_id;

    /**
     * 商品级优惠明细
     */
    private List<Promotion> promotions;


}
