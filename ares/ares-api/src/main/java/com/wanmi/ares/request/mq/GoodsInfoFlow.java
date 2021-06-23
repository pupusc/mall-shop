package com.wanmi.ares.request.mq;

import lombok.Data;

/**
 * sku 流量信息
 * Created by sunkun on 2017/9/25.
 */
@Data
public class GoodsInfoFlow {

    private String companyId;

    private String skuId;

    private TerminalStatistics pv;

    private TerminalStatistics uv;
}
