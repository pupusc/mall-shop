package com.wanmi.sbc.goods.cate.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品分类实体类
 * Created by dyt on 2017/4/11.
 */
@Data
public class GoodsCateBase implements Serializable {

    /**
     * 分类编号
     */
    private Long cateId;

    /**
     * 分类路径
     */
    private String catePath;

    /**
     * 分类名称
     */
    private String cateName;

    /**
     * 父类编号
     */
    private Long cateParentId;

    public GoodsCateBase(Long cateId, String catePath) {
        this.cateId = cateId;
        this.catePath = catePath;
    }

    public GoodsCateBase(Long cateId, String cateName, Long cateParentId) {
        this.cateId = cateId;
        this.cateName = cateName;
        this.cateParentId = cateParentId;
    }
}
