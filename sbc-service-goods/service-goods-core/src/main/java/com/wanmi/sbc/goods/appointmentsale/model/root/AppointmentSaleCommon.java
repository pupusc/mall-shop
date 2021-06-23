package com.wanmi.sbc.goods.appointmentsale.model.root;

import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>预约抢购实体</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleCommon extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 商户id
     */
    private Long storeId;

    /**
     * 预约类型 0：不预约不可购买  1：不预约可购买
     */
    private Integer appointmentType;

    /**
     * 预约开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime appointmentStartTime;

    /**
     * 预约结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime appointmentEndTime;

    /**
     * 抢购开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime snapUpStartTime;

    /**
     * 抢购结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime snapUpEndTime;

    /**
     * 发货日期 2020-01-10
     */
    private String deliverTime;

    /**
     * 参加会员  -1:全部客户 0:全部等级 other:其他等级
     */
    private String joinLevel;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    private DefaultFlag joinLevelType;

    /**
     * 是否删除标志 0：否，1：是
     */
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 是否暂停 0:否 1:是
     */
    private Integer pauseFlag;

    /**
     * 预约价
     */
    private BigDecimal price;


}