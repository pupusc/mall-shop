package com.wanmi.sbc.goods.appointmentsale.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoods;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>预约抢购实体类</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@Data
@Entity
@Table(name = "appointment_sale")
public class AppointmentSale extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 活动名称
     */
    @Column(name = "activity_name")
    private String activityName;

    /**
     * 商户id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 预约类型 0：不预约不可购买  1：不预约可购买
     */
    @Column(name = "appointment_type")
    private Integer appointmentType;

    /**
     * 预约开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "appointment_start_time")
    private LocalDateTime appointmentStartTime;

    /**
     * 预约结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "appointment_end_time")
    private LocalDateTime appointmentEndTime;

    /**
     * 抢购开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "snap_up_start_time")
    private LocalDateTime snapUpStartTime;

    /**
     * 抢购结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "snap_up_end_time")
    private LocalDateTime snapUpEndTime;

    /**
     * 发货日期 2020-01-10
     */
    @Column(name = "deliver_time")
    private String deliverTime;

    /**
     * 参加会员  -3:企业会员 -2:付费会员 -1:全部客户 0:全部等级 other:其他等级
     */
    @Column(name = "join_level")
    private String joinLevel;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    @Column(name = "join_level_type")
    private DefaultFlag joinLevelType;

    /**
     * 是否删除标志 0：否，1：是
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 是否暂停 0:否 1:是
     */
    @Column(name = "pause_flag")
    private Integer pauseFlag;

    @Transient
    private List<AppointmentSaleGoods> appointmentSaleGoods;

    @Transient
    private AppointmentSaleGoods appointmentSaleGood;

    /**
     * 预约价
     */
    @Transient
    private BigDecimal price;

}