package com.wanmi.sbc.marketing.coupon.response;

import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;

/**
 * 我的优惠券返回结果
 *
 * @author chenli
 * @date 2018/9/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeQueryResponse {

    /**
     * 我的优惠券列表
     */
    private Page<CouponCodeVO> couponCodeVos = new PageImpl<>(new ArrayList<>());

    /**
     * 我的优惠券未使用总数
     */
    private long unUseCount = 0;

    /**
     * 我的优惠券已使用总数
     */
    private long usedCount = 0;

    /**
     * 我的优惠券已过期总数
     */
    private long overDueCount = 0;
}
