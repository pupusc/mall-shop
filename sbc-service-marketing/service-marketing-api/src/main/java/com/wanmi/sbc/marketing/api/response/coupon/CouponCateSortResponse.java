package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CouponCateSortVO;
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
@AllArgsConstructor
@NoArgsConstructor
public class CouponCateSortResponse implements Serializable {

    private static final long serialVersionUID = 553982433974499855L;

    @ApiModelProperty(value = "优惠券分类排序列表")
    private List<CouponCateSortVO> list;
}
