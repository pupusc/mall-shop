package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class TopicStoreyContentResponse implements Serializable {

    private static final long serialVersionUID = 7169124215109467084L;

    @NotNull
    @ApiModelProperty("楼层id")
    private Integer storeyId;

    @ApiModelProperty("商品内容")
    private List<TopicStoreyContentDTO> goodsContents;

    @ApiModelProperty("图片+链接内容")
    private List<TopicStoreyContentDTO> linkContents;
}
