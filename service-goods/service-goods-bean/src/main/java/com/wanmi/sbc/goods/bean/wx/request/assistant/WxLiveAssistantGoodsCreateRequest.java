package com.wanmi.sbc.goods.bean.wx.request.assistant;

import lombok.Data;

import java.util.List;

@Data
public class WxLiveAssistantGoodsCreateRequest {

    /**
     * 直播计划id
     */
    private Long assistantId;
    /**
     * spu id
     */
    private List<String> goods;
}
