package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CouponCateRelaVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponCateRelaListByCouponIdsResponse implements Serializable {

    private static final long serialVersionUID = -4177732385732686005L;

    @ApiModelProperty(value = "优惠券关联分类列表")
    private List<CouponCateRelaVO> cateRelaVOList;
}
