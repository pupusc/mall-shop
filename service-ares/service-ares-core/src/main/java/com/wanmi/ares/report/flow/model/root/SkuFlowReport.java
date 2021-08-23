package com.wanmi.ares.report.flow.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

/**
 * sku流量统计
 * Created by sunkun on 2017/9/25.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkuFlowReport {

    @Id
    private String id;

    private String companyId;

    private Long pcPv = 0l;

    @Builder.Default
    private Long h5Pv = 0l;

    @Builder.Default
    private Long appPv = 0l;

    @Builder.Default
    private Long totalPv = 0l;

    @Builder.Default
    private Long pcUv = 0l;

    @Builder.Default
    private Long h5Uv = 0l;

    @Builder.Default
    private Long appUv = 0l;

    @Builder.Default
    private Long totalUv = 0l;

    @Builder.Default
    private Set<String> userIds = new HashSet<>();
}
