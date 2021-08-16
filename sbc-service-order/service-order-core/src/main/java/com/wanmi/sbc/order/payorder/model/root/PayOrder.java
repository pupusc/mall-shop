package com.wanmi.sbc.order.payorder.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.order.receivables.model.root.Receivable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账务支付单
 * Created by zhangjin on 2017/4/20.
 */
@Data
@Entity
@Table(name = "pay_order")
@EqualsAndHashCode(exclude="receivable")
public class PayOrder implements Serializable{


    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "pay_order_id")
    private String payOrderId;

    /**
     * 支付单号
     */
    @Column(name = "pay_order_no")
    private String payOrderNo;

    /**
     * 订单编号
     */
    @Column(name = "order_code")
    private String orderCode;

    /**
     * 会员id
     */
    @Column(name = "customer_detail_id")
    private String customerDetailId;

    /**
     * 支付单状态
     */
    @Column(name = "pay_order_status")
    @Enumerated
    private PayOrderStatus payOrderStatus;

    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @Column(name = "del_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "customer_detail_id", nullable=true, insertable = false, updatable = false)
//    private CustomerDetail customerDetail;
    @Transient
    private CustomerDetailVO customerDetail;

    /**
     * 支付单金额
     */
    @Column(name= "pay_order_price")
    private BigDecimal payOrderPrice;

    /**
     * 支付单积分
     */
    @Column(name= "pay_order_points")
    private Long payOrderPoints;

    /**
     * 支付单积分
     */
    @Column(name= "pay_order_knowledge")
    private Long payOrderKnowledge;

    @OneToOne(mappedBy = "payOrder")
    @JsonManagedReference
    private Receivable receivable;

    /**
     * 支付方式
     */
    @Column(name = "pay_type")
    private PayType payType;

    /**
     * 商家编号
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "company_info_id", insertable = false, updatable = false)
//    private CompanyInfo companyInfo;
    @Transient
    private CompanyInfoVO companyInfo;
}
