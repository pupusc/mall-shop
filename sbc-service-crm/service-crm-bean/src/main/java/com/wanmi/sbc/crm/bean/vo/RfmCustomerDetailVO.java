package com.wanmi.sbc.crm.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * rfm_customer_detail
 * @author 
 */
@Data
public class RfmCustomerDetailVO implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * R分对应得分
     */
    private Integer rScore;

    /**
     * F分对应得分
     */
    private Integer fScore;

    /**
     * M分对应得分
     */
    private Integer mScore;

    /**
     * 会员id
     */
    private String customerId;

    /**
     * 系统分群id
     */
    private Long systemGroupId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 统计时间
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate statDate;

    /**
     * R值（最近一次消费时间/天）
     */
    private Integer rValue;

    /**
     * F值（消费频次）
     */
    private Integer fValue;

    /**
     * M值（消费金额）
     */
    private BigDecimal mValue;

    /**
     * 系统分群名称
     */
    private String systemGroupName;

    private static final long serialVersionUID = 1L;
}