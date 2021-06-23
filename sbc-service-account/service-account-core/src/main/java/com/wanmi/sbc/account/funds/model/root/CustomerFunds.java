package com.wanmi.sbc.account.funds.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员资金表映射
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:42
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "customer_funds")
public class CustomerFunds {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "customer_funds_id")
    private String customerFundsId;

    /**
     * 会员ID
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 会员登录账号|手机号
     */
    @Column(name = "customer_account")
    private String customerAccount;

    /**
     * 会员名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 账户余额
     */
    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    /**
     * 冻结余额
     */
    @Column(name = "blocked_balance")
    private BigDecimal blockedBalance;

    /**
     * 可提现金额
     */
    @Column(name = "withdraw_amount")
    private BigDecimal withdrawAmount;


    /**
     * 已提现金额
     */
    @Column(name = "already_draw_amount")
    private BigDecimal alreadyDrawAmount;

    /**
     * 收入笔数
     */
    @Column(name = "income")
    private Long income;

    /**
     * 收入金额
     */
    @Column(name = "amount_received")
    private BigDecimal amountReceived;

    /**
     * 支出笔数
     */
    @Column(name = "expenditure")
    private Long expenditure;

    /**
     * 支出金额
     */
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    /**
     * 是否分销员 0：否，1：是
     */
    @Column(name = "is_distributor")
    private Integer distributor;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

}
