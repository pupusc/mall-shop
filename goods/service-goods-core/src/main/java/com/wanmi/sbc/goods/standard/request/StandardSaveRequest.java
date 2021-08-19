package com.wanmi.sbc.goods.standard.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardPropDetailRel;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpec;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品库新增/编辑请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class StandardSaveRequest extends BaseRequest {

    /**
     * 商品信息
     */
    @NotNull
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
    @NotNull
    private List<StandardSku> goodsInfos;

}
