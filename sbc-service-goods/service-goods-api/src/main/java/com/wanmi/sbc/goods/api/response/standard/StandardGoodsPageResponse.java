package com.wanmi.sbc.goods.api.response.standard;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
public class StandardGoodsPageResponse implements Serializable {

    private static final long serialVersionUID = -3802536433354214574L;

    /**
     * 商品分页数据
     */
    @ApiModelProperty(value = "商品分页数据")
    private MicroServicePage<StandardGoodsVO> standardGoodsPage ;

    /**
     * 商品SKU全部数据
     */
    @ApiModelProperty(value = "商品SKU全部数据")
    private List<StandardSkuVO> standardSkuList = new ArrayList<>();

    /**
     * 商品SKU的规格值全部数据
     */
    @ApiModelProperty(value = "商品SKU的规格值全部数据")
    private List<StandardSkuSpecDetailRelVO> standardSkuSpecDetails = new ArrayList<>();

    /**
     * 商品品牌所有数据
     */
    @ApiModelProperty(value = "商品品牌所有数据")
    private List<GoodsBrandVO> goodsBrandList = new ArrayList<>();

    /**
     * 商品分类所有数据
     */
    @ApiModelProperty(value = "商品分类所有数据")
    private List<GoodsCateVO> goodsCateList = new ArrayList<>();

    /**
     * 已被导入的商品库ID
     */
    @ApiModelProperty(value = "已被导入的商品库ID")
    private List<String> usedStandard = new ArrayList<>();

    /**
     * 需要同步的商品库ID
     */
    @ApiModelProperty(value = "需要同步的商品库ID")
    private List<String> needSynStandard = new ArrayList<>();

}
