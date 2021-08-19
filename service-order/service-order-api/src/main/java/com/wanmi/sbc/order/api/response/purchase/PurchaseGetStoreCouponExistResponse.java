package com.wanmi.sbc.order.api.response.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseGetStoreCouponExistResponse implements Serializable {

    private static final long serialVersionUID = -5799402283904395059L;

    @ApiModelProperty(value = "店铺是否有优惠券活动map ,key为店铺id，value为是否存在优惠券活动")
    private HashMap<Long, Boolean> map = new HashMap<>();
}
