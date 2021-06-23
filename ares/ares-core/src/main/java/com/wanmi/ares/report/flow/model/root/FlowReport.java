package com.wanmi.ares.report.flow.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import com.wanmi.ms.util.CustomLocalDateTimeDeserializer;
import com.wanmi.ms.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 全站流量统计
 * Created by sunkun on 2017/9/25.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlowReport {

    /**
     * 商家id 平台默认id为0
     */
    @Id
    @Builder.Default
    private String id = Constants.companyId;

    @Builder.Default
    private Long appPv = 0l;

    @Builder.Default
    private Long pcPv = 0l;

    @Builder.Default
    private Long h5Pv = 0l;

    @Builder.Default
    private Long appUv = 0l;

    @Builder.Default
    private Long pcUv = new Long(1);

    @Builder.Default
    private Long H5Uv = 0l;

    @Builder.Default
    private Long totalUv = 0l;

    @Builder.Default
    private Long totalPv = 0l;

    @Builder.Default
    private Long skuTotalPv = 0l;

    @Builder.Default
    private Long skuTotalUv = 0l;

    @Builder.Default
    private Set<String> skuTotalUvUserIds = new HashSet<>();

    @Builder.Default
    private Set<String> userIds = new HashSet<>();

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate date;


    /**
     * 操作时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime operationTime;

    /**
     * 发送时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;

    /**
     * 接收时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime receiveTime;
}
