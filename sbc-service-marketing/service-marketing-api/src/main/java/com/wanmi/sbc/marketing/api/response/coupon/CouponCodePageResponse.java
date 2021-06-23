package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 分页查询优惠券列表响应结构
 *
 * @author chenli
 * @date 2018/9/20
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodePageResponse implements Serializable {

    private static final long serialVersionUID = -646748397905460516L;

    /**
     * 我的优惠券列表
     */
    @ApiModelProperty(value = "我的优惠券列表")
    private MicroServicePage<CouponCodeVO> couponCodeVos = new MicroServicePage<>(new ArrayList<>());

    /**
     * 我的优惠券未使用总数
     */
    @ApiModelProperty(value = "我的优惠券未使用总数")
    private long unUseCount = 0;

    /**
     * 我的优惠券已使用总数
     */
    @ApiModelProperty(value = "我的优惠券已使用总数")
    private long usedCount = 0;

    /**
     * 我的优惠券已过期总数
     */
    @ApiModelProperty(value = "我的优惠券已过期总数")
    private long overDueCount = 0;
}
