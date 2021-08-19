package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>分销员邀新信息分页查询请求参数</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerInviteInfoPageRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -411073898479582623L;
    /**
     * 批量查询-分销员标识UUIDList
     */
    @ApiModelProperty(value = "批量查询-分销员标识")
    private List<String> distributionIdList;

    /**
     * 分销员标识UUID
     */
    @ApiModelProperty(value = "分销员标识UUID")
    private String distributionId;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 已发放邀新奖励现金人数-至
     */
    @ApiModelProperty(value = "已发放邀新奖励现金人数-至")
    private Integer rewardCashCountEnd;

    /**
     * 达到上限未发放奖励现金人数-从
     */
    @ApiModelProperty(value = "达到上限未发放奖励现金人数-从")
    private Integer rewardCashLimitCountStart;

    /**
     * 达到上限未发放奖励现金有效邀新人数-从
     */
    @ApiModelProperty(value = "达到上限未发放奖励现金有效邀新人数-从")
    private Integer rewardCashAvailableLimitCountStart;

    /**
     * 已发放邀新奖励优惠券人数-至
     */
    @ApiModelProperty(value = "已发放邀新奖励优惠券人数-至")
    private Integer rewardCouponCountEnd;

    /**
     * 达到上限未发放奖励优惠券人数-从
     */
    @ApiModelProperty(value = "达到上限未发放奖励优惠券金人数-从")
    private Integer rewardCouponLimitCountStart;

    /**
     * 达到上限未发放奖励优惠券有效邀新人数-从
     */
    @ApiModelProperty(value = "达到上限未发放奖励优惠券金有效邀新人数-从")
    private Integer rewardCouponAvailableLimitCountStart;

}