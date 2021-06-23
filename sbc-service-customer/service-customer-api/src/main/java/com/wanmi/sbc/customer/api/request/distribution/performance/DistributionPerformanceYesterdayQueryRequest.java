package com.wanmi.sbc.customer.api.request.distribution.performance;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * <p>查询分销员昨日业绩数据request</p>
 * Created by of628-wenzhi on 2019-06-18-16:48.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查询分销员昨日业绩数据request")
public class DistributionPerformanceYesterdayQueryRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 8595545263166096859L;

    /**
     * 分销员id
     */
    @NotBlank
    @ApiModelProperty("分销员id")
    private String distributionId;
}
