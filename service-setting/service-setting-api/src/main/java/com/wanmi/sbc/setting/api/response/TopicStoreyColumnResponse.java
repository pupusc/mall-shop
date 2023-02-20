package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.dto.TopicStoreyColumnDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 楼层专栏
 * @Author zh
 * @Date  2023/2/7 14:33
 * @param: null
 * @return: null
 */
@Data
public class TopicStoreyColumnResponse implements Serializable {

    private static final long serialVersionUID = 6127461558038166727L;

    @NotNull
    @ApiModelProperty("楼层id")
    private Integer storeyId;

    @ApiModelProperty("商品内容")
    private List<TopicStoreyColumnDTO> column;


}
