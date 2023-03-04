package com.wanmi.sbc.setting.api.request.topicconfig;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

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
}
