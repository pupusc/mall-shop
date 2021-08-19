package com.wanmi.sbc.order.api.request.yzorder;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzOrderCustomerQueryRequest extends BaseQueryRequest {

    /**
     * flag(是否补偿完成0：未开始 1：已完成)
     */
    @ApiModelProperty(value = "flag")
    private Integer flag;
}
