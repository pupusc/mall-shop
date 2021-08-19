package com.wanmi.sbc.order.receivables.model.root;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收款单
 * Created by zhangjin on 2017/3/20.
 */
@Data
@Entity
@Table(name = "receivable")
public class Receivable implements Serializable{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "receivable_id")
    private String receivableId;



    /**
     * 流水号
     */
    @Column(name = "receivable_no")
    private String receivableNo;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 线上账户
     */
    @Column(name = "online_account_id")
    private Long onlineAccountId;

    /**
     * 线下账户
     */
    @Column(name = "offline_account_id")
    private Long offlineAccountId;

    /**
     * 收款账号
     */
    @Column(name = "receivable_account")
    private String receivableAccount;

    /**
     * 评论
     */
    @Column(name = "comment")
    private String comment;

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
    @JoinColumn(name = "pay_order_id", insertable = false, updatable = false)
    @JsonBackReference
    private PayOrder payOrder;

    /**
     * 支付单外键
     */
    @Column(name = "pay_order_id")
    private String payOrderId;

    /**
     * 收款在线渠道
     */
    @Column(name = "pay_channel")
    private String payChannel;

    @Column(name = "pay_channel_id")
    private Long payChannelId;

    /**
     * 附件
     */
    @Column(name = "encloses")
    private String encloses;

    /**
     * 离线账户
     */
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "offline_account_id", insertable = false, updatable = false)
//    private OfflineAccount offlineAccount;
    @Transient
    private OfflineAccountVO offlineAccount;
}
