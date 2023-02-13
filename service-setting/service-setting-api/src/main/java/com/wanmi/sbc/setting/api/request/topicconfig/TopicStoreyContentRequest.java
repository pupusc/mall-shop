package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TopicStoreyContentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer storeyId;

    private Integer pageNum = 0;

    private Integer pageSize = 10;

    private Integer deleted;

    private Integer storeyType;

}
