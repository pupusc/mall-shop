package com.wanmi.sbc.setting.bean.vo;

import com.wanmi.sbc.setting.bean.dto.HeadImageConfigDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TopicActivityVO implements Serializable {
    private static final long serialVersionUID = -8099008658838388675L;

    @ApiModelProperty("头图信息")
    private List<HeadImageConfigDTO> headImageList;
}
