package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class TopicStoreyColumnQueryRequest implements Serializable {

    private Integer pageNum = 0;

    private Integer pageSize = 10;

    private Integer id;

    private String name;

    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;

    @NotNull
    @ApiModelProperty("楼层ID")
    private Integer topicStoreId;
}
