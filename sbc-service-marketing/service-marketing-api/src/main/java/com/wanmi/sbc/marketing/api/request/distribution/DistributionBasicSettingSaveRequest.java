package com.wanmi.sbc.marketing.api.request.distribution;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.bean.enums.CommissionPriorityType;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import com.wanmi.sbc.marketing.bean.enums.RegisterLimitType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:43 2019/2/23
 * @Description: 分销基础设置请求对象
 */
@ApiModel
@Data
public class DistributionBasicSettingSaveRequest extends BaseRequest {

    private static final long serialVersionUID = -2100769189448293816L;

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启社交分销")
    @NotNull
    private DefaultFlag openFlag;

    /**
     * 分销员名称
     */
    @ApiModelProperty(value = "分销员名称")
    @NotBlank
    private String distributorName;

    /**
     * 是否开启分销小店 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启分销小店")
    @NotNull
    private DefaultFlag shopOpenFlag;

    /**
     * 小店名称
     */
    @ApiModelProperty(value = "小店名称")
    private String shopName;

    /**
     * 店铺分享图片
     */
    @ApiModelProperty(value = "店铺分享图片")
    private String shopShareImg;

    /**
     * 注册限制
     */
    @ApiModelProperty(value = "注册限制")
    @NotNull
    private RegisterLimitType registerLimitType;

    /**
     * 基础邀新奖励限制 0：不限，1：仅限有效邀新
     */
    @ApiModelProperty(value = "基础邀新奖励限制")
    @NotNull
    private DistributionLimitType baseLimitType;

    /**
     * 佣金返利优先级
     */
    @ApiModelProperty(value = "佣金返利优先级")
    @NotNull
    private CommissionPriorityType commissionPriorityType;

    /**
     * 是否开启分销商品审核 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启分销商品审核")
    @NotNull
    private DefaultFlag goodsAuditFlag;

    /**
     * 分销业绩规则说明
     */
    @ApiModelProperty(value = "分销业绩规则说明")
    private String performanceDesc;

    @Override
    public void checkParam() {

        if (shopOpenFlag == DefaultFlag.YES) {

            if (StringUtils.isEmpty(shopName)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (StringUtils.isEmpty(shopShareImg)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

        }

    }

}
