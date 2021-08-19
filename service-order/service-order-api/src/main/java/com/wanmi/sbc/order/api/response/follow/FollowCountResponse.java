package com.wanmi.sbc.order.api.response.follow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FollowCountResponse {

    @ApiModelProperty(value = "总数")
    Long value;
}
