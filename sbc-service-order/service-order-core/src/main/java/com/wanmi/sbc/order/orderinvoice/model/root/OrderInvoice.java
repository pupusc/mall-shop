package com.wanmi.sbc.order.orderinvoice.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
import com.wanmi.sbc.account.bean.enums.IsCompany;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectVO;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单开票
 * Created by CHENLI on 2017/5/5.
 */
@Data
@Entity
@Table(name = "order_invoice")
public class OrderInvoice implements Serializable{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "order_invoice_id")
    private String orderInvoiceId;

    /**
     * 客户ID
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 发票类型 0普通发票 1增值税专用发票 -1无
     */
    @Column(name = "invoice_type")
    @Enumerated
    private InvoiceType invoiceType;

    /**
     * 发票抬头
     */
    @Column(name = "invoice_title")
    private String invoiceTitle;

    /**
     * 开票状态 0待开票 1 已开票
     */
    @Column(name = "invoice_state")
    @Enumerated
    private InvoiceState invoiceState;

    /**
     * 开票项目id
     */
    @Column(name = "project_id")
    private String projectId;

    /**
     * 删除标志
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 开票时间
     */
    @Column(name = "invoice_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime invoiceTime;

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
     * 操作人
     */
    @Column(name = "operator_id")
    private String operateId;

    /**
     * 开票项目
     */
    @Transient
    private InvoiceProjectVO invoiceProject;

    /**
     * 账务支付单
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnsOrFormulas(value={
            @JoinColumnOrFormula(column=@JoinColumn(name ="order_no", referencedColumnName ="order_code", nullable = true, insertable =false, updatable = false)) ,
            @JoinColumnOrFormula(formula=@JoinFormula(value="0", referencedColumnName = "del_flag"))})
    private PayOrder payOrder;


    /**
     * 是否是企业
     */
    @Column(name = "is_company")
    @Enumerated
    private IsCompany isCompany;

    /**
     * 发票地址
     */
    @Column(name = "invoice_address")
    private String invoiceAddress;

    /**
     * 商家id
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 纳税人识别号
     */
    @Column(name = "taxpayer_number")
    private String taxpayerNumber;

}
