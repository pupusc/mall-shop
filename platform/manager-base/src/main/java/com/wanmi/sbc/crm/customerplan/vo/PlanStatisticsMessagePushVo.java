package com.wanmi.sbc.crm.customerplan.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName PlanStatisticsMessagePushVo
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/2/6 11:39
 **/
@ApiModel
@Data
public class PlanStatisticsMessagePushVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 运营计划id
     */
    @ApiModelProperty(value = "运营计划id")
    private Long planId;

    /**
     * 站内信收到人数
     */
    @ApiModelProperty(value = "站内信收到人数")
    private Integer messageReceiveNum;

    /**
     * 站内信收到人次
     */
    @ApiModelProperty(value = "站内信收到人次")
    private Integer messageReceiveTotal;

    /**
     * 运营计划效果统计push收到次数
     */
    @ApiModelProperty(value = "运营计划效果统计push收到次数")
    private Integer pushNum;

    /**
     * 统计日期
     */
    @ApiModelProperty(value = "统计日期")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate statisticsDate;
}
