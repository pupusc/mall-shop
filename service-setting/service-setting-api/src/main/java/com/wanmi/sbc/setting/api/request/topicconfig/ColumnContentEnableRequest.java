package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class ColumnContentEnableRequest implements Serializable {
    @NotNull
    private Integer id;

    @NotNull
    @ApiModelProperty("操作类型：1-未启用 0-启用")
    private Integer publishState;
}
