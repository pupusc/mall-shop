package com.wanmi.sbc.goods.bean.wx.vo.assistant;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WxLiveAssistantGoodsVo {

    /**
     * 直播助手商品id
     */
    private Long assistantGoodsId;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 商品编号
     */
    private String goodsNo;
    /**
     * 上下架状态 0-下架 1-上架
     */
    private Integer addedFlag;
    /**
     * 商品名称
     */
    private String GoodsName;
    /**
     * 商品图片
     */
    private String GoodsImg;
    /**
     * 商品价格
     */
    private String marketPrice;
    /**
     * 商品库存
     */
    private Integer stock;
    /**
     * skus
     */
    private List<WxLiveAssistantGoodsInfoVo> goodsInfos;

    @Data
    public static class WxLiveAssistantGoodsInfoVo{

        /**
         * sku名称
         */
        private String goodsInfoName;
        /**
         * sku id
         */
        private String goodsInfoId;
        /**
         * sku no
         */
        private String goodsInfoNo;
        /**
         * sku价格
         */
        private String marketPrice;
        /**
         * sku库存
         */
        private Integer stock;
        /**
         * 商品规格
         */
        private Map<String, String> goodsInfoSpec;
    }

}
