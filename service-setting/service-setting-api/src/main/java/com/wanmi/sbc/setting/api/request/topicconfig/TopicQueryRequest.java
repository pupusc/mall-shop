package com.wanmi.sbc.setting.api.request.topicconfig;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TopicQueryRequest implements Serializable {
    private static final long serialVersionUID = -3552656379536632706L;

    @ApiModelProperty("页面id")
    private Integer id;

    @ApiModelProperty("页面名称")
    private String name;

   
}
