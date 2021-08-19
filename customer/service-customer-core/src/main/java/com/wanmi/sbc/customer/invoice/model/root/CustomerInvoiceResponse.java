package com.wanmi.sbc.customer.invoice.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

/**
 * 客户增专资质信息
 * Created by CHENLI on 2017/4/13.
 */
@Data
public class CustomerInvoiceResponse {

    /**
     * 增专资质ID
     */
    private Long customerInvoiceId;

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 单位全称
     */
    private String companyName;

    /**
     * 纳税人识别号
     */
    private String taxpayerNumber;

    /**
     * 单位电话
     */
    private String companyPhone;

    /**
     * 单位地址
     */
    private String companyAddress;

    /**
     * 银行基本户号
     */
    private String bankNo;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 营业执照复印件
     */
    private String businessLicenseImg;

    /**
     * 一般纳税人认证资格复印件
     */
    private String taxpayerIdentificationImg;

    /**
     * 增票资质审核状态  0:待审核 1:已审核通过 2:审核未通过
     */
    @Enumerated
    private CheckState checkState;

    /**
     * 审核未通过原因
     */
    private String rejectReason;

    /**
     * 增专资质是否作废 0：否 1：是
     */
    @Enumerated
    private InvalidFlag invalidFlag;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
