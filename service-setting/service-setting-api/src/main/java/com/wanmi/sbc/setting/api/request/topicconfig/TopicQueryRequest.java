package com.wanmi.sbc.setting.api.request.topicconfig;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel
public class TopicQueryRequest implements Serializable {
    private static final long serialVersionUID = -3552656379536632706L;

    @ApiModelProperty("页面id")
    private String topicKey;

    @ApiModelProperty("页面名称")
    private String topicName;


    private String keyWord;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum = 0;

    /**
     * 每页显示多少条
     */
    @ApiModelProperty(value = "每页显示多少条")
    private Integer pageSize = 10;
}
