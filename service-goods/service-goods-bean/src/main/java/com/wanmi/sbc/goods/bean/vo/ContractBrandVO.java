package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 签约品牌VO
 * Created by sunkun on 2017/10/31.
 */
@ApiModel
@Data
public class ContractBrandVO implements Serializable {

    private static final long serialVersionUID = -1889721118970868784L;

    /**
     * 签约品牌分类
     */
    @ApiModelProperty(value = "签约品牌分类")
    private Long contractBrandId;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 商品品牌
     */
    @ApiModelProperty(value = "商品品牌")
    private GoodsBrandVO goodsBrand;

    /**
     * 待审核品牌
     */
    @ApiModelProperty(value = "待审核品牌")
    private CheckBrandVO checkBrand;

    /**
     * 授权图片路径
     */
    @ApiModelProperty(value = "授权图片路径")
    private String authorizePic;
}
