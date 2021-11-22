package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class EnableTopicStoreyRequest implements Serializable {
    @NotNull
    @ApiModelProperty("楼层Id")
    private Integer storeyId;

    @NotNull
    @ApiModelProperty("操作类型：0禁用1启用")
    private Integer optType;


}
