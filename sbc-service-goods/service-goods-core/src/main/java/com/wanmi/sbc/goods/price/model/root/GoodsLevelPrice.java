package com.wanmi.sbc.goods.price.model.root;

import com.wanmi.sbc.goods.bean.enums.PriceType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 商品级别价格实体
 * Created by dyt on 2017/4/17.
 */
@Data
@Entity
@Table(name = "goods_level_price")
public class GoodsLevelPrice implements Serializable {

    /**
     * 级别价格ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_price_id")
    private Long levelPriceId;

    /**
     * 商品编号
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 等级ID
     */
    @Column(name = "level_id")
    private Long levelId;

    /**
     * 订货价
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 起订量
     */
    @Column(name = "count")
    private Long count;

    /**
     * 限订量
     */
    @Column(name = "max_count")
    private Long maxCount;

    /**
     * 商品ID
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 0:spu,1:sku,2:企业购sku
     */
    @Column(name = "type")
    @Enumerated
    private PriceType type;

    /**
     * 折扣 0-100
     */
    @Column(name = "discount")
    private Integer discount;

}
