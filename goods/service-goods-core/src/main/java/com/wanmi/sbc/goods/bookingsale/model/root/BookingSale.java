package com.wanmi.sbc.goods.bookingsale.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>预售信息实体类</p>
 *
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@Data
@Entity
@Table(name = "booking_sale")
public class BookingSale extends BaseEntity {
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
     * 预售类型 0：全款预售  1：定金预售
     */
    @Column(name = "booking_type")
    private Integer bookingType;

    /**
     * 定金支付开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "hand_sel_start_time")
    private LocalDateTime handSelStartTime;

    /**
     * 定金支付结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "hand_sel_end_time")
    private LocalDateTime handSelEndTime;

    /**
     * 尾款支付开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "tail_start_time")
    private LocalDateTime tailStartTime;

    /**
     * 尾款支付结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "tail_end_time")
    private LocalDateTime tailEndTime;

    /**
     * 预售开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "booking_start_time")
    private LocalDateTime bookingStartTime;

    /**
     * 预售结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "booking_end_time")
    private LocalDateTime bookingEndTime;


    /**
     * 预售活动总开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 预售活动总结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 发货日期 2020-01-10
     */
    @Column(name = "deliver_time")
    private String deliverTime;

    /**
     * 参加会员  -3:企业会员 -2：付费会员 -1:平台全部客户 0:店铺全部等级 other:店铺其他等级
     */
    @Column(name = "join_level")
    private String joinLevel;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺） 没有用到
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
    private List<BookingSaleGoods> bookingSaleGoodsList;

    @Transient
    private BookingSaleGoods bookingSaleGoods;
}