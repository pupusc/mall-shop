package com.wanmi.sbc.setting.api.request.topicconfig;

import com.wanmi.sbc.setting.api.response.GoodsAndAtmosphereResponse;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import io.swagger.annotations.ApiModelProperty;

public class TopicRankRequest extends TopicStoreyContentDTO {

    @ApiModelProperty("商品信息")
    private GoodsAndAtmosphereResponse goods;
}
