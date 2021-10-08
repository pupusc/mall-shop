package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.storegoodstab.model.root.GoodsTabRela;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 商品新增/编辑请求
 * Created by daiyitian on 2017/3/24.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSaveRequest extends BaseRequest {

    /**
     * 商品信息
     */
    private Goods goods;

    /**
     * 商品相关图片
     */
    private List<GoodsImage> images;

    @ApiModelProperty(value = "标签")
    private String tags;

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
     * 是否修改价格及订货量设置
     */
    private Integer isUpdatePrice;

    /**
     * 商品详情模板关联
     */
    private List<GoodsTabRela> goodsTabRelas;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updatePerson;

}
