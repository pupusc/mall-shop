package com.wanmi.sbc.goods.bean.wx.request;

import lombok.Data;

@Data
public class WxGoodsCreateRequest {

    private Long id;

    /**
     * spu id
     */
    private String goodsId;

    //微信类目id
    private String wxCategory;
}
