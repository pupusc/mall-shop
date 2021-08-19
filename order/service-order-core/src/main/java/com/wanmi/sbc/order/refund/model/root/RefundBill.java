package com.wanmi.sbc.order.refund.model.root;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款流水
 * Created by zhangjin on 2017/4/21.
 */
@Data
@Entity
@Table(name = "refund_bill")
public class RefundBill {

    /**
     * 退款流水主键
     */
    @Column(name = "refund_bill_id", nullable = false, length = 45)
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String refundBillId;

    /**
     * 退款单外键
     */
    @Column(name = "refund_order_id")
    private String refundId;

    /**
     * 退款流水编号
     */
    @Column(name = "refund_bill_code")
    private String refundBillCode;

    /**
     * 线下平台账户
     */
    @Column(name = "offline_account_id")
    private Long offlineAccountId;

    /**
     * 线上平台账户
     */
    @Column(name = "online_account_id")
    private String onlineAccountId;

    /**
     * 客户收款账号id
     */
    @Column(name = "customer_account_id")
    private String customerAccountId;

    /**
     * 退款评论
     */
    @Column(name = "refund_comment")
    private String refundComment;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 删除标志
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 删除时间
     */
    @Column(name = "del_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refund_order_id", insertable = false, updatable = false)
    @JsonBackReference
    private RefundOrder refundOrder;

    /**
     * 实退金额
     */
    @Column(name = "actual_return_price")
    private BigDecimal actualReturnPrice;

    /**
     * 实退积分
     */
    @Column(name = "actual_return_points")
    private Long actualReturnPoints;

    /**
     * 退款在线渠道
     */
    @Column(name = "pay_channel")
    private String payChannel;

    @Column(name = "pay_channel_id")
    private Long payChannelId;

}
