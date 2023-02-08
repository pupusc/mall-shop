package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.dto.TopicStoreyColumnDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyColumnGoodsDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 楼层专栏商品
 * @Author zh
 * @Date  2023/2/7 14:33
 * @param: null
 * @return: null
 */
@Data
public class TopicStoreyColumnGoodsResponse implements Serializable {

    private static final long serialVersionUID = -4063511194183912419L;

    @NotNull
    @ApiModelProperty("楼层id")
    private Integer topicStoreyId;

    @ApiModelProperty("商品内容")
    private List<TopicStoreyColumnGoodsDTO> column;


}
