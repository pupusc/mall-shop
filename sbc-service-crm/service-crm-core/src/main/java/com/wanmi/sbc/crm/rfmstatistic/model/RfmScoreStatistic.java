package com.wanmi.sbc.crm.rfmstatistic.model;

import com.wanmi.sbc.crm.bean.enums.RFMType;
import com.wanmi.sbc.crm.bean.enums.ScoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * rfm_score_statistic
 * @author zgl
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RfmScoreStatistic implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 得分
     */
    private BigDecimal score;

    /**
     * 参数类型：0:R,1:F,2:M
     */
    private RFMType type;

    /**
     * 得分对应人数
     */
    private Long personNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 统计时间
     */
    private LocalDate statDate;

    /**
     * 得分类型:0:参数得分，1:平均分
     */
    private ScoreType scoreType;

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", score=").append(score);
        sb.append(", type=").append(type);
        sb.append(", personNum=").append(personNum);
        sb.append(", createTime=").append(createTime);
        sb.append(", statDate=").append(statDate);
        sb.append(", scoreType=").append(scoreType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}