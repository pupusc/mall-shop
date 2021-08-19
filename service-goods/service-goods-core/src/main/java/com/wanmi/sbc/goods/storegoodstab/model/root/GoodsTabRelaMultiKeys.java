package com.wanmi.sbc.goods.storegoodstab.model.root;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-10-17
 */
@Data
@NoArgsConstructor
public class GoodsTabRelaMultiKeys implements Serializable {

    private static final long serialVersionUID = 6478844229594792464L;

    private String goodsId;

    private Long tabId;
}
