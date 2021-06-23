package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.dto.CouponActivityConfigAndCouponInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 精准发券
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeBatchSendCouponRequest implements Serializable {

    private static final long serialVersionUID = 243845102259185848L;

    @ApiModelProperty(value = "会员ID集合")
    private List<String> customerIds;

    /**
     * 优惠券活动配置信息以及优惠券信息
     */
    @ApiModelProperty(value = "优惠券活动配置信息以及优惠券信息")
    @NotEmpty
    private List<CouponActivityConfigAndCouponInfoDTO> list;

    public CouponCodeBatchSendCouponRequest(List<CouponActivityConfigAndCouponInfoDTO> list) {
        this.list = list;
    }
}
