package com.wanmi.sbc.order.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class YzOrderRequest implements Serializable {

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("每页数据量")
    private Integer pageSize;

    @ApiModelProperty("创建时间-开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createAtStart;

    @ApiModelProperty("创建时间-结束")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private  LocalDateTime createAtEnd;

    @ApiModelProperty("修改时间-开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateAtStart;

    @ApiModelProperty("修改时间-结束")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private  LocalDateTime updateAtEnd;

    @ApiModelProperty("订单状态")
    private String status;

    @ApiModelProperty(value = "是否只查询当前页，true/false")
    private Boolean onlyOnePage = Boolean.FALSE;

    @ApiModelProperty(value = "订单号列表")
    private List<String> ids;

    @ApiModelProperty(value = "是否查询未转换的")
    private Boolean convertFlag = Boolean.FALSE;
}
