package com.wanmi.sbc.crm.customerplan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * customer_plan_send_count
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPlanSendCount implements Serializable {
    /**
     * 礼包优惠券发放统计id
     */
    private Long id;

    /**
     * 运营计划id
     */
    private Long planId;

    /**
     * 发放礼包人数
     */
    private Long giftPersonCount;

    /**
     * 发放礼包次数
     */
    private Long giftCount;

    /**
     * 发放优惠券人数
     */
    private Long couponPersonCount;

    /**
     * 发放优惠券张数
     */
    private Long couponCount;

    /**
     * 优惠券使用人数
     */
    private Long couponPersonUseCount;

    /**
     * 优惠券使用张数
     */
    private Long couponUseCount;

    /**
     * 优惠券使用率
     */
    private Double couponUseRate;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;

}