package com.wanmi.sbc.goods.price.model.root;


import com.wanmi.sbc.goods.bean.enums.PriceType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 商品客户价格实体
 * Created by dyt on 2017/4/17.
 */
@Data
@Entity
@Table(name = "goods_customer_price")
public class GoodsCustomerPrice {

    /**
     * 客户价格ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_price_id")
    private Long customerPriceId;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 客户ID
     */
    @Column(name = "customer_id")
    private String customerId;

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
