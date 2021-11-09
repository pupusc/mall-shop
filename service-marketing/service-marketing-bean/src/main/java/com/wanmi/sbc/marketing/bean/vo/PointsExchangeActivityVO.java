package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PointsExchangeActivityVO implements Serializable {
    private static final long serialVersionUID = -288962820704070236L;
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    @ApiModelProperty("积分数量")
    private Integer points;

    @ApiModelProperty("状态0未开始1进行中2暂停3已结束")
    private Integer status;

    private List<PointsExchangeActivityGoodsVO> skus;
}
