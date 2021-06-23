package com.wanmi.sbc.order.api.response.follow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class IsFollowResponse {

    @ApiModelProperty(value = "商品是否已收藏")
    List<String> value;
}
