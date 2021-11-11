package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel
@Data
public class TopicConfigAddRequest implements Serializable {
    private static final long serialVersionUID = 5544669442425790415L;

    @ApiModelProperty("专题key")
    private String topicKey;
    @ApiModelProperty("页面名称")
    private String topicName;
    @ApiModelProperty("埋点key")
    private String trackKey;
}
