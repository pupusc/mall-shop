package com.wanmi.sbc.order.trade.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:51 2019/5/16
 * @Description: 团实例
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrouponInstance {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 团号
     */
    private String grouponNo;

    /**
     * 拼团活动id
     */
    private String grouponActivityId;

    /**
     * 开团时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     *团截止时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 成团时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime completeTime;

    /**
     * 团失败时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime failTime;


    /**
     * 拼团人数
     */
    private Integer grouponNum;

    /**
     * 参团人数
     */
    private Integer joinNum;

    /**
     * 团长用户id
     */
    private String customerId;

    /**
     * 拼团状态
     */
    private GrouponOrderStatus grouponStatus;

}
