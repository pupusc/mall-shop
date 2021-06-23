package com.wanmi.sbc.customer.storelevel.model.root;

import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>商户客户等级表实体类</p>
 *
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@Data
@Entity
@Table(name = "store_level")
public class StoreLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_level_id")
    private Long storeLevelId;

    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 等级名称
     */
    @Column(name = "level_name")
    private String levelName;

    /**
     * 折扣率
     */
    @Column(name = "discount_rate")
    private BigDecimal discountRate;

    /**
     * 客户升级所需累积支付金额
     */
    @Column(name = "amount_conditions")
    private BigDecimal amountConditions;

    /**
     * 客户升级所需累积支付订单笔数
     */
    @Column(name = "order_conditions")
    private Integer orderConditions;

    /**
     * 删除标记 0:未删除 1:已删除
     */
    @Column(name = "del_flag")
    private Integer delFlag;

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
     * 更新时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @Column(name = "delete_person")
    private String deletePerson;

}