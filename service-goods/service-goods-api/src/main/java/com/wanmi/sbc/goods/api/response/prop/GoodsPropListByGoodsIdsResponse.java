package com.wanmi.sbc.goods.api.response.prop;

import lombok.Data;

import java.io.*;

@Data
public class GoodsPropListByGoodsIdsResponse implements Serializable {
    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 引导文案
     */
    private String guideText;

    /**
     * 引导图片
     */
    private String guideImg;
}
