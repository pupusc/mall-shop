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
public class DistributionCountCouponByRequestCustomerIdResponse implements Serializable {

    /**
     * 已邀新总人数
     */
    @ApiModelProperty(value = "已邀新总人数")
    private Long totalCount;

    /**
     * 有效邀新人数
     */
    @ApiModelProperty(value = "有效邀新人数")
    private Long validCount;

    /**
     * 奖励已入账邀新人数
     */
    @ApiModelProperty(value = "奖励已入账邀新人数")
    private Long recordedCount;
}
