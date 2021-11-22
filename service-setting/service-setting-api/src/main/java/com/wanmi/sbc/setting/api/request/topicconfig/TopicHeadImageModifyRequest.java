package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class TopicHeadImageModifyRequest extends HeadImageConfigAddRequest {

    @ApiModelProperty("主键")
    private Integer id;
}
