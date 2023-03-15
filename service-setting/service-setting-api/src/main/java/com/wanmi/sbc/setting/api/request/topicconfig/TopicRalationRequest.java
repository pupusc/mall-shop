package com.wanmi.sbc.setting.api.request.topicconfig;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TopicRalationRequest implements Serializable {


    private Integer id;

    @JsonProperty(value = "pRankColumId")
    private Integer pRankColumId;

    @JsonProperty(value = "pRankId")
    private Integer pRankId;

    @JsonProperty(value = "cRankId")
    private Integer cRankId;

    @JsonProperty(value = "cRankName")
    private String cRankName;

    @JsonProperty(value = "cRankSorting")
    private Integer cRankSorting;

    @JsonProperty(value = "topicRankSorting")
    private Integer topicRankSorting;

    @JsonProperty(value = "startTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @JsonProperty(value = "endTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    @JsonProperty(value = "delFlag")
    private Integer delFlag;
}
