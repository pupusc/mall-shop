package com.wanmi.sbc.goods.info.reponse;

import com.wanmi.sbc.goods.price.response.GoodsIntervalResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * sku商品返回
 * Created by zhangjin on 2017/5/17.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoChooseResponse implements Serializable {

    /**
     * skuId
     */
    private String skuId;

    /**
     * sku编码
     */
    private String skuNo;

    /**
     * 图片
     */
    private String skuImg;

    /**
     * 货品名称
     */
    private String skuName;

    /**
     * 会员价
     */
    private BigDecimal customerPrice;

    /**
     * 库存
     */
    private Long stock;

    /**
     * 规格名称规格值 颜色:红色;大小:16G
     */
    private String specText;

    /**
     * 最小起定量
     */
    private Long minCount;

    /**
     * 最大订货量
     */
    private Long maxCount;

    /**
     * 区间价
     */
    private List<GoodsIntervalResponse> goodsIntervalResponses;
}
