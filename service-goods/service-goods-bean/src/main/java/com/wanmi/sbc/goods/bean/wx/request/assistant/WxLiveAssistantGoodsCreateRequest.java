package com.wanmi.sbc.goods.bean.wx.request.assistant;

import lombok.Data;

import java.util.List;

@Data
public class WxLiveAssistantGoodsCreateRequest {

    private Long assistantId;
    private List<String> goods;
}
