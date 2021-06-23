package com.wanmi.sbc.order.api.request.groupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * Created by feitingting on 2019/5/25.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityQueryRequest {
    @ApiModelProperty(value = "团编号")
    @NotBlank
    private String grouponNo;
}
