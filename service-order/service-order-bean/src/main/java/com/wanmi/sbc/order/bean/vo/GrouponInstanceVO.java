package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单状态
 * Created by jinwei on 14/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class GrouponInstanceVO implements Serializable {
    private static final long serialVersionUID = 4115427502089583389L;


    /**
     * 团号
     */
    @ApiModelProperty(value = "团号")
    private String grouponNo;

    /**
     * 拼团活动id
     */
    @ApiModelProperty(value = "拼团活动id")
    private String grouponActivityId;

    /**
     * 开团时间
     */
    @ApiModelProperty(value = "开团时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     *团截止时间
     */
    @ApiModelProperty(value = "团截止时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 成团时间
     */
    @ApiModelProperty(value = "成团时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime completeTime;

    /**
     * 拼团人数
     */
    @ApiModelProperty(value = "拼团人数")
    private Integer grouponNum;

    /**
     * 参团人数
     */
    @ApiModelProperty(value = "参团人数")
    private Integer joinNum;

    /**
     * 团长用户id
     */
    @ApiModelProperty(value = "团长用户id")
    private String customerId;

    /**
     * 拼团状态
     */
    @ApiModelProperty(value = "拼团状态")
    private GrouponOrderStatus grouponStatus;

}
