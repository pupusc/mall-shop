package com.wanmi.sbc.setting.api.request.topicconfig;


import lombok.Data;

import java.io.Serializable;

@Data
public class TopicRalationRequest implements Serializable {


    private Integer id;

    private Integer PRankColumId;

    private Integer PRankId;

    private Integer CRankId;

    private String CRankName;

    private Integer CRankSorting;
}
