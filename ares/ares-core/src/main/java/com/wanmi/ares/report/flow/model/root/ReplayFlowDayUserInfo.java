package com.wanmi.ares.report.flow.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * replay_flow_day_user_info
 *
 * @author
 */
@Data
@Builder
public class ReplayFlowDayUserInfo implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * flow_day表关联id
     */
    private String flowDayId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 对应日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate flowDate;

    /**
     * 用户类型：0:总访客用户，1：sku访客用户
     */
    private Integer userType;

    private static final long serialVersionUID = 1L;

}