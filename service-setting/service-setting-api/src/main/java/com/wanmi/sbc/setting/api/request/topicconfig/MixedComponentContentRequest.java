package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class MixedComponentContentRequest implements Serializable{

    private Integer topicStoreyId = new Integer(194);

    private String keyWord;

    private Integer tabId;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum = 0;

    /**
     * 每页显示多少条
     */
    @ApiModelProperty(value = "每页显示多少条")
    private Integer pageSize = 10;

}
