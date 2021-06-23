package com.wanmi.sbc.marketing.api.request.distribution;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.bean.dto.DistributionRewardCouponDTO;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import com.wanmi.sbc.marketing.bean.enums.RewardCashType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:44 2019/2/23
 * @Description: 分销奖励设置请求对象
 */
@ApiModel
@Data
public class DistributionRewardSettingSaveRequest extends BaseRequest {

    private static final long serialVersionUID = 8680590841288850016L;

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启社交分销")
    @NotNull
    private DefaultFlag openFlag;

    /**
     * 是否开启分销佣金 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启分销佣金")
    @NotNull
    private DefaultFlag commissionFlag;

    /**
     * 是否开启邀新奖励 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启邀新奖励")
    @NotNull
    private DefaultFlag inviteFlag;

    /**
     * 邀新入口海报
     */
    @ApiModelProperty(value = "邀新入口海报")
    private String inviteEnterImg;

    /**
     * 邀新专题页海报
     */
    @ApiModelProperty(value = "邀新专题页海报")
    private String inviteImg;

    /**
     * 邀新转发图片
     */
    @ApiModelProperty(value = "邀新转发图片")
    private String inviteShareImg;

    /**
     * 邀新奖励规则说明
     */
    @ApiModelProperty(value = "邀新奖励规则说明")
    private String inviteDesc;

    /**
     * 是否开启奖励现金 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启奖励现金")
    private DefaultFlag rewardCashFlag;

    /**
     * 奖励上限类型 0：不限， 1：限人数
     */
    @ApiModelProperty(value = "奖励上限类型")
    private RewardCashType rewardCashType;

    /**
     * 奖励现金上限(人数)
     */
    @ApiModelProperty(value = "奖励现金上限(人数)")
    private Integer rewardCashCount;

    /**
     * 每位奖励金额
     */
    @ApiModelProperty(value = "每位奖励金额")
    private BigDecimal rewardCash;

    /**
     * 是否开启奖励优惠券 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启奖励优惠券")
    private DefaultFlag rewardCouponFlag;

    /**
     * 奖励优惠券上限(组数)
     */
    @ApiModelProperty(value = "奖励优惠券上限(组数)")
    private Integer rewardCouponCount;

    /**
     * 奖励的优惠券
     */
    @ApiModelProperty(value = "奖励的优惠券")
    private List<DistributionRewardCouponDTO> chooseCoupons;


    @Override
    public void checkParam() {

        if (inviteFlag == DefaultFlag.YES) {

            if (StringUtils.isEmpty(inviteImg)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (StringUtils.isEmpty(inviteShareImg)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (Objects.isNull(rewardCashFlag)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (rewardCashFlag == DefaultFlag.YES) {

                if (Objects.isNull(rewardCashType)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }

                if (rewardCashType == RewardCashType.LIMITED) {

                    if (Objects.isNull(rewardCashCount)) {
                        throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                    }

                }

                if (Objects.isNull(rewardCash)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }

            }

            if (Objects.isNull(rewardCouponFlag)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (rewardCouponFlag == DefaultFlag.YES) {

                if (Objects.isNull(rewardCouponCount)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }

                if (CollectionUtils.isEmpty(chooseCoupons)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }

            }

        }

    }

}
