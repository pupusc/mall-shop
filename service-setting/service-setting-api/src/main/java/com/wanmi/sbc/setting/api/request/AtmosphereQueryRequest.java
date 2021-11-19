package com.wanmi.sbc.setting.api.request;

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
public class AtmosphereQueryRequest implements Serializable {
    private static final long serialVersionUID = 9151611009343706604L;
    @ApiModelProperty("sku编码")
    private List<String> skuNo;

    @ApiModelProperty("skuId")
    private List<String> skuId;

    private Integer pageNum = 0 ;

    private Integer pageSize = 10;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

}
