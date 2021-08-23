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
 * replay_sku_flow_user_info
 *
 * @author
 */
@Data
@Builder
public class ReplaySkuFlowUserInfo implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * sku_flow关联表id
     */
    private String skuFlowId;

    /**
     * 商品id
     */
    private String skuId;

    /**
     * 商品id
     */
    private List<String> skuIds;

    /**
     * 用户id
     */
    private String userId;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate skuFlowDate;

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

    private static final long serialVersionUID = 1L;

}