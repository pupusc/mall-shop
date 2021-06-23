package com.wanmi.sbc.goods.standard.response;

import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardPropDetailRel;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpec;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
import lombok.Data;

import java.util.List;

/**
 * 商品库编辑视图响应
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class StandardEditResponse {

    /**
     * 商品信息
     */
    private StandardGoods goods;

    /**
     * 商品相关图片
     */
    private List<StandardImage> images;

    /**
     * 商品属性列表
     */
    private List<StandardPropDetailRel> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    private List<StandardSpec> goodsSpecs;

    /**
     * 商品规格值列表
     */
    private List<StandardSpecDetail> goodsSpecDetails;

    /**
     * 商品SKU列表
     */
    private List<StandardSku> goodsInfos;

}
