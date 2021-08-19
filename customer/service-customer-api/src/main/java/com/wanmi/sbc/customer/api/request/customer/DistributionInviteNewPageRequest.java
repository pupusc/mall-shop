package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.bean.enums.FailReasonFlag;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.bean.enums.RewardFlag;
import com.wanmi.sbc.customer.bean.enums.RewardRecordedFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 邀请记录分页查询参数
 * Created by feitingting on 2019/2/21.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistributionInviteNewPageRequest extends BaseQueryRequest {

    /**
     * 批量导出-邀新记录表主键List
     */
    @ApiModelProperty(value = "邀新记录表主键List")
    private List<String> recordIdList;

    /**
     * 邀新记录表主键
     */
    @ApiModelProperty(value = "邀新记录表主键ID")
    private String recordId;

    /**
     * 受邀人ID
     */
    @ApiModelProperty(value = "受邀人ID")
    private String  invitedNewCustomerId;

    /**
     * 是否有效邀新
     */
    @ApiModelProperty(value = "是否有效邀新")
    private InvalidFlag availableDistribution;

    /**
     * 是否分销员 0：否 1：是
     */
    @ApiModelProperty(value = "是否分销员")
    private String isDistributor;

    /**
     * 邀请人ID
     */
    @ApiModelProperty(value = "邀请人ID")
    private String  requestCustomerId;

    /**
     * 注册时间
     */
    @ApiModelProperty(value = "注册时间")
    private String  registerTime;

    /**
     * 首次下单开始时间
     */
    @ApiModelProperty(value = "首次下单开始时间")
    private String firstOrderStartTime;

    /**
     * 首次下单结束时间
     */
    @ApiModelProperty(value = "首次下单结束时间")
    private String firstOrderEndTime;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    /**
     * 订单完成开始时间
     */
    @ApiModelProperty(value = "订单完成开始时间")
    private String orderFinishStartTime;

    /**
     * 订单完成结束时间
     */
    @ApiModelProperty(value = "订单完成结束时间")
    private String orderFinishEndTime;

    /**
     * 奖励是否入账 0:否 1:是
     */
    @ApiModelProperty(value = "奖励是否入账")
    private RewardRecordedFlag isRewardRecorded;

    /**
     * 奖励入账开始时间
     */
    @ApiModelProperty(value = "奖励入账开始时间")
    private String rewardRecordedStartTime;

    /**
     * 奖励入账结束时间
     */
    @ApiModelProperty(value = "奖励入账结束时间")
    private String rewardRecordedEndTime;

    /**
     * 入账类型, 0:现金，1：优惠券
     */
    @ApiModelProperty(value = "入账类型, 0:现金，1：优惠券")
    private RewardFlag rewardFlag;

    /**
     * 未入账原因(0:非有效邀新，1：奖励达到上限)
     */
    @ApiModelProperty(value = "未入账原因, 0:非有效邀新，1：奖励达到上限")
    private FailReasonFlag failReasonFlag;
}
