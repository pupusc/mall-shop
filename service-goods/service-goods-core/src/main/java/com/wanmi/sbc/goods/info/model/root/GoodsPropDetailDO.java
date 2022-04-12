package com.wanmi.sbc.goods.info.model.root;

import lombok.Data;

@Data
public class GoodsPropDetailDO {

    /**
     * 编号
     */
    private Long relId;

    /**
     * SPU标识
     */
    private String goodsId;

    /**
     *属性id
     */
    private Long propId;

    /**
     *属性值（文本框输入）
     */
    private String propValue;


    private Integer propType;

    private String propName;
}
