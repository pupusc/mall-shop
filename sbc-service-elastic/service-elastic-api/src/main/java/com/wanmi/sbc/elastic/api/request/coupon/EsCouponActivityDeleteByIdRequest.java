package com.wanmi.sbc.elastic.api.request.coupon;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 删除优惠券活动
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsCouponActivityDeleteByIdRequest implements Serializable {

    private static final long serialVersionUID = -9162622109556746710L;

    /**
     * 活动ID
     */
    @ApiModelProperty(value = "活动ID")
    @NotBlank
    private String activityId;
}
