package com.wanmi.sbc.goods.mini.mq.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxLiveAssistantMessageData {

    /**
     * 0-开始直播 1-结束直播
     */
    private Integer type;
    /**
     * 时间
     */
    private String time;
    /**
     * 直播助手id
     */
    private Long assistantId;
}
