package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MixedComponentQueryRequest implements Serializable {

    private static final long serialVersionUID = -8570083469603100649L;
    private Integer pageNum = 0;

    private Integer pageSize = 10;

    private Integer id;

    private Integer keywordId;

    private Integer topicStoreyId;

    private String tabName;

    private String keywords;
}
