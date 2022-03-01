package com.wanmi.sbc.goods.bean.wx.request.assistant;

import lombok.Data;

@Data
public class WxLiveAssistantGoodsUpdateRequest {

    private String goodsInfoId;
    private String price;
    private Integer stock;
}
