package com.wanmi.ares.report.customer.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>客户地区分布统计报表结构</p>
 * Created by of628-wenzhi on 2017-10-12-下午5:52.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaDistrReport implements Serializable{


    private static final long serialVersionUID = 6534986330191250433L;

    private String id;

    /**
     * 商户
     */
    private String companyId;

    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 会员数
     */
    private Long num;

    /**
     * 报表生成时间
     */
    private LocalDateTime createTime;

    /**
     * 统计的目标数据日期
     */
    private LocalDate targetDate;

}
