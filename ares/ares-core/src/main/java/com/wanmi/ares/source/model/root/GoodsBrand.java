package com.wanmi.ares.source.model.root;

import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.*;

/**
 * 商品品牌基础信息
 * Created by sunkun on 2017/9/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class GoodsBrand extends BaseData {


    private static final long serialVersionUID = 5844395279178543509L;
    /**
     * 品牌名称
     */
    private String name;

    /**
     * 商家id
     */
    private String companyId;

}
