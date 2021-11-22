package com.wanmi.sbc.setting.bean.vo;

import com.wanmi.sbc.setting.bean.dto.TopicHeadImageDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class TopicActivityVO implements Serializable {
    private static final long serialVersionUID = -8099008658838388675L;

    @ApiModelProperty("头图信息")
    private List<TopicHeadImageDTO> headImageList;

    @ApiModelProperty("楼层信息")
    private List<TopicStoreyDTO> storeyList;

    @ApiModelProperty("专题名称")
    private String topicName;

    @ApiModelProperty("主题色")
    private String topicColor;

    @ApiModelProperty("导航色")
    private String navigationColor;
}
