package com.wanmi.sbc.marketing.api.request.distribution;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import com.wanmi.sbc.marketing.bean.enums.RecruitApplyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:43 2019/2/23
 * @Description: 分销招募设置请求对象
 */
@ApiModel
@Data
public class DistributionRecruitSettingSaveRequest extends BaseRequest {

    private static final long serialVersionUID = -218024831711229660L;

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启社交分销")
    @NotNull
    private DefaultFlag openFlag;

    /**
     * 是否开启申请入口 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启申请入口")
    @NotNull
    private DefaultFlag applyFlag;

    /**
     * 申请条件 0：购买商品，1：邀请注册
     */
    @ApiModelProperty(value = "申请条件")
    private RecruitApplyType applyType;

    /**
     * 礼包商品ids
     */
    @ApiModelProperty(value = "礼包商品ids")
    private List<String> goodsInfoIds;

    /**
     * 邀请招募入口海报
     */
    @ApiModelProperty(value = "邀请招募入口海报")
    private String inviteRecruitEnterImg;

    /**
     * 邀请招募落地海报
     */
    @ApiModelProperty(value = "邀请招募落地海报")
    private String inviteRecruitImg;

    /**
     * 购买商品招募入口海报
     */
    @ApiModelProperty(value = "购买商品招募入口海报")
    private String buyRecruitEnterImg;

    /**
     * 购买商品招募落地海报
     */
    @ApiModelProperty(value = "招募海报")
    private String recruitImg;

    /**
     * 招募邀新转发图片
     */
    @ApiModelProperty(value = "招募邀新转发图片")
    private String recruitShareImg;

    /**
     * 招募规则说明
     */
    @ApiModelProperty(value = "招募规则说明")
    private String recruitDesc;

    /**
     * 邀请人数
     */
    @ApiModelProperty(value = "邀请人数")
    private Integer inviteCount;

    @Override
    public void checkParam() {

        if (applyFlag == DefaultFlag.YES) {

            if (Objects.isNull(applyType)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (applyType == RecruitApplyType.BUY) {
                // 购买商品
                if (CollectionUtils.isEmpty(goodsInfoIds)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
            } else {
                // 邀请注册
                if (Objects.isNull(inviteCount)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }

                if (StringUtils.isEmpty(recruitShareImg)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
            }

            if (StringUtils.isEmpty(recruitImg)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

        }

    }

}
