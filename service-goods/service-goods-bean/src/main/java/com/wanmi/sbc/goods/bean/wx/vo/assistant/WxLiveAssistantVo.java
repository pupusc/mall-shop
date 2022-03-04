package com.wanmi.sbc.goods.bean.wx.vo.assistant;

import lombok.Data;

@Data
public class WxLiveAssistantVo {

    private Long id;

    /**
     * 名称
     */
    private String theme;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

}
