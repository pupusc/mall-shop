package com.wanmi.sbc.customer.invoice.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户增专资质信息
 * Created by CHENLI on 2017/4/13.
 */
@Data
@Entity
@Table(name = "customer_invoice")
public class CustomerInvoice implements Serializable {

    /**
     * 增专资质ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_invoice_id")
    private Long customerInvoiceId;

    /**
     * 会员ID
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 单位全称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 纳税人识别号
     */
    @Column(name = "taxpayer_number")
    private String taxpayerNumber;

    /**
     * 单位电话
     */
    @Column(name = "company_phone")
    private String companyPhone;

    /**
     * 单位地址
     */
    @Column(name = "company_address")
    private String companyAddress;

    /**
     * 银行基本户号
     */
    @Column(name = "bank_no")
    private String bankNo;

    /**
     * 开户行
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 营业执照复印件
     */
    @Column(name = "business_license_img")
    private String businessLicenseImg;

    /**
     * 一般纳税人认证资格复印件
     */
    @Column(name = "taxpayer_identification_img")
    private String taxpayerIdentificationImg;

    /**
     * 增票资质审核状态  0:待审核 1:已审核通过 2:审核未通过
     */
    @Column(name = "check_state")
    @Enumerated
    private CheckState checkState;

    /**
     * 审核未通过原因
     */
    @Column(name = "reject_reason")
    private String rejectReason;

    /**
     * 增专资质是否作废 0：否 1：是
     */
    @Column(name = "invalid_flag")
    @Enumerated
    private InvalidFlag invalidFlag;

    /**
     * 删除标志 0未删除 1已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @Column(name = "delete_person")
    private String deletePerson;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = true, insertable = false,
// updatable = false)
//    private CustomerDetail customerDetail;
}
