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
public class FollowHaveInvalidGoodsResponse {

    @ApiModelProperty(value = "是否存在失效商品",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    Boolean boolValue;
}
