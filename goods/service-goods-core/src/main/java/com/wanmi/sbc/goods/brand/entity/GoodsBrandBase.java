package com.wanmi.sbc.goods.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品品牌实体类
 * Created by dyt on 2017/4/11.
 */
@Data
public class GoodsBrandBase implements Serializable {

    /**
     * 品牌编号
     */
    private Long brandId;

    /**
     * 品牌名称
     */
    private String brandName;

    public GoodsBrandBase(Long brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }
}
