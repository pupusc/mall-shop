package com.wanmi.sbc.goods.bean.wx.request.assistant;

import lombok.Data;

@Data
public class WxLiveAssistantSearchRequest {

    /**
     * 直播计划id
     */
    private Long liveAssistantId;

    private Integer pageNum = 0;
    private Integer pageSize = 10;
}
