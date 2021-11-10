package com.wanmi.sbc.setting.api.request.topicconfig;

import com.wanmi.sbc.setting.bean.dto.TopicStoreyDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class TopicStoreyAddRequest implements Serializable {
    private static final long serialVersionUID = -3354087848195632606L;

    @NotNull
    @ApiModelProperty("主题Id")
    private Integer topicId;

    @NotEmpty
    @ApiModelProperty("楼层导航")
    private List<TopicStoreyDTO> storeyList;
}
