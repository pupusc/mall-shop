package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>签约分类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateVO implements Serializable {

    private static final long serialVersionUID = 8743030864017516584L;

    /**
     * 签约分类主键
     */
    @ApiModelProperty(value = "签约分类主键")
    private Long contractCateId;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 平台类目id
     */
    @ApiModelProperty(value = "平台类目id")
    private Long cateId;

    /**
     * 平台类目名称
     */
    @ApiModelProperty(value = "平台类目名称")
    private String cateName;

    /**
     * 上级平台类目名称(一级/二级)
     */
    @ApiModelProperty(value = "上级平台类目名称(一级/二级)")
    private String parentGoodCateNames;

    /**
     * 分类扣率
     */
    @ApiModelProperty(value = "分类扣率")
    private BigDecimal cateRate;

    /**
     * 平台扣率
     */
    @ApiModelProperty(value = "平台扣率")
    private BigDecimal platformCateRate;

    /**
     * 资质图片路径
     */
    @ApiModelProperty(value = "资质图片路径")
    private String qualificationPics;

    /**
     * 平台分类
     */
    @ApiModelProperty(value = "平台分类")
    private GoodsCateVO goodsCate;
}
