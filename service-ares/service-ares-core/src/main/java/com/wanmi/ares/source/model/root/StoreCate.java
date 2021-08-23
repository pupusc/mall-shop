package com.wanmi.ares.source.model.root;

import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.*;

/**
 * 店铺分类
 * Created by hht on 2018/01/10.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class StoreCate extends BaseData{

    private static final long serialVersionUID = -4118186231736214026L;

    /**
     * 店铺Id
     */
    private String storeId;

    /**
     * 商家Id（冗余字段）
     */
    private String companyInfoId;

    /**
     * 分类名称
     */
    private String cateName;

    /**
     * 父分类ID
     */
    private String cateParentId;

    /**
     * 所有上级名称字符串以"/"分割
     */
    private String parentNames;

    /**
     * 分类层次路径,例1|01|001
     */
    private String catePath;

    /**
     * 分类层级
     */
    private Integer cateGrade;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否默认,0:否1:是
     */
    private boolean isDefault;

}
