package com.wanmi.sbc.customer.level.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户等级
 * Created by CHENLI on 2017/4/13.
 */
@Data
@Entity
@Table(name = "customer_level")
public class CustomerLevel implements Serializable {

    /**
     * 客户等级ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_level_id")
    private Long customerLevelId;

    /**
     * 客户等级名称
     */
    @Column(name = "customer_level_name")
    private String customerLevelName;

    /**
     * 客户等级折扣
     */
    @Column(name = "customer_level_discount")
    private BigDecimal customerLevelDiscount;

    /**
     * 所需成长值
     */
    @Column(name = "growth_value")
    private Long growthValue;

    /**
     * 等级徽章图
     */
    @Column(name = "rank_badge_img")
    private String rankBadgeImg;

    /**
     * 是否是默认 0：否 1：是
     */
    @Column(name = "is_defalt")
    @Enumerated
    private DefaultFlag isDefalt;

    /**
     * 删除标志 0未删除 1已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @Column(name = "delete_person")
    private String deletePerson;
}
