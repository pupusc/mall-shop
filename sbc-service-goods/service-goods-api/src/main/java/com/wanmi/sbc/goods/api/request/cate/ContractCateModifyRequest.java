package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>修改签约分类请求类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateModifyRequest implements Serializable {

    private static final long serialVersionUID = -1660971239685901499L;


    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @NotNull
    private Long contractCateId;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    @NotNull
    private Long storeId;

    /**
     * 商品分类标识
     */
    @ApiModelProperty(value = "商品分类标识")
    @NotNull
    private Long cateId;

    /**
     * 分类扣率
     */
    @ApiModelProperty(value = "分类扣率")
    @NotNull
    private BigDecimal cateRate;

    /**
     * 资质图片路径
     */
    @ApiModelProperty(value = "资质图片路径")
    private String qualificationPics;
}
