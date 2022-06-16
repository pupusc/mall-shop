package com.soybean.mall.cart.vo;

import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-15 17:08:00
 */
@Data
public class CartInfoResVO$Sku {
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
     * 购买量
     */
    private Long buyCount = 0L;
    /**
     * 规格ids
     */
    private List<Spec> specs;
    /**
     * 前端是否选中
     */
    private Boolean checked = false;
    /**
     * 商品状态 0：正常 1：缺货 2：失效
     */
    private GoodsStatus goodsStatus = GoodsStatus.OK;

    /**
     * 活动
     */
    private List<Marketing> marketings;

    @Data
    private static class Spec {
        /**
         * 规格id
         */
        private Long id;
        /**
         * 展示文本
         */
        private String text;
        /**
         * 是否选中
         */
        private boolean checked;
    }

    @Data
    private static class Marketing {
        /**
         * 营销Id
         */
        private Long marketingId;
        /**
         * 营销名称
         */
        private String marketingName;

        /**
         * 营销类型 0：满减 1:满折 2:满赠
         */
        private MarketingType marketingType;

        /**
         * 营销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠
         */
        private MarketingSubType subType;
    }
}
