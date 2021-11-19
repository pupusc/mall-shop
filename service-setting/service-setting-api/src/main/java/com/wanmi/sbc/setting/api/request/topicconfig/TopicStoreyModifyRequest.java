package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TopicStoreyModifyRequest extends  TopicStoreyAddRequest implements Serializable {
    private static final long serialVersionUID = -2872297385196659222L;
    @ApiModelProperty("id")
    private Integer id;
}
