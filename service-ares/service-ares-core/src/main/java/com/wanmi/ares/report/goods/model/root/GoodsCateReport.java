package com.wanmi.ares.report.goods.model.root;

import lombok.Data;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-9-6
 * \* Time: 15:19
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class GoodsCateReport extends GoodsReport {

    /**
     * 商品分类名称
     */
    private String cateName;

    /**
     * 上级分类id
     */
    private String cateParentId;

    /**
     * 上级分类名称
     */
    private String cateParentName;

}
