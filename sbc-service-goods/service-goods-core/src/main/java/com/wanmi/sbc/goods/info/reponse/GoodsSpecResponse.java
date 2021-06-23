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
 * @discription 商品规格
 * @author yangzhen
 * @date 2020/9/3 11:14
 * @param
 * @return
 */
@Data
public class GoodsSpecResponse {


    /**
     * 商品规格列表
     */
    private List<GoodsSpec> goodsSpecs;

    /**
     * 商品规格值列表
     */
    private List<GoodsSpecDetail> goodsSpecDetails;



}
