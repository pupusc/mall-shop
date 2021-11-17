package com.wanmi.sbc.setting.response;

import com.wanmi.sbc.setting.bean.dto.TopicHeadImageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TopicResponse implements Serializable {


    @ApiModelProperty("头图信息")
    private List<TopicHeadImageDTO> headImageList;

    @ApiModelProperty("楼层信息")
    private List<TopicStoreyResponse> storeyList;

    @ApiModelProperty("专题名称")
    private String topicName;

    @ApiModelProperty("主题色")
    private String topicColor;

    @ApiModelProperty("导航色")
    private String navigationColor;

}
