package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 包裹中的商品列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistItem implements Serializable {

    private static final long serialVersionUID = 5259215729713563579L;

    /**
     * 交易明细id
     */
    private String oid;
}
