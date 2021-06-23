package com.wanmi.sbc.marketing.distribution.model;

import java.math.BigDecimal;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

/**
 * <p>店铺分销设置实体类</p>
 *
 * @author gaomuwei
 * @date 2019-02-19 15:46:27
 */
@Data
@Entity
@Table(name = "distribution_store_setting")
public class DistributionStoreSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "setting_id")
    private String settingId;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @Column(name = "open_flag")
    private DefaultFlag openFlag;

    /**
     * 是否开启通用分销佣金 0：关闭，1：开启
     */
    @Column(name = "commission_flag")
    private DefaultFlag commissionFlag;

    /**
     * 通用分销佣金
     */
    @Column(name = "commission")
    private BigDecimal commission;

    /**
     * 佣金比例
     */
    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

}