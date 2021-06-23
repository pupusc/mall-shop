package com.wanmi.sbc.goods.storecate.request;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: bail
 * Time: 2017/11/13.10:22
 */
@Data
public class StoreCateSaveRequest implements Serializable {

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
     * 分类图片
     */
    private String cateImg;

    /**
     * 分类路径
     */
    private String catePath;

    /**
     * 分类层次
     */
    private Integer cateGrade;

    /**
     * 删除标记
     */
    private DeleteFlag delFlag;

    /**
     * 默认标记
     */
    private DefaultFlag isDefault;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 排序的集合
     */
    private List<StoreCate> storeCateList = new ArrayList<>();

}
