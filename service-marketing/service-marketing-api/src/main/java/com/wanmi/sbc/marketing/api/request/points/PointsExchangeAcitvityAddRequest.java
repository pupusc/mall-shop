package com.wanmi.sbc.marketing.api.request.points;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.dto.PointsExchangeSkuDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PointsExchangeAcitvityAddRequest implements Serializable {

    @ApiModelProperty("id")
    private Integer id;
    @NotNull
    @ApiModelProperty("活动名称")
    private String activityName;
    @NotNull
    @ApiModelProperty("开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;
    @NotNull
    @ApiModelProperty("结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;
    @ApiModelProperty("积分数量")
    @NotNull
    private Integer points;
    @NotNull
    @ApiModelProperty("目标客户类型0全平台1付费2企业")
    private Integer customerType;
    @NotNull
    @ApiModelProperty("客户登记0全部登记1银杏级，用，分割")
    private String customerLevel;

    @ApiModelProperty("商品信息")
    List<PointsExchangeSkuDTO> skus;
}
