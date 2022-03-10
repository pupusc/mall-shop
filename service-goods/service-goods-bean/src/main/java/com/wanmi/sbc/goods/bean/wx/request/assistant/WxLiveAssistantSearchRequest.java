package com.wanmi.sbc.goods.bean.wx.request.assistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxLiveAssistantSearchRequest {

    /**
     * 直播计划id
     */
    private Long liveAssistantId;
    /**
     * 直播计划名称
     */
    private String assistantName;
    /**
     * spu id
     */
    private List<String> goodsIdIn;
    /**
     * 商品名称
     */
    private String goodsName;

    private Integer pageNum = 0;
    private Integer pageSize = 10;
}
