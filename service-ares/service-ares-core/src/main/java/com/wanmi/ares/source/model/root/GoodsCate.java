package com.wanmi.ares.source.model.root;

import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.*;

/**
 * 商品分类基础信息
 * Created by sunkun on 2017/9/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class GoodsCate extends BaseData {


    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类id
     */
    private Long parentId;

    /**
     * 所有上级名称字符串以"/"分割
     */
    private String parentNames;

    /**
     * 分类图片
     */
    private String img;

    /**
     * 分类层次
     */
    private Integer grade;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否默认
     */
    private boolean isDefault;

    /**
     * 商家id
     */
    private String companyId;

}
