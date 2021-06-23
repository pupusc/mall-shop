package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 签约分类更新请求
 * Created by sunkun on 2017/10/31.
 */
@ApiModel
@Data
public class ContractCateSaveDTO implements Serializable {

    private static final long serialVersionUID = 3313179843927882868L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long contractCateId;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 商品分类标识
     */
    @ApiModelProperty(value = "商品分类标识")
    private Long cateId;

    /**
     * 分类扣率
     */
    @ApiModelProperty(value = "分类扣率")
    private BigDecimal cateRate;

    /**
     * 资质图片路径
     */
    @ApiModelProperty(value = "资质图片路径")
    private String qualificationPics;
}
