package com.wanmi.sbc.customer.api.response.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 邀新人数统计返回体
 * @Autho qiaokang
 * @Date：2019-03-07 19:10:58
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCountByRequestCustomerIdResponse implements Serializable {

    /**
     * 已邀新总人数（奖金）
     */
    @ApiModelProperty(value = "已邀新总人数（奖金）")
    private Long totalCountOfCash;

    /**
     * 有效邀新人数（奖金）
     */
    @ApiModelProperty(value = "有效邀新人数（奖金）")
    private Long validCountOfCash;

    /**
     * 奖励已入账邀新人数
     */
    @ApiModelProperty(value = "奖励已入账邀新人数（奖金）")
    private Long recordedCountOfCash;

    /**
     * 已邀新总人数（优惠券）
     */
    @ApiModelProperty(value = "已邀新总人数（优惠券）")
    private Long totalCountOfCoupon;

    /**
     * 有效邀新人数（优惠券）
     */
    @ApiModelProperty(value = "有效邀新人数（优惠券）")
    private Long validCountOfCoupon;

    /**
     * 奖励已入账邀新人数（优惠券）
     */
    @ApiModelProperty(value = "奖励已入账邀新人数（优惠券）")
    private Long recordedCountOfCoupon;
}
