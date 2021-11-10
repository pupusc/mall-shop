package com.wanmi.sbc.setting.api.request.topicconfig;

import com.wanmi.sbc.setting.bean.dto.TopicHeadImageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class HeadImageConfigAddRequest implements Serializable {
    private static final long serialVersionUID = -797995665161219409L;

    @ApiModelProperty("专题Id")
    @NotNull
    private Integer topicId;
    
    @NotEmpty
    List<TopicHeadImageDTO> headImage;
}
