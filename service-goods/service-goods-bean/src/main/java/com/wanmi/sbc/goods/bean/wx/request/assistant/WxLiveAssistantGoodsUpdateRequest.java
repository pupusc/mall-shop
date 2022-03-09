package com.wanmi.sbc.goods.bean.wx.request.assistant;

import lombok.Data;

import java.util.List;

@Data
public class WxLiveAssistantGoodsUpdateRequest {

    /**
     * 直播助手id
     */
    private Long assistantId;
    /**
     * 直播助手商品id
     */
    private Long assistantGoodsId;
    /**
     * skus
     */
    private List<WxLiveAssistantGoodsInfo> goodsInfos;

    @Data
    public static class WxLiveAssistantGoodsInfo{
        /**
         * sku id
         */
        private String goodsInfoId;
        /**
         * 价格
         */
        private String price;
        /**
         * 库存
         */
        private Integer stock;
    }
}
