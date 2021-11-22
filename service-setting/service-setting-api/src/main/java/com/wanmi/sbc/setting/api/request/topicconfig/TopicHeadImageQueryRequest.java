package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TopicHeadImageQueryRequest implements Serializable {

    @NotNull
    @ApiModelProperty("专题ID")
    private Integer topicId;
}
