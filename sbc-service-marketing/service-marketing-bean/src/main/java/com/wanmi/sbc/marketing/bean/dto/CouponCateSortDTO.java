package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponCateSortDTO implements Serializable {

    private static final long serialVersionUID = 7313451509296963254L;

    /**
     * 优惠券分类Id
     */
    @ApiModelProperty(value = "优惠券分类Id")
    @NotBlank
    private String couponCateId;

    /**
     * 优惠券排序顺序
     */
    @ApiModelProperty(value = "优惠券排序顺序")
    @NotNull
    private Integer cateSort;
}
