package com.wanmi.sbc.goods.info.reponse;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品SKU编辑视图响应
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class GoodsInfoDetailResponse {

    /**
     * 商品SKU信息
     */
    private GoodsInfo goodsInfo;

    /**
     * 相关商品SPU信息
     */
    private Goods goods;

    /**
     * 商品属性列表
     */
    private List<GoodsPropDetailRel> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    private List<GoodsSpec> goodsSpecs = new ArrayList<>();

    /**
     * 商品规格值列表
     */
    private List<GoodsSpecDetail> goodsSpecDetails = new ArrayList<>();

    /**
     * 商品等级价格列表
     */
    private List<GoodsLevelPrice> goodsLevelPrices = new ArrayList<>();

    /**
     * 商品客户价格列表
     */
    private List<GoodsCustomerPrice> goodsCustomerPrices = new ArrayList<>();

    /**
     * 商品订货区间价格列表
     */
    private List<GoodsIntervalPrice> goodsIntervalPrices = new ArrayList<>();

    /**
     * 商品相关图片
     */
    private List<GoodsImage> images = new ArrayList<>();

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺logo
     */
    private String storeLogo;

    /**
     * 商家类型
     */
    private BoolFlag companyType;

    /**
     * 店铺Id
     */
    private Long storeId;

}
