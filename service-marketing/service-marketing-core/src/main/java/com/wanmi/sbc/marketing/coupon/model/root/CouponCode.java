package com.wanmi.sbc.marketing.coupon.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 优惠券码表
 */
@Entity
@Table(name = "coupon_code")
@Data
public class CouponCode {

    /**
     *  优惠券码id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "coupon_code_id")
    private String couponCodeId;

    /**
     *  优惠券码
     */
    @Column(name = "coupon_code")
    private String couponCode;

    /**
     * 优惠券Id
     */
    @Column(name = "coupon_id")
    private String couponId;

    /**
     *  优惠券活动id
     */
    @Column(name = "activity_id")
    private String activityId;

    /**
     *  领取人id,同时表示领取状态
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     *  使用状态,0(未使用)，1(使用)
     */
    @Column(name = "use_status")
    @Enumerated
    private DefaultFlag useStatus;

    /**
     *  获得优惠券时间
     */
    @Column(name = "acquire_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime acquireTime;

    /**
     *  使用时间
     */
    @Column(name = "use_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime useDate;

    /**
     *  使用的订单号
     */
    @Column(name = "order_code")
    private String orderCode;

    /**
     *  开始时间
     */
    @Column(name = "start_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     *  结束时间
     */
    @Column(name = "end_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     *  是否删除标志 0：否，1：是
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     *  创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     *  创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     *  修改时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     *  修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     *  删除时间
     */
    @Column(name = "del_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    /**
     *  删除人
     */
    @Column(name = "del_person")
    private String delPerson;

}
