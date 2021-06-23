package com.wanmi.sbc.elastic.api.request.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/9 17:52
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class EsDistributionCustomeffBatchModifyRequest {

    /**
     * 分销员ID
     */
    @ApiModelProperty(value = "分销员ID列表")
    private List<String> distributionIds;
}
