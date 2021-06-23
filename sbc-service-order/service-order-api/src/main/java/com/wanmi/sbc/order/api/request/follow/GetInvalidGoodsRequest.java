package com.wanmi.sbc.order.api.request.follow;

import com.wanmi.sbc.order.bean.dto.GoodsCustomerFollowDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class GetInvalidGoodsRequest {

    @ApiModelProperty(value = "请求信息")
    List<GoodsCustomerFollowDTO> customerFollowDTOS;
}
