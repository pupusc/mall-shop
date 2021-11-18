package com.wanmi.sbc.topic.response;

import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TopicStoreyContentReponse extends TopicStoreyContentDTO {

    @ApiModelProperty("商品信息")
    private GoodsAndAtmosphereResponse goods;


}
