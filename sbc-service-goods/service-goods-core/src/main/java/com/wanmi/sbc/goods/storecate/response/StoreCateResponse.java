package com.wanmi.sbc.goods.storecate.response;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Author: bail
 * Time: 2017/11/13.10:25
 */
@Data
public class StoreCateResponse implements Serializable {
    /**
     * 店铺分类标识
     */
    private Long storeCateId;

    /**
     * 店铺标识
     */
    private Long storeId;

    /**
     * 店铺分类名称
     */
    private String cateName;

    /**
     * 父分类标识
     */
    private Long cateParentId;

    /**
     * 分类层次
     */
    private Integer cateGrade;

    /**
     * 分类路径
     */
    private String catePath;

    /**
     * 删除标记
     */
    private DeleteFlag delFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 默认标记
     */
    private DefaultFlag isDefault;

    /**
     * 一对多关系，子分类
     */
    private List<StoreCate> storeCateList;
}
