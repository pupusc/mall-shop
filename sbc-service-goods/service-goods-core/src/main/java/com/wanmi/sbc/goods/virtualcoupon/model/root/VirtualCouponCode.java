package com.wanmi.sbc.goods.virtualcoupon.model.root;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;

import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;

import javax.persistence.*;

import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>券码实体类</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@Data
@Entity
@Table(name = "virtual_coupon_code")
public class VirtualCouponCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 券码ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 电子卡券ID
     */
    @Column(name = "coupon_id")
    private Long couponId;

    /**
     * 批次号
     */
    @Column(name = "batch_no")
    private String batchNo;

    /**
     * 有效期
     */
    @Column(name = "valid_days")
    private Integer validDays;

    /**
     * 0:兑换码 1:券码+密钥 2:链接
     */
    @Column(name = "provide_type")
    private Integer provideType;

    /**
     * 兑换码/券码/链接
     */
    @Column(name = "coupon_no")
    private String couponNo;

    /**
     * 密钥
     */
    @Column(name = "coupon_secret")
    private String couponSecret;

    /**
     * 领取结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "receive_end_time")
    private LocalDateTime receiveEndTime;

    /**
     * 兑换开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "exchange_start_time")
    private LocalDateTime exchangeStartTime;

    /**
     * 兑换结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "exchange_end_time")
    private LocalDateTime exchangeEndTime;

    /**
     * 0:未发放 1:已发放 2:已过期
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 订单号
     */
    @Column(name = "tid")
    private String tid;

    /**
     * 删除标识;0:未删除1:已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

}