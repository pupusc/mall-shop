package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponCateSortVO implements Serializable {

    private static final long serialVersionUID = 6873173856040941768L;

    /**
     * 优惠券分类Id
     */
    @ApiModelProperty(value = "优惠券分类Id")
    private String couponCateId;

    /**
     * 优惠券排序顺序
     */
    @ApiModelProperty(value = "优惠券分类排序顺序")
    private Integer cateSort;

}
