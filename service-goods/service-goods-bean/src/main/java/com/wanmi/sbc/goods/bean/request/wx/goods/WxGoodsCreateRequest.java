package com.wanmi.sbc.goods.bean.request.wx.goods;

import lombok.Data;

@Data
public class WxGoodsCreateRequest {

    private Long id;

    private String goodsId;

    private String goodsInfoId;

    //微信类目id
    private Integer wxCategory;
}
