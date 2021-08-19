package com.wanmi.sbc.elastic.api.response.spu;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品索引SPU查询结果
 * Created by daiyitian on 2017/3/24.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsSpuPageResponse {

    /**
     * 商品分页数据
     */
    @ApiModelProperty(value = "商品分页数据")
    private MicroServicePage<GoodsPageSimpleVO> goodsPage;

    /**
     * 商品SKU全部数据
     */
    @ApiModelProperty(value = "商品SKU全部数据")
    private List<GoodsInfoForGoodsSimpleVO> goodsInfoList;

    /**
     * 商品品牌所有数据
     */
    @ApiModelProperty(value = "商品品牌所有数据")
    private List<GoodsBrandSimpleVO> goodsBrandList;

    /**
     * 商品分类所有数据
     */
    @ApiModelProperty(value = "商品分类所有数据")
    private List<GoodsCateSimpleVO> goodsCateList;

    /**
     * 存放导入商品库的商品
     */
    @ApiModelProperty(value = "存放导入商品库的商品")
    private List<String> importStandard = new ArrayList<>();
}
