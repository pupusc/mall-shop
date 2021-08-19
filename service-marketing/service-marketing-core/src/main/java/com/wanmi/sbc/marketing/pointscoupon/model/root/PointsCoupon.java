package com.wanmi.sbc.marketing.pointscoupon.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>积分兑换券表实体类</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@Data
@Entity
@Table(name = "points_coupon")
public class PointsCoupon implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分兑换券id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "points_coupon_id")
    private Long pointsCouponId;

    /**
     * 活动id
     */
    @Column(name = "activity_id")
    private String activityId;

    /**
     * 优惠券id
     */
    @Column(name = "coupon_id")
    private String couponId;

    /**
     * 优惠券信息
     */
    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private CouponInfo couponInfo;

    /**
     * 兑换总数
     */
    @Column(name = "total_count")
    private Long totalCount;

    /**
     * 已兑换数量
     */
    @Column(name = "exchange_count")
    private Long exchangeCount;

    /**
     * 兑换积分
     */
    @Column(name = "points")
    private Long points;

    /**
     * 是否售罄
     */
    @Column(name = "sell_out_flag")
    private BoolFlag sellOutFlag;

    /**
     * 是否启用 0：停用，1：启用
     */
    @Column(name = "status")
    @Enumerated
    private EnableStatus status;

    /**
     * 兑换开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    /**
     * 兑换结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 删除标识,0: 未删除 1: 已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

}