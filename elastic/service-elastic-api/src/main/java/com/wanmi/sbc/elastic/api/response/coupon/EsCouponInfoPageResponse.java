package com.wanmi.sbc.elastic.api.response.coupon;


import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.coupon.EsCouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 优惠券分页列表响应结构
 * Created by daiyitian on 2018/11/22.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsCouponInfoPageResponse implements Serializable {

    private static final long serialVersionUID = -1629767606219827600L;

    /**
     * 优惠券分页列表 {@link CouponInfoVO}
     */
    @ApiModelProperty(value = "优惠券分页列表")
    private MicroServicePage<EsCouponInfoVO> couponInfos;

}
