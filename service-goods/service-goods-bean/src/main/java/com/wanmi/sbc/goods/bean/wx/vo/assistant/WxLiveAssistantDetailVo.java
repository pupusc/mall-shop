package com.wanmi.sbc.goods.bean.wx.vo.assistant;

import lombok.Data;

import java.util.List;

@Data
public class WxLiveAssistantDetailVo {

    /**
     * 直播助手商品id
     */
    private Long assistantId;
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
     * spu
     */
    private List<WxLiveAssistantGoodsVo> goods;

}
