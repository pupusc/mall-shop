package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.dto.DistributionRewardCouponDTO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

/**
 * 新增邀新记录（普通邀新）
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionInviteNewAddRegisterRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -7136373208496469361L;

    /**
     * 邀请人会员id
     */
    @ApiModelProperty(value = "邀请人会员id")
    @NotBlank
    private String requestCustomerId;

    /**
     * 受邀人会员id
     */
    @ApiModelProperty(value = "受邀人会员id")
    @NotBlank
    private String customerId;

    /**
     * 社销分销开关
     */
    @ApiModelProperty(value = "社销分销开关")
    private DefaultFlag openFlag;

    /**
     * 是否开启邀新奖励
     */
    @ApiModelProperty(value = "是否开启邀新奖励")
    private DefaultFlag inviteFlag;


    /**
     * 是否开启邀新奖励限制
     */
    @ApiModelProperty(value = "是否开启邀新奖励限制")
    private DefaultFlag distributionLimitType;

    /**
     * 是否开启奖励现金开关
     */
    @ApiModelProperty(value = "是否开启奖励现金开关")
    private DefaultFlag rewardCashFlag;

    /**
     * 后台配置的奖励金额
     */
    @ApiModelProperty(value = "后台配置的奖励金额")
    private BigDecimal settingAmount;

    /**
     * 奖励上限设置
     */
    @ApiModelProperty(value = "奖励上限设置")
    private DefaultFlag rewardCashType;


    /**
     * 奖励现金上限(人数)
     */
    @ApiModelProperty(value = "奖励现金上限(人数)")
    private Integer rewardCashCount;

    /**
     * 是否开启奖励优惠券
     */
    @ApiModelProperty("是否开启奖励优惠券")
    private DefaultFlag rewardCouponFlag;

    /**
     * 优惠券名称集合
     */
    @ApiModelProperty("优惠券名称集合")
    private List<String> couponNameList;

    /**
     * 优惠券ID和组数集合
     */
    @ApiModelProperty("优惠券ID和组数集合")
    private List<DistributionRewardCouponDTO> distributionRewardCouponDTOList;

    /**
     * 奖励优惠券上限(组数)
     */
    @ApiModelProperty("奖励优惠券上限(组数)")
    private Integer rewardCouponCount;


    /**
     * 邀请注册可用状态
     */
    @ApiModelProperty(" 邀请注册可用状态")
    private DefaultFlag enableFlag;

    /**
     * 限制条件
     */
    @ApiModelProperty("限制条件")
    private DefaultFlag limitType;

    /**
     * 邀请人数
     */
    @ApiModelProperty("邀请人数")
    private Integer inviteCount;

    /**
     * 分销员等级设置信息
     */
    @ApiModelProperty("邀请人数")
    private List<DistributorLevelVO> distributorLevelVOList;

    /**
     * 基础邀新奖励限制
     */
    @ApiModelProperty("基础邀新奖励限制")
    private DefaultFlag baseLimitType;

    /**
     * 优惠券面值总额
     */
    @ApiModelProperty("优惠券面值总额")
    private BigDecimal denominationSum;

    /**
     * 终端
     */
    @ApiModelProperty(value = "终端")
    private TerminalSource terminalSource;


}
