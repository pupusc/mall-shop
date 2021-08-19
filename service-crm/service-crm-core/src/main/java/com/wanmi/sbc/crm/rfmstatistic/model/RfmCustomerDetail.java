package com.wanmi.sbc.crm.rfmstatistic.model;

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
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RfmCustomerDetail implements Serializable {
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



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", rScore=").append(rScore);
        sb.append(", fScore=").append(fScore);
        sb.append(", mScore=").append(mScore);
        sb.append(", customerId=").append(customerId);
        sb.append(", systemGroupId=").append(systemGroupId);
        sb.append(", createTime=").append(createTime);
        sb.append(", statDate=").append(statDate);
        sb.append(", rValue=").append(rValue);
        sb.append(", fValue=").append(fValue);
        sb.append(", mValue=").append(mValue);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}