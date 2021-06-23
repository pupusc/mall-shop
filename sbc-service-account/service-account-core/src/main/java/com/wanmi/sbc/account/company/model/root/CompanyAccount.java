package com.wanmi.sbc.account.company.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 线下账户entity
 * Created by sunkun on 2017/11/30.
 */
@Data
@Entity
@Table(name = "company_account")
public class CompanyAccount implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;


    /**
     * 公司信息ID
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 是否收到平台首次打款 0、否 1、是
     */
    @Column(name = "is_received")
    @Enumerated
    private DefaultFlag isReceived = DefaultFlag.NO;

    /**
     * 是否主账号 0、否 1、是
     */
    @Column(name = "is_default_account")
    @Enumerated
    private DefaultFlag isDefaultAccount = DefaultFlag.NO;

    /**
     * 账户名称
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 开户银行
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 支行
     */
    @Column(name = "bank_branch")
    private String bankBranch;

    /**
     * 账号
     */
    @Column(name = "bank_no")
    private String bankNo;

    @Column(name = "bank_code")
    private String bankCode;

    /**
     * 账号状态 0: 启用 1:禁用'
     */
    @Column(name = "bank_status")
    private Integer bankStatus;

    /**
     * 第三方店铺id
     */
    @Column(name = "third_id")
    private String thirdId;

    /**
     * 修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime update_time;

    /**
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "del_time")
    private LocalDateTime deleteTime;

    /**
     * 删除标志
     */
    @Column(name = "del_flag")
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 打款金额
     */
    @Column(name = "remit_price")
    private BigDecimal remitPrice;
}
