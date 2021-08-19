package com.wanmi.sbc.goods.api.response.standard;

import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品库Sku获取响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
public class StandardSkuByIdResponse implements Serializable {
    private static final long serialVersionUID = -2137751302275895389L;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private StandardGoodsVO goods;

    /**
     * 商品SKU列表
     */
    @ApiModelProperty(value = "商品SKU列表")
    private StandardSkuVO goodsInfo;

    /**
     * 商品规格值列表
     */
    @ApiModelProperty(value = "商品规格值列表")
    private List<StandardSpecDetailVO> goodsSpecDetails;

    /**
     * 商品规格列表
     */
    @ApiModelProperty(value = "商品规格列表")
    private List<StandardSpecVO> goodsSpecs;

    /**
     * 商品相关图片
     */
    @ApiModelProperty(value = "商品相关图片")
    private List<StandardImageVO> images;
}
