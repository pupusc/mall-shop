package com.wanmi.sbc.marketing.api.request.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class CouponCateListLimitThreeByCateIdsRequest implements Serializable {

    private static final long serialVersionUID = 7184772921297004402L;

    @ApiModelProperty(value = "优惠券分类Id列表")
    @NotNull
    @Size(min = 1)
    private List<String> couponCateIds;
}
