package com.wanmi.sbc.goods.bean.wx.request.assistant;

import lombok.Data;

import java.util.List;

@Data
public class WxLiveAssistantCreateRequest {

    private Long id;
    /**
     * 名称
     */
    private String theme;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * spu id
     */
    private List<String> goodsId;
}
