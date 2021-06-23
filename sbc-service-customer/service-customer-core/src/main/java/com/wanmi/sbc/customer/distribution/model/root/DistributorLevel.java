package com.wanmi.sbc.customer.distribution.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>分销员等级</p>
 *
 * @author gaomuwei
 */
@Data
@Entity
@Table(name = "distributor_level")
public class DistributorLevel implements Serializable {

    private static final long serialVersionUID = 8857314372930844164L;

    /**
     * 分销员等级id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "distributor_level_id")
    private String distributorLevelId;


    /**
     * 分销员等级名称
     */
    @Column(name = "distributor_level_name")
    private String distributorLevelName;

    /**
     * 佣金比例
     */
    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

    /**
     * 佣金提成比例
     */
    @Column(name = "percentage_rate")
    private BigDecimal percentageRate;

    /**
     * 销售额门槛是否开启
     */
    @Column(name = "sales_flag")
    private DefaultFlag salesFlag;

    /**
     * 销售额门槛
     */
    @Column(name = "sales_threshold")
    private BigDecimal salesThreshold;

    /**
     * 到账收益额门槛是否开启
     */
    @Column(name = "record_flag")
    private DefaultFlag recordFlag;

    /**
     * 到账收益额门槛
     */
    @Column(name = "record_threshold")
    private BigDecimal recordThreshold;

    /**
     * 邀请人数门槛是否开启
     */
    @Column(name = "invite_flag")
    private DefaultFlag inviteFlag;

    /**
     * 邀请人数门槛
     */
    @Column(name = "invite_threshold")
    private Integer inviteThreshold;

    /**
     * 等级排序
     */
    @Column(name = "sort")
    private Integer sort;

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
     * 删除标志
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

}