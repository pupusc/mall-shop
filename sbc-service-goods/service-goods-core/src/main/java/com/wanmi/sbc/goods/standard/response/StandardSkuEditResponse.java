package com.wanmi.sbc.goods.standard.response;

import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpec;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品库SKU编辑视图响应
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class StandardSkuEditResponse {

    /**
     * 商品SKU信息
     */
    private StandardSku goodsInfo;

    /**
     * 相关商品SPU信息
     */
    private StandardGoods goods;

    /**
     * 商品规格列表
     */
    private List<StandardSpec> goodsSpecs = new ArrayList<>();

    /**
     * 商品规格值列表
     */
    private List<StandardSpecDetail> goodsSpecDetails = new ArrayList<>();

    /**
     * 商品相关图片
     */
    private List<StandardImage> images = new ArrayList<>();

}
