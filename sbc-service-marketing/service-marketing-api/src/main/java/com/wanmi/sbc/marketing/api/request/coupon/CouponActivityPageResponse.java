package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponActivityPageResponse implements Serializable {

    private static final long serialVersionUID = 2380002784243643198L;

    @ApiModelProperty(value = "优惠券活动信息分页列表")
    private MicroServicePage<CouponActivityVO> couponActivityVOPage;
}
