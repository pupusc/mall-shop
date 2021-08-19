package com.wanmi.ares.report.customer.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>客户等级分布报表结构</p>
 * Created by of628-wenzhi on 2017-10-11-下午9:34.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelDistrReport implements Serializable{


    private static final long serialVersionUID = 7211861894326525940L;
    private String id;

    /**
     * 商户id
     */
    private String companyId;

    /**
     * 会员级别id
     */
    private Long levelId;

    /**
     * 当前级别会员数
     */
    private Long num;

    /**
     * 会员总数
     */
    private Long total;

    /**
     * 百分率占比(包含'%')
     */
    private String centage;

    /**
     * 生成时间
     */
    private LocalDateTime createTime;

    /**
     * 统计的目标数据日期
     */
    private LocalDate targetDate;

}
