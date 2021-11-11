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
    @ApiModelProperty("专题Id")
    private Integer topicId;

    @ApiModelProperty("楼层名称")
    private String name;

    @ApiModelProperty("导航名称")
    private String navigationName;

    @ApiModelProperty("楼层类型1：一层一个2：一层两个，3：一层3个")
    private String storeyType;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("排序")
    private Integer sorting;
}
