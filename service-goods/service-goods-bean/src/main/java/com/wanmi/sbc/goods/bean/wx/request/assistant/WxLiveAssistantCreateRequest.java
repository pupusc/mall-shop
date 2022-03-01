package com.wanmi.sbc.goods.bean.wx.request.assistant;

import lombok.Data;

@Data
public class WxLiveAssistantCreateRequest {

    private Long id;
    private String theme;
    private String startTime;
    private String endTime;
}
