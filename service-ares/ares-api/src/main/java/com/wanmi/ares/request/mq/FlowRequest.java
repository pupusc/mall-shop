package com.wanmi.ares.request.mq;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ares.base.BaseMqRequest;
import com.wanmi.ms.util.CustomLocalDateTimeDeserializer;
import com.wanmi.ms.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * 流量基础数据
 * Created by sunkun on 2017/9/25.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class FlowRequest extends BaseMqRequest {

    private static final long serialVersionUID = -8444377999243814831L;


    /**
     * 当天日期
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDate time;

    /**
     * 全站pv
     */
    private TerminalStatistics pv;

    /**
     * 全站uv
     */
    private TerminalStatistics uv;

    private TerminalStatistics skuTotalPv;

    private TerminalStatistics skuTotalUv;

    /**
     * sku pv uv
     */
    private List<GoodsInfoFlow> skus;

    /**
     * 商家 pv uv
     */
    private List<CompanyFlow> companyFlows;

}
