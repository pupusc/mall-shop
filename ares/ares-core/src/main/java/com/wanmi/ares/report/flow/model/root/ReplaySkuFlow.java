package com.wanmi.ares.report.flow.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import com.wanmi.ms.util.CustomLocalDateTimeDeserializer;
import com.wanmi.ms.util.CustomLocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * replay_sku_flow
 * @author 
 */
@Data
@Builder
public class ReplaySkuFlow implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 商品id
     */
    private String skuId;

    /**
     * 商品id
     */
    private List<String> skuIds;

    /**
     * 商家id
     */
    private String companyId;

    /**
     * pc端pv
     */
    private Long pcPv;

    /**
     * h5端pv
     */
    private Long h5Pv;

    /**
     * app端pv
     */
    private Long appPv;

    /**
     * 商城浏览pv
     */
    private Long totalPv;

    /**
     * pc端uv
     */
    private Long pcUv;

    /**
     * h5端uv
     */
    private Long h5Uv;

    /**
     * app端uv
     */
    private Long appUv;

    /**
     * 商城访客数uv
     */
    private Long totalUv;

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

    /**
     * 商品流量统计日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate skuFlowDate;

    /**
     * 某月的商品流量统计
     */
    private String skuFlowMonth;

    private static final long serialVersionUID = 1L;

}