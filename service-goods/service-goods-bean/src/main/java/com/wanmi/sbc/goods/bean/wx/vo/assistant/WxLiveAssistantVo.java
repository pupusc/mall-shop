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
     * 是否直播计划商品有效0 不同步 1同步
     */
    private Integer hasAssistantGoodsValid;

    /**
     * 是否直播计划的开始同步/结束同步按钮可以触发 0 不可以 1可以
     */
    private Integer hasAssistantTouch;


    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 结束时长,单位分
     */
    private Long duration;

    /**
     * 0-未开始 1-直播中 2-已结束
     */
    private Integer status;

}
