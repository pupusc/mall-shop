package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class EnableTopicStoreyColumnGoodsRequest implements Serializable {

    private static final long serialVersionUID = 1000269530529985570L;

    @NotNull
    @ApiModelProperty("专栏Id")
    private Integer id;

    @NotNull
    @ApiModelProperty("操作类型：0启用1禁用")
    private Integer publishState;


}
