package com.wanmi.sbc.customer.api.response.liveroom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class LiveRoomGoodsAddResponse {

    @ApiModelProperty(value = "错误码")
    private Integer errcode;

}
