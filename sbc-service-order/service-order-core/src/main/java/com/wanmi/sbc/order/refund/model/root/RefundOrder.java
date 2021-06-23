package com.wanmi.sbc.order.refund.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.order.bean.enums.RefundChannel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@Entity
@Table(name = "refund_order")
public class RefundOrder {

    /**
     * 退款单主键
     */
    @Column(name = "refund_id", nullable = false, length = 45)
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String refundId;

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
     * 退款单编号
     */
    @Column(name = "return_order_code")
    private String returnOrderCode;

    /**
     * 退款单
     */
    @Column(name = "refund_code")
    private String refundCode;

    /**
     * 会员详情外键
     */
    @Column(name= "customer_detail_id")
    private String customerDetailId;

    /**
     * 退款单状态
     */
    @Column(name = "refund_status")
    @Enumerated
    private RefundStatus refundStatus;

    /**
     * del
     */
    @Column(name = "del_flag")
    private DeleteFlag delFlag;

    /**
     * 删除时间
     */
    @Column(name = "del_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;


    @OneToOne(mappedBy = "refundOrder", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private RefundBill refundBill;

    //TODO:去除关联查询，暂无其他使用的地方
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "customer_detail_id", nullable=true, insertable = false, updatable = false)
//    private CustomerDetail customerDetail;
//    @Transient
//    private CustomerDetailVO customerDetail;

    /**
     * 应退金额
     */
    @Column(name = "return_price")
    private BigDecimal returnPrice;

    /**
     * 应退积分
     */
    @Column(name = "return_points")
    private Long returnPoints;

    /**
     * 支付方式
     */
    @Column(name = "pay_type")
    @Enumerated
    private PayType payType;

    /**
     * 拒绝原因
     */
    @Column(name = "refuse_reason")
    private String refuseReason;

    //TODO:去除关联查询，暂无其他使用的地方
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
//    private CompanyInfo companyInfo;
    @Transient
    private CompanyInfoVO companyInfo;

    /**
     * 商家编号
     */
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 退款渠道
     */
    @Transient
    private RefundChannel refundChannel;
}
