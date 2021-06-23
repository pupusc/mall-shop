package com.wanmi.sbc.goods.info.reponse;

import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.pointsgoods.model.root.PointsGoods;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.storegoodstab.model.root.GoodsTabRela;
import com.wanmi.sbc.goods.storegoodstab.model.root.StoreGoodsTab;
import lombok.Data;

import java.util.List;

/**
 * 商品编辑视图响应
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class GoodsEditResponse {

    /**
     * 商品信息
     */
    private Goods goods;

    /**
     * 商品相关图片
     */
    private List<GoodsImage> images;

    /**
     * 商品属性列表
     */
    private List<GoodsPropDetailRel> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    private List<GoodsSpec> goodsSpecs;

    /**
     * 商品规格值列表
     */
    private List<GoodsSpecDetail> goodsSpecDetails;

    /**
     * 商品SKU列表
     */
    private List<GoodsInfo> goodsInfos;

    /**
     * 积分商品列表
     */
    private List<PointsGoods> pointsGoodsList;

    /**
     * 商品等级价格列表
     */
    private List<GoodsLevelPrice> goodsLevelPrices;

    /**
     * 商品客户价格列表
     */
    private List<GoodsCustomerPrice> goodsCustomerPrices;

    /**
     * 商品订货区间价格列表
     */
    private List<GoodsIntervalPrice> goodsIntervalPrices;

    /**
     * 商品详情模板关联
     */
    private List<GoodsTabRela> goodsTabRelas;

    /**
     * 商品模板配置
     */
    private List<StoreGoodsTab> storeGoodsTabs;

}
