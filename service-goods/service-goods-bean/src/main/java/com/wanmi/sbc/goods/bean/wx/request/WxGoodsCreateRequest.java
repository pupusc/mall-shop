package com.wanmi.sbc.goods.bean.wx.request;

import lombok.Data;

import java.util.Map;

@Data
public class WxGoodsCreateRequest {

    private Long id;

    /**
     * spu id
     */
    private String goodsId;

    //微信类目id
    private Map<String, String> wxCategory;
}
