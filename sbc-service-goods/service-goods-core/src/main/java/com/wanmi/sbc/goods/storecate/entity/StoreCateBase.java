package com.wanmi.sbc.goods.storecate.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品分类实体类
 * Created by bail on 2017/11/13.
 */
@Data
public class StoreCateBase implements Serializable {

    /**
     * 店铺分类标识
     */
    private Long storeCateId;

    /**
     * 店铺分类名称
     */
    private String cateName;

    /**
     * 父分类标识
     */
    private Long cateParentId;

    public StoreCateBase(Long storeCateId, String cateName, Long cateParentId) {
        this.storeCateId = storeCateId;
        this.cateName = cateName;
        this.cateParentId = cateParentId;
    }
}

