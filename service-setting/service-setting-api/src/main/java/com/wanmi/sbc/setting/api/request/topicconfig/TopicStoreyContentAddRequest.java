package com.wanmi.sbc.setting.api.request.topicconfig;

import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class TopicStoreyContentAddRequest implements Serializable {

    @NotNull
    @ApiModelProperty("楼层id")
    private Integer storeyId;

    @NotNull
    @ApiModelProperty("专题id")
    private Integer topicId;

    @ApiModelProperty("楼层类型")
    private Integer storeyType;

    @ApiModelProperty("商品内容")
    private List<TopicStoreyContentDTO> goodsContents;

    @ApiModelProperty("图片+链接内容")
    private List<TopicStoreyContentDTO> linkContents;

}
