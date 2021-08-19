package com.wanmi.ares.report.flow.model.reponse;

import lombok.Data;

import java.time.LocalDate;

/**
 * Created by sunkun on 2017/10/11.
 */
@Data
public class FlowReponse {

    private String id;

    private Long totalPv;

    private Long totalUv;

    private Long skuTotalPv;

    private Long skuTotalUv;

    private LocalDate date;

    private String title;
}
