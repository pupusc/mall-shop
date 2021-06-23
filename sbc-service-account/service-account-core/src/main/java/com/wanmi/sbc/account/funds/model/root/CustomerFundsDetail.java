package com.wanmi.sbc.account.funds.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.FundsStatus;
import com.wanmi.sbc.account.bean.enums.FundsSubType;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员资金明细表映射
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:42
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "customer_funds_detail")
public class CustomerFundsDetail {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "customer_funds_detail_id")
    private String customerFundsDetailId;

    /**
     * 会员ID
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 业务编号
     */
    @Column(name = "business_id")
    private String businessId;

    /**
     * 资金类型
     */
    @Column(name = "funds_type")
    @Enumerated
    private FundsType fundsType;

    /**
     * 佣金提现id
     */
    @Column(name = "draw_cash_id")
    private String drawCashId;

    /**
     * 收支金额
     */
    @Column(name = "receipt_payment_amount")
    private BigDecimal receiptPaymentAmount;

    /**
     * 资金是否成功入账 0:否，1：成功
     */
    @Column(name = "funds_status")
    @Enumerated
    private FundsStatus fundsStatus;

    /**
     * 账户余额
     */
    @Column(name = "account_balance")
    private BigDecimal accountBalance;

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


    /**
     * 资金账务子类型
     */
    @Column(name = "sub_type")
    @Enumerated
    private FundsSubType subType;

}
