package com.wanmi.ares.report.flow.model.reponse;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * es 查询流量统计返回数据格式
 * Created by sunkun on 2017/9/26.
 */
@Data
public class FlowReportReponse {

    private String id;

    private Long totalPv = 0l;

    private Long totalUv = 0l;

    private Long skuTotalPv = 0l;

    private Long skuTotalUv = 0l;

    private List<FlowReponse> flowList = new ArrayList<>();

    private LocalDate date;
}
