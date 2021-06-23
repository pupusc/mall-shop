package com.wanmi.ares.request.mq;

import lombok.Data;

/**
 * 商家流量信息
 * Created by sunkun on 2017/9/28.
 */
@Data
public class CompanyFlow {

    /**
     * 商家id
     */
    private String companyId;

    private TerminalStatistics pv;

    private TerminalStatistics uv;

    private TerminalStatistics skuTotalPv;

    private TerminalStatistics skuTotalUv;
}
