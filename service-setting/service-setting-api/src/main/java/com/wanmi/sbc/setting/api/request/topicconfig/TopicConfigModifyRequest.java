package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TopicConfigModifyRequest extends TopicConfigAddRequest {
    @ApiModelProperty("id")
    @NotNull
    private Integer id;
}
