package com.wanmi.sbc.goods.customer.model.root;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品客户全局购买数实体类
 * Created by dyt on 2017/5/17.
 */
@Data
@Entity
@Table(name = "goods_customer_num")
public class GoodsCustomerNum implements Serializable {

    /**
     * 图片编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 客户编号
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * SKU编号
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 全局购买数
     */
    @Column(name = "goods_num")
    private Long goodsNum;

}
