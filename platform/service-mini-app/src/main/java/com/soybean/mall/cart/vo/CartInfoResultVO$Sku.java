package com.soybean.mall.cart.vo;

import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-15 17:08:00
 */
@Data
public class CartInfoResultVO$Sku {
    /**
     * 商品编号
     */
    private String goodsId;
    /**
     * 商品编码
     */
    private String goodsNo;
    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    private Integer goodsType;
    /**
     * 商品SKU编号
     */
    private String goodsInfoId;
    /**
     * 商品SKU编码
     */
    private String goodsInfoNo;
    /**
     * 商品SKU名称
     */
    private String goodsInfoName;
    /**
     * 商品图片
     */
    private String goodsInfoImg;
    /**
     * 商品市场价
     */
    private BigDecimal marketPrice;
    /**
     * 最新计算的会员价
     * 为空，以市场价为准
     */
    private BigDecimal salePrice;
    /**
     * 购买量数量
     */
    private Long buyCount = 0L;
    /**
     * 最大数量
     */
    private Long maxCount = 0L;
    /**
     * 规格文案
      */
    private String specText;
    /**
     * 是否有更多规格
     */
    private boolean specMore;
    /**
     * 是否有更多促销
     */
    private List<Marketing> marketings = new ArrayList<>();
    /**
     * 前端是否选中
     */
    private boolean checked;
    /**
     * 商品状态 0：正常 1：缺货 2：失效
     */
    private GoodsStatus goodsStatus = GoodsStatus.OK;
    /**
     * vip折扣率
     */
    private String vipPriceRate;

    @Data
    public static class Marketing {
        /**
         * 营销id
         */
        private Long marketingId;
        /**
         * 营销名称
         */
        private String marketingName;
        /**
         * 营销描述
         */
        private String marketingDesc;
    }
}
