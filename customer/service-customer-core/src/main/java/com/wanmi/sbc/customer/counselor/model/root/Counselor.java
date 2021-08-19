package com.wanmi.sbc.customer.counselor.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * t_counselor
 *
 * @author
 */
@Table(name = "t_counselor")
@Data
@Entity
public class Counselor implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    /**
     * 上级id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 分销总数
     */
    @Column(name = "sell_count")
    private Integer sellCount;

    /**
     * 分销商品总价
     */
    @Column(name = "total_fee")
    private Long totalFee;

    /**
     * 状态 0:未激活 1:正常 2:解除  3:冻结
     */
    
    private Integer status;

    /**
     * 评分
     */
    
    private Integer score;

    /**
     * 等级进度
     */
    @Column(name = "level_rate")
    private Integer levelRate;

    /**
     * 是否用等级进度 1:是 2:否
     */
    @Column(name = "level_rate_flag")
    private Integer levelRateFlag;


    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * 冻结金额
     */

    private Integer freeze;

    /**
     * 可提现金额
     */
    
    private Integer profit;

    private static final long serialVersionUID = 1L;
}