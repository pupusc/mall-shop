package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MixedComponentQueryRequest implements Serializable {

    private Integer id;

    private Integer topicStoreyId;

    private String tabName;

    private String keywords;
}
