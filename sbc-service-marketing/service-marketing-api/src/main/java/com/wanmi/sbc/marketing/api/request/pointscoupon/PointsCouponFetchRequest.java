package com.wanmi.sbc.marketing.api.request.pointscoupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户领取积分优惠券请求结构
 *
 * @author minchen
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsCouponFetchRequest implements Serializable {

    private static final long serialVersionUID = -2064831726535984044L;
    /**
     * 领券用户Id
     */
    @ApiModelProperty(value = "领券用户Id")
    @NotBlank
    private String customerId;

    /**
     * 积分优惠券Id
     */
    @ApiModelProperty(value = "积分优惠券Id")
    @NotNull
    private Long pointsCouponId;

}
