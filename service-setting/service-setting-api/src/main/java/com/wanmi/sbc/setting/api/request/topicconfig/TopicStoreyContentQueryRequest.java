package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class TopicStoreyContentQueryRequest implements Serializable {


    @NotNull
    @ApiModelProperty("楼层ID")
    private Integer storeyId;

    @ApiModelProperty(value = "专题id",hidden = true)
    private Integer topicId;
}
