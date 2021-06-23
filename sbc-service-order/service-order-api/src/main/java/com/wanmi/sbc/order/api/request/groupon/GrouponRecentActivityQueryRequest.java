package com.wanmi.sbc.order.api.request.groupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * Created by feitingting on 2019/5/25.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponRecentActivityQueryRequest {
    @ApiModelProperty(value = "团编号")
    @NotBlank
    private String activityId;
}
